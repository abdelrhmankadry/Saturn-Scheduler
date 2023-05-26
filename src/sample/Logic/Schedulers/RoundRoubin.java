package sample.Logic.Schedulers;

import javafx.scene.layout.VBox;
import sample.Logic.Scheduler;
import sample.Logic.Transforem;
import sample.Process;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class RoundRoubin implements Scheduler {
    private  float quantam;
    private List<props> processeses = new ArrayList<>();
   private int[] processes = new int[100];
    private float[] arrival = new float[100];
    private  float[] burst_time = new float[100];
    private float total_wt = 0;
    private int n;
    public RoundRoubin(float quantam){
        this.quantam = quantam;
    }

    @Override
    public List<VBox> getListOfGanttRectangles(List<Process> processesInput) {
         n = processesInput.size();
        for (int i = 0; i < processesInput.size(); i++) {
            Process process= processesInput.get(i);
            processes[i] = process.getId();
            arrival[i] = process.getArrivalTime();
            burst_time[i] = process.getTime();
        }
        findavgTime(processes, n, burst_time, arrival, quantam);
        List<Process> list = new ArrayList<>();
        for (int i = 0; i < processeses.size(); i++) {
            Process process = new Process(processeses.get(i).id,processeses.get(i).timeburst,processeses.get(i).arrival,0);
            list.add(process);
        }

        Transforem transforem = new Transforem();
        List<VBox> listOfRectangles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listOfRectangles.add(transforem.getGantt(list.get(i)));
        }
        return listOfRectangles;
    }

    @Override
    public float getAverageWaitingTime() {
        return total_wt/n;
    }

    void findavgTime(int [] processes, int n, float[] bt, float[] arrival,
                    float quantum) {
        float[] wt = new float[100];
        float[] tat = new float[100];
        float  total_tat = 0;

        // Function to find waiting time of all processes
        findWaitingTime(processes, n, bt, wt, arrival, quantum);

        // Function to find turn around time for all processes
        findTurnAroundTime(processes, n, bt, wt, tat);
        for (int i = 0; i < n; i++)
        {
            total_wt = total_wt + wt[i];
            total_tat = total_tat + tat[i];
        }
    }

    void findTurnAroundTime(int[] processes, int n,
                            float[] bt, float[] wt, float[] tat)
    {
        // calculating turnaround time by adding
        // bt[i] + wt[i]
        for (int i = 0; i < n; i++)
            tat[i] = bt[i] + wt[i];
    }

    void findWaitingTime(int [] processes, int n,
                         float[] bt, float[] wt, float[] arrival, float quantum)
    {
        // Make a copy of burst times bt[] to store remaining
        // burst times.
        float []rem_bt = new float[100];
        for (int i = 0; i < n; i++)
            rem_bt[i] = bt[i];

        float t = 0; // Current time
        float largest = arrival[0];
        for (int i = 1; i < n; i++)
        {
            if (arrival[i] > largest)
                largest = arrival[i];
        }

        // Keep traversing processes in round robin manner
        // until all of them are not done.
        while (true)
        {
            boolean done = true;

            // Traverse all processes one by one repeatedly
            for (int i = 0; i < n; i++)
            {
                // If burst time of a process is greater than 0
                // then only need to process further
                if (rem_bt[i] > 0 && arrival[i] <= t)
                {
                    done = false; // There is a pending process
                    processeses.add(new props());

                    if (rem_bt[i] > quantum)
                    {
                        processeses.get(processeses.size() - 1).id = processes[i];

                        // Increase the value of t i.e. shows
                        // how much time a process has been processed
                        t += quantum;

                        // Decrease the burst_time of current process
                        // by quantum
                        rem_bt[i] -= quantum;
                        processeses.get(processeses.size() - 1).timeburst = quantum;
                    }

                    // If burst time is smaller than or equal to
                    // quantum. Last cycle for this process
                    else
                    {
                        processeses.get(processeses.size() - 1).id = processes[i];
                        // Increase the value of t i.e. shows
                        // how much time a process has been processed
                        t = t + rem_bt[i];

                        // Waiting time is current time minus time
                        // used by this process
                        wt[i] = t - bt[i] - arrival[i];

                        // As the process gets fully executed
                        // make its remaining burst time = 0
                        processeses.get(processeses.size() - 1).timeburst = rem_bt[i];
                        rem_bt[i] = 0;
                    }
                }
            }

            // If all processes are done
            if (done && t > largest)
                break;
            else if (done && t < largest)
            {
                t++;
                if (processeses.size() > 0)
                {
                    if (processeses.get(processeses.size() - 1).id == 0 && processeses.get(processeses.size() - 1).timeburst < quantum)
                    {
                        processeses.get(processeses.size() - 1).timeburst++;
                    }
                    else
                    {
                        processeses.add(new props());
                        processeses.get(processeses.size() - 1).id = 0;
                        processeses.get(processeses.size() - 1).timeburst = 1;
                    }
                }
                else
                {
                    processeses.add(new props());
                    processeses.get(processeses.size() - 1).id = 0;
                    processeses.get(processeses.size() - 1).timeburst = 1;
                }
            }
        }
    }

        private class props{
        int id;
        float timeburst;
        float arrival;
    }
}
