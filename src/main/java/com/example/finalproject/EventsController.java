package com.example.finalproject;

import com.example.finalproject.controller.Controller;
import com.example.finalproject.domain.Event;
import com.example.finalproject.domain.User;
import com.example.finalproject.utils.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class EventsController implements Observer {

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, String> dateColumnComing;

    @FXML
    private TextField description;

    @FXML
    private TableColumn<Event, String> descriptionColumn;

    @FXML
    private TableColumn<Event, String> descriptionColumnComing;

    @FXML
    private DatePicker eventDate;

    @FXML
    private TableView<Event> eventsView;

    @FXML
    private TableView<Event> eventsViewComing;

    @FXML
    private Label userLoggedInLbl;


    private Controller service;
    private Stage primaryStage;
    ObservableList<Event> modelEvents = FXCollections.observableArrayList();
    ObservableList<Event> modelEventsComing = FXCollections.observableArrayList();

    public void setService(Controller service){
        this.service = service;
        this.service.addObserver(this);
        userLoggedInLbl.setText(service.findOneByEmail(service.getCurrentEmail()).getFirstName() + " " + service.findOneByEmail(service.getCurrentEmail()).getLastName());
        initModel();
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize(){
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("date"));
        eventsView.setItems(modelEvents);

        descriptionColumnComing.setCellValueFactory(new PropertyValueFactory<Event, String>("description"));
        dateColumnComing.setCellValueFactory(new PropertyValueFactory<Event, String>("date"));
        eventsViewComing.setItems(modelEventsComing);
    }

    private void initModel() {
        List<Event> eventList = this.service.getEventsNotParticipate(this.service.findOneByEmail(this.service.getCurrentEmail()));
        modelEvents.setAll(eventList);

        List<Event>eventListComing = this.service.getComingEvents(this.service.findOneByEmail(this.service.getCurrentEmail()));
        modelEventsComing.setAll(eventListComing);
    }

    @FXML
    void handleCreateEvent(ActionEvent event) {
        try {
            String date = eventDate.getValue().format(Constants.DATE_TIME_FORMATTER_DP);
            String descript = description.getText();
            List<User> participants = Arrays.asList(this.service.findOneByEmail(this.service.getCurrentEmail()));
            this.service.addEvent(descript, participants, date);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Event created successfully!");
        }catch(Exception e){
            MessageAlert.showErrorMessage(null,"Please pick a date and write a description for your event!");
        }
    }

    @FXML
    public void handleMyFriendsButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("myfriends-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("My friends");
        primaryStage.setScene(scene);
        MyFriendsController myFriendsController = fxmlLoader.getController();
        myFriendsController.setService(service);
        myFriendsController.setStage(primaryStage);
    }

    @FXML
    public void handleRequestsButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("requests_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Connefy");
        primaryStage.setScene(scene);

        RequestsController requestsController = fxmlLoader.getController();
        requestsController.setService(service);
        requestsController.setStage(primaryStage);
    }

    @FXML
    public void handleMessangerButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("messanger-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Connefy");
        primaryStage.setScene(scene);

        MessengerController messangerController = fxmlLoader.getController();
        messangerController.setService(service);
        messangerController.setStage(primaryStage);
    }

    @FXML
    void handleSubscribeButton(ActionEvent event) {
        Event selected = eventsView.getSelectionModel().getSelectedItem();
        if(selected != null){
            List<User> participants = selected.getParticipants();
            participants.add(this.service.findOneByEmail(this.service.getCurrentEmail()));
            selected.setParticipants(participants);
            this.service.updateEvent(selected);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Subscribed to the event!");
        }
        else
        {
            MessageAlert.showErrorMessage(null, "Must select an event!");
        }
    }

    @FXML
    void handleUnsubscribeButton(ActionEvent event) {
        Event selected = eventsViewComing.getSelectionModel().getSelectedItem();
        if(selected != null){
            List<User> participants = selected.getParticipants();
            participants.remove(this.service.findOneByEmail(this.service.getCurrentEmail()));
            selected.setParticipants(participants);
            this.service.updateEvent(selected);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Unsubscribed to the event!");
        }
        else
        {
            MessageAlert.showErrorMessage(null, "Must select an event!");
        }
    }

    @FXML
    void home(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Connefy");
        primaryStage.setScene(scene);

        MainController mainController = fxmlLoader.getController();
        mainController.setService(service);
        mainController.setStage(primaryStage);
    }

    @Override
    public void update(Observable o, Object arg) {
        initModel();
    }
}
