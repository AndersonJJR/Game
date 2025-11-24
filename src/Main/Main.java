package Main;

import javax.swing.*;

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