package com.mycompany.text_mode_project.Service;

import com.mycompany.text_mode_project.model.Event;
import com.mycompany.text_mode_project.model.Company;

import java.util.*;

public class ServiceEvent {

    public void addEventToCompany(Company company, Event event) {
        company.getEvents().add(event);
    }

    // FIX: renamed from deelteEventToCompany
    public void deleteEventFromCompany(Company company, Event event) {
        company.getEvents().remove(event);
    }
}
