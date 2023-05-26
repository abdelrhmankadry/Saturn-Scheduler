package sample.canvas;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Logic.Facde;
import sample.Logic.Repository;
import sample.Logic.Schedulers.FCFS;

import java.util.List;

public class CanvasController {
    @FXML
    HBox container;

    @FXML
    ScrollPane scroll;

    @FXML
    Pane parent;
    @FXML
    Text averageWaitingTime;
    @FXML
    void initialize(){
        Facde facde = new Facde();
        List<VBox> list = facde.getGanntRectangles();
        averageWaitingTime.setText(String.valueOf(facde.getAverageWaitingTime()));
        for (int i = 0; i < list.size(); i++) {
            container.getChildren().add(list.get(i));
        }

        System.out.println(list.size());
    }
}
