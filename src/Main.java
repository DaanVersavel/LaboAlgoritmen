import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    private static ArrayList<Crane> cranes;
    private static ArrayList<Log> logs;

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
    private static void updateCraneTime(Crane crane){
        int time = crane.getTimeCrane();
        for(Crane c: cranes){
            if(c!=crane){
                c.setTimeCrane(time);
            }
        }
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
                        //TODO bij voorbeeld van container moet van 12 naar 7 kiest hij eerst kraan 1 en daarna kraan 2
//                        if (endSlot.getX() > crane.getXmax()) {
//                            break;
//                        } else {
//                            craneToUse = crane;
//                        }
                        if(crane.getId()==1){
                            if (endSlot.getX() < crane.getXmin() ) {
                                continue;
                                //break;
                            } else {
                                craneToUse = crane;
                            }
                        }
                        else {
                            if (endSlot.getX() > crane.getXmax() ) {
                                continue;
                                //break;
                            } else {
                                craneToUse = crane;
                            }
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
        //if in interval
        if(endSlot.getX() <= craneToUse.getXmax() && endSlot.getX() >= craneToUse.getXmin()) {
            // Always move other crane
            for (int i = 0; i < cranes.size(); i++) {
                if(cranes.get(i) != craneToUse) {
                    //int time = cranes.get(i).moveOutOverlap(sharedInterval);
                    //craneToUse.setTimeCrane(time);
                    cranes.get(i).moveOutOverlap(sharedInterval);
                    updateCraneTime(cranes.get(i));
                    return true;
                }
            }
            return true;
        }
        // Always move other crane
        for (int i = 0; i < cranes.size(); i++) {
            if(cranes.get(i) != craneToUse) {
                //int time = cranes.get(i).moveOutOverlap(sharedInterval);
                //craneToUse.setTimeCrane(time);
                cranes.get(i).moveOutOverlap(sharedInterval);
                updateCraneTime(cranes.get(i));
            }
        }
        return false;
    }
    private static Slot getSlot(ArrayList<Slot> slotlist,int slotid){
        for(Slot slot : slotlist) {
            if(slot.getId() == slotid) {
                return slot;
            }
        }
        return null;
    }

    public static void main(String[] args) {

//        InputData inputdata= readFile("src/input/terminal22_1_100_1_10.json");
//        Target target = readFileTarget("src/input/terminal22_1_100_1_10target.json");

        InputData inputdata= readFile("src/input/3t/TerminalA_20_10_3_2_160.json");
        Target target = readFileTarget("src/input/3t/targetTerminalA_20_10_3_2_160.json");
        inputdata.initAssignment();
        inputdata.modifyInputData();
        ContainerField containerField = new ContainerField(inputdata.getContainersMap(),inputdata.getSlots(),inputdata.getAssignments());

        cranes = new ArrayList<>(inputdata.getCranes());
        logs = new ArrayList<>();
        target.initAssignments();
        target.modifyTargetData(containerField.getSlots(),containerField.getContainers());

        // methode voor verdelen
        ArrayList<Assignment> assignments = target.getAssignments();

        while(!assignments.isEmpty()){
            //TODO y value not correct
            //TODO geval container 115 is nakijken voor input 3t
            //for(int a = 0; a < assignments.size(); a++){
                Assignment assignment = assignments.remove(0);
                Map<Integer, Slot> slotsMap= containerField.getSlots();
                Log log = new Log();
                int aid=assignment.getContainer_id();
                if(aid==25||aid==40||aid==105||aid==115||aid==153||aid==155){
                    System.out.println();
                }
                if(aid==115){
                    System.out.println();
                }

                if(!containerField.canMoveContainer(assignment.getContainer_id(), assignment.getSlot_idArray())) continue;

                //for loop for finding most left startslot with slot_id
                Slot beginSlot= null;
//                for(int j= 0;j<slotlist.size();j++){
//                    if(slotlist.get(j).getStack().contains(assignment.getContainer_id())){
//                        beginSlot= slotlist.get(j);
//                        break;
//                    }
//                }
            //TODO index afhankelijk
                for(Slot slot : slotsMap.values()){
                    if(slot.getStack().contains(assignment.getContainer_id())){
                        beginSlot= slot;
                        break;
                    }
                }
                //for loop for finding most left endslot with slot_id
                Slot endSlot= slotsMap.get(assignment.getSlot_id());
                Container container = containerField.getContainers().get(assignment.getContainer_id());
                //if container is already in the right place
                if(endSlot.equals(beginSlot)){
                    continue;
                }

                double[] sharedInterval = null;
                if(cranes.size() == 2) {
                    sharedInterval = new double[2];
                    sharedInterval[0] = cranes.get(1).getXmin();
                    sharedInterval[1] = cranes.get(0).getXmax();
                }

                Crane craneToUse = getBestFittingCrane(sharedInterval, beginSlot, endSlot);
                log.setCraneId(craneToUse.getId());
                log.setContainerId(assignment.getContainer_id());
                //als we met 2 kranen werken
                if(sharedInterval!=null) {
                    // Verplaatsing mogelijk met 1 kraan?
                    Boolean canReachEnd = canReachTarget(craneToUse, endSlot, sharedInterval);
                    //calculate begin x position of crane
                    double beginSlotXCoordinate= beginSlot.getX();
                    if(container.getLength() == 2) {
                        beginSlotXCoordinate+=1;
                    }
                    else beginSlotXCoordinate+=0.5;
                    //calculate end x position of crane
                    double endSlotXCoordinate= endSlot.getX();
                    if(container.getLength() == 2) {
                        endSlotXCoordinate+=1;
                    }
                    else endSlotXCoordinate+=0.5;

                    Coordinate begin = new Coordinate(beginSlotXCoordinate, beginSlot.getY());
                    Coordinate end = new Coordinate(endSlotXCoordinate, endSlot.getY());

                    //move container if we can do in one movement
                    if(canReachEnd)  {
                        log.setPickUpTime(craneToUse.getTimeCrane());
                        craneToUse.doAssignement(containerField, assignment.getContainer_id(),
                                assignment.getSlot_idArray(),begin,end);
                        updateCraneTime(craneToUse);
                        log.addPositions(begin,end);
                        log.setEndTime(craneToUse.getTimeCrane());
                        logs.add(log);
                    }

                    // Plaats container in gemeenschappelijk deel
                    else {
                        log.setInterval(true);
                        //if container is length 1 we can place in first shared interval
                        if(container.getLength()==1) {
                            ArrayList<Integer> possibleFreeSlots = new ArrayList<>();
//                            for (int s = 0; s < slotlist.size(); s++) {
//                                if(slotlist.get(s).getX() >= sharedInterval[0] && slotlist.get(s).getX() <= sharedInterval[1]) {
//                                    if(slotlist.get(s).getStack().size()==0 ||
//                                            slotlist.get(s).getStack().size()==slotlist.get(s).getMaxHeight()) {
//                                        possibleFreeSlots.add(slotlist.get(s).getId());
//                                    }
//                                }
//                            }
                            for (Slot slot : slotsMap.values()) {
                                if(slot.getX() >= sharedInterval[0] && slot.getX() <= sharedInterval[1]) {
                                    if(slot.getStack().size()==0 ||
                                            slot.getStack().size()==slot.getMaxHeight()) {
                                        possibleFreeSlots.add(slot.getId());
                                    }
                                }
                            }
                            boolean containermoved=true;
                            while(!possibleFreeSlots.isEmpty() &&containermoved) {
                                int slotId= possibleFreeSlots.remove(0);
                                ArrayList<Integer> targetSlotIDs = new ArrayList<>();
                                targetSlotIDs.add(slotId);
                                if(containerField.canMoveContainer(assignment.getContainer_id(),targetSlotIDs)) {
//                                    Slot targetSlotShared= null;
//                                    for(Slot slot : slotlist) {
//                                        if(slot.getId() == slotId) {
//                                            targetSlotShared = slot;
//                                            break;
//                                        }
//                                    }
                                    Slot targetSlotShared=slotsMap.get(slotId);
                                    //container length is 1 so
                                    Coordinate containerEnd = new Coordinate(targetSlotShared.getX()+0.5,targetSlotShared.getY());
                                    // verplaats container
                                    log.setPickUpTime(craneToUse.getTimeCrane());
                                    craneToUse.doAssignement(containerField, assignment.getContainer_id(), targetSlotIDs,begin,containerEnd);
                                    updateCraneTime(craneToUse);
                                    log.addPositions(begin,containerEnd);
                                    log.setEndTime(craneToUse.getTimeCrane());
                                    //if container moved we can stop for loop
                                    containermoved=false;
                                    logs.add(log);
                                }
                            }
//                            for (Integer slotID : possibleFreeSlots) {
//                                ArrayList<Integer> targetSlotIDs = new ArrayList<>();
//                                targetSlotIDs.add(slotID);
//                                if(containerField.canMoveContainer(assignment.getContainer_id(),targetSlotIDs)) {
//                                    Slot targetSlotShared= null;
//                                    for(Slot slot : slotlist) {
//                                        if(slot.getId() == slotID) {
//                                            targetSlotShared = slot;
//                                            break;
//                                        }
//                                    }
//                                    //container length is 1 so
//                                    Coordinate containerEnd = new Coordinate(targetSlotShared.getX()+0.5,targetSlotShared.getY());
//                                    // verplaats container
//                                    craneToUse.doAssignement(containerField, assignment.getContainer_id(), targetSlotIDs,begin,containerEnd);
//                                    updateCraneTime(craneToUse);
//                                }
//                            }
                        }
                        //2 op elkaar volgende x sloten vinden
                        if(container.getLength()==2) {
                            //find all available slots
                            ArrayList<Integer> possibleFreeSlots = new ArrayList<>();
//                            for (int s = 0; s < slotlist.size(); s++) {
//                                if(slotlist.get(s).getX() >= sharedInterval[0] && slotlist.get(s).getX() <= sharedInterval[1]) {
//                                    if(slotlist.get(s).getStack().isEmpty() ||
//                                            slotlist.get(s).getStack().size()==slotlist.get(s).getMaxHeight()) {
//                                        possibleFreeSlots.add(slotlist.get(s).getId());
//                                    }
//                                }
//                            }
                            for (Slot slot: slotsMap.values()) {
                                if(slot.getX() >= sharedInterval[0] && slot.getX() <= sharedInterval[1]) {
                                    if(slot.getStack().isEmpty() ||
                                            slot.getStack().size()==slot.getMaxHeight()) {
                                        possibleFreeSlots.add(slot.getId());
                                    }
                                }
                            }
                            //2 opelkaar volgende sloten
                            boolean containermoved=false;
                            for(int i=0;i<possibleFreeSlots.size();i++) {
//                                Slot slot1= getSlot(slotlist, possibleFreeSlots.get(i));
                                Slot slot1= slotsMap.get(possibleFreeSlots.get(i));
                                ArrayList<Integer> targetSlotIDs = new ArrayList<>();
                                for(int j=0;j<possibleFreeSlots.size();j++) {
//                                    Slot slot2= getSlot(slotlist, possibleFreeSlots.get(j));
                                    Slot slot2= slotsMap.get(possibleFreeSlots.get(j));
                                    assert slot1 != null;
                                    assert slot2 != null;
                                    if(slot1.getX()+1 == slot2.getX()) {
                                        targetSlotIDs.add(slot1.getId());
                                        targetSlotIDs.add(slot2.getId());
                                        //container length is 2 so
                                        Coordinate containerEnd = new Coordinate(slot1.getX()+1,slot1.getY());
                                        // verplaats container
                                        log.setPickUpTime(craneToUse.getTimeCrane());

                                        containermoved= craneToUse.doAssignement(containerField, assignment.getContainer_id(), targetSlotIDs,begin,containerEnd);
                                        if(containermoved) {
                                            log.addPositions(begin,containerEnd);
                                            log.setEndTime(craneToUse.getTimeCrane());
                                            updateCraneTime(craneToUse);
                                            logs.add(log);
                                            break;
                                        }
                                        else{
                                            targetSlotIDs.clear();
                                        }
                                    }
                                }
                                //for loop stoppen als we container verplaatst hebben
                                if(containermoved) {
                                    break;
                                }
                            }
                        }
                        //Assignement terug toevoegen want is nog niet af
                        assignments.add(assignment);
                    }
                }
                else {
                    double beginSlotXCoordinate= beginSlot.getX();
                    if(container.getLength() == 2) {
                        beginSlotXCoordinate+=1;
                    }
                    else beginSlotXCoordinate+=0.5;
                    //calculate end x position of crane
                    double endSlotXCoordinate= beginSlot.getX();
                    if(container.getLength() == 2) {
                        endSlotXCoordinate+=1;
                    }
                    else endSlotXCoordinate+=0.5;
                    Coordinate begin = new Coordinate(beginSlotXCoordinate, beginSlot.getY());
                    Coordinate end = new Coordinate(endSlotXCoordinate, endSlot.getY());
                    log.setPickUpTime(craneToUse.getTimeCrane());
                    craneToUse.doAssignement(containerField, assignment.getContainer_id(),
                            assignment.getSlot_idArray(),begin,end);
                    log.addPositions(begin,end);
                    log.setEndTime(craneToUse.getTimeCrane());
                    logs.add(log);
                }
            //}
        }
        for(Log l : logs) {
            l.printLog();
        }
    }
}