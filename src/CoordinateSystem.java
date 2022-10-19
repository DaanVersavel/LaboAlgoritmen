import java.util.HashMap;
import java.util.Map;

public class CoordinateSystem {
    private Map<Integer, Coordinate> coordinates;
    public CoordinateSystem() {
        coordinates = new HashMap<>();
    }

    public void addCoordinate(int time, int x, int y) {
        Coordinate newCoordinate = new Coordinate(x, y);
        coordinates.put(time, newCoordinate);
    }

    public int movement(int time1,int x1, int y1, int x2, int y2, int vx,int vy) {
        //check if position already exists
        if(!coordinates.containsKey(time1)){
            addCoordinate(time1,x1,y1);
        }
        //Calculation x   v=x/t => t=x/v
        int dx= Math.abs(x2 - x1) ;
        int timex2=dx/vx;
        //Calculation y
        int dy= Math.abs(y2 - y1) ;
        int timey2=dy/vy;

        int time2;
        if(timex2>timey2){
            time2=timex2;
        }
        else time2=timey2;

        return time2;

    }

    public Map<Integer, Coordinate> getCoordinates() {
        return coordinates;
    }
}
