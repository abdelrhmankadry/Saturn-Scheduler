package sample.Logic.Schedulers;

import javafx.scene.layout.VBox;
import sample.Logic.Scheduler;
import sample.Logic.Transforem;
import sample.Process;

import java.util.ArrayList;
import java.util.List;

public class PriorityPeemptive implements Scheduler {
    private List <props> process = new ArrayList<>();
    private float[] burstTime = new float[100];
    private int [] processId= new int[100];
    private float [] waitingTime= new float[100];
    private int [] priority= new int[100];
    private float[]arrival= new float[100];
    private int  n;
    private float avg_wt = 0;
    @Override
    public List<VBox> getListOfGanttRectangles(List<Process> processes) {
        n= processes.size();
        for (int i = 0; i < processes.size(); i++) {
            Process pros = processes.get(i);
            burstTime[i] = pros.getTime();
            processId[i] = pros.getId();
            priority[i] = pros.getPriority();
            arrival[i] = pros.getArrivalTime();
        }
        for (int i = 0; i < n; i++)
        {
            int pos = i;
            for (int j = i + 1; j < n; j++)
            {
                if (priority[j] < priority[pos])
                    pos = j;
            }

            int temp = priority[i];
            priority[i] = priority[pos];
            priority[pos] = temp;

            float tempFloat = burstTime[i];
            burstTime[i] = burstTime[pos];
            burstTime[pos] = tempFloat;
            //id:
            temp = processId[i];
            processId[i] = processId[pos];
            processId[pos] = temp;

            tempFloat = arrival[i];
            arrival[i] = arrival[pos];
            arrival[pos] = tempFloat;
        }
        calcwaiting_time(processId, arrival, burstTime, priority, waitingTime, n);

        for (int i = 0; i < n; i++)
        {
            avg_wt += waitingTime[i];
        }
        List<VBox> list = new ArrayList<>();
        Transforem transforem = new Transforem();
        for (int i = 0; i < process.size(); i++) {
            props props = process.get(i);
            Process item = new Process(props.id,props.timeburst,props.arrival,0 );
            list.add(transforem.getGantt(item));
        }

        return list;
    }

    @Override
    public float getAverageWaitingTime() {
        return avg_wt/n;
    }


    private void calcwaiting_time(int[] id, float[] arrival, float[] bt, int[] priority, float[] wait, int n)
    {
        int time = 0;
        float[] btc = new float[n];
        float largest = bt[0];
        for (int i = 0; i < n; i++)
        {
            btc[i] = bt[i];
            if (arrival[i] > largest)
            {
                largest = arrival[i];
            }
        }

        while (true)
        {
            boolean flag = true;
            for (int i = 0; i < n; i++)
            {
                if (arrival[i] <= time && btc[i] > 0)
                {
                    flag = false;
                    time++;
                    wait[i] = time - bt[i] - arrival[i];
                    btc[i]--;
                    if (process.size() == 0)
                    {
                        process.add(new props());
                        process.get(process.size() - 1).id = id[i];
                        process.get(process.size() - 1).timeburst = 1;
                    }
                    else
                    {
                        if (process.get(process.size() - 1).id == id[i])
                        {
                            process.get(process.size() - 1).timeburst++;
                        }
                        else
                        {
                            process.add(new props());
                            process.get(process.size() - 1).id = id[i];
                            process.get(process.size() - 1).timeburst = 1;
                        }
                    }
                    i = -1;
                }

            }

            if (flag && time > largest)
                break;
            else if (flag&& time < largest)
            {
                time++;
                if (process.size() == 0)
                {
                    process.add(new props());
                    process.get(process.size() - 1).id = 0;
                    process.get(process.size() - 1).timeburst = 1;
                }
                else
                {
                    if (process.get(process.size() - 1).id == 0)
                    {
                        process.get(process.size() - 1).timeburst++;
                    }
                    else
                    {
                        process.add(new props());
                        process.get(process.size() - 1).id = 0;
                        process.get(process.size() - 1).timeburst = 1;
                    }
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
