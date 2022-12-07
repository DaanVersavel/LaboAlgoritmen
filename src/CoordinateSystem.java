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
        int dx= Math.abs(c2.getXCoordinate() - c1.getXCoordinate()) ;
        int timex2=dx/vx;
        //Calculation y
        int dy= Math.abs(c2.getYCoordinate() - c2.getXCoordinate()) ;
        int timey2=dy/vy;

        int time2;
        if(timex2>timey2){
            time2=timex2;
        }
        else time2=timey2;

        addCoordinate(time2, c2);
        return time2;

    }

    public ArrayList<Coordinate> getTrajectory() {
        ArrayList<Coordinate> trajectory = new ArrayList<>();
        if (getHighestKey()== coordinates.size()) {
            return trajectory;
        }
        Coordinate tempCoordinate= null;
        for(int i=0; i<getHighestKey(); i++) {
            if(coordinates.containsKey(i)){
                trajectory.add(coordinates.get(i));
                tempCoordinate = coordinates.get(i);
            }
            else {
                trajectory.add(tempCoordinate);
            }
            trajectory.add(coordinates.get(i));
        }
        return trajectory;
    }

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
}
