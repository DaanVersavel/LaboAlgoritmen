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
}
