import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    private static ArrayList<Crane> cranes;

    private static InputData readFile(String path){
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

    private static Target readFileTarget(String path){
        Target target;
        try{
            String jsonString= Files.readString(Paths.get(path));
            Gson gson= new Gson();
            target= gson.fromJson(jsonString, Target.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    private static Crane getBestFittingCrane(double[] sharedInterval, Slot beginSlot, Slot endSlot) {
        Crane craneToUse = null;
        if(sharedInterval == null) {craneToUse = cranes.get(0);}
        else {
            for (Crane crane : cranes) {

                // Kijk of kraan aan begintslot kan
                if (crane.getXmin() <= beginSlot.getX() && beginSlot.getX() <= crane.getXmax()) {
                    // Scenario: beginslot in overlapping gebied
                    if (sharedInterval[0] <= beginSlot.getX() && beginSlot.getX() <= sharedInterval[1]) {
                        if (endSlot.getX() > crane.getXmax()) {
                            break;
                        } else {
                            craneToUse = crane;
                        }
                    }
                    // Scenario: beginslot gebied 1
                    if (beginSlot.getX() <= sharedInterval[0] && crane.getXmin() < sharedInterval[0]) {
                        craneToUse = crane;
                    }
                    // Scenario: beginslot gebied 2
                    if (beginSlot.getX() >= sharedInterval[1] && crane.getXmax() > sharedInterval[1]) {
                        craneToUse = crane;
                    }
                }
            }
        }
        return craneToUse;
    }

    private static Boolean canReachTarget(Crane craneToUse, Slot endSlot, double[] sharedInterval) {
        if(endSlot.getX() <= craneToUse.getXmax() && endSlot.getX() >= craneToUse.getXmin()) {
            for (int i = 0; i < cranes.size(); i++) {
                if(cranes.get(i) != craneToUse) {
                    cranes.get(i).moveOutOverlap(sharedInterval);
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        InputData inputdata= readFile("src/input/terminal22_1_100_1_10.json");
        Target target = readFileTarget("src/input/terminal22_1_100_1_10target.json");
        inputdata.initAssignment();
        inputdata.modifieInputData();
        ContainerField containerField = new ContainerField(inputdata.getContainers(),inputdata.getSlots(),inputdata.getAssignments());

        cranes = new ArrayList<>(inputdata.getCranes());

        // methode voor verdelen
        ArrayList<Assignment> assignments = target.getAssignments();

        while(!assignments.isEmpty()){
            for(int i = 0; i < assignments.size(); i++){
                Assignment assignment = assignments.get(i);
                ArrayList<Slot> slotlist= containerField.getSlots();
                //find slot
                Slot beginSlot= null;
                for(int j= 0;j<slotlist.size()-1;j++){
                    if(slotlist.get(j).getStack().contains(assignment.getContainer_id())){
                        beginSlot= slotlist.get(j);
                        break;
                    }
                }
                //for loop for finding slot with slot_id
                Slot endSlot= null;
                for(int j= 0;j<slotlist.size();j++){
                    if(slotlist.get(j).getId() ==assignment.getSlot_id()) {
                        endSlot= slotlist.get(j);
                        break;
                    }
                }

                double[] sharedInterval = null;
                if(cranes.size() == 2) {
                    sharedInterval = new double[2];
                    sharedInterval[0] = cranes.get(1).getXmin();
                    sharedInterval[1] = cranes.get(0).getXmax();
                }
                Crane craneToUse = getBestFittingCrane(sharedInterval, beginSlot, endSlot);
                if(sharedInterval!=null) {
                    Boolean canReachEnd = canReachTarget(craneToUse, endSlot, sharedInterval);
                }
            }
        }
    }
}