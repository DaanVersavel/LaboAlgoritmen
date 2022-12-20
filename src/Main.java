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
    private static Crane getBestFittingCrane(double[] sharedInterval, Slot beginSlot, Slot endSlot, double extra) {
        Crane craneToUse = null;
        double beginSlotX = beginSlot.getX()+extra;
        double endSlotX = endSlot.getX()+extra;

        if(sharedInterval == null) {craneToUse = cranes.get(0);}
        else {
            for (Crane crane : cranes) {
                // Kijk of kraan aan begintslot kan
                if (crane.getXmin() <= beginSlotX && beginSlotX <= crane.getXmax()) {
                    // Scenario: beginslot in overlapping gebied
                    if (sharedInterval[0] <= beginSlotX && beginSlotX <= sharedInterval[1]) {
                        if(crane.getId()==1){
                            if (endSlotX < crane.getXmin() ) {
                                continue;
                            } else {
                                craneToUse = crane;
                            }
                        }
                        else {
                            if (endSlotX > crane.getXmax() ) {
                                continue;
                            } else {
                                craneToUse = crane;
                            }
                        }
                    }
                    // Scenario: beginslot gebied 1
                    if (beginSlotX <= sharedInterval[0] && crane.getXmin() < sharedInterval[0]) {
                        craneToUse = crane;
                    }
                    // Scenario: beginslot gebied 2
                    if (beginSlotX >= sharedInterval[1] && crane.getXmax() > sharedInterval[1]) {
                        craneToUse = crane;
                    }
                }
            }
        }
        return craneToUse;
    }
    private static boolean canReachTarget(Crane craneToUse, Slot endSlot, double[] sharedInterval) {
        if(endSlot.getX() <= craneToUse.getXmax() && endSlot.getX() >= craneToUse.getXmin()) {
            // Move other crane out of shared interval to clear shared interval
            for (int i = 0; i < cranes.size(); i++) {
                if(cranes.get(i) != craneToUse) {
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
                cranes.get(i).moveOutOverlap(sharedInterval);
                updateCraneTime(cranes.get(i));
            }
        }
        return false;
    }
    private static ArrayList<Integer> findFreeSlots(double[] sharedInterval, Map<Integer, Slot> slotsMap){
        ArrayList<Integer> possibleFreeSlots = new ArrayList<>();
        for (Slot slot: slotsMap.values()) {
            if(slot.getX() >= sharedInterval[0] && slot.getX() <= sharedInterval[1]) {
                if(slot.getStack().isEmpty() ||
                        slot.getStack().size()<slot.getMaxHeight()) {
                    possibleFreeSlots.add(slot.getId());
                }
            }
        }
        return possibleFreeSlots;
    }
    private static double[] calculateSharedInterval(ArrayList<Crane> cranes) {
        double[] overlappingArea = null;
        if(cranes.size() == 2) {
            overlappingArea = new double[2];
            overlappingArea[0] = cranes.get(1).getXmin();
            overlappingArea[1] = (cranes.get(0).getXmax()+0.5);
        }
        return overlappingArea;
    }
    private static Coordinate[] calculateNewPositionsCrane(Container container, Slot beginSlot, Slot endSlot) {
        //calculate begin x position of crane
        double beginSlotXCoordinate= beginSlot.getX();
        beginSlotXCoordinate += container.getLength()/2;
        //calculate end x position of crane
        double endSlotXCoordinate= endSlot.getX();
        endSlotXCoordinate += container.getLength()/2;

        Coordinate[] newCoordinates = new Coordinate[2];
        newCoordinates[0] = new Coordinate(beginSlotXCoordinate, beginSlot.getY()+0.5);
        newCoordinates[1] = new Coordinate(endSlotXCoordinate, endSlot.getY()+0.5);

        return newCoordinates;
    }

    public static void main(String[] args) {

        /*InputData inputdata= readFile("src/input/terminal22_1_100_1_10.json");
        Target target = readFileTarget("src/input/terminal22_1_100_1_10target.json");*/
        /*InputData inputdata= readFile("src/input/1t/TerminalA_20_10_3_2_100.json");
        Target target = readFileTarget("src/input/1t/targetTerminalA_20_10_3_2_100.json");*/
        /*InputData inputdata= readFile("src/input/3t/TerminalA_20_10_3_2_160.json");
        Target target = readFileTarget("src/input/3t/targetTerminalA_20_10_3_2_160.json");*/
        InputData inputdata= readFile("src/input/5tUPDATE/TerminalB_20_10_3_2_160.json");
        Target target = readFileTarget("src/input/5tUPDATE/targetTerminalB_20_10_3_2_160UPDATE.json");
        /*InputData inputdata= readFile("src/input/6t/Terminal_10_10_3_1_100.json");
        Target target = readFileTarget("src/input/6t/targetTerminal_10_10_3_1_100.json");*/

        inputdata.initAssignment();
        inputdata.modifyInputData();

        ContainerField containerField = new ContainerField(inputdata.getContainersMap(),inputdata.getSlots(),inputdata.getAssignments());

        target.initAssignments();
        target.modifyTargetData(containerField.getSlots(),containerField.getContainers());

        cranes = new ArrayList<>(inputdata.getCranes());
        logs = new ArrayList<>();

        ArrayList<Assignment> assignments = target.getAssignments();
        while(!assignments.isEmpty()){
            Assignment assignment = assignments.remove(0);
            Map<Integer, Slot> slotsMap = containerField.getSlots();

            //for loop for finding most left startslot with slot_id
            Slot beginSlot= null;
            for(Slot slot : slotsMap.values()){
                if(slot.getStack().contains(assignment.getContainer_id())){
                    beginSlot= slot;
                    break;
                }
            }
            //for loop for finding most left endslot with slot_id
            Slot endSlot= slotsMap.get(assignment.getSlot_id());
            Container container = containerField.getContainers().get(assignment.getContainer_id());

            //Skip assignment if container is already in the right place
            if(endSlot.equals(beginSlot)){continue;}

            // Skip assignment if not executable
            if(!containerField.canMoveContainer(assignment.getContainer_id(), assignment.getSlot_idArray())) {
                assignments.add(assignment);
                continue;
            }

            double[] overlappingArea = calculateSharedInterval(cranes);
            double extra = container.getLength()/2;

            Log log = new Log();
            Crane craneToUse = getBestFittingCrane(overlappingArea, beginSlot, endSlot, extra);
            log.setCraneId(craneToUse.getId());
            log.setContainerId(assignment.getContainer_id());

            //als we met 2 kranen werken
            if(overlappingArea!=null) {
                // Verplaatsing mogelijk met 1 kraan?
                Boolean canReachEnd = canReachTarget(craneToUse, endSlot, overlappingArea);

                Coordinate[] newCoordinates = calculateNewPositionsCrane(container, beginSlot, endSlot);
                Coordinate begin = newCoordinates[0];
                Coordinate end = newCoordinates[1];

                //move container if we can do in one movement
                if (canReachEnd) {
                    log.setPickUpTime(craneToUse.getTimeCrane());
                    craneToUse.doAssignement(containerField, assignment.getContainer_id(),
                            assignment.getSlot_idArray(), begin, end);
                    updateCraneTime(craneToUse);
                    log.addPositions(begin, end);
                    log.setEndTime(craneToUse.getTimeCrane());
                    logs.add(log);
                }

                // Plaats container in gemeenschappelijk deel
                else {
                    log.setInterval(true);
                    //find all available slots
                    ArrayList<Integer> possibleFreeSlots = findFreeSlots(overlappingArea, slotsMap);
                    //if container is length 1 we can place in first shared interval
                    if (container.getLength() == 1) {
                        boolean containermoved = true;
                        // Check free slots in overlapping area
                        while (!possibleFreeSlots.isEmpty() && containermoved) {
                            ArrayList<Integer> targetSlotIDs = new ArrayList<>();
                            int slotId = possibleFreeSlots.get(0);
                            targetSlotIDs.add(slotId);
                            if (containerField.canMoveContainer(assignment.getContainer_id(), targetSlotIDs)) {
                                Slot targetSlotShared = slotsMap.get(slotId);
                                //container length is 1 so
                                Coordinate containerEnd = new Coordinate(targetSlotShared.getX() + 0.5, targetSlotShared.getY() + 0.5);
                                // verplaats container
                                log.setPickUpTime(craneToUse.getTimeCrane());
                                craneToUse.doAssignement(containerField, assignment.getContainer_id(), targetSlotIDs, begin, containerEnd);
                                updateCraneTime(craneToUse);
                                log.addPositions(begin, containerEnd);
                                log.setEndTime(craneToUse.getTimeCrane());
                                //if container moved we can stop for loop
                                containermoved = false;
                                logs.add(log);
                            }
                        }
                    }

                    //2 op elkaar volgende x sloten vinden
                    else if (container.getLength() == 2) {
                        //2 op elkaar volgende sloten
                        boolean containermoved = false;
                        // Check free slots in overlapping area
                        for (int i = 0; i < possibleFreeSlots.size(); i++) {
                            ArrayList<Integer> targetSlotIDs = new ArrayList<>();
                            Slot slot1 = slotsMap.get(possibleFreeSlots.get(i));
                            assert slot1 != null;
                            targetSlotIDs.add(slot1.getId());
                            for (int j = 0; j < possibleFreeSlots.size(); j++) {
                                Slot slot2 = slotsMap.get(possibleFreeSlots.get(j));
                                assert slot2 != null;

                                if (slot1.getX() + 1 == slot2.getX() && slot1.getY() == slot2.getY()) {
                                    targetSlotIDs.add(slot2.getId());
                                    extra = container.getLength() / 2;
                                    Coordinate containerEnd = new Coordinate(slot1.getX() + extra, slot1.getY() + 0.5);
                                    // Verplaats container
                                    log.setPickUpTime(craneToUse.getTimeCrane());

                                    containermoved = craneToUse.doAssignement(containerField, assignment.getContainer_id(), targetSlotIDs, begin, containerEnd);
                                    if (containermoved) {
                                        log.addPositions(begin, containerEnd);
                                        log.setEndTime(craneToUse.getTimeCrane());
                                        updateCraneTime(craneToUse);
                                        logs.add(log);
                                        break;
                                    } else {
                                        targetSlotIDs.clear();
                                    }
                                }
                            }
                            //for loop stoppen als we container verplaatst hebben
                            if (containermoved) {
                                break;
                            }
                        }
                    }

                    //3 op elkaar volgende x sloten vinden
                    else if (container.getLength() == 3) {
                        //3 op elkaar volgende sloten
                        boolean containermoved = false;
                        // Check free slots in overlapping area
                        for (int i = 0; i < possibleFreeSlots.size(); i++) {
                            ArrayList<Integer> targetSlotIDs = new ArrayList<>();
                            Slot slot1 = slotsMap.get(possibleFreeSlots.get(i));
                            assert slot1 != null;
                            targetSlotIDs.add(slot1.getId());
                            for (int j = 0; j < possibleFreeSlots.size(); j++) {
                                Slot slot2 = slotsMap.get(possibleFreeSlots.get(j));
                                assert slot2 != null;
                                for (int k = 0; k < possibleFreeSlots.size(); k++) {
                                    Slot slot3 = slotsMap.get(possibleFreeSlots.get(k));
                                    assert slot3 != null;
                                    //TODO checken op Y value ook aangepast in de lengte 2
                                    if (slot1.getX() + 1 == slot2.getX() && slot2.getX() + 1 == slot3.getX() && slot1.getY() == slot2.getY() && slot2.getY() == slot3.getY()) {
                                        targetSlotIDs.add(slot2.getId());
                                        targetSlotIDs.add(slot3.getId());

                                        if (slot1.getX() + 1 == slot2.getX() && slot2.getX() + 1 == slot3.getX()) {
                                            extra = container.getLength() / 2;
                                            Coordinate containerEnd = new Coordinate(slot1.getX() + extra, slot1.getY() + 0.5);
                                            // Verplaats container
                                            log.setPickUpTime(craneToUse.getTimeCrane());
                                            containermoved = craneToUse.doAssignement(containerField, assignment.getContainer_id(), targetSlotIDs, begin, containerEnd);
                                            if (containermoved) {
                                                log.addPositions(begin, containerEnd);
                                                log.setEndTime(craneToUse.getTimeCrane());
                                                updateCraneTime(craneToUse);
                                                logs.add(log);
                                                break;
                                            } else {
                                                targetSlotIDs.clear();
                                            }
                                        }
                                    }
                                    //for loop stoppen als we container verplaatst hebben
                                    if (containermoved) {
                                        break;
                                    }
                                }
                            }
                        }
                        //Assignement terug toevoegen want is nog niet af
                        assignments.add(assignment);
                    }
                }
            }
            else {
                Coordinate[] newCoordinates = calculateNewPositionsCrane(container, beginSlot, endSlot);
                Coordinate begin = newCoordinates[0];
                Coordinate end = newCoordinates[1];

                log.setPickUpTime(craneToUse.getTimeCrane());
                craneToUse.doAssignement(containerField, assignment.getContainer_id(),
                        assignment.getSlot_idArray(),begin,end);
                log.addPositions(begin,end);
                log.setEndTime(craneToUse.getTimeCrane());
                logs.add(log);
            }
        }
        for(Log l : logs) {
            l.printLog();
        }
    }
}