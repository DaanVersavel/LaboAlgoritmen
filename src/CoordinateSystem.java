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

    public Map<Integer, Coordinate> getCoordinates() {
        return coordinates;
    }
}
