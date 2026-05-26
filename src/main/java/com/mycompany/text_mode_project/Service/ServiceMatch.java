/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.text_mode_project.Service;

/**
 *
 * @author s.munozl.2023
 */

import com.mycompany.text_mode_project.model.Match;

import com.mycompany.text_mode_project.model.Event;

import java.util.*;
public class ServiceMatch {
    public void addMatchToEvent(Event event, Match match) {
        event.getMatches().add(match);
    }

    public void deleteMatchFromEvent(Event event, Match match) {
        event.getMatches().remove(match);
    }
}

