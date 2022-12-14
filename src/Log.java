public class Log {
    private int craneId;
    private int containerId;
    private int pickUpTime;
    private int endTime;
    private double pickUpPositionX;
    private double pickUpPositionY;
    private double endPositionX;
    private double endPositionY;

    public Log() {}

    public int getCraneId() {
        return craneId;
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
        System.out.println(craneId+","+containerId+","+pickUpTime+","+endTime+","+pickUpPositionX+","+
                pickUpPositionY+","+endPositionX+","+endPositionY);
    }
}
