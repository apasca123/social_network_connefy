package com.example.finalproject.service;

import com.example.finalproject.domain.Entity;
import com.example.finalproject.repository.Repository;

import java.util.Observable;

public class EventService <ID, E extends Entity<ID>> extends Observable implements Service<ID, E>{

    private Repository<ID, E> eventRepository;

    public EventService(Repository<ID, E> eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Iterable<E> getAll() {
        return this.eventRepository.getAllEntities();
    }

    @Override
    public void add(E e) {
        this.eventRepository.save(e);
        setChanged();
        notifyObservers();
    }

    @Override
    public void update(E e) {
        this.eventRepository.update(e);
        setChanged();
        notifyObservers();
    }
    public Long size(){
        return eventRepository.getEntitiesCount();
    }

    @Override
    public ID getCurrentId() {
        return null;
    }

    @Override
    public void setCurrentUserId(ID id) {

    }

    @Override
    public Long findFistFreeId() {
        return null;
    }

    @Override
    public void remove(E e) {

    }
}
