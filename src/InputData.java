import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputData {
    @SerializedName("name")
    private String name;
    @SerializedName("length")
    private int length;
    @SerializedName("width")
    private int width;
    @SerializedName("targetheight")
    private int targetheight=0;
    @SerializedName("maxheight")
    private int maxheight;

    @SerializedName("cranes")
    private ArrayList<Crane> cranes = new ArrayList<>();


    @SerializedName("slots")
    private ArrayList<Slot> slots = new ArrayList<>();

    @SerializedName("containers")
    private ArrayList<Container> containers = new ArrayList<>();

    @SerializedName("assignments")
    private ArrayList<Assignment> assignments = new ArrayList<>();

    private Map<Integer, Container> containersMap = new HashMap<>();
    private Map<Integer, Slot> slotMap = new HashMap<>();



    public Map<Integer, Slot> getSlots() {
        for (Slot slot : slotMap.values()) {
            slot.initialiseStack();
            slot.initialiseMaxHeight();
        }
        return slotMap;
    }

    private void makeSlotsMap() {
        for(Slot slot : slots) {
            slotMap.put(slot.getId(), slot);
        }
    }

    private void makeContainerMap() {
        for(Container container : containers) {
            containersMap.put(container.getId(), container);
        }
    }

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getMaxheight() {
        return maxheight;
    }

    public ArrayList<Crane> getCranes() {
        return cranes;
    }

    public Map<Integer, Container> getContainersMap() {
        return containersMap;
    }

    public void setContainersMap(Map<Integer, Container> containersMap) {
        this.containersMap = containersMap;
    }

    public void modifyInputData(){
        makeContainerMap();
        makeSlotsMap();

        for(Crane crane : cranes) {
            crane.init();
        }

        for(Assignment assignment : assignments) {
            int firstslotID = assignment.getSlotID();
            Container container = containersMap.get(assignment.getContainerID());

            //assign first slot
            assignment.getSlot_idArray().add(firstslotID);

            Slot previousSlot= slotMap.get(firstslotID);
            int lengthToGo= container.getLength()-1;
            for(int i=0;i<lengthToGo;i++){
                for(Slot slot : slotMap.values()){
                    if(slot.getX()== previousSlot.getX()+1 &&slot.getY()== previousSlot.getY()) {
                        assignment.getSlot_idArray().add(slot.getId());
                        previousSlot=slot;
                        break;
                    }
                }
            }
        }
    }

    public void initAssignment() {
        for(Assignment assignment : assignments) {
            assignment.init();
        }
    }

    public int getTargetheight() {
        return targetheight;
    }

    public void setTargetheight(int targetheight) {
        this.targetheight = targetheight;
    }
}
