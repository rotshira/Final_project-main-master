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
}
