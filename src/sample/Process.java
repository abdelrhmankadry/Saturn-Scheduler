package sample;

public class Process {
    private int id;
    private  float time;
    private float arrivalTime;
    private int priority;


    public Process(int id, float time, float arrivalTime, int priority) {
        this.id = id;
        this.time = time;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setArrivalTime(float arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public float getArrivalTime() {
        return arrivalTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
