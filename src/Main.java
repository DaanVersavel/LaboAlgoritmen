import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static InputData readFile(String path){
        InputData inputdata= null;
        try{
            String jsonString= Files.readString(Paths.get(path));
            Gson gson= new Gson();
            inputdata= gson.fromJson(jsonString,InputData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inputdata;
    }
    public static void main(String[] args) {
        InputData inputdata= readFile("src/input/terminal_4_3.json");
        ContainerField containers = new ContainerField(inputdata.getContainers(),inputdata.getSlots(),inputdata.getAssignments());
        System.out.println();
    }
}