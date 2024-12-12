package Algorithm;

import GNSS.Sat;
import Geometry.Building;
import Geometry.Point3D;
import Geometry.BuildingsFactory;
import ParticleFilter.Particles;
import ParticleFilter.Particle;
import Utils.KML_Generator;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class LosNlosComputer {
    private static final double SEARCH_RADIUS = 3.0; // meters - reduced from 10.0
    private static final double HEIGHT = 1.8; // meters - changed to match KML height

    public static void main(String[] args) throws Exception {
        System.out.println("Starting LosNlosComputer...");
        
        // 1. קריאת נקודות מקובץ ה-KML של המסלול
        System.out.println("\nStep 1: Loading route points from KML file...");
        String routeKMLPath = "c:/Users/A/Desktop/לימודים/FinalProjectGPS/Final_project-main-master/routeABCDFabricated.kml";
        String buildingsKMLPath = "c:/Users/A/Desktop/לימודים/FinalProjectGPS/Final_project-main-master/Esri_v0.4.kml";
        
        // קריאת נקודות המסלול מקובץ ה-KML
        List<Point3D> routePoints = BuildingsFactory.parseKML(routeKMLPath);  // קריאת נקודות המסלול
        System.out.println("Loaded " + routePoints.size() + " route points from KML");
        
        // בדיקת תקינות - יצירת קובץ KML חדש מהנקודות המקוריות
        String validationKMLPath = "c:/Users/A/Desktop/לימודים/FinalProjectGPS/Final_project-main-master/route_validation.kml";
        KML_Generator.Generate_kml_from_List(routePoints, validationKMLPath, true);  // true מציין שהנקודות כבר ב-LAT/LON
        System.out.println("Created validation KML file at: " + validationKMLPath);
        
        // 2. יצירת רשימת בניינים מקובץ ה-ESRI
        System.out.println("\nStep 2: Loading buildings from ESRI KML file...");
        List<Building> buildings = BuildingsFactory.generateUTMBuildingListfromKMLfile(buildingsKMLPath);
        System.out.println("Successfully loaded " + buildings.size() + " buildings");

        // יצירת לווינים לבדיקה
        System.out.println("\nStep 3: Creating sample satellites...");
        List<Sat> satellites = createSampleSatellites();
        System.out.println("Successfully created " + satellites.size() + " sample satellites");

        // עיבוד כל זוג נקודות עוקבות
        System.out.println("\nStep 4: Processing consecutive point pairs...");
        List<Point3D> bestParticles = new ArrayList<>();
        for (int i = 0; i < routePoints.size() - 1; i++) {
            System.out.println("\nProcessing point pair " + (i + 1) + "/" + (routePoints.size() - 1));
            Point3D p1 = routePoints.get(i);
            Point3D p2 = routePoints.get(i + 1);
            System.out.println("Point 1: " + p1);
            System.out.println("Point 2: " + p2);
            
            // חישוב וקטור LOS/NLOS עבור הנקודה הראשונה (ground truth)
            System.out.println("Computing LOS/NLOS for all satellites...");
            Boolean[] losResults = new Boolean[satellites.size()];
            for (int j = 0; j < satellites.size(); j++) {
                losResults[j] = LosAlgorithm.ComputeLos(p1, buildings, satellites.get(j));
                if (j % 10 == 0) { // log every 10 satellites
                    System.out.println("Processed " + (j + 1) + "/" + satellites.size() + " satellites");
                }
            }
            System.out.println("Finished LOS/NLOS computation");

            // יצירת חלקיקים בין שתי נקודות
            System.out.println("Initializing particles...");
            Particles particles = new Particles();
            // Initialize particles with tighter constraints around the line between p1 and p2
            particles.initParticlesIn3D(p1, p2, HEIGHT);
            System.out.println("Created " + particles.getParticleList().size() + " particles");

            // הערכת החלקיקים על סמך התאמת LOS/NLOS
            System.out.println("Evaluating particles...");
            List<Particle> particleList = particles.getParticleList();
            int processedParticles = 0;
            
            // Calculate the expected direction vector between p1 and p2
            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();
            double expectedDirection = Math.atan2(dy, dx);
            
            for (Particle particle : particleList) {
                // חישוב LOS/NLOS עבור החלקיק הנוכחי
                Boolean[] particleLos = new Boolean[satellites.size()];
                for (int j = 0; j < satellites.size(); j++) {
                    particleLos[j] = LosAlgorithm.ComputeLos(particle.getLocation(), buildings, satellites.get(j));
                }
                particle.setLOS(particleLos);
                
                // בדיקת התאמה לכל לווין
                int matchingSats = particle.ReturnNumberOfMatchingSats(losResults);
                
                // Calculate direction penalty
                double particleDirection = Math.atan2(
                    particle.getLocation().getY() - p1.getY(),
                    particle.getLocation().getX() - p1.getX()
                );
                double directionDiff = Math.abs(normalizeAngle(particleDirection - expectedDirection));
                double directionPenalty = 1.0 - (directionDiff / Math.PI);
                
                // Calculate distance from line penalty
                double distanceFromLine = pointToLineDistance(
                    particle.getLocation().getX(), particle.getLocation().getY(),
                    p1.getX(), p1.getY(), p2.getX(), p2.getY()
                );
                double distancePenalty = Math.max(0, 1.0 - (distanceFromLine / SEARCH_RADIUS));
                
                // Combine all factors for final weight
                particle.EvaluateWeightsNoHistory(losResults);
                double weight = particle.getWeight() * 
                               directionPenalty * 
                               distancePenalty;
                particle.setWeight(weight);
                
                processedParticles++;
                if (processedParticles % 100 == 0) {
                    System.out.println("Evaluated " + processedParticles + "/" + particleList.size() + " particles");
                }
            }

            // מיון החלקיקים לפי משקל
            particleList.sort((particle1, particle2) -> Double.compare(particle2.getWeight(), particle1.getWeight()));
            
            // בחירת החלקיקים הטובים ביותר
            System.out.println("Finding best particles...");
            Point3D bestPosition = null;
            double maxWeight = -1;
            for (Particle p : particleList) {
                if (p.getWeight() > maxWeight) {
                    maxWeight = p.getWeight();
                    bestPosition = p.getLocation();
                }
            }
            if (bestPosition != null) {
                bestParticles.add(bestPosition);
                System.out.println("Best particle found at: " + bestPosition + " with weight: " + maxWeight);
            }

            System.out.println("Finished processing point pair " + (i + 1) + "/" + (routePoints.size() - 1));
        }

        // יצירת קובץ KML מהחלקיקים הטובים ביותר
        System.out.println("\nStep 5: Generating KML output file...");
        String outputKMLPath = "particle_filter_results.kml";
        KML_Generator.Generate_kml_from_List(bestParticles, outputKMLPath);
        System.out.println("Successfully generated results KML file: " + outputKMLPath);
        
        System.out.println("\nProgram completed successfully!");
    }

    private static List<Sat> createSampleSatellites() {
        List<Sat> satellites = new ArrayList<>();
        System.out.println("Creating satellite grid...");
        
        // יצירת רשת של לווינים בזוויות שונות
        for (int elevation = 15; elevation <= 90; elevation += 15) {
            for (int azimuth = 0; azimuth < 360; azimuth += 45) {
                satellites.add(new Sat(satellites.size() + 1, elevation, azimuth));
                System.out.println("Created satellite: elevation=" + elevation + ", azimuth=" + azimuth);
            }
        }
        
        return satellites;
    }
    
    // Helper method to normalize angle to [-π, π]
    private static double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return Math.abs(angle);
    }
    
    // Helper method to calculate point-to-line distance
    private static double pointToLineDistance(
            double px, double py, 
            double x1, double y1, 
            double x2, double y2) {
        double A = px - x1;
        double B = py - y1;
        double C = x2 - x1;
        double D = y2 - y1;
        
        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        
        double param = dot / len_sq;
        
        double xx, yy;
        
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }
        
        double dx = px - xx;
        double dy = py - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
