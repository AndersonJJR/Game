package Main;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {

    public GameWindow(GamePanel gamePanel, GameEngine gameEngine) {

        // Cria um novo objeto JFrame com um título
        JFrame janela = new JFrame("Jogo 2D de Plataforma"); 

        // Define o que acontece quando o usuário clica no botão de fechar.
        // EXIT_ON_CLOSE garante que a aplicação termine.
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Impede que o usuário redimensione a janela.
        this.setResizable(true);

        // Ajusta o tamanho da janela automaticamente para caber o Main.GamePanel.
        this.add(gamePanel);
        this.pack();

        // Centraliza a janela na tela do computador.
        this.setLocationRelativeTo(null);

        // Torna a janela visível.
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Isso será executado quando você clicar no 'X'
                System.out.println("Janela fechando, salvando pontuação...");

                // 1. Chama o método stop() para salvar no banco
                gameEngine.stop();

                // 2. Fecha a aplicação
                System.exit(0);
            }
        });
    }

}
