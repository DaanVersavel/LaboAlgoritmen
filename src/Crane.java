import java.util.ArrayList;

public class Crane {
    private double x;
    private double y;
    private double ymin;
    private double ymax;
    private int id;
    private double xspeed;
    private double yspeed;
    private double xmax;
    private double xmin;

    private int timeCrane;
    private CoordinateSystem coordinaatSystem;

    public Crane() {
        this.coordinaatSystem = new CoordinateSystem();
    }


    public boolean doAssignement(ContainerField containerField, int containerId, ArrayList<Integer> destinationSlot_ids,
                              Coordinate beginCrane, Coordinate endCrane)
    {
        boolean canmove= containerField.canMoveContainer(containerId,destinationSlot_ids);
        //if canmove container
        if(canmove) {
            //move container
            containerField.moveContainer(containerId,destinationSlot_ids);
            //update crane trajectory
            timeCrane+= coordinaatSystem.movement(timeCrane, beginCrane, endCrane, getXspeed(), getYspeed());
            return true;
        }
        return false;
    }

    public int moveOutOverlap(double[] sharedInterval) {
        int highestKey=coordinaatSystem.getHighestKey();
        Coordinate beginCoordinate = coordinaatSystem.getCoordinate(highestKey);
        // Move crane 2 out overlapping area
        if(getXmax() > sharedInterval[1]) {
            setX(sharedInterval[1]+2);
            Coordinate targetCoordinate = new Coordinate(sharedInterval[1]+1, getY());
            timeCrane+= coordinaatSystem.movement(timeCrane,beginCoordinate,targetCoordinate, getXspeed(), getYspeed());
        }
        // Move crane 1 out overlapping area
        else {
            setX(sharedInterval[0]-2);
            Coordinate targetCoordinate = new Coordinate(sharedInterval[0]-1 ,getY());
            timeCrane += coordinaatSystem.movement(timeCrane,beginCoordinate,targetCoordinate, getXspeed(), getYspeed());
        }
        return timeCrane;
    }

    public CoordinateSystem getCoordinaatSystem() {
        return coordinaatSystem;
    }

    public void setCoordinaatSystem(CoordinateSystem coordinaatSystem) {
        this.coordinaatSystem = coordinaatSystem;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getYmin() {
        return ymin;
    }

    public void setYmin(double ymin) {
        this.ymin = ymin;
    }

    public double getYmax() {
        return ymax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getXspeed() {
        return xspeed;
    }

    public void setXspeed(double xspeed) {
        this.xspeed = xspeed;
    }

    public double getYspeed() {
        return yspeed;
    }

    public void setYspeed(double yspeed) {
        this.yspeed = yspeed;
    }

    public double getXmax() {
        return xmax;
    }

    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    public double getXmin() {
        return xmin;
    }

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public int getTimeCrane() {
        return timeCrane;
    }

    public void setTimeCrane(int timeCrane) {
        this.timeCrane = timeCrane;
    }

    public void init() {
        coordinaatSystem= new CoordinateSystem();
        Coordinate coordinate = new Coordinate(x,y);
        coordinaatSystem.addCoordinate(timeCrane,coordinate);
    }
}
