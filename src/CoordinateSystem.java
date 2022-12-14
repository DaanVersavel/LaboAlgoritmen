import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoordinateSystem {
    private Map<Integer, Coordinate> coordinates;

    public CoordinateSystem() {
        coordinates = new HashMap<>();
    }

    public void addCoordinate(int time, Coordinate newCoordinate) {
        coordinates.put(time, newCoordinate);
    }

    public int movement(int time1, Coordinate c1, Coordinate c2, int vx,int vy) {
        //check if position already exists
        if(!coordinates.containsKey(time1)){
            addCoordinate(time1,c1);
        }
        //Calculation x   v=x/t => t=x/v
        double dx= Math.abs(c2.getXCoordinate() - c1.getXCoordinate()) ;
        int timex2= (int) (dx/vx);
        //Calculation y
        double dy= Math.abs(c2.getYCoordinate() - c2.getXCoordinate()) ;
        int timey2= (int) (dy/vy);

        int time2;
        if(timex2>timey2){
            time2=timex2;
        }
        else time2=timey2;

        addCoordinate(time2, c2);
        return time2;
    }

    public ArrayList<Coordinate> getTrajectory() {
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

    // key of map
    public int getHighestKey() {
        int max = 0;
        Set<Integer> keys = coordinates.keySet();
        for(int key: keys) {
            if (key>max) max = key;
        }
        return max;
    }

    public Map<Integer, Coordinate> getCoordinates() {
        return coordinates;
    }

    // Safety for cranes
    public boolean isSafe(ArrayList<Coordinate> trajectory1, ArrayList<Coordinate> trajectory2) {
        // Aannames:
        // Beide trajectorys even lang en chronologisch geordend
        // Kranen kunnen elkaar niet voorbij gaan
        // Crane 1 zit op linker helft, kraan 2 zit op rechter helft
        int safetyDistance = 1;
        for(int i=0; i<trajectory1.size(); i++) {
            if(((trajectory2.get(i).getXCoordinate()-trajectory1.get(i).getXCoordinate()) < safetyDistance) ||
                    (trajectory1.get(i).getXCoordinate() > trajectory2.get(i).getXCoordinate()))  {
                return false;
            }
        }
        return true;
    }


}
