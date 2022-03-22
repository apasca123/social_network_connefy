package com.example.finalproject.repository.db;

import com.example.finalproject.domain.Cerere;
import com.example.finalproject.domain.Event;
import com.example.finalproject.domain.Message;
import com.example.finalproject.domain.User;
import com.example.finalproject.domain.validators.exceptions.EntityNullException;
import com.example.finalproject.paging.Page;
import com.example.finalproject.paging.Pageable;
import com.example.finalproject.repository.Repository;
import com.example.finalproject.utils.Constants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class EventDbRepository implements Repository<Long, Event> {

    private String url;
    private String username;
    private String password;

    public EventDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Event save(Event entity) {
        if (entity == null)
            throw new EntityNullException();

        String sql = "insert into events (id, description ,participants, datetbl ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            String participantsStr = "";
            for(User user : entity.getParticipants()) {
                participantsStr = participantsStr.concat(user.getEmail());
                participantsStr = participantsStr.concat(";");
            }
            participantsStr = participantsStr.substring(0, participantsStr.length() - 1);
            ps.setInt(1, Integer.parseInt(entity.getId().toString()));
            ps.setString(2, entity.getDescription());
            ps.setString(3, participantsStr);
            ps.setString(4, entity.getDate());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Event> getAllEntities() {
        Set<Event> eventList = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from events");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                String participantsStr = resultSet.getString("participants");
                String date = resultSet.getString("datetbl");

                List<String> participantsSplit = Arrays.asList(participantsStr.split(";"));
                List<User> participants = new ArrayList<>();
                for (String email : participantsSplit) {
                    if(this.getUserFromUsers(email) != null)
                        participants.add(this.getUserFromUsers(email));
                }

                Event event = new Event(id, participants, description, date);
                eventList.add(event);
            }
            return eventList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public User getUserFromUsers(String email){
        String sql = "SELECT * FROM users WHERE email= ?";
        User user = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String parola = resultSet.getString(5);
                user = new User(firstName, lastName, email,parola);
                user.setId(resultSet.getLong(1));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Long getEntitiesCount() {
        String sql = "SELECT COUNT(id) FROM events";
        int size = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();) {
            resultSet.next();
            size = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Long.valueOf(size);
    }

    @Override
    public Event update(Event entity) {
        String sql = "update events set participants = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            String participantsStr = "";
            for(User user : entity.getParticipants()) {
                participantsStr = participantsStr.concat(user.getEmail());
                participantsStr = participantsStr.concat(";");
            }
            if(participantsStr.length() != 0)
                participantsStr = participantsStr.substring(0, participantsStr.length() - 1);
            ps.setString(1, (participantsStr));
            ps.setInt(2, Integer.parseInt(entity.getId().toString()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Page<User> friendsOfAnUser(Pageable<User> pageable, User user) {
        return null;
    }

    @Override
    public Page<Message> conversation(Pageable<Message> pageable, String email1, String email2) {
        return null;
    }

    @Override
    public Page<Cerere> getReqByName(Pageable<Cerere> pageable, String email) {
        return null;
    }

    @Override
    public Page<Cerere> getSentReqs(Pageable<Cerere> pageable, String email) {
        return null;
    }

    @Override
    public Page<User> getAllEntities(Pageable<User> pageable) {
        return null;
    }

    @Override
    public Long getCurrentId() {
        return null;
    }

    @Override
    public void setCurrentId(Long currentId) {

    }

    @Override
    public Event findOne(Long aLong) throws SQLException {
        return null;
    }

    @Override
    public Event delete(Long aLong) {
        return null;
    }

    @Override
    public String getCurrentEmail() {
        return null;
    }

    @Override
    public List<Long> getAllIDs() {
        return null;
    }

    @Override
    public Event findOneByEmail(String email) {
        return null;
    }

    @Override
    public Event findOneByParola(String parola) {
        return null;
    }

    @Override
    public Iterable<Event> friendshipsOfAnUser(User e) {
        return null;
    }

    @Override
    public void removeFriendship(Long id1, Long id2) {

    }

    @Override
    public void removeFriendRequest(String email1, String email2) {

    }

    @Override
    public List<Event> conversation(String email1, String email2) {
        return null;
    }

    @Override
    public Iterable<Event> getReqByEmail(String email) {
        return null;
    }

    @Override
    public Iterable<Cerere> getSentReqs(String email) {
        return null;
    }

    @Override
    public List<User> friendsOfAnUser(User user) {
        return null;
    }
}
