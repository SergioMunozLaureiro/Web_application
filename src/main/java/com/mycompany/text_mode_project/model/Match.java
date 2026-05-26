package com.mycompany.text_mode_project.model;

import java.util.*;
import java.util.stream.*;

public class Match {

    private List<Wrestler> participants;
    private Wrestler winner;
    private Company company;
    private Event place;
    private String matchType;
    private boolean isFinished;
    private boolean isTitleMatch;

    public Match(List<Wrestler> participants, Company company, Event place, boolean isTitleMatch) {
        if (participants == null || participants.size() < 2)
            throw new IllegalArgumentException("A match must have at least 2 participants");
        if (company == null)
            throw new IllegalArgumentException("Company cannot be null");
        if (place == null)
            throw new IllegalArgumentException("Place cannot be empty");

        this.participants = participants;
        this.company = company;
        this.place = place;
        this.matchType = null;
        this.isFinished = false;
        this.isTitleMatch = isTitleMatch;
        this.winner = null;
    }

    public List<Wrestler> getParticipants() { return participants; }
    public Company getCompany() { return company; }
    public String getPlace() { return place.getName(); }
    public String getMatchType() { return matchType; }
    public boolean getFinished() { return this.isFinished; }
    public String getWinner() { return this.winner != null ? this.winner.getName() : "None"; }

    public String setMatchType(int s) {
        if (s == 2) matchType = "Singles Match";
        else if (s == 3) matchType = "Triple-Threat Match";
        else if (s == 4) matchType = "Fatal 4-Way Match";
        else if (s > 4) matchType = "Multi-Wrestler Match";
        else matchType = null;
        return matchType;
    }

    public void setPlace(String place) {
        if (place == null || place.isBlank())
            throw new IllegalArgumentException("Place cannot be empty");
        this.place.setPlace(place);
    }

    public void setWinner(Wrestler winner) {
        if (!participants.contains(winner))
            throw new IllegalArgumentException("Winner must be one of the match participants");
        this.winner = winner;
        this.isFinished = true;
    }

    public void setFinished() { this.isFinished = true; }

    public boolean isTitleHeldByAnyParticipant(String title) {
        return participants.stream().anyMatch(w -> w.getTitles().contains(title));
    }

    public Wrestler matchResultChooseTitle(Match match, Wrestler winner, String title) {
        isTitleMatch = true;

        if (!company.getChampionships().contains(title))
            throw new IllegalArgumentException("This title does not belong to " + company.getName());

        Wrestler champion = findChampion(title);

        if (champion != null && !match.getParticipants().contains(champion))
            match.getParticipants().add(champion);

        match.setWinner(winner);
        updateStats(match.getParticipants(), winner);
        updateTitle(winner, champion, title);

        return champion;
    }

    public void matchResultChooseNoTitle(Match match, Wrestler winner) {
        if (!participants.contains(winner))
            throw new IllegalArgumentException("Winner must be a participant of the match");

        isTitleMatch = false;
        match.setWinner(winner);
        updateStats(participants, winner);
    }

    public String matchResultRandomTitle(Match match, String title) {
        isTitleMatch = true;

        if (!company.getChampionships().contains(title))
            throw new IllegalArgumentException("This title does not belong to " + company.getName());

        Wrestler champion = findChampion(title);

        if (champion != null && !match.getParticipants().contains(champion))
            match.getParticipants().add(champion);

        Wrestler winner = pickRandomWinner(match.getParticipants());
        match.setWinner(winner);
        updateStats(match.getParticipants(), winner);
        updateTitle(winner, champion, title);

        return winner.getName();
    }

    public String matchResultRandomNoTitle(Match match) {
        isTitleMatch = false;

        // Weighted random by rating
        double totalRating = participants.stream().mapToDouble(Wrestler::getRating).sum();
        double randomValue = Math.random() * totalRating;
        double cumulative = 0;
        Wrestler winner = participants.get(0); // safe fallback

        for (Wrestler w : participants) {
            cumulative += w.getRating();
            if (randomValue <= cumulative) {
                winner = w;
                break;
            }
        }

        // FIX: removed duplicate setWinner call that could double-count stats
        match.setWinner(winner);
        updateStats(participants, winner);

        return winner.getName();
    }

    // ── Helpers ──────────────────────────────────────────────

    private Wrestler findChampion(String title) {
        for (Wrestler w : company.getWrestlers()) {
            if (w.getTitles().contains(title)) return w;
        }
        return null;
    }

    private Wrestler pickRandomWinner(List<Wrestler> pool) {
        return pool.get(new Random().nextInt(pool.size()));
    }

    private void updateStats(List<Wrestler> wrestlers, Wrestler winner) {
        for (Wrestler w : wrestlers) {
            if (w.equals(winner)) {
                w.addWin();
                w.setHealth(Math.max(w.getHealth() - 3, 0));
                if (w.getRating() < 99) w.setRating(w.gettrueRating() + 0.5);
            } else {
                w.addLoss();
                w.setHealth(Math.max(w.getHealth() - 3, 0));
                if (w.getRating() > 0) w.setRating(Math.max(w.gettrueRating() - 0.5, 0));
            }
        }
    }

    private void updateTitle(Wrestler winner, Wrestler champion, String title) {
        if (champion == null) {
            if (!winner.getTitles().contains(title)) winner.addTitle(title);
        } else if (!winner.equals(champion)) {
            champion.removeTitle(title);
            if (!winner.getTitles().contains(title)) winner.addTitle(title);
        }
        // If winner IS the champion, they retain — no changes needed
    }

    @Override
    public String toString() {
        String participantNames = participants.stream()
                .map(Wrestler::getName)
                .collect(Collectors.joining(" vs "));
        String result = "Match: " + participantNames;
        result += " | Type: " + setMatchType(participants.size());
        if (isTitleMatch) result += " | Title Match";
        if (isFinished && winner != null) result += " | Winner: " + winner.getName();
        return result;
    }
}
