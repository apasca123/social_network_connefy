package com.example.finalproject.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Event extends Entity<Long>{
    private Long id;
    private List<User> participants;
    private String description;
    private String date;

    public Event(Long id, List<User> participants, String description, String date) {
        this.id = id;
        this.participants = participants;
        this.description = description;
        this.date = date;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
