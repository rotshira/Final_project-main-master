package GNSS;

public class SatellitePositionCalculator {
    private static final double EARTH_RADIUS = 6371000; // meters
    private static final double GPS_ORBIT_RADIUS = 26559000; // meters (approximate GPS orbit radius)
    
    /**
     * Calculate satellite position from azimuth and elevation angles
     * @param azimuth in degrees
     * @param elevation in degrees
     * @param pseudoRange in meters
     * @return double array with [x, y, z] coordinates in meters
     */
    public static double[] calculateSatellitePosition(double azimuth, double elevation, double pseudoRange) {
        // Convert angles to radians
        double azRad = Math.toRadians(azimuth);
        double elRad = Math.toRadians(elevation);
        
        // Calculate satellite position relative to receiver
        double r = GPS_ORBIT_RADIUS; // Use orbit radius instead of pseudorange for better position estimation
        
        // Convert from spherical to cartesian coordinates
        double x = r * Math.cos(elRad) * Math.sin(azRad);
        double y = r * Math.cos(elRad) * Math.cos(azRad);
        double z = r * Math.sin(elRad);
        
        return new double[]{x, y, z};
    }
}
