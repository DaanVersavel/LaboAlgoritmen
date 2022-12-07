import java.util.ArrayList;

public class ContainerField {
    private ArrayList<Container> containers  = new ArrayList<Container>();
    private ArrayList<Slot> slots  = new ArrayList<Slot>();
    private ArrayList<Assignment> assignments  = new ArrayList<Assignment>();

    //works only if there are no missing containers
    ContainerField(ArrayList<Container>containers, ArrayList<Slot>slots, ArrayList<Assignment>assignments){
        this.containers = containers;
        this.slots = slots;
        this.assignments = assignments;
        makeMatrix();
        placeContainers();
    }

    private void makeMatrix(){
        int maxX=0;
        int maxY=0;
        for(int i = 0; i < slots.size(); i++){
            Slot slot = slots.get(i);
            if (maxX<slot.getX()) maxX = slot.getX();
            if (maxY<slot.getY()) maxY = slot.getY();
        }
    }

    private void  placeContainers(){
        for(Assignment a : assignments){
            int cont_id = a.getContainer_id();
            ArrayList<Integer> slot_ids= a.getSlot_ids();
            for (int slot_id: slot_ids) {
                slots.get(slot_id-1).addContainer(cont_id);
            }
        }
        System.out.println("");
    }

    public boolean canRemoveContainer(int container_id) {
        ArrayList<Slot> containerSlots = new ArrayList<>();

        for(Slot slot: slots) {
            if(slot.getStack().contains(container_id)) containerSlots.add(slot);
        }
        for(Slot slot: containerSlots) {
            if(slot.getTopContainer() != container_id) return false;
        }
        return true;
    }

    public boolean canPlaceContainer(int container_id,ArrayList<Integer> destinationSlot_ids) {
        ArrayList<Slot> destinationSlots =  new ArrayList<>();
        for (Integer i: destinationSlot_ids){
            destinationSlots.add(slots.get(i-1));
        }
        //check if not surpass Maxheight
        for(Slot s : destinationSlots){
            if((s.getStack().size()+1)> s.getMaxHeight()) return false;
        }
        //check if slots have same height
        Slot s1= destinationSlots.get(0);
        for(int i=1;i<destinationSlot_ids.size();i++){
            if(s1.getStack().size() == destinationSlots.get(i).getStack().size()) return false;
        }
        //if all slots have same container id then we can place them
        s1= destinationSlots.get(0);
        int temp=0;
        for(int i=1;i<destinationSlot_ids.size();i++){
            if(s1.getTopContainer()==destinationSlots.get(i).getTopContainer()) {
                temp++;
            }
            s1= destinationSlots.get(i);
        }
        if(temp!=destinationSlots.size()) return true;

        //last one
        //credits to lucas
        for(Slot s : destinationSlots){
            ArrayList<Slot> containerSlots = new ArrayList<>();
            int lowerContainerID = s.getTopContainer();
            for(Slot slot: slots) {
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


    public void moveContainer(int container_id,ArrayList<Integer> destinationSlot_ids){
        ArrayList<Slot> sourceSlots = new ArrayList<>();
        ArrayList<Slot> destinationSlots = new ArrayList<>();
        // Update stack of source slots
        for (Slot s: slots) {
            if (s.getStack().contains(container_id)) sourceSlots.add(s);
        }
        for (Slot ss: sourceSlots) ss.removeTopContainer();

        // Update stack of destination slots
        for(int ds_id: destinationSlot_ids) destinationSlots.add(slots.get(ds_id));
        for (Slot ds: destinationSlots) ds.addContainer(container_id);
    }
}
