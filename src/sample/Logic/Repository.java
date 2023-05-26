package sample.Logic;

import sample.Process;

import java.util.List;

public class Repository {
    static List<Process> listOfProceses ;
    static String schedulerType;
    static String interruptino;
    static float quantam;

    private static Repository INSTANCE;
    private Repository(){

    }
    public static Repository getINSTANCE(){
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }
     public void setListOfProceses(List<Process> list){
        listOfProceses = list;
    }

    public  static List<Process> getListOfProceses() {
        return listOfProceses;
    }

    public  static String getSchedulerType() {
        return schedulerType;
    }

    public  void setSchedulerType(String schedulerType) {
        Repository.schedulerType = schedulerType;
    }

    public  String getInterruption() {
        return interruptino;
    }

    public  void setInterruption(String interruptino) {
        Repository.interruptino = interruptino;
    }

    public  float getQuantam() {
        return quantam;
    }

    public  void setQuantam(float quantam) {
        Repository.quantam = quantam;
    }
}
