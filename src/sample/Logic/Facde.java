package sample.Logic;

import javafx.scene.layout.VBox;

import java.util.List;

public class Facde {
    private Repository repository = Repository.getINSTANCE();
    Scheduler scheduler;
   public List<VBox> getGanntRectangles(){


       String schedulerType = Repository.getSchedulerType();

       String interrupt = repository.getInterruption();
       float quantam = repository.getQuantam();
       Builder builder = new Builder(schedulerType,interrupt,quantam);

       scheduler =builder.getScheduler();
       return scheduler.getListOfGanttRectangles(Repository.getListOfProceses());
   }

   public float getAverageWaitingTime(){
       return scheduler.getAverageWaitingTime();
   }
}
