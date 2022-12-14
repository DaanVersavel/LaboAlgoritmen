public class Log {
    private int craneId;
    private int containerId;
    private int pickUpTime;
    private int endTime;
    private int containerLength;
    private double pickUpPositionX;
    private double pickUpPositionY;
    private double endPositionX;
    private double endPositionY;
    private Boolean interval;

    public Log() {
        interval=false;
    }

    public Log(int craneId, int containerId, int containerLength){
        this.craneId=craneId;
        this.containerId=containerId;
        this.containerLength=containerLength;
        interval=false;
    }

    public int getCraneId() {
        return craneId;
    }

    public Boolean getInterval() {
        return interval;
    }

    public int getContainerLength() {
        return containerLength;
    }

    public void setContainerLength(int containerLength) {
        this.containerLength = containerLength;
    }

    public void setInterval(Boolean interval) {
        this.interval = interval;
    }

    public void setCraneId(int craneId) {
        this.craneId = craneId;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(int pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public double getPickUpPositionX() {
        return pickUpPositionX;
    }

    public void setPickUpPositionX(double pickUpPositionX) {
        this.pickUpPositionX = pickUpPositionX;
    }

    public double getPickUpPositionY() {
        return pickUpPositionY;
    }

    public void setPickUpPositionY(double pickUpPositionY) {
        this.pickUpPositionY = pickUpPositionY;
    }

    public double getEndPositionX() {
        return endPositionX;
    }

    public void setEndPositionX(double endPositionX) {
        this.endPositionX = endPositionX;
    }

    public double getEndPositionY() {
        return endPositionY;
    }

    public void setEndPositionY(double endPositionY) {
        this.endPositionY = endPositionY;
    }

    public void printLog() {
        System.out.println("CraneID:"+craneId+", ContainerID:"+containerId+", ContainerLength:"+containerLength+", PickupTime:"+pickUpTime+", EndTime: "+endTime+", PickupPositionX:"+
                pickUpPositionX+", PickupPositionY:"+pickUpPositionY+", EndPositionX:"+endPositionX+", EndPositionY:"+endPositionY);
    }

    public void addPositions(Coordinate begin, Coordinate end) {
        pickUpPositionX= begin.getXCoordinate();
        pickUpPositionY= begin.getYCoordinate();
        endPositionX=end.getXCoordinate();
        endPositionY= end.getYCoordinate();
    }
}
