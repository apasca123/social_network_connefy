package com.example.finalproject;

import com.example.finalproject.controller.Controller;
import com.example.finalproject.domain.*;
import com.example.finalproject.domain.validators.FriendshipValidator;
import com.example.finalproject.domain.validators.UserValidator;
import com.example.finalproject.domain.validators.Validator;
import com.example.finalproject.repository.Repository;
import com.example.finalproject.repository.db.*;
import com.example.finalproject.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 720);
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userDbRepository = new UserDbRepository("jdbc:postgresql://localhost:2001/SocialNetwork", "postgres", "ioni", userValidator);
        Repository<Long, Friendship> friendshipDbRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:2001/SocialNetwork", "postgres", "ioni", friendshipValidator);
        Repository<Long, Message> messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:2001/SocialNetwork", "postgres", "ioni");
        Repository<Long, Cerere> requestsDbRepository = new RequestsDbRepository("jdbc:postgresql://localhost:2001/SocialNetwork", "postgres", "ioni");
        Repository<Long, Event> eventsDbRepository = new EventDbRepository("jdbc:postgresql://localhost:2001/SocialNetwork", "postgres", "ioni");

        Service userService = new UserService(userDbRepository);
        Service friendshipService = new FriendshipService(friendshipDbRepository);
        Service messageService = new MessageService(messageDbRepository);
        Service requestService = new RequestsService(requestsDbRepository);
        Service eventService = new EventService(eventsDbRepository);
        Controller<Long, User, Long, Friendship, Long, Message, Long, Cerere, Long, Event> controller = new Controller((UserService) userService, (FriendshipService) friendshipService, (MessageService) messageService, (RequestsService) requestService, (EventService) eventService);
        LoginController loginController = fxmlLoader.getController();
        loginController.setController(controller);
        stage.setTitle("Connefy");
        stage.setScene(scene);
        loginController.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
