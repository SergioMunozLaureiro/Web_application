package com.mycompany.text_mode_project.model;

import java.time.LocalDate;
import java.util.*;

public class Event {
    private String name;
    private String place;
    private String date;
    private List<Match> matches;
    private Company company;

    public Event(String name, String place, String date, Company company) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("Event place cannot be empty");
        }
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Event date cannot be empty");
        }
        if (company == null) {
            throw new IllegalArgumentException("Event must belong to a company");
        }

        this.name = name;
        this.place = place;
        this.date = date;
        this.company = company;
        this.matches = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public Company getCompany() {
        return company;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        this.name = name;
    }

    public void setPlace(String place) {
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("Event place cannot be empty");
        }
        this.place = place;
    }

    public void setDate(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Event date cannot be empty");
        }
        this.date = date;
    }

    public void setCompany(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Event must belong to a company");
        }
        this.company = company;
    }

    public void addMatch(Match match) {
        if (match != null) {
            matches.add(match);
        }
    }

    public void removeMatch(Match match) {
        if (matches.contains(match)) {
            matches.remove(match);
            System.out.println("Match removed from event.");
        } else {
            System.out.println("Match not found in this event.");
        }
    }



    public String getEventSummary() {
        StringBuilder sb = new StringBuilder();
        //sb.append(toString()).append("\nMatches:\n");
        for (Match m : matches) {
            sb.append(" - ").append(m).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + " (" + date + ") - Location: " + place +
                " - Matches: " + matches.size();
    }

}

