package com.mycompany.text_mode_project.Controller;
import com.mycompany.text_mode_project.Service.*;
import com.mycompany.text_mode_project.view.View;
import com.mycompany.text_mode_project.model.*;

import java.util.*;

public class Controller {
    private View view;
    private List<Company> company;
    private List<Wrestler> wrestlers;
    private List<Event> events;
    private ServiceCompany companyService;
    private ServiceWrestler wrestlerService;
    private ServiceEvent eventService;
    private ServiceMatch matchService;

    public Controller(View view) {
        this.view = view;
        this.company = Data.loadCompanies();
        this.wrestlers = Data.loadWrestlers(company);
        this.events = Data.loadEvents(company);
        this.companyService = new ServiceCompany(company);
        this.wrestlerService = new ServiceWrestler(wrestlers);
        this.eventService = new ServiceEvent();
        this.matchService = new ServiceMatch();
    }

    public void mainMenu() {
        int option = -1;
        do {
            view.showMainMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> manageCompanies();
                case 2 -> manageWrestlers();
                case 3 -> manageMatches();
                case 4 -> manageStadistics();
                case 0 -> {
                    view.showMessage("Exiting...");
                    view.close();
                }
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public void manageCompanies() {
        int option;
        do {
            option = view.showCompanyMenu();
            switch (option) {
                case 1 -> addCompany();
                case 2 -> showAllCompaniesInView();
                case 3 -> {
                    Company selected = chooseCompany();
                    if (selected != null) manageSelectedCompany(selected);
                }
                case 0 -> view.showMessage("Returning to main menu...");
                default -> view.showMessage("Invalid option.");
            }
        } while (option != 0);
    }

    public void manageWrestlers() {
        int option = -1;
        do {
            view.showWrestlerMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> addWrestler();
                case 2 -> {
                    Wrestler w = view.askWrestler(wrestlers);
                    if (w != null) {
                        editWrestler(w);
                        view.showMessage("Wrestler edited successfully");
                    }
                }
                case 3 -> deleteWrestler();
                case 4 -> showAllWrestlersInView();
                case 5 -> {
                    Company optionC = view.askCompany(company);
                    while (optionC == null) {
                        view.showMessage("No Company selected. Please choose a company.");
                        optionC = view.askCompany(company);
                    }
                    moveWrestler(optionC);
                }
                case 0 -> view.showMessage("Exiting to main menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public void showAllWrestlersInView() {
        String wrestlersInfo = this.wrestlerService.getAllWrestlersString();
        view.displayText(wrestlersInfo);
    }

    public void showAllCompaniesInView() {
        String companiesInfo = this.companyService.getAllCompaniesString();
        view.displayText(companiesInfo);
    }

    public void addWrestler() {
        String name = view.askString("Enter wrestler name: ");
        int rating = view.askInt("Enter wrestler rating (0-99): ");
        int age = view.askInt("Enter wrestler age: ");
        int weight = view.askInt("Enter wrestler weight (in kilos): ");
        int height = view.askInt("Enter wrestler height (in cm): ");
        String hometown = view.askString("Enter wrestler hometown (country): ");
        String style = view.askString("Enter wrestler style: ");
        String finisher = view.askString("Enter wrestler finisher: ");
        Wrestler w = new Wrestler(name, null, rating, age, weight, height, hometown, style, finisher);
        wrestlerService.addWrestler(w);
        wrestlers.add(w);
        view.showMessage("Wrestler created successfully");
    }

    public void editWrestler(Wrestler w) {
        int option;
        do {
            view.showWrestlerEditMenu(w);
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> w.setName(view.askString("New name: "));
                case 2 -> w.setAge(view.askInt("New age: "));
                case 3 -> w.setWeight(view.askInt("New weight (in kilos): "));
                case 4 -> w.setStyle(view.askString("New style: "));
                case 5 -> w.setFinisher(view.askString("New finisher: "));
                case 0 -> view.showMessage("Returning...");
                default -> view.showMessage("Invalid option");
            }
        } while (option != 0);
    }

    public void moveWrestler(Company originCompany) {
        // FIX: filter wrestlers that actually belong to the chosen company
        List<Wrestler> companyWrestlers = new ArrayList<>();
        for (Wrestler w : originCompany.getWrestlers()) {
            if (w.getCompany() != null && w.getCompany().equals(originCompany)) {
                companyWrestlers.add(w);
            }
        }
        if (companyWrestlers.isEmpty()) {
            view.showMessage("No wrestlers available to move from " + originCompany.getName());
            return;
        }

        Wrestler wrestler = view.askWrestler(companyWrestlers);
        while (wrestler == null) {
            view.showMessage("No Wrestler selected. Please choose a wrestler.");
            wrestler = view.askWrestler(companyWrestlers);
        }

        // Choose destination company (different from origin)
        Company destination = null;
        do {
            destination = view.askCompany(company);
            while (destination == null) {
                view.showMessage("No Company selected, choose a company.");
                destination = view.askCompany(company);
            }
            if (destination.getName().equals(originCompany.getName())) {
                view.showMessage("The wrestler already belongs to that company.");
            }
        } while (destination.getName().equals(originCompany.getName()));

        // Strip titles before moving
        if (!wrestler.getTitles().isEmpty()) {
            wrestler.setTitles(new ArrayList<>());
        }
        originCompany.getWrestlers().remove(wrestler);
        destination.getWrestlers().add(wrestler);
        wrestler.setCompany(destination);
        view.showMessage("Wrestler moved successfully");
    }

    public void deleteWrestler() {
        Wrestler option = view.askWrestler(wrestlers);
        if (option == null) {
            view.showMessage("No wrestler selected.");
            return;
        }
        // Also remove from their company roster if they have one
        if (option.getCompany() != null) {
            option.getCompany().getWrestlers().remove(option);
        }
        wrestlerService.removeWrestler(option);
        view.showMessage("Wrestler deleted successfully");
    }

    public void addCompany() {
        String name = view.askString("Enter company name: ");
        String country = view.askString("Enter company country: ");
        Company c = new Company(name, country);
        companyService.addCompany(c);
        view.showMessage("Company added successfully");
    }

    public void manageMatches() {
        int option = -1;
        do {
            view.showMatchMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> {
                    Company selected = chooseCompany();
                    if (selected != null) {
                        Event eventSelected = chooseEvent(selected);
                        if (eventSelected != null) manageNewMatches(selected, eventSelected);
                    }
                }
                case 2 -> {
                    // FIX: was missing break, falling through to case 0
                    Company selected = chooseCompany();
                    if (selected != null) {
                        Event eventSelected = chooseEvent(selected);
                        if (eventSelected != null) manageDeleteMatch(selected, eventSelected);
                    }
                }
                case 0 -> view.showMessage("Exiting to main menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public void manageDeleteMatch(Company company, Event event) {
        // FIX: removed the redundant askInt after askmatches — askmatches already returns the selected match
        if (event.getMatches().isEmpty()) {
            view.showMessage("No matches in this event.");
            return;
        }
        Match m = view.askmatches(event.getMatches());
        if (m == null) {
            view.showMessage("No match selected.");
            return;
        }
        event.removeMatch(m);
        view.showMessage("Match deleted successfully");
    }

    public void manageStadistics() {
        if (company.isEmpty()) {
            view.showMessage("No companies registered. Returning to main menu...");
            return;
        }
        Company selectedCompany = chooseCompany();
        if (selectedCompany == null) return;

        int option = -1;
        do {
            view.showStatsCompanyChoosen(selectedCompany);
            option = view.askInt("Select an option: ");
            switch (option) {
                case 1 -> mostWins(selectedCompany);
                case 2 -> mostLosses(selectedCompany);
                case 3 -> mostMatches(selectedCompany);
                case 4 -> mostWinRate(selectedCompany);
                case 5 -> mostTitles(selectedCompany);
                case 6 -> mostRating(selectedCompany);
                case 7 -> totalMatches(selectedCompany);
                case 0 -> view.showMessage("Returning to statistics menu...");
                default -> view.showMessage("Invalid option.");
            }
        } while (option != 0);
    }

    public void mostWins(Company company) {
        List<Wrestler> wrestlers = company.getWrestlers();
        if (wrestlers.isEmpty()) {
            view.showMessage("No wrestlers registered in " + company.getName() + ".");
            return;
        }
        int maxWins = wrestlers.stream().mapToInt(Wrestler::getWins).max().orElse(0);
        List<Wrestler> top = wrestlers.stream().filter(w -> w.getWins() == maxWins).toList();
        view.showMessage("=== Wrestler(s) with the most wins in " + company.getName() + " ===");
        for (Wrestler w : top) view.showMessage(w.getName() + " → " + w.getWins() + " wins");
    }

    public void mostLosses(Company company) {
        List<Wrestler> wrestlers = company.getWrestlers();
        if (wrestlers.isEmpty()) {
            view.showMessage("No wrestlers registered in " + company.getName() + ".");
            return;
        }
        int maxLosses = wrestlers.stream().mapToInt(Wrestler::getLosses).max().orElse(0);
        List<Wrestler> top = wrestlers.stream().filter(w -> w.getLosses() == maxLosses).toList();
        view.showMessage("=== Wrestler(s) with the most losses in " + company.getName() + " ===");
        for (Wrestler w : top) view.showMessage(w.getName() + " → " + w.getLosses() + " losses");
    }

    public void mostMatches(Company company) {
        List<Wrestler> wrestlers = company.getWrestlers();
        if (wrestlers.isEmpty()) {
            view.showMessage("No wrestlers registered in " + company.getName() + ".");
            return;
        }
        int maxMatches = wrestlers.stream().mapToInt(Wrestler::getMatches).max().orElse(0);
        List<Wrestler> top = wrestlers.stream().filter(w -> w.getMatches() == maxMatches).toList();
        view.showMessage("=== Wrestler(s) with the most matches in " + company.getName() + " ===");
        for (Wrestler w : top) view.showMessage(w.getName() + " → " + w.getMatches() + " matches");
    }

    public void mostRating(Company company) {
        List<Wrestler> wrestlers = company.getWrestlers();
        if (wrestlers.isEmpty()) {
            view.showMessage("No wrestlers registered in " + company.getName() + ".");
            return;
        }
        int maxRating = wrestlers.stream().mapToInt(Wrestler::getRating).max().orElse(0);
        List<Wrestler> top = wrestlers.stream().filter(w -> w.getRating() == maxRating).toList();
        view.showMessage("=== Wrestler(s) with the highest rating in " + company.getName() + " ===");
        for (Wrestler w : top) view.showMessage(w.getName() + " → " + w.getRating() + " rating");
    }

    public void totalMatches(Company company) {
        int total = company.getEvents().stream().mapToInt(e -> e.getMatches().size()).sum();
        view.showMessage("=== Total matches in " + company.getName() + ": " + total + " ===");
    }

    public void mostTitles(Company company) {
        List<Wrestler> wrestlers = company.getWrestlers();
        if (wrestlers.isEmpty()) {
            view.showMessage("No wrestlers registered in " + company.getName() + ".");
            return;
        }
        int maxTitles = wrestlers.stream().mapToInt(w -> w.getTitles().size()).max().orElse(0);
        List<Wrestler> top = wrestlers.stream().filter(w -> w.getTitles().size() == maxTitles).toList();
        view.showMessage("=== Wrestler(s) with the most titles in " + company.getName() + " ===");
        for (Wrestler w : top) view.showMessage(w.getName() + " → " + w.getTitles().size() + " titles");
    }

    public void mostWinRate(Company company) {
        List<Wrestler> wrestlers = company.getWrestlers();
        if (wrestlers.isEmpty()) {
            view.showMessage("No wrestlers registered in " + company.getName() + ".");
            return;
        }
        List<Wrestler> active = wrestlers.stream().filter(w -> w.getMatches() > 0).toList();
        if (active.isEmpty()) {
            view.showMessage("No wrestlers with matches in " + company.getName() + ".");
            return;
        }
        double maxRate = active.stream()
                .mapToDouble(w -> (double) w.getWins() / w.getMatches())
                .max().orElse(0);
        view.showMessage("=== Wrestler(s) with highest win rate in " + company.getName() + " ===");
        for (Wrestler w : active) {
            double rate = (double) w.getWins() / w.getMatches();
            if (Math.abs(rate - maxRate) < 0.0001) {
                view.showMessage(String.format("%s → %.2f%% win rate (%d wins / %d matches)",
                        w.getName(), rate * 100, w.getWins(), w.getMatches()));
            }
        }
    }

    public void manageEvents(Company company) {
        int option = -1;
        do {
            view.showEventMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> createEvent(company);
                case 2 -> {
                    if (company.getEvents().isEmpty()) {
                        view.showMessage("There are no events in this company.");
                        break;
                    }
                    Event optionEvent = chooseEvent(company);
                    if (optionEvent != null) manageChoosenEvent(company, optionEvent);
                }
                case 0 -> view.showMessage("Exiting to company menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public Company chooseCompany() {
        if (company.isEmpty()) {
            view.showMessage("No companies registered.");
            return null;
        }
        int choice = view.showCompanyChooseMenu(this.company);
        if (choice <= 0 || choice > company.size()) {
            view.showMessage("Invalid choice.");
            return null;
        }
        return company.get(choice - 1);
    }

    public Event chooseEvent(Company company) {
        List<Event> events = company.getEvents();
        if (events.isEmpty()) {
            view.showMessage("No events registered.");
            return null;
        }
        int choice = view.showEventChooseMenu(company, events);
        if (choice <= 0 || choice > events.size()) {
            view.showMessage("Invalid choice.");
            return null;
        }
        return events.get(choice - 1);
    }

    public void manageNewMatches(Company company, Event event) {
        int option = -1;
        boolean title = false;
        do {
            view.showTypeMatchMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> { title = true; manageWinner(title, company, event); return; }
                case 2 -> { manageWinner(title, company, event); return; }
                case 0 -> view.showMessage("Exiting to match menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public void manageWinner(boolean title, Company company, Event event) {
        int option = -1;
        do {
            view.showEdingDecisionMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> { runChosenMatch(title, company, event); return; }
                case 2 -> { runRandomMatch(title, company, event); return; }
                case 0 -> view.showMessage("Exiting to match menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    private void runChosenMatch(boolean title, Company company, Event event) {
        List<String> wrestlerNames = new ArrayList<>();
        List<Wrestler> selectedWrestlers = new ArrayList<>();

        String choosenTitle = null;
        Wrestler champion = null;

        if (title) {
            if (company.getChampionships().isEmpty()) {
                view.showMessage("The company doesn't have any titles.");
                return;
            }
            choosenTitle = view.askTitle(company.getChampionships());
            while (choosenTitle == null) {
                view.showMessage("No title selected. Please choose a title.");
                choosenTitle = view.askTitle(company.getChampionships());
            }
            for (Wrestler w : company.getWrestlers()) {
                if (w.getTitles().contains(choosenTitle)) {
                    champion = w;
                    view.showMessage("The champion for this title is: " + champion.getName() + " — they must participate.");
                    break;
                }
            }
            if (champion == null) view.showMessage("The title " + choosenTitle + " is vacant. Any participant can win.");
        }

        int nParticipants = askValidParticipantCount(company);
        if (nParticipants == -1) return;

        selectedWrestlers = pickParticipants(company, nParticipants);
        if (selectedWrestlers == null) return;
        for (Wrestler w : selectedWrestlers) wrestlerNames.add(w.getName());

        if (title && champion != null && !wrestlerNames.contains(champion.getName())) {
            view.showMessage("The current champion must participate in a title match.");
            return;
        }

        Wrestler winner = view.askWrestler(selectedWrestlers);
        while (winner == null) {
            view.showMessage("No wrestler selected. Please choose a winner.");
            winner = view.askWrestler(selectedWrestlers);
        }

        Match match = new Match(selectedWrestlers, company, event, title);
        if (title) {
            match.matchResultChooseTitle(match, winner, choosenTitle);
            announceChampionshipResult(winner, champion, choosenTitle);
        } else {
            match.matchResultChooseNoTitle(match, winner);
            view.showMessage(winner.getName() + " wins the match!");
        }
        match.setFinished();
        event.addMatch(match);
        view.showMessage("Match added successfully to event: " + event.getName());
        restoreLowHealthWrestlers(company);
    }

    private void runRandomMatch(boolean title, Company company, Event event) {
        List<String> wrestlerNames = new ArrayList<>();
        List<Wrestler> selectedWrestlers;

        String choosenTitle = null;
        Wrestler champion = null;

        if (title) {
            if (company.getChampionships().isEmpty()) {
                view.showMessage("The company doesn't have any titles.");
                return;
            }
            choosenTitle = view.askTitle(company.getChampionships());
            while (choosenTitle == null) {
                view.showMessage("No title selected. Please choose a title.");
                choosenTitle = view.askTitle(company.getChampionships());
            }
            for (Wrestler w : company.getWrestlers()) {
                if (w.getTitles().contains(choosenTitle)) {
                    champion = w;
                    view.showMessage("The champion for this title is: " + champion.getName() + " — they must participate.");
                    break;
                }
            }
            if (champion == null) view.showMessage("The title " + choosenTitle + " is vacant. Any participant can win.");
        }

        int nParticipants = askValidParticipantCount(company);
        if (nParticipants == -1) return;

        selectedWrestlers = pickParticipants(company, nParticipants);
        if (selectedWrestlers == null) return;
        for (Wrestler w : selectedWrestlers) wrestlerNames.add(w.getName());

        if (title && champion != null && !wrestlerNames.contains(champion.getName())) {
            view.showMessage("The current champion must participate in a title match.");
            return;
        }

        Match match = new Match(selectedWrestlers, company, event, title);
        if (title) {
            String winnerName = match.matchResultRandomTitle(match, choosenTitle);
            Wrestler winner = selectedWrestlers.stream().filter(w -> w.getName().equals(winnerName)).findFirst().orElse(null);
            announceChampionshipResult(winner, champion, choosenTitle);
        } else {
            String winnerName = match.matchResultRandomNoTitle(match);
            view.showMessage(winnerName + " wins the match!");
        }
        match.setFinished();
        event.addMatch(match);
        view.showMessage("Match added successfully to event: " + event.getName());
        restoreLowHealthWrestlers(company);
    }

    private int askValidParticipantCount(Company company) {
        int n;
        do {
            n = view.askInt("Choose the number of participants: ");
            if (n < 2) view.showMessage("You need at least 2 wrestlers.");
            else if (n > company.getWrestlers().size()) view.showMessage("The company doesn't have that many wrestlers.");
        } while (n < 2 || n > company.getWrestlers().size());
        return n;
    }

    private List<Wrestler> pickParticipants(Company company, int n) {
        List<Wrestler> selected = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Wrestler w;
            do {
                w = view.askWrestler(company.getWrestlers());
                while (w == null) {
                    view.showMessage("No wrestler selected. Please choose a participant.");
                    w = view.askWrestler(company.getWrestlers());
                }
                if (w.getHealth() < 30) view.showMessage(w.getName() + "'s health is too low to compete.");
                if (selected.contains(w)) view.showMessage(w.getName() + " is already in the match.");
            } while (w.getHealth() < 30 || selected.contains(w));
            selected.add(w);
        }
        return selected;
    }

    private void announceChampionshipResult(Wrestler winner, Wrestler champion, String title) {
        if (winner == null) return;
        if (champion == null) {
            view.showMessage(winner.getName() + " wins the vacant " + title + "!");
        } else if (!winner.equals(champion)) {
            view.showMessage(winner.getName() + " wins the " + title + " from " + champion.getName() + "!");
        } else {
            view.showMessage(winner.getName() + " retains the " + title + "!");
        }
    }

    private void restoreLowHealthWrestlers(Company company) {
        int restored = 0;
        for (Wrestler w : company.getWrestlers()) {
            if (w.getHealth() < 30) {
                w.setHealth(100);
                restored++;
            }
        }
        if (restored > 0) view.showMessage(restored + " wrestler(s) with low health have been restored.");
    }

    public void manageSelectedCompany(Company company) {
        int option = -1;
        do {
            view.showCompanyChosen(company);
            option = view.askInt("Choose an option");
            switch (option) {
                case 1 -> view.showCompany(company);
                case 2 -> { editCompany(company); view.showMessage("Company edited successfully"); }
                case 3 -> addWrestlerToCompany(company);
                case 4 -> deleteWrestlerCompany(company);
                case 5 -> {
                    Wrestler optionW = view.askWrestler(company.getWrestlers());
                    if (optionW != null) view.showWrestler(optionW);
                }
                case 6 -> addShow(company);
                case 7 -> deleteShow(company);
                case 8 -> addTitleToCompany(company);
                case 9 -> deleteTitle(company);
                case 10 -> chooseChampion(company);
                case 11 -> { deleteCompany(company); view.showMessage("Company deleted successfully"); return; }
                case 12 -> manageEvents(company);
                case 0 -> view.showMessage("Returning to company menu...");
                default -> view.showMessage("Invalid option.");
            }
        } while (option != 0);
    }

    public void chooseChampion(Company company) {
        if (company.getChampionships().isEmpty()) {
            view.showMessage("The company doesn't have any titles.");
            return;
        }
        if (company.getWrestlers().isEmpty()) {
            view.showMessage("The company doesn't have any wrestlers.");
            return;
        }
        // FIX: ask first, then loop only if null
        String option = view.askTitle(company.getChampionships());
        while (option == null) {
            view.showMessage("No title selected, choose a title.");
            option = view.askTitle(company.getChampionships());
        }
        for (Wrestler w : company.getWrestlers()) {
            if (w.getTitles().contains(option)) {
                view.showMessage("The title already has a champion: " + w.getName());
                return;
            }
        }
        Wrestler wrestler = view.askWrestler(company.getWrestlers());
        while (wrestler == null) {
            view.showMessage("No wrestler selected, choose a wrestler.");
            wrestler = view.askWrestler(company.getWrestlers());
        }
        wrestler.getTitles().add(option);
        view.showMessage("Champion assigned successfully");
    }

    public void addShow(Company company) {
        String newShow = view.askString("Name of the new show: ");
        for (String s : company.getShows()) {
            if (newShow.equalsIgnoreCase(s)) {
                view.showMessage("The show is already in the company.");
                return;
            }
        }
        company.getShows().add(newShow);
        view.showMessage("Show added successfully");
    }

    public void deleteShow(Company company) {
        if (company.getShows().isEmpty()) {
            view.showMessage("The company doesn't have any shows to delete.");
            return;
        }
        String selected = view.askShow(company.getShows());
        while (selected == null) {
            view.showMessage("No show selected.");
            selected = view.askShow(company.getShows());
        }
        company.getShows().remove(selected);
        view.showMessage("Show deleted successfully");
    }

    public void addWrestlerToCompany(Company company) {
        List<Wrestler> freeWrestlers = new ArrayList<>();
        for (Wrestler w : wrestlers) {
            if (w.getCompany() == null) freeWrestlers.add(w);
        }
        if (freeWrestlers.isEmpty()) {
            view.showMessage("No free agents available to add.");
            return;
        }
        Wrestler selected = view.askWrestler(freeWrestlers);
        while (selected == null) {
            view.showMessage("No wrestler selected.");
            selected = view.askWrestler(freeWrestlers);
        }
        selected.setCompany(company);
        company.addWrestler(selected);
        view.showMessage(selected.getName() + " has been added to " + company.getName());
    }

    public void editCompany(Company company) {
        int option;
        do {
            view.showCompanyEditMenu(company);
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> company.setName(view.askString("New name: "));
                case 2 -> company.setCountry(view.askString("New country: "));
                case 0 -> view.showMessage("Returning...");
                default -> view.showMessage("Invalid option");
            }
        } while (option != 0);
    }

    public void deleteWrestlerCompany(Company company) {
        if (company.getWrestlers().isEmpty()) {
            view.showMessage("No wrestlers in this company.");
            return;
        }
        Wrestler option = view.askWrestler(company.getWrestlers());
        while (option == null) {
            view.showMessage("No wrestler selected.");
            option = view.askWrestler(company.getWrestlers());
        }
        company.getWrestlers().remove(option);
        option.setCompany(null);
        view.showMessage(option.getName() + " has been removed from " + company.getName());
    }

    public void addTitleToCompany(Company company) {
        String newTitle = view.askString("Name of the new title: ");
        for (String t : company.getChampionships()) {
            if (newTitle.equalsIgnoreCase(t)) {
                view.showMessage("The title is already in the company.");
                return;
            }
        }
        company.getChampionships().add(newTitle);
        view.showMessage("Title added successfully");
    }

    public void deleteTitle(Company company) {
        if (company.getChampionships().isEmpty()) {
            view.showMessage("The company doesn't have any titles to delete.");
            return;
        }
        String selected = view.askShow(company.getChampionships());
        if (selected == null) {
            view.showMessage("No title selected.");
            return;
        }
        // Remove title from any wrestler holding it
        for (Wrestler w : company.getWrestlers()) {
            w.getTitles().remove(selected);
        }
        company.getChampionships().remove(selected);
        view.showMessage("Title deleted successfully");
    }

    public void deleteCompany(Company company) {
        companyService.deleteCompany(company);
    }

    public void manageChoosenEvent(Company company, Event event) {
        int option = -1;
        do {
            view.showChoosenEvent(event);
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> { editEvent(event); view.showMessage("Event edited successfully"); }
                case 2 -> { deleteEvent(company, event); return; }
                case 3 -> view.showAllEvents(event);
                case 4 -> manageMatchesEvent(company, event);
                case 0 -> view.showMessage("Exiting to Event menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public void deleteEvent(Company company, Event event) {
        eventService.deleteEventFromCompany(company, event);
        view.showMessage("Event deleted successfully");
    }

    public void editEvent(Event event) {
        int option;
        do {
            view.showEditEventMenu(event);
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> event.setName(view.askString("New name: "));
                case 2 -> {
                    event.setPlace(view.askString("New place: "));
                    for (Match m : event.getMatches()) m.setPlace(event.getPlace());
                }
                case 3 -> event.setDate(view.askString("New date: "));
                case 4 -> {
                    if (event.getMatches().isEmpty()) {
                        view.showMessage("No matches in this event.");
                        break;
                    }
                    Match optionM = view.askMatch(event.getMatches());
                    if (optionM != null && !optionM.getFinished()) {
                        editMatch(event, optionM, event.getCompany());
                    } else if (optionM != null) {
                        view.showMessage("This match is already finished and cannot be edited.");
                    }
                }
                case 0 -> view.showMessage("Returning...");
                default -> view.showMessage("Invalid option");
            }
        } while (option != 0);
    }

    public void editMatch(Event event, Match match, Company company) {
        int option;
        do {
            view.showMatchEditMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> {
                    // FIX: condition was inverted — should be !contains to keep picking from company roster
                    List<Wrestler> newParticipants = new ArrayList<>();
                    int n = view.askInt("Choose the number of participants: ");
                    for (int i = 0; i < n; i++) {
                        Wrestler w;
                        do {
                            w = view.askWrestler(company.getWrestlers());
                        } while (w == null || newParticipants.contains(w));
                        newParticipants.add(w);
                    }
                    view.showMessage("Participants updated.");
                }
                case 2 -> {
                    view.showMessage("Choose another winner (not the current one: " + match.getWinner() + ")");
                    Wrestler wWinner;
                    do {
                        wWinner = view.askWrestler(match.getParticipants());
                    } while (wWinner == null || wWinner.getName().equals(match.getWinner()));
                    match.setWinner(wWinner);
                    view.showMessage("Winner changed successfully");
                }
                case 0 -> view.showMessage("Returning to Event menu...");
                default -> view.showMessage("Invalid option");
            }
        } while (option != 0);
    }

    public void manageMatchesEvent(Company selected, Event eventSelected) {
        int option = -1;
        do {
            view.showMatchMenu();
            option = view.askInt("Choose an option: ");
            switch (option) {
                case 1 -> { if (selected != null) manageNewMatches(selected, eventSelected); }
                case 2 -> { if (selected != null) manageDeleteMatch(selected, eventSelected); }
                case 0 -> view.showMessage("Exiting to event menu...");
                default -> view.showMessage("Invalid option, choose another one");
            }
        } while (option != 0);
    }

    public void createEvent(Company company) {
        String name = view.askString("Enter Event name: ");
        String place = view.askString("Enter Event place: ");
        String date = view.askString("Enter Event date: ");
        Event event = new Event(name, place, date, company);
        eventService.addEventToCompany(company, event);
        view.showMessage("Event created successfully");
    }
}
