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
    public static void updateCraneTime(Crane crane){
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
                if (crane.getXmin() <= beginSlotX && beginSlotX <= crane.getXmax()+0.5) {
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
                            if (endSlotX > crane.getXmax()+0.5) {
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
    private static boolean canReachTarget(Crane craneToUse, Slot endSlot, double[] sharedInterval,double extra) {
        //moeten hier ook midden van container hebben
        double endSlotX =endSlot.getX()+extra;
        if(endSlotX <= craneToUse.getXmax() && endSlotX >= craneToUse.getXmin()) {
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
    private static Slot getBeginSlot(Map<Integer, Slot> slotsMap,int containerId){
        Slot beginSlot= null;
        for(Slot slot : slotsMap.values()){
            if(slot.getStack().contains(containerId)){
                beginSlot= slot;
                return  beginSlot;
            }
        }
        return null;
    }
    private static Coordinate[] calculateNewPositionsCrane(Container container, Slot beginSlot, Slot endSlot) {
        //calculate begin x position of crane
        double beginSlotXCoordinate= beginSlot.getX();
        beginSlotXCoordinate += (double) container.getLength()/2;
        //calculate end x position of crane
        double endSlotXCoordinate= endSlot.getX();
        endSlotXCoordinate += (double) container.getLength()/2;

        Coordinate[] newCoordinates = new Coordinate[2];
        newCoordinates[0] = new Coordinate(beginSlotXCoordinate, beginSlot.getY()+0.5);
        newCoordinates[1] = new Coordinate(endSlotXCoordinate, endSlot.getY()+0.5);

        return newCoordinates;
    }
    private static void moveContainerField(ArrayList<Assignment> assignments,ContainerField containerField){
        while(!assignments.isEmpty()){
            Assignment assignment = assignments.remove(0);
            Map<Integer, Slot> slotsMap = containerField.getSlots();

            //for loop for finding most left startslot with slot_id
            Slot beginSlot= getBeginSlot(slotsMap, assignment.getContainerID());
            //for loop for finding most left endslot with slot_id
            Slot endSlot= slotsMap.get(assignment.getSlotID());
            Container container = containerField.getContainers().get(assignment.getContainerID());

            //Skip assignment if container is already in the right place
            if(endSlot.equals(beginSlot)){continue;}

            // Skip assignment if not executable
            int temp=containerField.canMoveContainer2(assignment.getContainerID(), assignment.getSlot_idArray());
            if(temp==-2){
                assignments.add(assignment);
                continue;
            }else if(temp >= 0){
                ArrayList<Assignment> extraAssignments= new ArrayList<>();
                //move container above it
                ArrayList<Integer> containersToMove = new ArrayList<>();
                while(true){
                    assert beginSlot != null;
                    if (!(beginSlot.getStack().peek()!=assignment.getContainerID())) break;
                    int contid=beginSlot.getStack().pop();
                    containersToMove.add(contid);
                    ArrayList<Integer> freeslots= containerField.findFreeSlotsEverywhere(containerField.getContainers().get(contid).getLength(),slotsMap);
                    extraAssignments.add(new Assignment(contid,freeslots));
                }
                //re add containers to original stack
                for(int i=containersToMove.size()-1; i>=0; i--) {
                    beginSlot.getStack().add(containersToMove.get(i));
                }
                //move container itself
                extraAssignments.add(assignment);
                //place back containers
                for(int i=containersToMove.size()-1; i>=0; i--) {
                    ArrayList<Integer> arrayList = new ArrayList<>();
                    for(Slot slot: slotsMap.values()) {
                        if(slot.getStack().contains(containersToMove.get(i))){
                            arrayList.add(slot.getId());
                        }
                    }
                    extraAssignments.add(new Assignment(containersToMove.get(i),arrayList));
                }
                doAssignment(extraAssignments,containerField,cranes);
                continue;
            }

            double[] overlappingArea = calculateSharedInterval(cranes);
            double extra = (double)container.getLength()/2;

            assert beginSlot != null;
            Crane craneToUse = getBestFittingCrane(overlappingArea, beginSlot, endSlot, extra);
            Log log = new Log(craneToUse.getId(),assignment.getContainerID(),container.getLength());


            //als we met 2 kranen werken
            if(overlappingArea!=null) {
                // Verplaatsing mogelijk met 1 kraan?
                boolean canReachEnd = canReachTarget(craneToUse, endSlot, overlappingArea,extra);

                Coordinate[] newCoordinates = calculateNewPositionsCrane(container, beginSlot, endSlot);
                Coordinate begin = newCoordinates[0];
                Coordinate end = newCoordinates[1];

                //move container if we can do in one movement
                if (canReachEnd) {
                    log.setPickUpTime(craneToUse.getTimeCrane());
                    craneToUse.doAssignement(containerField, assignment.getContainerID(),
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
                        log = containerField.moveMidLength1(log, possibleFreeSlots,craneToUse,assignment,begin);
                        logs.add(log) ;
                    }

                    //2 op elkaar volgende x sloten vinden
                    else if (container.getLength() == 2) {
                        log = containerField.moveMidLength2(log, possibleFreeSlots,craneToUse, assignment,extra, begin);
                        logs.add(log);
                    }

                    //3 op elkaar volgende x sloten vinden
                    else if (container.getLength() == 3) {
                        log = containerField.moveMidLength3(log, possibleFreeSlots,craneToUse,assignment,extra, begin);
                        logs.add(log);
                    }
                    //Assignement terug toevoegen want is nog niet af
                    assignments.add(assignment);
                }
            }
            else {
                Coordinate[] newCoordinates = calculateNewPositionsCrane(container, beginSlot, endSlot);
                Coordinate begin = newCoordinates[0];
                Coordinate end = newCoordinates[1];

                log.setPickUpTime(craneToUse.getTimeCrane());
                craneToUse.doAssignement(containerField, assignment.getContainerID(),
                        assignment.getSlot_idArray(),begin,end);
                log.addPositions(begin,end);
                log.setEndTime(craneToUse.getTimeCrane());
                logs.add(log);
            }
        }
    }
    private static ArrayList<Log> reduceHeight(int targetHeight, ContainerField containerField, ArrayList<Crane> cranes) {
        //containers die verplaats moeten worden vinden
        Map<Integer, Slot> slots = containerField.getSlots();
        Map<Integer, Container> containers = containerField.getContainers();

        ArrayList<Integer> containersToMove = new ArrayList<>();
        ArrayList<Integer> noPlaceFound = new ArrayList<>();
        ArrayList<Log> logs = new ArrayList<>();

        for( Slot slot : slots.values()) {
            if(slot.getStack().size()>targetHeight){
                if(!containersToMove.contains(slot.getTopContainer())) containersToMove.add(slot.getTopContainer());
            }
        }
        ArrayList<Integer> containersLength1 = new ArrayList<>();
        ArrayList<Integer> containersLength2 = new ArrayList<>();
        ArrayList<Integer> containersLength3 = new ArrayList<>();
        for( Integer containerId : containersToMove) {
            Container container = containers.get(containerId);
            if(container.getLength()==1) containersLength1.add(containerId);
            if(container.getLength()==2) containersLength2.add(containerId);
            if(container.getLength()==3) containersLength3.add(containerId);
        }
        //start with the smallest containers and place as low as possible
        for(Integer containerId : containersLength1) {
            ArrayList<Integer> destinationSLotID = containerField.findBestTargetSlot1(targetHeight,containerId);
            if(!destinationSLotID.isEmpty()){
                Assignment assignment = new Assignment(containerId,destinationSLotID);
                ArrayList<Assignment> assignments = new ArrayList<>();
                assignments.add(assignment);
                logs.addAll(doAssignment(assignments,containerField,cranes));
            }else noPlaceFound.add(containerId);

        }

        //Containers of length 2 and place as low as possible
        for(Integer containerId : containersLength2) {
            ArrayList<Integer> destinationSlotID = containerField.findBestTargetSlot2(targetHeight,containerId);
            if(!destinationSlotID.isEmpty()){
                Assignment assignment = new Assignment(containerId,destinationSlotID);
                ArrayList<Assignment> assignments = new ArrayList<>();
                assignments.add(assignment);
                logs.addAll(doAssignment(assignments,containerField,cranes));
            } else noPlaceFound.add(containerId);

        }
        //Containers of length 3 and place as low as possible
        for(Integer containerId : containersLength3) {
            ArrayList<Integer> destinationSlotID = containerField.findBestTargetSlot3(targetHeight,containerId);
            if(!destinationSlotID.isEmpty()){
                Assignment assignment = new Assignment(containerId,destinationSlotID);
                ArrayList<Assignment> assignments = new ArrayList<>();
                assignments.add(assignment);
                logs.addAll(doAssignment(assignments,containerField,cranes));
            } else noPlaceFound.add(containerId);
        }
        return logs;
    }

    private static ArrayList<Log> doAssignment(ArrayList<Assignment> assignments, ContainerField containerField, ArrayList<Crane> cranes) {

        ArrayList<Log> logs = new ArrayList<>();
        while(!assignments.isEmpty()){
            Assignment assignment = assignments.remove(0);
            double[] overlappingArea = calculateSharedInterval(cranes);
            Container container= containerField.getContainers().get(assignment.getContainerID());
            double extra =(double) container.getLength()/2;
            Map<Integer, Slot> slotsMap = containerField.getSlots();
            Slot beginSlot = getBeginSlot(slotsMap, container.getId());

            Slot endSlot =slotsMap.get(assignment.getSlotID()) ;


            assert beginSlot != null;
            Crane craneToUse = getBestFittingCrane(overlappingArea, beginSlot, endSlot, extra);
            Log log = new Log(craneToUse.getId(),assignment.getContainerID(),container.getLength());


            //als we met 2 kranen werken
            if(overlappingArea!=null) {
                // Verplaatsing mogelijk met 1 kraan?
                boolean canReachEnd = canReachTarget(craneToUse, endSlot, overlappingArea,extra);
                Coordinate[] newCoordinates = calculateNewPositionsCrane(container, beginSlot, endSlot);
                Coordinate begin = newCoordinates[0];
                Coordinate end = newCoordinates[1];

                //move container if we can do in one movement
                if (canReachEnd) {
                    log.setPickUpTime(craneToUse.getTimeCrane());
                    craneToUse.doAssignement(containerField, assignment.getContainerID(),
                            assignment.getSlot_idArray(), begin, end);
                    updateCraneTime(craneToUse);
                    log.addPositions(begin, end);
                    log.setEndTime(craneToUse.getTimeCrane());
                    logs.add(log) ;
                }
                // Plaats container in gemeenschappelijk deel
                else {
                    log.setInterval(true);
                    //find all available slots
                    ArrayList<Integer> possibleFreeSlots = findFreeSlots(overlappingArea, containerField.getSlots());
                    //if container is length 1 we can place in first shared interval
                    if (container.getLength() == 1) {
                        log = containerField.moveMidLength1(log, possibleFreeSlots,craneToUse,assignment,begin);
                        logs.add(log) ;

                    }
                    //2 op elkaar volgende x sloten vinden
                    else if (container.getLength() == 2) {
                        log = containerField.moveMidLength2(log, possibleFreeSlots,craneToUse, assignment,extra, begin);
                        logs.add(log);
                    }
                    //3 op elkaar volgende x sloten vinden
                    else if (container.getLength() == 3) {
                        log = containerField.moveMidLength3(log, possibleFreeSlots,craneToUse,assignment,extra, begin);
                        logs.add(log);

                    }
                    //Assignement terug toevoegen want is nog niet af
                    if(assignments.isEmpty()){
                        assignments = new ArrayList<>();
                        assignments.add(assignment);
                    }else assignments.set(0,assignment);
                }
            }
            else {
                Coordinate[] newCoordinates = calculateNewPositionsCrane(container, beginSlot, endSlot);
                Coordinate begin = newCoordinates[0];
                Coordinate end = newCoordinates[1];

                log.setPickUpTime(craneToUse.getTimeCrane());
                craneToUse.doAssignement(containerField, assignment.getContainerID(),
                        assignment.getSlot_idArray(),begin,end);
                log.addPositions(begin,end);
                log.setEndTime(craneToUse.getTimeCrane());
                logs.add(log);
            }
        }
        return logs;
    }

    public static void main(String[] args) {
        InputData inputdata= readFile(args[0]);
        inputdata.initAssignment();
        inputdata.modifyInputData();
        ContainerField containerField = new ContainerField(inputdata.getContainersMap(),inputdata.getSlots(),inputdata.getAssignments());
        cranes = new ArrayList<>(inputdata.getCranes());
        logs = new ArrayList<>();

        if(inputdata.getTargetheight()!=0){
            logs = reduceHeight(inputdata.getTargetheight(),containerField,cranes);
        } else{
            Target target = readFileTarget (args[1]) ;
            target.initAssignments();
            target.modifyTargetData(containerField.getSlots(),containerField.getContainers());
            ArrayList<Assignment> assignments = target.getAssignments();
            moveContainerField(assignments,containerField);
        }
        for(Log log : logs) {
            log.printLog();
        }
    }
}