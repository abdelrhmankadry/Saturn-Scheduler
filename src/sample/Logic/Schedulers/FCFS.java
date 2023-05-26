package sample.Logic.Schedulers;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Logic.Scheduler;
import sample.Logic.Transforem;
import sample.Process;

import java.util.*;

public class FCFS implements Scheduler {
    float time = 1;
    boolean flag = true;
    Transforem transforem = new Transforem();
    float waitingTimeSummation = 0;
    int processesNumber = 0;
    @Override
    public List<VBox> getListOfGanttRectangles(List<Process> processes) {
        processesNumber = processes.size();
        List<Process> newList = new ArrayList<>() ;
        float trace = 0;
        for (int i = 0; i < processes.size(); i++) {
            newList.add(processes.get(i));
        }
        List<VBox> listOfRectangles = new ArrayList<>();

        while (!newList.isEmpty()){

            int index = 0;
            float min = newList.get(0).getArrivalTime();
            for (int i = 0; i < newList.size(); i++) {
                if (newList.get(i).getArrivalTime() < min) {
                    min = newList.get(i).getArrivalTime();
                    index = i;
                }
            }

            Process firstArriaval = newList.get(index);

            if(firstArriaval.getArrivalTime() > trace){
                Process gap = new Process(0,firstArriaval.getArrivalTime() - trace,trace,0);
                listOfRectangles.add(transforem.getGantt(gap));
                trace +=  gap.getTime() + firstArriaval.getTime();
            }else {
                waitingTimeSummation += (trace - firstArriaval.getArrivalTime());
                trace += firstArriaval.getTime();

            }
            listOfRectangles.add(transforem.getGantt(firstArriaval));
            newList.remove(index);
        }


        return listOfRectangles;
    }

    @Override
    public float getAverageWaitingTime() {
        return waitingTimeSummation / processesNumber;
    }

    private double getColor(){
        return (Math.random());
    }
    private VBox getGantt(Process process) {
        Rectangle gantt = new Rectangle(30 * process.getTime(),30,new Color(getColor(),getColor(),getColor(),getColor()));
        gantt.setStroke(new Color(0,0,0,1));
        HBox timeContainer = new HBox();
        float newTime = time + process.getTime();
        int size =34 - Float.toString(newTime - 1).length()*3 ;

        if(flag){
            Text text1 = new Text(Float.toString(time));
            timeContainer.getChildren().add(text1);
            Text text2 = new Text(String.format ("%.2f", time + process.getTime() - 1));
            Rectangle filler = new Rectangle(process.getTime() * 30 - 5* (text1.getText().length()  + text2.getText().length())-6,10,new Color(1,1,1,0));
            timeContainer.getChildren().add(filler);
            timeContainer.getChildren().add(text2);
             flag = false;

        }else{
            Text text1 = new Text("");
            Text text2 = new Text(String.format ("%.2f", newTime - 1));
            timeContainer.getChildren().add(text1);
            Rectangle filler = new Rectangle(process.getTime() * 30 - 6* (text1.getText().length() + text2.getText().length()),10,new Color(1,1,1,0));
            timeContainer.getChildren().add(filler);
            timeContainer.getChildren().add(text2);
        }
        time = time + process.getTime();
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(gantt,new Text("P"+process.getId()));
        VBox container = new VBox(1);
        container.setPrefWidth(20 * process.getTime());
        container.setMaxWidth(20 * process.getTime());
        container.getChildren().addAll(stackPane,timeContainer);
        return container;
    }
}
