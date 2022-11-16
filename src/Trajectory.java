import java.util.ArrayList;

public class Trajectory {
    private ArrayList<Coordinate> trajectory;

    public Trajectory(){
        this.trajectory = new ArrayList<>();
    }

    public void addToTrajectory(Coordinate c) {
        trajectory.add(c);
    }

    public ArrayList getTrajectory() {
        return trajectory;
    }

    public void printTrajectory() {
        for(Coordinate c: trajectory) {
            System.out.println("(" + c.getXCoordinate() + "," + c.getYCoordinate() + ")");
        }
    }
}
