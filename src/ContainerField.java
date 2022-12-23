import java.util.ArrayList;
import java.util.Map;

public class ContainerField {
    private Map<Integer,Container> containers;
    private Map<Integer, Slot> slots;
    private ArrayList<Assignment> assignments;
    private int maxHeight;

    //works only if there are no missing containers
    ContainerField(Map<Integer,Container>containers, Map<Integer, Slot>slots, ArrayList<Assignment>assignments, int maxheight){
        this.containers = containers;
        this.slots = slots;
        this.assignments = assignments;
        this.maxHeight = maxheight;
        placeContainers();
    }

    private void  placeContainers(){
        for(Assignment a : assignments){
            int cont_id = a.getContainer_id();
            ArrayList<Integer> slot_ids= a.getSlot_idArray();

            for (int slot_id: slot_ids) {
                Slot s= slots.get(slot_id);
                s.addContainer(cont_id);
//                for(Slot slot : slots){
//                    if(slot.getId() ==slot_id) slot.addContainer(cont_id);
//                }
            }
        }
    }

    public boolean canRemoveContainer(int container_id) {
        ArrayList<Slot> containerSlots = new ArrayList<>();

        for(Slot slot: slots.values()) {
            if(slot.getStack().contains(container_id)) containerSlots.add(slot);
        }

        for(Slot slot: containerSlots) {
            if(slot.getTopContainer() != container_id) return false;
        }
        return true;
    }
    public int canRemoveContainer2(int container_id) {
        ArrayList<Slot> containerSlots = new ArrayList<>();

        for(Slot slot: slots.values()) {
            if(slot.getStack().contains(container_id)) containerSlots.add(slot);
        }

        for(Slot slot: containerSlots) {
            if(slot.getTopContainer() != container_id) return slot.getStack().search(container_id);
        }
        return -1;
    }

    public boolean canPlaceContainer(int container_id,ArrayList<Integer> destinationSlot_ids) {
        ArrayList<Slot> destinationSlots =  new ArrayList<>();
        for (Integer slotid: destinationSlot_ids){
            Slot s = slots.get(slotid);
            destinationSlots.add(s);
            //destinationSlots.add(slots.get(i-1));
//            for(Slot slot : slots){
//                if(slot.getId()==slotid) destinationSlots.add(slot);
//            }
        }
        //check if not surpass Maxheight
        for(Slot s : destinationSlots){
            if((s.getStack().size()+1)> s.getMaxHeight()) return false;
        }
        //check if slots have same height
        Slot s1= destinationSlots.get(0);
        for(int i=1;i<destinationSlot_ids.size();i++){
            if(s1.getStack().size() != destinationSlots.get(i).getStack().size()) return false;
        }
        //if all slots have same container id then we can place them
        s1= destinationSlots.get(0);
        int temp=0;
        if(!s1.getStack().isEmpty()){
            for(int i=1;i<destinationSlot_ids.size();i++){
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

    public boolean canMoveContainer(int container_id, ArrayList<Integer> destinationSlot_ids) {
        // Check if replacement is possible
        if(!canRemoveContainer(container_id) || !canPlaceContainer(container_id, destinationSlot_ids))
            return false;
        return true;
    }
    public int canMoveContainer2(int container_id, ArrayList<Integer> destinationSlot_ids) {
        // Check if replacement is possible
        //-1 if we can remove container
        int canRemoveContainer=canRemoveContainer2(container_id);
        if(canRemoveContainer!=-1) return canRemoveContainer;
        if(!canPlaceContainer(container_id, destinationSlot_ids)) return -2;
        return -1;
        //return -1 als we hem kunnen verplaatsen
        //return -2 als we hem nit kunnen plaaatsen
        //return 0 of hoger als we de containers er boven moeten verplaatsen
    }


    public void moveContainer(int container_id,ArrayList<Integer> destinationSlot_ids){
        ArrayList<Slot> sourceSlots = new ArrayList<>();
        ArrayList<Slot> destinationSlots = new ArrayList<>();
        // TODO is dit niet nutteloos eigenlijk dat eerst toevoegen aan lijst om ze dan aantepassen
        // Update stack of source slots
        for (Slot s: slots.values()) {
            if (s.getStack().contains(container_id)) sourceSlots.add(s);
        }
        for (Slot ss: sourceSlots) ss.removeTopContainer();
        // Update stack of destination slots
        for(Slot s: slots.values()){
            for(int id: destinationSlot_ids){
                if(s.getId() == id){
                    destinationSlots.add(s);
                }
            }
        }
        for (Slot ds: destinationSlots) ds.addContainer(container_id);
    }

    public ArrayList<Integer> findBestTargetSlot1(int targetHeight, Integer containerId) {
        boolean found = false;
        int level=0; //no container placed
        ArrayList<Integer> destinationSlotID = new ArrayList<>();
        //start at bottom
//        ArrayList<Integer> destinationSlot;
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
//                                firstEndslot=s1;
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
//                                firstEndslot=s1;
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

    //TODO niet algemeen maar weet niks beters
    public ArrayList<Integer> findFreeSlotsEverywhere(int length, Map<Integer,Slot> slotsMap) {
        if(length==1){
            for(Slot slot : slotsMap.values()){
                if(slot.getStack().size()==1) {
                    return new ArrayList<>(slot.getId());
                }
            }
        }
        if(length==2) {
            for (Slot slot1 : slotsMap.values()) {
                for (Slot slot2 : slotsMap.values()){
                    if(slot1.getStack().size() == 1&& slot2.getStack().size() == 1
                            && slot1.getX()+1 == slot2.getX() && slot1.getY() ==slot2.getY()){
                        ArrayList<Integer> freeSlots= new ArrayList<>();
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
                    if(slot1.getStack().size() == 1&& slot2.getStack().size() == 1
                            && slot1.getX()+1 == slot2.getX() && slot1.getY() ==slot2.getY()){
                        for(Slot slot3 : slotsMap.values()){
                            if(slot2.getStack().size() ==slot3.getStack().size() &&
                                    slot2.getX()+1==slot3.getX() && slot2.getY()==slot3.getY()){
                                ArrayList<Integer> freeSlots= new ArrayList<>();
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

        return null;
    }
}
