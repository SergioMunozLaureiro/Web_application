# Web_application
# Wrestling Manager

A Java-based wrestling promotion management application built with a terminal UI. Manage companies, rosters, championships, events, and matches — all from an interactive text interface powered by [Lanterna](https://github.com/mabe02/lanterna).

---

## Features

- Manage multiple wrestling promotions (WWE, AEW, NJPW, TNA, AAA) pre-loaded
- Full roster management: create, edit, delete, and transfer wrestlers between companies
- Championship system: assign titles, track current champions, and vacate belts
- Event management: create events, schedule matches, and track results
- Match engine: choose the winner manually or let the result be randomized
- Statistics per company: most wins, losses, matches, best win rate, most titles, highest rating
- Health system: wrestlers with low health are ineligible until restored

---

## Tech Stack

- **Java 21**
- **Maven** (build & dependency management)
- **Lanterna 3.2.0-alpha1** — terminal UI rendered in a Swing window

---

## Project Structure

```
src/
└── main/java/com/mycompany/text_mode_project/
    ├── Text_mode_project.java       # Entry point
    ├── Controller/
    │   └── Controller.java          # Application logic and navigation
    ├── model/
    │   ├── Company.java             # Promotion entity (wrestlers, titles, shows, events)
    │   ├── Wrestler.java            # Wrestler entity (stats, health, titles)
    │   ├── Event.java               # Event entity (name, place, date, matches)
    │   ├── Match.java               # Match entity (participants, winner, type)
    │   └── Data.java                # Pre-loaded seed data
    ├── Service/
    │   ├── ServiceCompany.java      # CRUD operations for companies
    │   ├── ServiceWrestler.java     # CRUD operations for wrestlers
    │   ├── ServiceEvent.java        # CRUD operations for events
    │   └── ServiceMatch.java        # CRUD operations for matches
    └── view/
        ├── View.java                # All UI components (menus, dialogs, lists)
        └── textArea.java            # Auxiliary text display component
```

---

## How to Run

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Steps

1. Clone the repository:

```bash
git clone [https://github.com/your-username/wrestling-manager.git](https://github.com/SergioMunozLaureiro/Web_application.git)
cd wrestling-manager
```

2. Build the project:

```bash
mvn clean package
```

3. Run the application:

```bash
java -jar target/Text_mode_project-1.0-SNAPSHOT.jar
```

A terminal window will open with the main menu. Use the on-screen options to navigate.

> **Note:** The application runs in a Lanterna terminal emulated inside a Swing window. No additional configuration is required.

---

## Notes

- Data is not persisted between sessions. All changes are lost when the application closes.
- The application ships with pre-loaded data for differnt companies, including wrestlers and championships.
