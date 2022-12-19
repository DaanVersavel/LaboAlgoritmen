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
            int firstslotID = assignment.getSlot_id();
            Container container = containers.get(assignment.getContainer_id());

            //assign first slot
            assignment.getSlot_idArray().add(firstslotID);

            Slot previousSlot= slots.get(assignment.getSlot_id());
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

//            if(c.getLength()==2){
//                for(Slot slot: slots.values()) {
//                    if(s1.getX()+1==slot.getX() &&s1.getId()!=slot.getId()) assignment.getSlot_idArray().add(slot.getId());
//                }
//            }

        }
    }

    public void initAssignments() {
        for (Assignment assignment : assignments) {
            assignment.init();
        }
    }
}

