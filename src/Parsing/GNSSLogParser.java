package Parsing;

import java.io.*;
import java.util.*;
import GNSS.SatellitePositionCalculator;

/**
 * Parser for GNSS log files that converts raw measurements to CSV format
 */
public class GNSSLogParser {
    private String inputFile;
    private String outputFile;
    
    // קבועים לזיהוי סוגי שורות בקובץ הלוג
    private static final String RAW_PREFIX = "Raw,";
    private static final String STATUS_PREFIX = "Status,";
    private static final String OUTPUT_HEADER = "GPS time,SatPRN,Sat.X,Sat.Y,Sat.Z,Pseudo-Range,CN0,Doppler";
    
    private Map<Integer, double[]> lastKnownAngles = new HashMap<>(); // Store last known angles for each satellite
    private static long currentGpsTime = 0; // Track current GPS time
    
    public GNSSLogParser(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    /**
     * מחלקה פנימית לאחסון נתוני מדידה של לוויין
     */
    private static class SatelliteMeasurement {
        long gpsTime;
        int satPRN;
        double pseudoRange;
        double cn0;
        double doppler;
        double satX;
        double satY;
        double satZ;
        
        public String toCSVString() {
            return String.format("%d,%d,%.3f,%.3f,%.3f,%.3f,%.1f,%.3f",
                    gpsTime, satPRN, satX, satY, satZ, pseudoRange, cn0, doppler);
        }
    }

    /**
     * Parse the input file and create CSV output with the following columns:
     * GPS time, SatPRN (ID), Sat.X, Sat.Y, Sat.Z, Pseudo-Range, CN0, Doppler
     */
    public void parseToCSV() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            
            writer.println(OUTPUT_HEADER);
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                try {
                    if (line.startsWith(RAW_PREFIX)) {
                        SatelliteMeasurement measurement = parseRawLine(line);
                        if (measurement != null) {
                            writer.println(measurement.toCSVString());
                        }
                    } else if (line.startsWith(STATUS_PREFIX)) {
                        processSatelliteStatus(line);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Process Status line to extract satellite angles
     */
    private void processSatelliteStatus(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            System.err.println("Status line too short: " + line);
            return;
        }
        
        try {
            // Skip empty status lines
            if (parts[1].isEmpty()) {
                return;
            }

            long statusTime = Long.parseLong(parts[1]);
            int numSatellites = Integer.parseInt(parts[2]);
            int baseIndex = 3;
            
            // Clear old angles if there's a large time gap
            if (Math.abs(statusTime - currentGpsTime) > 30000) {
                System.out.println("Clearing old angles due to time gap: " + (Math.abs(statusTime - currentGpsTime)) + "ms");
                lastKnownAngles.clear();
            }
            
            for (int i = 0; i < numSatellites; i++) {
                int offset = i * 8; // 8 values per satellite
                if (baseIndex + offset + 7 >= parts.length) {
                    System.err.println("Status line truncated at satellite " + i);
                    break;
                }
                
                try {
                    int prn = Integer.parseInt(parts[baseIndex + offset]);
                    double azimuth = Double.parseDouble(parts[baseIndex + offset + 2]);
                    double elevationNs = Double.parseDouble(parts[baseIndex + offset + 3]);
                    
                    // Convert elevation from nanoseconds to degrees (assuming 90 degrees = 1575420000 ns)
                    double elevation = (elevationNs / 1575420000.0) * 90.0;
                    
                    // Store angles if they are not 0 (invalid) and within reasonable ranges
                    if (azimuth != 0.0 && elevation != 0.0 && 
                        azimuth >= 0 && azimuth <= 360 && 
                        elevation >= -90 && elevation <= 90) {
                            
                        // Store angles and timestamp for this satellite
                        lastKnownAngles.put(prn, new double[]{azimuth, elevation, (double)statusTime});
                        System.out.println(String.format("Updated angles for PRN %d: Az=%.1f°, El=%.1f°", 
                            prn, azimuth, elevation));
                    } else {
                        System.out.println(String.format("PRN %d: Invalid angles (Az=%.1f, El=%.1f)", 
                            prn, azimuth, elevation));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing satellite " + i + " in status line");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing status line: " + line);
        }
    }

    /**
     * Parse a single Raw measurement line
     */
    private SatelliteMeasurement parseRawLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 30) {
            return null;
        }

        try {
            SatelliteMeasurement measurement = new SatelliteMeasurement();
            
            measurement.gpsTime = Long.parseLong(parts[1]); // utcTimeMillis
            currentGpsTime = measurement.gpsTime;
            measurement.satPRN = Integer.parseInt(parts[11]); // Svid
            measurement.cn0 = Double.parseDouble(parts[16]); // Cn0DbHz
            measurement.doppler = Double.parseDouble(parts[17]); // PseudorangeRateMetersPerSecond
            
            // Use the pre-calculated pseudorange from the log file
            measurement.pseudoRange = Double.parseDouble(parts[15]); // PseudorangeMeters
            
            // Get last known angles for this satellite
            double[] angles = lastKnownAngles.get(measurement.satPRN);
            if (angles != null && angles.length == 3) {  // Check if we have valid angles and their timestamp
                long angleAge = measurement.gpsTime - (long)angles[2];
                if (angleAge < 30000) {  // Only use angles if they're less than 30 seconds old
                    double[] satPos = SatellitePositionCalculator.calculateSatellitePosition(
                        angles[0], angles[1], measurement.pseudoRange);
                    
                    measurement.satX = satPos[0];
                    measurement.satY = satPos[1];
                    measurement.satZ = satPos[2];
                } else {
                    System.out.println(String.format("PRN %d: Angles too old (%.1f seconds)", 
                        measurement.satPRN, angleAge/1000.0));
                    // Angles too old, use default position
                    measurement.satX = 0;
                    measurement.satY = 0;
                    measurement.satZ = -26559000; // Default to straight up
                    // Remove old angles
                    lastKnownAngles.remove(measurement.satPRN);
                }
            } else {
                System.out.println(String.format("PRN %d: No angle data available", measurement.satPRN));
                // If no angles available, set default position using orbit radius
                measurement.satX = 0;
                measurement.satY = 0;
                measurement.satZ = -26559000; // Default to straight up
            }
            
            return measurement;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing raw line: " + line);
            return null;
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: GNSSLogParser <input_file> <output_file>");
            return;
        }
        
        GNSSLogParser parser = new GNSSLogParser(args[0], args[1]);
        try {
            parser.parseToCSV();
            System.out.println("Parsing completed successfully!");
        } catch (IOException e) {
            System.err.println("Error parsing file: " + e.getMessage());
        }
    }
}
