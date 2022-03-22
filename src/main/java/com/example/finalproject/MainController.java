package com.example.finalproject;
import com.example.finalproject.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class MainController {

    private Controller service;
    private Stage primaryStage;

    @FXML
    private DatePicker endDate1;

    @FXML
    private TextField friendEmail1;

    @FXML
    private DatePicker startDate1;

    @FXML
    private Label userLoggedInLbl;


    public void setService(Controller service) {
        this.service = service;
        userLoggedInLbl.setText(service.findOneByEmail(service.getCurrentEmail()).getFirstName() + " " + service.findOneByEmail(service.getCurrentEmail()).getLastName());
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
    public void handleLogOutButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Connefy");
        primaryStage.setScene(scene);

        LoginController loginController = fxmlLoader.getController();
        loginController.setController(service);
        loginController.setStage(primaryStage);
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

    public void handleGeneralButton(ActionEvent actionEvent) {
        try{
            LocalDate start = startDate1.getValue();
            LocalDate end = endDate1.getValue();
            if (start.isAfter(end)) {
                MessageAlert.showErrorMessage(null, "Invalid time interval!");
            } else {
                try {
                    this.service.saveGeneralReport(this.service.getCurrentEmail(), Timestamp.valueOf(start.atStartOfDay().plusDays(1).minusSeconds(1)), Timestamp.valueOf(end.atStartOfDay()));
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Report generated successfully into E:/PDFExport/reports.pdf !");
                } catch (IOException e) {
                    MessageAlert.showErrorMessage(null, "Could not save report as pdf!");
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            MessageAlert.showErrorMessage(null, "Please pick a date!");
        }
    }

    public void handleMessagesReport(ActionEvent actionEvent) {
        try {
            LocalDate start = startDate1.getValue();
            LocalDate end = endDate1.getValue();
            if (start.isAfter(end)) {
                MessageAlert.showErrorMessage(null, "Invalid time interval!");
            } else {
                String friend = friendEmail1.getText();
                try {
                    if (friendEmail1.getText().isEmpty()) {
                        MessageAlert.showErrorMessage(null, "Please write your friend email first!");
                        return;
                    }
                    this.service.saveConversation(this.service.getCurrentEmail(), friend, Timestamp.valueOf(start.atStartOfDay().plusDays(1).minusSeconds(1)), Timestamp.valueOf(end.atStartOfDay()));
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Report generated successfully into E:/PDFExport/reports.pdf !");
                } catch (IOException e) {
                    MessageAlert.showErrorMessage(null, "Could not save report as pdf!");
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, "Please pick a date and write your friend's email!");
        }
    }

    @FXML
    void handleEventsButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("events-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Connefy");
        primaryStage.setScene(scene);

        EventsController eventsController = fxmlLoader.getController();
        eventsController.setService(service);
        eventsController.setStage(primaryStage);
    }
}
