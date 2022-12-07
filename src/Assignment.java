import java.util.ArrayList;

public class Assignment {
    private int container_id;
    private int slot_id;
    private ArrayList<Integer> slot_idArray = new ArrayList<>();


    Assignment(int container_id, ArrayList<Integer> slot_id) {
        this.container_id = container_id;
        this.slot_idArray = slot_idArray;
    }


    public int getContainer_id() {
        return container_id;
    }

    public void setContainer_id(int container_id) {
        this.container_id = container_id;
    }

    public ArrayList<Integer> getSlot_ids() {
        return slot_idArray;
    }

    public void setSlot_idArray(ArrayList<Integer> slot_ids) {
        this.slot_idArray = slot_ids;
    }

    public void init() {
        slot_idArray = new ArrayList<>();
    }
}