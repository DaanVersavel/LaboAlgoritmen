import java.util.*;

public class CoordinateSystem {
    private Map<Integer, Coordinate> coordinates;

    public CoordinateSystem() {
        coordinates = new HashMap<>();
    }

    public void addCoordinate(int time, Coordinate newCoordinate) {
        coordinates.put(time, newCoordinate);
    }

    // Crane movement en trajectory updaten
    public int movement(int time1, Coordinate c1, Coordinate c2, double vx,double vy) {
        //check if position already exists
        if(!coordinates.containsKey(time1)){
            addCoordinate(time1,c1);
        }
        //Calculation x   v=x/t => t=x/v
        double dx= Math.abs(c2.getXCoordinate() - c1.getXCoordinate()) ;
        int timex2= (int) (dx/vx);
        //Calculation y
        double dy= Math.abs(c2.getYCoordinate() - c1.getYCoordinate()) ;
        int timey2= (int) (dy/vy);
        int time2;
        if(timex2>timey2){
            time2=timex2;
        }
        else time2=timey2;
        addCoordinate(time2, c2);
        return time2;
    }

    // key of map
    public int getHighestKey() {
        int max = 0;
        Set<Integer> keys = coordinates.keySet();
        for(int key: keys) {
            if (key>max) max = key;
        }
        return max;
    }

    public List<Coordinate> getTrajectory() {
        Trajectory traj = new Trajectory();
        Coordinate tempCoordinate= null;
        for(int i=0; i<getHighestKey(); i++) {
            if(coordinates.containsKey(i)){
                traj.addToTrajectory(coordinates.get(i));
                tempCoordinate = coordinates.get(i);
            }
            else {
                traj.addToTrajectory(tempCoordinate);
            }
            traj.addToTrajectory(coordinates.get(i));
        }
        return traj.getTrajectory();
    }

    public Map<Integer, Coordinate> getCoordinates() {
        return coordinates;
    }

    public Coordinate getCoordinate(int key) {
        return coordinates.get(key);
    }
}
