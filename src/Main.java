import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static InputData readFile(String path){
        InputData inputdata;
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
        Target target = readFileTarget("src/input/terminal22_1_100_1_10target.json");
        inputdata.initAssignment();
        inputdata.modifieInputData();
        ContainerField containerField = new ContainerField(inputdata.getContainers(),inputdata.getSlots(),inputdata.getAssignments());

        ArrayList<Crane> cranes = new ArrayList<>(inputdata.getCranes());

        // methode voor verdelen
        ArrayList<Assignment> assignments = target.getAssignments();

        while(!assignments.isEmpty()){
            for(int i = 0; i < assignments.size(); i++){
                Assignment assignment = assignments.get(i);
                ArrayList<Slot> slotlist= containerField.getSlots();
                int indexBeginSlot=0;
                //find slot
                Slot beginSlot= null;
                for(int j= 0;j<slotlist.size()-1;j++){
                    if(slotlist.get(j).getStack().contains(assignment.getContainer_id())){
                        beginSlot= slotlist.get(j);
                        break;
                    }
                }
                //choose crane
                //for loop for finding slot with slot_id
                Slot endSlot= null;
                for(int j= 0;j<slotlist.size();j++){
                    if(slotlist.get(j).getId() ==assignment.getSlot_id()) {
                        endSlot= slotlist.get(j);
                        break;
                    }
                }
                Crane craneToUse= null;
                for(Crane crane : cranes){
                    if(crane.getXmin()<= beginSlot.getX()&&beginSlot.getX()<=crane.getXmax()&&
                    crane.getXmin()<= endSlot.getX()&&endSlot.getX()<=crane.getXmax()){

                        craneToUse=crane;

                    }

                }



            }
        }

        // container per container verplaatsen
        //kraan1.moveContainer();

        /*System.out.println();
        ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(2);
        System.out.println(containers.containerMoved(4,list ));*/
    }


}