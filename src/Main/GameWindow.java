package Main;

import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow(GamePanel gamePanel) {

        // Cria um novo objeto JFrame com um título
        JFrame janela = new JFrame("Jogo 2D de Plataforma"); 

        // Define o que acontece quando o usuário clica no botão de fechar.
        // EXIT_ON_CLOSE garante que a aplicação termine.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impede que o usuário redimensione a janela.
        this.setResizable(true);

        // Ajusta o tamanho da janela automaticamente para caber o Main.GamePanel.
        this.add(gamePanel);
        this.pack();

        // Centraliza a janela na tela do computador.
        this.setLocationRelativeTo(null);

        // Torna a janela visível.
        this.setVisible(true);
    }
}
