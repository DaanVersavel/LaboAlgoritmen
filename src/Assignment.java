import java.util.ArrayList;

public class Assignment {
    private int container_id;
    private int slot_id;
    private ArrayList<Integer> slot_idArray;

    Assignment(int containerID, ArrayList<Integer> slotidArray) {
        this.container_id = containerID;
        this.slot_idArray = slotidArray;
    }

    public int getSlotID() {
        return slot_id;
    }

    public void setSlotID(int slotID) {
        this.slot_id = slotID;
    }

    public ArrayList<Integer> getSlot_idArray() {
        return slot_idArray;
    }

    public int getContainerID() { return container_id; }

    public void setContainerID(int containerID) { this.container_id = containerID; }

    public void setSlot_idArray(ArrayList<Integer> slotsIDS) {
        this.slot_idArray = slotsIDS;
    }

    public void init() {
        slot_idArray = new ArrayList<>();
    }
}
