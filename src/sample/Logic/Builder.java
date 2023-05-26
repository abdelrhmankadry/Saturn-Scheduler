package sample.Logic;

import sample.Logic.Schedulers.*;

public class Builder {

    private String scheduler;
    private String interrupte;
    private float quantam;
    public Builder(String scheduler,String interrupte,float quantam){
        this.scheduler = scheduler;
        this.interrupte = interrupte;
        this.quantam = quantam;

    }

    public Scheduler getScheduler(){
       if(scheduler.equals("FCFS")){
           return new FCFS();
       } else if (scheduler.equals("SJF")){
           if(interrupte.equals("Preemptive"))
               return new SJFPreemptive();
           else if (interrupte.equals("Non-Preemptive"))
               return new SJFNonPreemptive();
       } else if (scheduler.equals("Priority")){
           if(interrupte.equals("Preemptive"))
               return new PriorityPeemptive();
           else if (interrupte.equals("Non-Preemptive"))
               return new PriorityNonPreemptive();
       } else if(scheduler.equals("Round Roubin")){
           return new RoundRoubin(quantam);
       }
       return null;
    }

}
