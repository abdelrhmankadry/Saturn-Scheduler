package sample.Logic;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Process;

import java.util.*;

public class Transforem {

    private float time =0;
    private boolean flag = true;
    Random random = new Random();
    Map<Integer,Double> listOfColor = new HashMap<Integer, Double>();
    public VBox getGantt(Process process) {//changed
        Rectangle gantt = new Rectangle(50 * process.getTime(),30,new Color(getColor(process.getId()),getColor(process.getId()*2),getColor(process.getId()*3),getColor(process.getId()*4)));
        gantt.setStroke(new Color(0,0,0,1));
        HBox timeContainer = new HBox();
        float newTime = time + process.getTime();
        int size =34 - Float.toString(newTime-1 ).length()*3 ; //changed

        if(flag){
            Text text1 = new Text(Float.toString(time));
            timeContainer.getChildren().add(text1);
            Text text2 = new Text(String.format ("%.2f", time + process.getTime() ));//changed 30 -> 50 //remove -1
            Rectangle filler = new Rectangle(process.getTime() * 50 - 5* (text1.getText().length()  + text2.getText().length())-6,10,new Color(1,1,1,0));
            timeContainer.getChildren().add(filler);
            timeContainer.getChildren().add(text2);
            flag = false;

        }else{
            Text text1 = new Text("");
            Text text2 = new Text(String.format ("%.2f", newTime )); //remove -1
            timeContainer.getChildren().add(text1);//changed 30 -> 50
            Rectangle filler = new Rectangle(process.getTime() * 50 - 6* (text1.getText().length() + text2.getText().length()),10,new Color(1,1,1,0));
            timeContainer.getChildren().add(filler);
            timeContainer.getChildren().add(text2);
        }
        time = time + process.getTime();
        StackPane stackPane = new StackPane();
        String processId = "P"+process.getId();
        if(process.getId() == 0)
            processId = "";
        stackPane.getChildren().addAll(gantt,new Text(processId));
        VBox container = new VBox(1);
        container.setPrefWidth(20 * process.getTime());
        container.setMaxWidth(20 * process.getTime());
        container.getChildren().addAll(stackPane,timeContainer);
        return container;
    }
    private double getColor(int id){
        double randNum = Math.random();
        if(listOfColor.containsKey(0))
             listOfColor.put(0,1.00);
        if(listOfColor.containsKey(id))
            return listOfColor.get(id);
        else{
        listOfColor.put(id,randNum);
        return randNum;
        }
    }
}
