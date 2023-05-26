package sample;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import sample.Logic.Repository;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    ChoiceBox<String> scheduler_choice_box;

    @FXML
    Button add_process_btn;

    @FXML
    TableView<Process> process_table;

    @FXML
    TableColumn process_id_column;

    @FXML
    TableColumn time_column;

    @FXML
    TableColumn arrival_time_column;

    @FXML
    TableColumn priority_column;

    @FXML
    TextField time_quantam_tf;

    @FXML
    Button simulate_btn;

    @FXML
    ChoiceBox<String> interruption_choice_box;

    double tableSize;
    int index = 1;
    ObservableList<Process> data = FXCollections.observableArrayList();
    Boolean flag_RR = false;
    Boolean flag_interruption = false;
    private boolean flag_FCFS =true;

    @FXML
    void initialize(){
        scheduler_choice_box.getItems().addAll("FCFS","SJF","Priority","Round Roubin");
        interruption_choice_box.getItems().addAll("Preemptive","Non-Preemptive");
        initilizeTable();
        tableSize = process_table.getHeight();
        scheduler_choice_box.getSelectionModel().selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    switch (new_val.intValue()){
                        case 0 : FCFSselected(); break;
                        case 1 : SJFselected();break;
                        case 2 : Priorityselected(); break;
                        case 3 : RRselected(); break;
                    }
                } );
        time_quantam_tf.setVisible(false);
        interruption_choice_box.setVisible(false);
        time_quantam_tf.setTranslateY(-37);
        initializeAnimation();

    }

    private void RRselected() {
        if(ComingFromFCFS()){
            showTF();
            animateSimulateBtnDown();
        }else {
            time_quantam_tf.setVisible(true);
            interruption_choice_box.setVisible(false);
        }
        priority_column.setVisible(false);
        flag_RR = true;
        flag_FCFS = false;
        flag_interruption = false;
    }

    private void showTF() {
        PauseTransition showBtn = new PauseTransition(Duration.seconds(0.3));
        showBtn.setOnFinished(e -> {time_quantam_tf.setVisible(true);
        });
        showBtn.play();
    }

    private boolean ComingFromFCFS() {
        return flag_FCFS;
    }

    private void Priorityselected() {
        if(ComingFromFCFS()){
            showCB();
            animateSimulateBtnDown();
        } else if(comingFromRR()){
            time_quantam_tf.setVisible(false);
            interruption_choice_box.setVisible(true);
        }
        priority_column.setVisible(true);
        //time_quantam_tf.setVisible(false);
//        if(flag_RR){
//            animateSimulateBtn(0.3);
//            flag_RR = false;
//        }
        flag_interruption = true;
        flag_FCFS = false;
        flag_RR = false;
    }

    private boolean comingFromRR() {
        return flag_RR;
    }

    private void showCB() {
        PauseTransition showBtn = new PauseTransition(Duration.seconds(0.3));
        showBtn.setOnFinished(e -> interruption_choice_box.setVisible(true));
        showBtn.play();
    }

    private void SJFselected() {
        if(ComingFromFCFS()){
            showCB();
            animateSimulateBtnDown();
        } else if(comingFromRR()){
            time_quantam_tf.setVisible(false);
            interruption_choice_box.setVisible(true);
        }
        priority_column.setVisible(false);
        //time_quantam_tf.setVisible(false);
//        if(flag_RR){
//            animateSimulateBtn(0.3);
//            flag_RR = false;
//        }
        flag_interruption = true;
        flag_FCFS = false;
        flag_RR = false;
    }

    private void FCFSselected() {
        if(comingFromSjfOrPriority()){
            interruption_choice_box.setVisible(false);
            animateSimulateBtn(0.3);
        } else if(comingFromRR()){
            time_quantam_tf.setVisible(false);
            animateSimulateBtn(0.3);
        }
        priority_column.setVisible(false);

//        if(flag_RR){
//            animateSimulateBtn(0.3);
//            flag_RR = false;
//        }
        flag_FCFS = true;
        flag_interruption = false;
        flag_RR = false;

    }

    private boolean comingFromSjfOrPriority() {
        return flag_interruption;
    }

    @FXML
    void onAddProcessClicked(){
        if(index != 1){
            process_table.setPrefHeight(process_table.getHeight()+24);
        }

        data.add(new Process(index,0,0,0));
        index++;
        process_table.setItems(data);
    }
    void initilizeTable(){

        process_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        time_column.setCellValueFactory(new PropertyValueFactory<>("time"));
        arrival_time_column.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priority_column.setCellValueFactory(new PropertyValueFactory<>("priority"));

        time_column.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        time_column.setOnEditCommit(t ->{
            ((Process) ((TableColumn.CellEditEvent<Process,Float>) t).getTableView()
                    .getItems()
                    .get(((TableColumn.CellEditEvent<Process,Float>) t).getTablePosition().getRow()))
                    .setTime(((TableColumn.CellEditEvent<Process,Float>) t).getNewValue());
        });

        arrival_time_column.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        arrival_time_column.setOnEditCommit(t ->{
            ((Process) ((TableColumn.CellEditEvent<Process,Float>) t).getTableView()
                    .getItems()
                    .get(((TableColumn.CellEditEvent<Process,Float>) t).getTablePosition().getRow()))
                    .setArrivalTime(((TableColumn.CellEditEvent<Process,Float>) t).getNewValue());
        });

        priority_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priority_column.setOnEditCommit(t ->{
            ((Process) ((TableColumn.CellEditEvent<Process,Integer>) t).getTableView()
                    .getItems()
                    .get(((TableColumn.CellEditEvent<Process,Integer>) t).getTablePosition().getRow()))
                    .setPriority(((TableColumn.CellEditEvent<Process,Integer>) t).getNewValue());
        });

        data.add(new Process(index,0,0,0));
        index++;
        process_table.setItems(data);

        process_table.setEditable(true);
    }

    @FXML
    void onNewSimulationClicked(){
        data = FXCollections.observableArrayList();
        index=1;
        process_table.setItems(data);
        process_table.setPrefHeight(tableSize);
    }

    private void animateSimulateBtn(double duration){
        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setDuration(Duration.seconds(duration));
        translateTransition.setNode(simulate_btn);
        translateTransition.setByY(-37);

        translateTransition.play();

    }
    private void initializeAnimation(){
        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setDuration(Duration.seconds(0.1));
        translateTransition.setNode(simulate_btn);
        translateTransition.setByY(-74);

        translateTransition.play();

    }
    void animateSimulateBtnDown(){
        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setDuration(Duration.seconds(0.3));
        translateTransition.setNode(simulate_btn);
        translateTransition.setByY(37);

        translateTransition.play();
    }

    @FXML
    void onSimulateClicked() throws IOException {

        ArrayList<Process> list = new ArrayList<>();
        process_table.getItems().iterator().forEachRemaining(list::add);

        Repository repo = Repository.getINSTANCE();

        if(!time_quantam_tf.getText().equals(""))
            repo.setQuantam(Float.parseFloat(time_quantam_tf.getText()));

        repo.setListOfProceses(list);
        repo.setSchedulerType(scheduler_choice_box.getValue());
        repo.setInterruption(interruption_choice_box.getValue());

        if(scheduler_choice_box.getValue() != null){
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("canvas.fxml"));
            primaryStage.setTitle("Simulation");
             Scene scene = new Scene(root, 1200, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }


    }
    @FXML
    void onClick(){

    }
}
