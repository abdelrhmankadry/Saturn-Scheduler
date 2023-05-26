package sample.Logic.Schedulers;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import sample.Logic.Scheduler;
import sample.Logic.Transforem;
import sample.Process;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SJFPreemptive implements Scheduler {
    private List<Process> newList = new ArrayList<>();
    private List<Process> waitingList = new ArrayList<>();
    private List<Process> sortedList ;
    float systemTime;
    private boolean flag = false;
    private boolean waitAsec = false;
    private int numberOfProcesses;
    private List<ProcessInformation> listOfProcessInformation = new ArrayList<>();
    List<Process>   list;
    @Override
    public List<VBox> getListOfGanttRectangles(List<Process> processes) {
        list = processes;
        numberOfProcesses = processes.size();
        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return Float.compare(o1.getArrivalTime(), o2.getArrivalTime());
            }
        });
        sortedList = new ArrayList<>(processes);
        Process process = getNewProcess();
        do{
            if (flag) {
                waitingList.remove(process);
                flag = false;
            }
            waitAsec = false;
            if (checkShorterJobExist(process)) {
                System.out.println("checked");
                 Process temp = process;
                 process = getNewProcess();
                 float processedTime = process.getArrivalTime() - systemTime;
                 newList.add(new Process(temp.getId(),processedTime,temp.getArrivalTime(),0));
                 systemTime += processedTime;
                 waitingList.add(new Process(temp.getId(),temp.getTime() - processedTime,0,0));
            } else {
                newList.add(process);
                systemTime += process.getTime();
                System.out.println(sortedList.size() + " " + waitingList.size());
                if (!waitingList.isEmpty()) {
                    process = getShorterJob(waitingList);
                    flag = true;
                }else {
                    if (!sortedList.isEmpty()) {
                        process = getNewProcess();
                        handleGap(process);
                        waitAsec = true;
                    }
                }
            }
        }
        while (!sortedList.isEmpty() || !waitingList.isEmpty() || waitAsec);
        Transforem transforem = new Transforem();
        List<VBox> listOfRectangles = new ArrayList<>();
        for (int i = 0; i < newList.size(); i++) {
            listOfRectangles.add(transforem.getGantt(newList.get(i)));
        }
        return listOfRectangles;

    }

    @Override
    public float getAverageWaitingTime() {
        float waitingTimeSummation = 0;
        calculateProcessInformation();
        for (ProcessInformation processInfo :
                listOfProcessInformation) {
            waitingTimeSummation += processInfo.completeTime - processInfo.arrivalTime - processInfo.brustTime;
        }
        return waitingTimeSummation/numberOfProcesses;
    }

    private boolean checkShorterJobExist(Process process){

        for (int i = 0 ; i < sortedList.size(); i++) {
            Process value = sortedList.get(i);
            float arrivalTime = value.getArrivalTime();
            float brustTime = value.getTime();
            if ((arrivalTime > systemTime && arrivalTime <= systemTime + process.getTime()) ) {
                if(brustTime < process.getTime() - (systemTime - arrivalTime ))
                return true;
                else{
                    waitingList.add(value);
                    sortedList.remove(value);
                }
            }
        }
        return false;
    }

    private Process getNewProcess(){
        Process firstProcess = sortedList.get(0);
        List<Process> answerList = new ArrayList<>();
        answerList.add(firstProcess);
        //sortedList.remove(0);
        int n = 0;
        n= sortedList.size();
        for (int i = 0; i<n;i++) {
            Process process = sortedList.get(i);
            if (process.getArrivalTime() == firstProcess.getArrivalTime()) {
                answerList.add(process);
            }
        }
        Process process = getShorterJob(answerList);
        sortedList.removeAll(answerList);
        answerList.remove(process);
        waitingList.addAll(answerList);
        return process;
    }

    private Process getShorterJob(List<Process> listOfProcesses){
        float min = 999;
        Process process = null;
        for (Process item :
                listOfProcesses) {
            if (item.getTime() < min) {
                process = item;
                min = item.getTime();
            }
        }
        if (process == null) System.out.println("Null Exception in getShorterJob");
        return process;
    }
    private void handleGap(Process process){

        newList.add(new Process(0,process.getArrivalTime() - systemTime,0,0));

    }

    void calculateProcessInformation() {
        for(int j = 1; j <= numberOfProcesses; j++){
            int id = j;
            int index=0 ;
            float completeTime;
            float accmulator = 0;
             for (int i = 0; i < newList.size(); i++) {
                 if (newList.get(i).getId() == id)
                     index = i;
             }
             for(int i = 0; i <= index;i++)
                accmulator += newList.get(i).getTime();
                completeTime = accmulator ;
                Process process = list.get(id-1);
                listOfProcessInformation.add(new ProcessInformation(process.getArrivalTime(),completeTime,process.getTime()));
        }
    }

    private class ProcessInformation{
        float arrivalTime;
        float completeTime;
        float brustTime;

        public ProcessInformation(float arrivalTime, float completeTime, float brustTime) {
            this.arrivalTime = arrivalTime;
            this.completeTime = completeTime;
            this.brustTime = brustTime;
        }
    }
}
