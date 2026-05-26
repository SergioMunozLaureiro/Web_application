/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.text_mode_project;
import com.mycompany.text_mode_project.Controller.Controller;
import com.mycompany.text_mode_project.view.View;
import com.mycompany.text_mode_project.model.*;
import java.util.*;

/**
 *
 * @author s.munozl.2023
 */
public class Text_mode_project {

    public static void main(String[] args) {
        View view=new View();
        Controller controller = new Controller(view);
        controller.mainMenu();
  
    }
}
