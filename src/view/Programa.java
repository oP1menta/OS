package view;


import Controller.Controller;

import javafx.application.Application;

public class Programa {
    public static void main(String[] args) {

        Controller controller = new Controller();

        MainView.setLoginHandler(controller::login);
         
        Application.launch(MainView.class, args);
    }
}

