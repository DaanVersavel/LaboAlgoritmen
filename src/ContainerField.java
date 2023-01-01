import java.util.ArrayList;
import java.util.Map;

public class ContainerField {
    private Map<Integer,Container> containers;
    private Map<Integer, Slot> slots;
    private ArrayList<Assignment> assignments;

    //works only if there are no missing containers
    ContainerField(Map<Integer,Container>containers, Map<Integer, Slot>slots, ArrayList<Assignment>assignments){
        this.containers = containers;
        this.slots = slots;
        this.assignments = assignments;
        placeContainers();
    }

    private void  placeContainers(){
        for(Assignment a : assignments){
            int contID = a.getContainerID();
            ArrayList<Integer> slotIDS= a.getSlot_idArray();

            for (int slot_id: slotIDS) {
                Slot s= slots.get(slot_id);
                s.addContainer(contID);
            }
        }
    }

    public boolean canRemoveContainer(int containerID) {
        ArrayList<Slot> containerSlots = new ArrayList<>();

        for(Slot slot: slots.values()) {
            if(slot.getStack().contains(containerID)) containerSlots.add(slot);
        }

        for(Slot slot: containerSlots) {
            if(slot.getTopContainer() != containerID) return false;
        }
        return true;
    }
    public int canRemoveContainer2(int containerID) {
        ArrayList<Slot> containerSlots = new ArrayList<>();

        for(Slot slot: slots.values()) {
            if(slot.getStack().contains(containerID)) containerSlots.add(slot);
        }

        for(Slot slot: containerSlots) {
            if(slot.getTopContainer() != containerID) return slot.getStack().search(containerID);
        }
        return -1;
    }

    public boolean canPlaceContainer(ArrayList<Integer> destinationSlotIDS) {
        ArrayList<Slot> destinationSlots =  new ArrayList<>();
        //Look for destionation slots and put in arraylist
        for (Integer slotid: destinationSlotIDS){
            Slot s = slots.get(slotid);
            destinationSlots.add(s);
        }
        //check if not surpass Maxheight
        for(Slot s : destinationSlots){
            if((s.getStack().size()+1)> s.getMaxHeight()) return false;
        }
        //check if slots have same height
        Slot s1= destinationSlots.get(0);
        for(int i=1;i<destinationSlotIDS.size();i++){
            if(s1.getStack().size() != destinationSlots.get(i).getStack().size()) return false;
        }
        //if all slots have same container id then we can place them
        s1= destinationSlots.get(0);
        int temp=0;
        if(!s1.getStack().isEmpty()){
            for(int i=1;i<destinationSlotIDS.size();i++){
                if(!destinationSlots.get(i).getStack().isEmpty()){
                    if(s1.getTopContainer()==destinationSlots.get(i).getTopContainer()) {
                        temp++;
                    }
                    s1= destinationSlots.get(i);
                }
            }
        }

        if(temp!=destinationSlots.size()) return true;

        //last one
        //credits to lucas
        for(Slot s : destinationSlots){
            ArrayList<Slot> containerSlots = new ArrayList<>();
            int lowerContainerID = s.getTopContainer();
            for(Slot slot: slots.values()) {
                if(slot.getStack().contains(lowerContainerID)) containerSlots.add(slot);
            }
            for (Slot slot1 : containerSlots){
                if(!destinationSlots.contains(slot1)) return false;
            }
        }
        return true;
    }

    public boolean canMoveContainer(int containerID, ArrayList<Integer> destinationSlotIDS) {
        // Check if replacement is possible
        if(!canRemoveContainer(containerID) || !canPlaceContainer(destinationSlotIDS))
            return false;
        return true;
    }
    public int canMoveContainer2(int containerID, ArrayList<Integer> destinationSlotIDS) {
        // Check if replacement is possible
        //-1 if we can remove container
        int canRemoveContainer=canRemoveContainer2(containerID);
        if(canRemoveContainer!=-1) return canRemoveContainer;
        if(!canPlaceContainer(destinationSlotIDS)) return -2;
        return -1;
        //return -1 als we hem kunnen verplaatsen
        //return -2 als we hem niet kunnen plaatsen
        //return 0 of hoger als we de containers er boven moeten verplaatsen
    }


    public void moveContainer(int containerID,ArrayList<Integer> destinationSlotIDS){
        ArrayList<Slot> destinationSlots = new ArrayList<>();
        for (Slot s: slots.values()) {
            if (s.getStack().contains(containerID)) s.removeTopContainer();
        }
        // Update stack of destination slots
        for(Slot s: slots.values()){
            for(int id: destinationSlotIDS){
                if(s.getId() == id){
                    destinationSlots.add(s);
                }
            }
        }
        for (Slot ds: destinationSlots) ds.addContainer(containerID);
    }

    public ArrayList<Integer> findBestTargetSlot1(int targetHeight, Integer containerId) {
        boolean found = false;
        int level=0; //no container placed
        ArrayList<Integer> destinationSlotID = new ArrayList<>();
        //start at bottom
        while(!found &&level<=targetHeight-1){
            for(Slot s: slots.values()){
                destinationSlotID = new ArrayList<>();
                if(s.getStack().size()==level){
                    destinationSlotID.add(s.getId());
                    if(canMoveContainer(containerId,destinationSlotID)){
                        found = true;
                        break;
                    }
                }
            }
            level++;
        }
        return destinationSlotID;
    }


    public ArrayList<Integer> findBestTargetSlot2(int targetHeight, Integer containerId) {
        boolean found = false;
        int level=0; //no container placed
        ArrayList<Integer> destinationSlotID = new ArrayList<>();

        while(!found&&level<=targetHeight-1){
            for(Slot s1: slots.values()){
                destinationSlotID = new ArrayList<>();
                for(Slot s2: slots.values()){
                    if(s1.getX()+1==s2.getX()&&s1.getY()==s2.getY()){
                        if(s1.getStack().size()==level&&s2.getStack().size()==level){
                            destinationSlotID.add(s1.getId());
                            destinationSlotID.add(s2.getId());
                            if(canMoveContainer(containerId,destinationSlotID)){
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if(found)break;
            }
            level++;
        }
        return destinationSlotID;
    }
    public ArrayList<Integer> findBestTargetSlot3(int targetHeight, Integer containerId) {
        boolean found = false;
        int level=0; //no container placed
        ArrayList<Integer> destinationSlotID = new ArrayList<>();

        while(!found&&level<=targetHeight-1){
            for(Slot s1: slots.values()){
                destinationSlotID = new ArrayList<>();
                for(Slot s2: slots.values()){
                    for(Slot s3: slots.values()){
                        if(s1.getX()+1==s2.getX()&& s2.getX()+1==s3.getX() && s1.getY()==s2.getY() && s2.getY()==s3.getY()){
                            if(s1.getStack().size()==level && s2.getStack().size()==level && s3.getStack().size()==level){
                                destinationSlotID.add(s1.getId());
                                destinationSlotID.add(s2.getId());
                                destinationSlotID.add(s3.getId());
                                if(canMoveContainer(containerId,destinationSlotID)){
                                    found = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(found)break;
                }
                if(found)break;
            }
            level++;
        }
        return destinationSlotID;
    }

    public Map<Integer,Container> getContainers() {
        return containers;
    }

    public void setContainers(Map<Integer,Container> containers) {
        this.containers = containers;
    }

    public Map<Integer, Slot> getSlots() {
        return slots;
    }

    public void setSlots(Map<Integer, Slot> slots) {
        this.slots = slots;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public ArrayList<Integer> findFreeSlotsEverywhere(int length, Map<Integer,Slot> slotsMap) {
        ArrayList<Integer> freeSlots;
        if(length==1){
            for(Slot slot : slotsMap.values()){
                freeSlots= new ArrayList<>();
                if(slot.getStack().size()==1) {
                    freeSlots.add(slot.getId());
                    return freeSlots;
                }
            }
        }
        if(length==2) {
            for (Slot slot1 : slotsMap.values()) {
                for (Slot slot2 : slotsMap.values()){
                    if(slot1.getStack().size() == 1&& slot2.getStack().size() == 1
                            && slot1.getX()+1 == slot2.getX() && slot1.getY() ==slot2.getY()){
                        freeSlots= new ArrayList<>();
                        freeSlots.add(slot1.getId());
                        freeSlots.add(slot2.getId());
                        return freeSlots;
                    }
                }

            }
        }

        if(length==3) {
            for (Slot slot1 : slotsMap.values()) {
                for (Slot slot2 : slotsMap.values()){
                    if(slot1.getStack().size() == slot2.getStack().size()
                            && slot1.getX()+1 == slot2.getX() && slot1.getY() ==slot2.getY()){
                        for(Slot slot3 : slotsMap.values()){
                            if(slot2.getStack().size() ==slot3.getStack().size() &&
                                    slot2.getX()+1==slot3.getX() && slot2.getY()==slot3.getY()){
                                freeSlots= new ArrayList<>();
                                freeSlots.add(slot1.getId());
                                freeSlots.add(slot2.getId());
                                freeSlots.add(slot3.getId());
                                return freeSlots;
                            }
                        }
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    public Log moveMidLength2(Log log,ArrayList<Integer> possibleFreeSlots, Crane craneToUse, Assignment assignment, double extra, Coordinate begin ) {
        //2 op elkaar volgende sloten
        boolean containermoved = false;
        // Check free slots in overlapping area
        for (int i = 0; i < possibleFreeSlots.size(); i++) {
            ArrayList<Integer> targetSlotIDs = new ArrayList<>();
            Slot slot1 = slots.get(possibleFreeSlots.get(i));
            assert slot1 != null;
            targetSlotIDs.add(slot1.getId());
            for (int j = 0; j < possibleFreeSlots.size(); j++) {
                Slot slot2 = slots.get(possibleFreeSlots.get(j));
                assert slot2 != null;

                if (slot1.getX() + 1 == slot2.getX() && slot1.getY() == slot2.getY()) {
                    targetSlotIDs.add(slot2.getId());
                    Coordinate containerEnd = new Coordinate(slot1.getX()+extra, slot1.getY() + 0.5);
                    // Verplaats container
                    log.setPickUpTime(craneToUse.getTimeCrane());
                    containermoved = craneToUse.doAssignement(this, assignment.getContainerID(), targetSlotIDs, begin, containerEnd);
                    if (containermoved) {
                        log.addPositions(begin, containerEnd);
                        log.setEndTime(craneToUse.getTimeCrane());
                        Main.updateCraneTime(craneToUse);
                        break;
                    } else {targetSlotIDs.clear();}
                }
            }
            //for loop stoppen als we container verplaatst hebben
            if (containermoved) {break;}
        }
        return log;
    }

    public Log moveMidLength1(Log log, ArrayList<Integer> possibleFreeSlots,Crane craneToUse, Assignment assignment,Coordinate begin) {
        boolean containermoved = true;
        // Check free slots in overlapping area
        while (!possibleFreeSlots.isEmpty() && containermoved) {
            ArrayList<Integer> targetSlotIDs = new ArrayList<>();
            int slotId = possibleFreeSlots.get(0);
            targetSlotIDs.add(slotId);
            if (canMoveContainer(assignment.getContainerID(), targetSlotIDs)) {
                Slot targetSlotShared = slots.get(slotId);
                //container length is 1 so
                Coordinate containerEnd = new Coordinate(targetSlotShared.getX() + 0.5, targetSlotShared.getY() + 0.5);
                // verplaats container
                log.setPickUpTime(craneToUse.getTimeCrane());
                craneToUse.doAssignement(this, assignment.getContainerID(), targetSlotIDs, begin, containerEnd);
                Main.updateCraneTime(craneToUse);
                log.addPositions(begin, containerEnd);
                log.setEndTime(craneToUse.getTimeCrane());
                //if container moved we can stop for loop
                containermoved = false;
            }
        }
        return log;
    }

    public Log moveMidLength3(Log log, ArrayList<Integer> possibleFreeSlots, Crane craneToUse, Assignment assignment, double extra, Coordinate begin) {
        //3 op elkaar volgende sloten
        boolean containermoved = false;
        // Check free slots in overlapping area
        for (int i = 0; i < possibleFreeSlots.size(); i++) {
            ArrayList<Integer> targetSlotIDs = new ArrayList<>();
            Slot slot1 = slots.get(possibleFreeSlots.get(i));
            assert slot1 != null;
            targetSlotIDs.add(slot1.getId());
            for (int j = 0; j < possibleFreeSlots.size(); j++) {
                Slot slot2 = slots.get(possibleFreeSlots.get(j));
                assert slot2 != null;
                for (int k = 0; k < possibleFreeSlots.size(); k++) {
                    Slot slot3 = slots.get(possibleFreeSlots.get(k));
                    assert slot3 != null;

                    if (slot1.getX() + 1 == slot2.getX() && slot2.getX() + 1 == slot3.getX() && slot1.getY() == slot2.getY() && slot2.getY() == slot3.getY()) {
                        targetSlotIDs.add(slot2.getId());
                        targetSlotIDs.add(slot3.getId());

                        if (slot1.getX() + 1 == slot2.getX() && slot2.getX() + 1 == slot3.getX()) {
                            Coordinate containerEnd = new Coordinate(slot1.getX() + extra, slot1.getY() + 0.5);
                            // Verplaats container
                            log.setPickUpTime(craneToUse.getTimeCrane());
                            containermoved = craneToUse.doAssignement(this, assignment.getContainerID(), targetSlotIDs, begin, containerEnd);
                            if (containermoved) {
                                log.addPositions(begin, containerEnd);
                                log.setEndTime(craneToUse.getTimeCrane());
                                Main.updateCraneTime(craneToUse);
//                                logs.add(log);
                                break;
                            } else {
                                targetSlotIDs.clear();
                            }
                        }
                    }
                    //for loop stoppen als we container verplaatst hebben
                    if (containermoved) {break;}
                }
            }
        }
        return log;
    }
}
