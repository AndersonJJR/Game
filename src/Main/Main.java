package Main;

import javax.swing.*;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Inicia o Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameWindow();
            }
        });
    }
}