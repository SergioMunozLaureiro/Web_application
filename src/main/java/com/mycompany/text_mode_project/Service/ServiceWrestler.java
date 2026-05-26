package com.mycompany.text_mode_project.Service;

import com.mycompany.text_mode_project.model.Wrestler;
import java.util.*;

public class ServiceWrestler {

    private List<Wrestler> wrestlers;

    public ServiceWrestler(List<Wrestler> wrestlers) {
        this.wrestlers = (wrestlers != null) ? wrestlers : new ArrayList<>();
    }

    public void addWrestler(Wrestler wrestler) {
        wrestlers.add(wrestler);
    }

    public void removeWrestler(Wrestler wrestler) {
        wrestlers.remove(wrestler);
    }

    public List<Wrestler> getAllWrestlers() {
        return wrestlers;
    }

    public String getAllWrestlersString() {
        if (wrestlers.isEmpty()) {
            return "No wrestlers available.";
        }
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Wrestler w : wrestlers) {
            sb.append(index++).append(". ").append(w.toString()).append("\n");
        }
        return sb.toString();
    }
}
