package com.mycompany.text_mode_project.model;
import java.util.*;

public class Company {
    //Atributos
    private String name; //Nombre
    private String country; //Pais
    private List<Wrestler> wrestler; //Luchadores en tu empresa
    private List<String> titles; //Campeonatos en tu empresa
    private List<String> shows;
    private List<Event> events;//Shows en tu empresa


    public Company(String name, String country) {
        this.name = name;
        this.country = country;

        this.wrestler = new ArrayList<>();
        this.titles = new ArrayList<>();
        this.shows = new ArrayList<>();
        this.events=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public List<Wrestler> getWrestlers() {
        return wrestler;
    }

    public List<String> getChampionships() {
        return titles;
    }

    public List<String> getShows() {
        return shows;
    }

    public List<Event> getEvents() {
        return events;
    }

    // Setters con validación
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Company name cannot be empty");
        this.name = name;
    }

    public void setCountry(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("Country cannot be empty");
        this.country = country;
    }

    // Métodos para añadir elementos con validación
    public void addWrestler(Wrestler w) {
        if (w == null)
            throw new IllegalArgumentException("Wrestler cannot be null");
        wrestler.add(w);
    }

    public void addChampionship(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be empty");
        titles.add(title);
    }

    public void addShow(String show) {
        if (show == null || show.isBlank())
            throw new IllegalArgumentException("Show cannot be empty");
        shows.add(show);
    }

    public void addEvent(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");
        events.add(event);
    }

    @Override
    public String toString() {
        return name + " (" + country + ") - Wrestlers: " + wrestler.size() +
                " - Championships: " + titles + " - Shows: " + shows +"\n";
    }
}

