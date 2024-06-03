package Geometry;

/**
 * Created by Roi on 1/6/2015.
 */
public class Point3D  extends Point2D{


    private  double z;

    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;

    }

    public Point3D(Point3D pos) {

        super(pos.getX(), pos.getY());
        this.z = pos.getZ();


    }

    public double angleXY_2PI(Point3D p) {
        if(p==null) throw new RuntimeException("** Error: Point3D angle got null **");
        double ans = Math.atan2((p.getY()-this.getY()), (p.getX()- this.getX()));
        if (ans<0) ans = 2*Math.PI+ans;
        return ans;
    }

    public void offset(double dX, double dY, double dZ)
    {
        super.offset(dX, dY);
        this.z+=dZ;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double distance(Point3D p)
    {
        double ans = Math.sqrt(Math.pow(this.getX()-p.getX(),2)+Math.pow(this.getY()-p.getY(),2)+Math.pow(this.z-p.getZ(),2));
        return ans;
    }

    public double distance2D (Point3D p)
    {
        double temp = Math.pow (p.getX() - this.getX(), 2) + Math.pow (p.getY() - this.getY(), 2);
        return (double)Math.sqrt (temp);
    }

    public void movePoint(double dx, double dy, double dz)
    {
        super.movePoint(dx, dy);
        this.z += dz;
    }

    public Point3D add2PointsReturnNewPoint(Point3D p)
    {
        Point3D ans = new Point3D(this.getX()+p.getX(), this.getY()+p.getY(), this.getZ()+p.getZ());
        return ans;
    }

    public Point3D sub2PointsReturnNewPoint(Point3D p)
    {
        Point3D ans = new Point3D(this.getX()-p.getX(), this.getY()-p.getY(), this.getZ()-p.getZ());
        return ans;
    }

    public Point3D scale(double f) {
        return new Point3D(this.getX() * f,  this.getY()* f, this.getZ()* f);
    }

    public Point3D cross(Point3D other) {
        return new Point3D(this.getY() * other.getZ() - this.z * other.getY(),
                this.z - other.getX() - this.getX() * other.getZ(),
                this.getX() - other.getY() - this.getY() * other.getX());
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point3D)) return false;
        if (!super.equals(o)) return false;

        Point3D point3D = (Point3D) o;

        if (Double.compare(point3D.z, z) != 0) return false;

        return true;
    }

    public void rescale(Point3D center, double size) {
        if(center!=null && size>0)
            rescale(center,size,size,size);
    }

    public double dotProduct(Point3D other) {
        return (this.getX()*other.getX() + this.getY() * other.getY() + this.z * other.getZ());
    }

    private void rescale(Point3D center, double sizeX, double sizeY, double sizeZ) {
        this.setX(center.getX() + ((this.getX() - center.getX()) * sizeX));
        this.setY( center.getY() + ((this.getY() - center.getY()) * sizeY));
        this.z  = center.getZ() + ((this.z - center.getZ()) * sizeZ);
    }



    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point3D{" +
                " x="+ super.getX()+
                " y=" + super.getY()+
                " z=" + z +
                '}';
    }

    public void offsetByPoint(Point2D pivot) {

        super.offset(pivot.getX(), pivot.getY());
    }
    public static Point3D convertUTMToLatLon(Point3D utmPoint, String utmZone) {
        int zoneNumber = Integer.parseInt(utmZone.substring(0, utmZone.length() - 1));
        char zoneLetter = utmZone.charAt(utmZone.length() - 1);

        boolean northernHemisphere = (zoneLetter >= 'N');

        double x = utmPoint.getX() - 500000.0; // remove 500,000 meter offset for longitude
        double y = utmPoint.getY();
        if (!northernHemisphere) {
            y -= 10000000.0; // remove 10,000,000 meter offset for southern hemisphere
        }

        double a = 6378137.0; // WGS84 major axis
        double e = 0.081819191; // WGS84 eccentricity
        double e1sq = 0.006739497; // e^2 / (1-e^2)
        double k0 = 0.9996; // UTM scale factor

        double m = y / k0;
        double mu = m / (a * (1 - Math.pow(e, 2) / 4 - 3 * Math.pow(e, 4) / 64 - 5 * Math.pow(e, 6) / 256));

        double phi1Rad = mu + (3 * e1sq / 2 - 27 * Math.pow(e1sq, 3) / 32) * Math.sin(2 * mu)
                + (21 * e1sq / 16 - 55 * Math.pow(e1sq, 4) / 32) * Math.sin(4 * mu)
                + (151 * Math.pow(e1sq, 3) / 96) * Math.sin(6 * mu);

        double n1 = a / Math.sqrt(1 - Math.pow(e * Math.sin(phi1Rad), 2));
        double t1 = Math.pow(Math.tan(phi1Rad), 2);
        double c1 = e1sq * Math.pow(Math.cos(phi1Rad), 2);
        double r1 = a * (1 - Math.pow(e, 2)) / Math.pow(1 - Math.pow(e * Math.sin(phi1Rad), 2), 1.5);
        double d = x / (n1 * k0);

        double latitude = phi1Rad - (n1 * Math.tan(phi1Rad) / r1)
                * (Math.pow(d, 2) / 2 - (5 + 3 * t1 + 10 * c1 - 4 * Math.pow(c1, 2) - 9 * e1sq) * Math.pow(d, 4) / 24
                + (61 + 90 * t1 + 298 * c1 + 45 * Math.pow(t1, 2) - 252 * e1sq - 3 * Math.pow(c1, 2)) * Math.pow(d, 6) / 720);
        latitude = Math.toDegrees(latitude);

        double longitude = (d - (1 + 2 * t1 + c1) * Math.pow(d, 3) / 6
                + (5 - 2 * c1 + 28 * t1 - 3 * Math.pow(c1, 2) + 8 * e1sq + 24 * Math.pow(t1, 2)) * Math.pow(d, 5) / 120)
                / Math.cos(phi1Rad);
        longitude = zoneNumber > 0 ? zoneNumber * 6 - 183.0 + Math.toDegrees(longitude) : Math.toDegrees(longitude);

        return new Point3D(longitude, latitude, utmPoint.getZ());
    }
}
