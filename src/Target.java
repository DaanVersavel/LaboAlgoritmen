import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    public void modifyTargetData(ArrayList<Slot> slots, ArrayList<Container> containers) {
        for(Assignment assignment: assignments) {
            Container c=null;
            for(Container container :containers ){
                if(container.getId()==assignment.getContainer_id()) c=container;
            }
            //start slot
            Slot s1= null;
            for(Slot slot: slots) {
                if(slot.getId()==assignment.getSlot_id()) s1=slot;
            }
            assignment.getSlot_ids().add(s1.getId());
            if(c.getLength()==2){
                for(Slot slot: slots) {
                    if(s1.getX()+1==slot.getX() &&s1.getId()!=slot.getId()) assignment.getSlot_ids().add(slot.getId());
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

