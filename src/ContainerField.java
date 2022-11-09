import java.util.ArrayList;

public class ContainerField {
    private ArrayList<Container> containers  = new ArrayList<Container>();
    private ArrayList<Slot> slots  = new ArrayList<Slot>();
    private ArrayList<Assignment> assignments  = new ArrayList<Assignment>();


    ContainerField(ArrayList<Container>containers, ArrayList<Slot>slots, ArrayList<Assignment>assignments){
        this.containers = containers;
        this.slots = slots;
        this.assignments = assignments;
        makeMatrix();
        placecontainers();
    }
    private void  placecontainers(){
        for(Assignment a : assignments){
            int cont_id=a.getContainer_id();
            ArrayList<Integer> slot_ids=a.getSlot_id();
            for(int i=0; i<slot_ids.size(); i++){
                Slot s= slots.get(i);
                s.addContainer(cont_id);
            }
        }
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
}
