import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InputData {
    @SerializedName("name")
    private String name;
    @SerializedName("length")
    private int length;
    @SerializedName("width")
    private int width;
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


    public ArrayList<Slot> getSlots() {
        for(Slot slot : slots) {
            slot.initialiseStack();
            slot.initialiseMaxHeight();
        }
        return slots;
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

    public void modifieInputData(){
        for(Assignment assignment : assignments) {
            int containerId = assignment.getContainer_id();
            for(Container container : containers) {
                if(container.getId() == containerId) {
                    Slot startSlot=null;
                    for(Slot slot : slots) {
                        if(assignment.getSlot_id() == slot.getId()) {
                            startSlot=slot;
                        }
                    }
                    //container en assignment
                    int containerLength= container.getLength();
                    assignment.getSlot_idArray().add(assignment.getSlot_id());
                    for( int i = 0; i < containerLength; i++ ) {
                        for(Slot slot : slots) {
                            if(slot.getY() == startSlot.getY() &&
                                    slot.getId() != startSlot.getId() && slot.getX() == startSlot.getY()+1) {
                                //if we find next slot
                                assignment.getSlot_idArray().add(slot.getId());
                                startSlot=slot;
                            }
                        }
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
}
