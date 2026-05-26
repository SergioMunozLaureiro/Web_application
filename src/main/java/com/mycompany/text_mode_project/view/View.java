package com.mycompany.text_mode_project.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.mycompany.text_mode_project.model.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.TextColor;
import static java.awt.SystemColor.text;

import java.io.IOException;
import java.util.*;

public class View {
    private MultiWindowTextGUI gui;
    DefaultTerminalFactory factory = new DefaultTerminalFactory();

   
    public View() {
        try {
            com.googlecode.lanterna.terminal.DefaultTerminalFactory terminalFactory
                    = new com.googlecode.lanterna.terminal.DefaultTerminalFactory();

            // Crear el terminal (puede ser un SwingTerminalFrame)
            com.googlecode.lanterna.terminal.Terminal terminal = terminalFactory.createTerminal();

            // Si es SwingTerminalFrame (ventana Swing), ajustamos colores del contenido Swing
            if (terminal instanceof com.googlecode.lanterna.terminal.swing.SwingTerminalFrame swingTerminal) {
                // Ejecutar cambios en el hilo de Swing para evitar problemas de concurrencia
                javax.swing.SwingUtilities.invokeLater(() -> {
                    java.awt.Container content = swingTerminal.getContentPane();
                    if (content != null) {
                        content.setBackground(java.awt.Color.BLACK);
                        content.setForeground(java.awt.Color.WHITE);
                        // Forzar repintado / actualización de la UI
                        content.invalidate();
                        content.validate();
                        content.repaint();
                    }

                    // También, intentar cambiar la fuente (opcional)
                    // swingTerminal.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
                });
            }

            // Crear screen y GUI con el terminal ya creado
            com.googlecode.lanterna.screen.Screen screen
                    = new com.googlecode.lanterna.screen.TerminalScreen(terminal);
            screen.startScreen();

            gui = new com.googlecode.lanterna.gui2.MultiWindowTextGUI(
                    screen, new com.googlecode.lanterna.gui2.DefaultWindowManager(), new com.googlecode.lanterna.gui2.EmptySpace()
            );

        } catch (java.io.IOException e) {
            throw new RuntimeException("Error initializing terminal interface", e);
        }
    }

 // MÉTODOS DE ENTRADA
    // ========================
   public String askString(String message) {
    String input;
    do {
        TextInputDialog dialog = new TextInputDialog(message, "");
        input = dialog.showDialog(gui);

        if (input == null || input.trim().isEmpty()) {
            showMessage("This field cannot be empty. Please enter a valid value.");
            input = null;
        }
    } while (input == null);

    return input.trim();
}


    public int askInt(String message) {
        while (true) {
            String input = new TextInputDialog(message, "").showDialog(gui);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showMessage("Please introduce a valid number.");
            }
        }
    }

    public float askFloat(String message) {
        while (true) {
            String input = new TextInputDialog(message, "").showDialog(gui);
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                showMessage("Please introduce a valid number.");
            }
        }
    }

    // ========================
    // SELECCIÓN DE OBJETOS
    // ========================
    public Company askCompany(List<Company> companies) {
        if (companies == null || companies.isEmpty()) {
            showMessage("No Companies registered.");
            return null;
        }
        return new ListSelectDialog<>("Select a Company", companies, Company::getName).showDialog(gui);
    }

    public Match askMatch(List<Match> matches) {
        if (matches == null || matches.isEmpty()) {
            showMessage("No matches registered.");
            return null;
        }
        return new ListSelectDialog<>("Select a Match", matches, Match::toString).showDialog(gui);
    }

    public String askShow(List<String> shows) {
        if (shows.isEmpty()) {
            showMessage("No shows available.");
            return null;
        }
        return new ListSelectDialog<>("Select a Show", shows, s -> s).showDialog(gui);
    }

    public String askTitle(List<String> titles) {
        if (titles.isEmpty()) {
            showMessage("No titles available.");
            return null;
        }
        return new ListSelectDialog<>("Select a Title", titles, s -> s).showDialog(gui);
    }

    public Wrestler askWrestler(List<Wrestler> participants) {
        if (participants.isEmpty()) {
            showMessage("No wrestlers available.");
            return null;
        }
        return new ListSelectDialog<>("Select a Wrestler", participants, Wrestler::getName).showDialog(gui);
    }
    


    // ========================
    // MOSTRAR MENSAJES Y DATOS
    // ========================
    public void showMessage(String message) {
        if (message.length() < 300) {
            MessageDialog.showMessageDialog(gui, "Info", message);
            return;
        }

        // Si es largo, mostrarlo en un cuadro con scroll
        BasicWindow window = new BasicWindow("Details");
           message = wrapText(message, 80);

        TextBox textBox = new TextBox(message, TextBox.Style.MULTI_LINE)
                .setReadOnly(true)
                .setHorizontalFocusSwitching(true)
                .setPreferredSize(new TerminalSize(80, 25)); // Ajusta ancho y alto

        Panel panel = new Panel(new BorderLayout());
        panel.addComponent(textBox, BorderLayout.Location.CENTER);
        panel.addComponent(new Button("OK", () -> window.close()), BorderLayout.Location.BOTTOM);

        window.setComponent(panel);
        window.setHints(java.util.List.of(Window.Hint.CENTERED));
        gui.addWindowAndWait(window);
    }
      
    public void showWrestler(Wrestler w) {
        String details = String.format("""
                NAME: %s
                AGE: %d
                WEIGHT: %d
                HEIGHT: %d
                HOMETOWN: %s
                RATING: %d
                COMPANY: %s
                TITLES: %s
                STYLE: %s
                FINISHER: %s
                TOTAL MATCHES: %d
                WINS: %d
                LOSSES: %d
                HEALTH: %d
                """,
                w.getName(), w.getAge(), w.getWeight(), w.getHeight(), w.getHometown(),
                w.getRating(), w.getCompany() != null ? w.getCompany().getName() : "None",
                w.getTitles(), w.getStyle(), w.getFinisher(),
                w.getMatches(), w.getWins(), w.getLosses(), w.getHealth());
        // Creamos la ventana
    BasicWindow window = new BasicWindow("Wrestler Details");
    Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

    // TextBox para mostrar información (read-only)
    TextBox infoBox = new TextBox(wrapText(details, 80))
            .setReadOnly(true)
            .setPreferredSize(new TerminalSize(80, 20)); // puedes ajustar altura

    // Botón para salir/cerrar
    Button backButton = new Button("Back", window::close);

    panel.addComponent(infoBox);
    panel.addComponent(new EmptySpace());
    panel.addComponent(backButton);

    window.setComponent(panel);

    // Mostrar la ventana y esperar que se cierre
    gui.addWindowAndWait(window);
    }

   public void showCompany(Company company) {
    // Formateamos la información
    String details = String.format("""
            NAME: %s
            COUNTRY: %s
            SHOWS: %s
            WRESTLERS: %s
            TITLES: %s
            EVENTS: %s
            """,
            company.getName(),
            company.getCountry(),
            company.getShows(),
            company.getWrestlers(),
            company.getChampionships(),
            company.getEvents()
    );

    // Creamos la ventana
    BasicWindow window = new BasicWindow("Company Details");
    Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

    // TextBox para mostrar información (read-only)
    TextBox infoBox = new TextBox(wrapText(details, 80))
            .setReadOnly(true)
            .setPreferredSize(new TerminalSize(80, 20)); // puedes ajustar altura

    // Botón para salir/cerrar
    Button backButton = new Button("Back", window::close);

    panel.addComponent(infoBox);
    panel.addComponent(new EmptySpace());
    panel.addComponent(backButton);

    window.setComponent(panel);

    // Mostrar la ventana y esperar que se cierre
    gui.addWindowAndWait(window);
}


    // ========================
    // MENÚS
    // ========================
    public void showMainMenu() {
        showMessage("""
                ===== MAIN MENU =====
                1. Manage Companies
                2. Manage Wrestlers
                3. Manage Matches
                4. Manage Stats
                0. Exit
                """);
    }
    public void showWrestlerMenu(){
        showMessage("""
                ===== WRESTLER MENU =====
                1. Create Wrestler
                2. Edit Wrestler
                3. Delete Wrestler
                4. Show Wrestlers
                5. Move Wrestler to another Company
                0. Exit
                """);
    }
    public void showWrestlerEditMenu(Wrestler w) {
        showMessage("""
                ===== EDIT WRESTLER: %s =====
                1. Change Name
                2. Change Age
                3. Change Weight
                4. Change Style
                5. Change Finisher
                0. Return
                """.formatted(w.getName()));
    }

    public void showCompanyEditMenu(Company c) {
        showMessage("""
                ===== EDIT COMPANY: %s =====
                1. Change Name
                2. Change Country
                0. Return
                """.formatted(c.getName()));
    }

    public void showStatsCompanyChoosen(Company company) {
        showMessage("""
                ===== %s STATS MENU =====
                1. Most wins
                2. Most losses
                3. Most matches
                4. Win-rate
                5. Most titles held currently
                6. Highest rating
                7. Total matches
                0. Back to Statistics menu
                """.formatted(company.getName()));
    }

    public int showCompanyChooseMenu(List<Company> companies) {
        if (companies.isEmpty()) {
            showMessage("There are no companies registered.");
            return 0;
        }
        StringBuilder sb = new StringBuilder("===== SELECT A COMPANY =====\n");
        int i = 1;
        for (Company c : companies) sb.append(i++).append(". ").append(c.getName()).append("\n");
        showMessage(sb.toString());
        return askInt("Choose a company: ");
    }

    public void showCompanyChosen(Company company) {
        showMessage("""
                ===== %s MENU =====
                1. Show company
                2. Edit company
                3. Add wrestler
                4. Delete wrestler
                5. View wrestler
                6. Add show
                7. Delete show
                8. Add title
                9. Delete title
                10. Choose a champion
                11. Delete company
                12. Manage events
                0. Back to Company menu
                """.formatted(company.getName()));
    }

    public void showMatchMenu() {
        showMessage("""
                ===== MATCH MENU =====
                1. Create a Match
                2. Delete a Match
                0. Back to Main menu
                """);
    }
   public int showCompanyMenu() {
    Panel panel = new Panel();
    panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    RadioBoxList<String> options = new RadioBoxList<>();
    options.addItem("1. Add Company");
    options.addItem("2. Show Companies");
    options.addItem("3. Select Company");
    options.addItem("0. Back to Main menu");

    panel.addComponent(new Label("===== COMPANY MENU ====="));
    panel.addComponent(options);

    BasicWindow window = new BasicWindow("Company Menu");

    Button ok = new Button("OK", window::close);
    panel.addComponent(ok);

    window.setComponent(panel);
    gui.addWindowAndWait(window);

    String selected = options.getCheckedItem();
    if (selected == null) return -1;

    return Character.getNumericValue(selected.charAt(0));
}
public void close() {
    try {
        if (gui != null && gui.getScreen() != null) {
            gui.getScreen().stopScreen();
        }
    } catch (IOException e) {
        System.err.println("Error closing screen: " + e.getMessage());
    }
}



    public void showTypeMatchMenu() {
        showMessage("""
                ===== TITLE ON THE LINE? =====
                1. Yes
                2. No
                0. Back to Match menu
                """);
    }

   

    public void showEventMenu() {
        showMessage("""
                ===== EVENT MENU =====
                1. Create event
                2. Choose event
                0. Back to Company menu
                """);
    }

    public void showMatchEditMenu() {
        showMessage("""
                ===== MATCH EDIT MENU =====
                1. Participants
                2. Winner
                0. Back to Event menu
                """);
    }

    

    public int showEventChooseMenu(Company company, List<Event> events) {
        if (events.isEmpty()) {
            showMessage("There are no events registered.");
            return 0;
        }
        StringBuilder sb = new StringBuilder("===== SELECT AN EVENT =====\n");
        int i = 1;
        for (Event e : events) sb.append(i++).append(". ").append(e.getName()).append("\n");
        showMessage(sb.toString());
        return askInt("Choose an event: ");
    }

    public void showChoosenEvent(Event event) {
        showMessage("""
                ===== %s MENU =====
                1. Edit event
                2. Delete event
                3. Show event
                4. Manage matches
                0. Return to Event menu
                """.formatted(event.getName()));
    }
public void showAllEvents(Event event) {
    
    String details = String.format("""
            NAME: %s
            PLACE: %s
            DATE: %s
            MATCHES:                                           
            """,
            event.getName(),
            event.getPlace(),
            event.getDate()
            
    );
    for (Match m : event.getMatches()) {
        details += "- " + m.toString() + "\n\n";
    }
    // Creamos la ventana
    BasicWindow window = new BasicWindow("Company Details");
    Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

    // TextBox para mostrar información (read-only)
    TextBox infoBox = new TextBox(wrapText(details, 80))
            .setReadOnly(true)
            .setPreferredSize(new TerminalSize(80, 20)); // puedes ajustar altura

    // Botón para salir/cerrar
    Button backButton = new Button("Back", window::close);

    panel.addComponent(infoBox);
    panel.addComponent(new EmptySpace());
    panel.addComponent(backButton);

    window.setComponent(panel);

    // Mostrar la ventana y esperar que se cierre
    gui.addWindowAndWait(window);
}



 public void showEditEventMenu(Event event) {
        showMessage("""
                ===== EDIT EVENT: %s =====
                1. Change Name
                2. Change Place
                3. Change Date
                0. Return
                """.formatted(event.getName()));
    }

    public Match askmatches(List<Match> matches) {
        if (matches == null || matches.isEmpty()) {
            MessageDialog.showMessageDialog(gui, "Warning", "No matches available");
            return null;
        }

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        RadioBoxList<Match> list = new RadioBoxList<>();
        for (Match match : matches) {
            list.addItem(match);
        }

        panel.addComponent(new Label("Select a Match:"));
        panel.addComponent(list);

        final BasicWindow window = new BasicWindow("Matches");
        window.setComponent(panel);

        Button ok = new Button("OK", window::close);
        panel.addComponent(ok);

        gui.addWindowAndWait(window);

        return list.getCheckedItem();
    }

    public void showEdingDecisionMenu() {
        showMessage("""
                ===== WINNER? =====
                1. I decide
                2. Random
                0. Back to Type match menu
                """);
    }
    private String wrapText(String text, int lineLength) {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (String word : text.split(" ")) {
            if (count + word.length() > lineLength) {
                sb.append("\n");
                count = 0;
            }
            sb.append(word).append(" ");
            count += word.length() + 1;
        }
        return sb.toString();
    }

    // En tu clase View
    public void displayText(String text) {
        showMessage(text); // reutiliza tu método existente
    }

    

    
}

// ===============================
// CLASES AUXILIARES
// ===============================
class TextInputDialog {
    private final String title;
    private final String defaultValue;

    TextInputDialog(String title, String defaultValue) {
        this.title = title;
        this.defaultValue = defaultValue;
    }

   public String showDialog(MultiWindowTextGUI gui) {
    Panel panel = new Panel();
    panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    TextBox input = new TextBox(new TerminalSize(30, 1), defaultValue);
    panel.addComponent(new Label(title));
    panel.addComponent(input);

    BasicWindow window = new BasicWindow(title);

    Button ok = new Button("OK", window::close);
    panel.addComponent(ok);

    window.setComponent(panel);
    gui.addWindowAndWait(window);

    return input.getText();
}

}

class ListSelectDialog<T> {
    private final String title;
    private final List<T> items;
    private final java.util.function.Function<T, String> toStringFunc;

    ListSelectDialog(String title, List<T> items, java.util.function.Function<T, String> toStringFunc) {
        this.title = title;
        this.items = items;
        this.toStringFunc = toStringFunc;
    }
    

  public T showDialog(MultiWindowTextGUI gui) {
    Panel panel = new Panel();
    panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    RadioBoxList<T> list = new RadioBoxList<>();
    for (T item : items) list.addItem(item);

    panel.addComponent(new Label(title));
    panel.addComponent(list);

    BasicWindow window = new BasicWindow(title);

    Button ok = new Button("OK", window::close);
    panel.addComponent(ok);

    window.setComponent(panel);
    gui.addWindowAndWait(window);

    return list.getCheckedItem();
}

}

