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
            int firstslotID = assignment.getSlot_id();
            Container container = containersMap.get(assignment.getContainer_id());

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
//        for(Assignment assignment : assignments) {
//            int containerId = assignment.getContainer_id();
//            for(Container container : containers) {
//                if(container.getId() == containerId) {
//                    for(Slot slot : slots) {
//                        if(assignment.getSlot_id() == slot.getId()) {
//                            startSlot=slot;
//                        }
//                    }
//                    //container en assignment
//            int containerLength= container.getLength();
//            assignment.getSlot_idArray().add(assignment.getSlot_id());
//            //Gaan ervan uit dat max 2 lengte
//            if(containerLength==2){
//                for(Slot slot : slots) {
//                    if(slot.getY() == startSlot.getY() &&
//                            slot.getId() != startSlot.getId() &&
//                            slot.getX() == startSlot.getX()+1) {
//                        //if we find next slot
//                        assignment.getSlot_idArray().add(slot.getId());
//                    }
//                }
//            }
////                    for( int i = 0; i < containerLength; i++ ) {
////                        for(Slot slot : slots) {
////                            if(slot.getY() == startSlot.getY() &&
////                                    slot.getId() != startSlot.getId() &&
////                                    slot.getX() == startSlot.getX()+1) {
////                                //if we find next slot
////                                assignment.getSlot_idArray().add(slot.getId());
////                                startSlot=slot;
////                            }
////                        }
////                    }
//
//            }
//        }
    }

    public void initAssignment() {
        for(Assignment assignment : assignments) {
            assignment.init();
        }
    }
}
