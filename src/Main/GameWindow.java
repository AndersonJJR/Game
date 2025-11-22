package Main;

import audio.AudioManager;
import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {

    // --- Variáveis ---
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private GameEngine gameEngine;
    private GamePanel gamePanel;
    private LoginPanel loginPanel;

    public GameWindow() {
        // 1. Configuração da Janela (JFrame)
        super("2D SquareGame");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicia Maximizado

        // 2. Configuração do CardLayout
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 3. Cria e adiciona o Login
        loginPanel = new LoginPanel(this);
        mainContainer.add(loginPanel, "LOGIN");

        // 4. Adiciona o container à janela
        this.add(mainContainer);
        this.setVisible(true);

        // 5. Listener de fechamento
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (gameEngine != null) {
                    System.out.println("Salvando e fechando...");
                    gameEngine.stop();
                }
                System.exit(0);
            }
        });
    }

    /**
     * CORREÇÃO FEITA AQUI:
     * Adicionamos revalidate/repaint para garantir que a tela apareça.
     * Usamos invokeLater para garantir que o teclado funcione.
     */
    public void startGame(int playerID) {
        try {
            // 1. Cria os componentes do jogo
            gamePanel = new GamePanel();
            gameEngine = new GameEngine(gamePanel, playerID);

            // Linka o player
            gamePanel.setPlayer(gameEngine.getPlayer());

            // 2. Adiciona o painel do jogo
            mainContainer.add(gamePanel, "GAME");

            // 3. Troca a tela
            cardLayout.show(mainContainer, "GAME");

            // --- CORREÇÃO 1: Força o Java a desenhar a nova tela AGORA ---
            mainContainer.revalidate();
            mainContainer.repaint();

            // PARAR MÚSICA DO MENU E INICIAR FASE 1
            AudioManager.stopBackgroundMusic();
            AudioManager.playBackgroundMusic("1-fase.wav");


            // --- CORREÇÃO 2: Delay no Foco ---
            // Isso espera a tela terminar de desenhar para ENTÃO pedir o teclado e iniciar o loop.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    gamePanel.requestFocusInWindow(); // Pega o teclado
                    gameEngine.start(); // Inicia o Loop
                }
            });

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o jogo:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro fatal ao iniciar: " + e.getMessage());
        }
    }

} // --- Fim da Classe GameWindow ---