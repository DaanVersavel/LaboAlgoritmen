import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    public static Target readFileTarget(String path){
        Target target= null;
        try{
            String jsonString= Files.readString(Paths.get(path));
            Gson gson= new Gson();
            target= gson.fromJson(jsonString, Target.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }
    public static void main(String[] args) {
        InputData inputdata= readFile("src/input/terminal22_1_100_1_10.json");
        Target target = readFileTarget("");
        inputdata.initAssignment();
        inputdata.modifieInputData();
        ContainerField containerField = new ContainerField(inputdata.getContainers(),inputdata.getSlots(),inputdata.getAssignments());

        ArrayList<Crane> cranes = new ArrayList<>(inputdata.getCranes());

        // methode voor verdelen

        // container per container verplaatsen
        //kraan1.moveContainer();

        /*System.out.println();
        ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(2);
        System.out.println(containers.containerMoved(4,list ));*/
    }


}