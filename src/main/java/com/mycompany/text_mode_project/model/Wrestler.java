package com.mycompany.text_mode_project.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wrestler {
    //Atributos
    private String name; //Nombre
    private int rating;
    private double trueRating;//Rating
    private int age; //Edad
    private int weight;//Peso(en libras)
    private int height;  //Altura(en pies)
    private String hometown; //Hometown
    private Company company;//Empresa
    private String style;  //Style
    private String finisher; //Finisher
    private int matches; //Matches
    private int win; //Wins
    private int loss; //Losses
    private List<String> titles; //Titles
    private int health;//Health

    public Wrestler(String name, Company company, int rating, int age, int weight, int height,
                    String hometown, String style, String finisher) {


        if(rating < 0 || rating > 99) throw new IllegalArgumentException("Rating must be 0-100");
        if(age <= 0) throw new IllegalArgumentException("Age must be positive");
        if(weight <= 0) throw new IllegalArgumentException("Weight must be positive");
        if(height <= 0) throw new IllegalArgumentException("Height must be positive");
        if(hometown == null || hometown.isBlank()) throw new IllegalArgumentException("Hometown cannot be empty");
        if(style == null || style.isBlank()) throw new IllegalArgumentException("Style cannot be empty");
        if(finisher == null || finisher.isBlank()) throw new IllegalArgumentException("Finisher cannot be empty");

        this.name = name;
        this.company = company;
        this.rating = rating;
        this.trueRating=rating;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.hometown = hometown;
        this.style = style;
        this.finisher = finisher;
        this.win = 0;
        this.loss = 0;
        this.health = 100;
        this.titles = new ArrayList<>();
    }

    // Getters y setters
    public String getName() { return name; }
    public int getAge(){return age;}
    public String getHometown(){return hometown;}
    public int getWeight(){return weight;}
    public int getHeight(){return height;}
    public Company getCompany() { return company; }
    public String getStyle() { return style; }
    public String getFinisher() { return finisher; }
    public int getRating() { return rating; }
    public double gettrueRating(){return trueRating;}
    public int getWins() { return win; }
    public int getLosses() { return loss; }
    public int getMatches() { return matches; }
    public int getHealth() { return health; }
    public List<String> getTitles() {
        return titles;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFinisher(String finisher) {
        this.finisher = finisher;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setRating(double rating) {
        this.trueRating = rating;
        this.rating=(int) Math.floor(rating);
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // Métodos para actualizar estadísticas
    public void addWin() { win++; matches++; }
    public void addLoss() { loss++; matches++; }
    // Añadir título con validación
    public void addTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (!titles.contains(title)) {
            titles.add(title);
        } else {
            System.out.println("The wrestler already has the title: " + title);
        }
    }

    // Quitar título con validación
    public void removeTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (titles.contains(title)) {
            titles.remove(title);
        } else {
            System.out.println("The wrestler does not have the title: " + title);
        }
    }


    @Override
    public String toString() {
        if (company==null){
            return name +   " - Style: " + style +
                    " - Rating: " + rating + " - W:" + win + " L:" + loss +
                    " - Titles: " + titles;
        }
        else {
            return name + " (" + company.getName() + ") - Style: " + style +
                    " - Rating: " + rating + " - W:" + win + " L:" + loss +
                    " - Titles: " + titles;
        }
    }









}

