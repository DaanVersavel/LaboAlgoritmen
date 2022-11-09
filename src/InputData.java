import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InputData {
    @SerializedName("slots")
    private ArrayList<Slot> slots = new ArrayList<>();

    @SerializedName("containers")
    private ArrayList<Container> containers = new ArrayList<>();

    @SerializedName("assignments")
    private ArrayList<Assignment> assignments = new ArrayList<>();


    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
}
