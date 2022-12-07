import java.util.ArrayList;

public class Crane {
    private double x;
    private double y;
    private double ymin;
    private double ymax;
    private double id;
    private double xspeed;
    private double yspeed;
    private double xmax;
    private double xmin;
    private CoordinateSystem coordinaatSystem;

    public Crane() {
        this.coordinaatSystem = new CoordinateSystem();
    }

    public void moveContainer(ContainerField containerField, int containerId, ArrayList<Integer> destinationSlot_ids) {
        Boolean canmove= false;
        canmove= containerField.canMoveContainer(containerId,destinationSlot_ids);
        //if canmove container if can move crane
        if(canmove) {

        }

    }
}
