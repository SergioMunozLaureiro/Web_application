package com.mycompany.text_mode_project.model;

import java.util.*;

public class Data {

    public static List<Company> loadCompanies() {
        List<Company> companies = new ArrayList<>();

        Company wwe = new Company("WWE", "USA");
        wwe.addChampionship("WWE Championship");
        wwe.addChampionship("WWE Intercontinental Championship");
        wwe.addChampionship("WWE US Championship");
        wwe.addChampionship("WWE Hardcore Championship");
        wwe.addShow("Raw");
        wwe.addShow("SmackDown");

        Company aew = new Company("AEW", "USA");
        aew.addChampionship("AEW World Championship");
        aew.addChampionship("AEW International Championship");
        aew.addChampionship("AEW TNT Championship");
        aew.addShow("Dynamite");
        aew.addShow("Rampage");

        Company tna = new Company("TNA", "USA");
        tna.addChampionship("TNA World Championship");
        tna.addChampionship("TNA X Division Championship");
        tna.addShow("Impact");

        Company njpw = new Company("NJPW", "Japan");
        njpw.addChampionship("IWGP World Heavyweight Championship");
        njpw.addChampionship("IWGP Jr. Heavyweight Championship");
        njpw.addChampionship("IWGP Never Openweight Championship");
        njpw.addShow("G1 Climax");

        Company aaa = new Company("AAA", "Mexico");
        aaa.addChampionship("Megacampeonato de AAA");
        aaa.addChampionship("Campeonato Latino-Americano de AAA");
        aaa.addChampionship("Campeonato Mundial Crucero de AAA");
        aaa.addShow("Weekly Show");

        companies.add(wwe);
        companies.add(aew);
        companies.add(tna);
        companies.add(njpw);
        companies.add(aaa);

        return companies;
    }

    public static List<Wrestler> loadWrestlers(List<Company> companies) {
        List<Wrestler> wrestlers = new ArrayList<>();

        Company wwe = null, aew = null, tna = null, njpw = null, aaa = null;
        for (Company c : companies) {
            if (c.getName().equalsIgnoreCase("WWE"))  wwe  = c;
            if (c.getName().equalsIgnoreCase("AEW"))  aew  = c;
            if (c.getName().equalsIgnoreCase("TNA"))  tna  = c;
            if (c.getName().equalsIgnoreCase("NJPW")) njpw = c;
            if (c.getName().equalsIgnoreCase("AAA"))  aaa  = c;
        }

        if (wwe == null || aew == null || tna == null || njpw == null || aaa == null) {
            System.out.println("Error: missing companies while loading wrestlers.");
            return wrestlers;
        }

        // WWE
        Wrestler cena    = new Wrestler("John Cena",    wwe,  93, 48, 114, 185, "West Newbury (USA)",   "PowerHouse", "Attitude Adjustment");
        Wrestler reigns  = new Wrestler("Roman Reigns", wwe,  98, 40, 130, 191, "Pensacola (USA)",      "PowerHouse", "Spear");
        Wrestler truth   = new Wrestler("R-Truth",      wwe,  71, 53, 100, 185, "Charlotte (USA)",      "Technical",  "Truth Detector");
        wwe.addWrestler(cena); wwe.addWrestler(reigns); wwe.addWrestler(truth);
        wrestlers.add(cena); wrestlers.add(reigns); wrestlers.add(truth);

        // AEW
        Wrestler omega = new Wrestler("Kenny Omega", aew, 95, 42, 104, 183, "Transcona (Canada)", "Technical", "One Winged Angel");
        // FIX: Cole was added to wrestlers list but never assigned to AEW roster
        Wrestler cole  = new Wrestler("Adam Cole",   aew, 84, 34,  91, 183, "Pennsylvania (USA)", "Technical", "Last Shot");
        aew.addWrestler(omega); aew.addWrestler(cole);
        wrestlers.add(omega); wrestlers.add(cole);

        // TNA
        Wrestler hendry = new Wrestler("Joe Hendry",   tna, 88, 37, 105, 188, "Edinburgh (UK)",    "PowerHouse", "The Standing Ovation");
        Wrestler nemeth = new Wrestler("Nick Nemeth",  tna, 83, 45,  99, 183, "Cleveland (USA)",   "Technical",  "Zig Zag");
        tna.addWrestler(hendry); tna.addWrestler(nemeth);
        wrestlers.add(hendry); wrestlers.add(nemeth);

        // NJPW
        Wrestler naito     = new Wrestler("Tetsuya Naito",    njpw, 90, 43, 102, 180, "Tokyo (Japan)",  "Technical",   "Destino");
        Wrestler tanahashi = new Wrestler("Hiroshi Tanahashi",njpw, 85, 48, 101, 181, "Ogaki (Japan)",  "StrongStyle", "High Fly Flow");
        njpw.addWrestler(naito); njpw.addWrestler(tanahashi);
        wrestlers.add(naito); wrestlers.add(tanahashi);

        // AAA
        Wrestler vikingo = new Wrestler("El Hijo del Vikingo", aaa, 87, 28,  73, 169, "Puebla (Mexico)",          "HighFlyer",  "Avalancha de 630");
        Wrestler clown   = new Wrestler("Psycho Clown",        aaa, 83, 39, 107, 187, "Ciudad de Mexico (Mexico)","PowerHouse", "Psycho Destroyer");
        aaa.addWrestler(vikingo); aaa.addWrestler(clown);
        wrestlers.add(vikingo); wrestlers.add(clown);

        // Free agents
        Wrestler lesnar  = new Wrestler("Brock Lesnar",          null, 93, 48, 130, 191, "Webster (USA)",      "PowerHouse", "F-5");
        Wrestler sabre   = new Wrestler("Zack Sabre Jr.",        null, 86, 38,  95, 183, "Kent (UK)",          "Technical",  "Clarky Cat");
        Wrestler claudio = new Wrestler("Claudio Castagnoli",    null, 82, 44, 105, 196, "Lucerne (Switzerland)", "Technical", "The Neutralizer");
        wrestlers.add(lesnar); wrestlers.add(sabre); wrestlers.add(claudio);

        return wrestlers;
    }

    public static List<Event> loadEvents(List<Company> companies) {
        List<Event> events = new ArrayList<>();

        Company wwe = null, aew = null, tna = null, njpw = null, aaa = null;
        for (Company c : companies) {
            if (c.getName().equalsIgnoreCase("WWE"))  wwe  = c;
            if (c.getName().equalsIgnoreCase("AEW"))  aew  = c;
            if (c.getName().equalsIgnoreCase("TNA"))  tna  = c;
            if (c.getName().equalsIgnoreCase("NJPW")) njpw = c;
            if (c.getName().equalsIgnoreCase("AAA"))  aaa  = c;
        }

        if (wwe == null || aew == null || tna == null || njpw == null || aaa == null) {
            System.out.println("Error: missing companies while loading events.");
            return events;
        }

        Event mania       = new Event("WrestleMania",       "Philadelphia (USA)",       "24/04/2025", wwe);
        Event summerslam  = new Event("SummerSlam",         "Cleveland (USA)",          "02/08/2025", wwe);
        Event allin       = new Event("ALL IN",             "Wembley (UK)",             "16/08/2025", aew);
        // FIX: place and name were swapped in the original
        Event doubleNothing = new Event("Double or Nothing","Las Vegas (USA)",          "25/05/2025", aew);
        Event slam        = new Event("Slammiversary",      "Los Angeles (USA)",        "21/11/2025", tna);
        Event kingdom     = new Event("WrestleKingdom",     "Tokyo (Japan)",            "04/01/2026", njpw);
        Event tmania      = new Event("Triplemania",        "Ciudad de Mexico (Mexico)","24/06/2025", aaa);

        wwe.addEvent(mania);  wwe.addEvent(summerslam);
        aew.addEvent(allin);  aew.addEvent(doubleNothing);
        tna.addEvent(slam);
        njpw.addEvent(kingdom);
        aaa.addEvent(tmania);

        events.add(mania); events.add(summerslam);
        events.add(allin); events.add(doubleNothing);
        events.add(slam);
        events.add(kingdom);
        events.add(tmania);

        return events;
    }
}
