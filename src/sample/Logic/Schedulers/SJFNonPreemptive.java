package sample.Logic.Schedulers;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import sample.Logic.Scheduler;
import sample.Logic.Transforem;
import sample.Process;

import java.util.ArrayList;
import java.util.List;

public class SJFNonPreemptive implements Scheduler {
    private float systemTime=0;
    private int  total=0;
    private List<Process> newList = new ArrayList <>();
    boolean isGap = false;
    float trace = 0;
    private float waitingTimeSummation = 0;
    private int processesNumber;

    @Override
    public List<VBox> getListOfGanttRectangles(List<Process> processes) {
        boolean a = true;
        int n = processes.size();
        processesNumber = processes.size();
        while(true)
        {
            int c=n;
            float min=999;
            if (total == n) // total no of process = completed process loop will be terminated
                break;

            for (int i=0; i<processes.size(); i++)
            {
                Process current = processes.get(i);
                if ((current.getArrivalTime() <= systemTime)  && (current.getTime()<min))
                {
                    min=current.getTime();
                    c=i;
                }
            }

            if(isGap && c != n)   {
                newList.add(new Process(0,processes.get(c).getArrivalTime() - trace,0,0));
                systemTime = processes.get(c).getArrivalTime();
                isGap = false;
            }
            /* If c==n means c value can not updated because no process arrival time< system time so we increase the system time */
            if (c==n){
               if(!isGap){
                   trace = systemTime;
                   isGap = true;
               }
                systemTime++;
            }
            else
            {

                newList.add(processes.get(c));
                float completeTime = systemTime + processes.get(c).getTime();
                systemTime += processes.get(c).getTime();
                float waitingTime = completeTime -processes.get(c).getArrivalTime()- processes.get(c).getTime();
                waitingTimeSummation += waitingTime;
                processes.remove(c);
                total++;
        }

    }
        //making gantt chart
        List<VBox> listOfRectangles = new ArrayList<>();
        Transforem transforem =new Transforem();
        for(int i = 0;i < newList.size();i++){
            listOfRectangles.add(transforem.getGantt(newList.get(i)));
        }
        return listOfRectangles;
    }

    @Override
    public float getAverageWaitingTime() {
        return waitingTimeSummation/processesNumber;
    }
}
