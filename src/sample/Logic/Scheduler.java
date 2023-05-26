package sample.Logic;

import javafx.scene.layout.VBox;
import sample.Process;

import java.util.List;


public interface Scheduler {
List<VBox> getListOfGanttRectangles(List<Process> processes);
float getAverageWaitingTime();
}
