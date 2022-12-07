import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Target {

    @SerializedName("name")
    private String name;
    @SerializedName("maxheight")
    private String maxheight;

    @SerializedName("assignments")
    private ArrayList assignments= new ArrayList<>();

}
