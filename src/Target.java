import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Target {

    @SerializedName("name")
    private String name;
    @SerializedName("maxheight")
    private String maxheight;

    @SerializedName("assignments")
    private ArrayList<Assignment> assignments= new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaxheight() {
        return maxheight;
    }

    public void setMaxheight(String maxheight) {
        this.maxheight = maxheight;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void modifyTargetData(Map<Integer, Slot> slots, Map<Integer,Container> containers) {
        for(Assignment assignment: assignments) {
            int firstslotID = assignment.getSlotID();
            Container container = containers.get(assignment.getContainerID());

            //assign first slot
            assignment.getSlot_idArray().add(firstslotID);

            Slot previousSlot= slots.get(assignment.getSlotID());
            int lengthToGo= container.getLength()-1;
            for(int i=0;i<lengthToGo;i++){
                for(Slot slot : slots.values()){
                    if(slot.getX()== previousSlot.getX()+1 &&slot.getY()== previousSlot.getY()) {
                        assignment.getSlot_idArray().add(slot.getId());
                        previousSlot=slot;
                        break;
                    }
                }
            }

        }
    }

    public void initAssignments() {
        for (Assignment assignment : assignments) {
            assignment.init();
        }
    }
}

