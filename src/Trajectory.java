import java.util.ArrayList;

public class Trajectory {
    private ArrayList<Coordinate> trajectoryCoordinates;

    public Trajectory(){
        this.trajectoryCoordinates = new ArrayList<>();
    }

    public void addToTrajectory(Coordinate c) {
        trajectoryCoordinates.add(c);
    }

    public ArrayList<Coordinate> getTrajectory() {
        return trajectoryCoordinates;
    }

    public void printTrajectory() {
        for(Coordinate c: trajectoryCoordinates) {
            System.out.println("(" + c.getXCoordinate() + "," + c.getYCoordinate() + ")");
        }
    }
}
