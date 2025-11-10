package Main;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Você esqueceu desta linha - a declaração da classe
public class GameWindow extends JFrame {

    // --- Variáveis movidas para dentro da classe ---
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private GameEngine gameEngine; // O motor do jogo, começa nulo
    private GamePanel gamePanel;   // O painel do jogo
    private LoginPanel loginPanel; // O painel de login

    public GameWindow() {
        // 1. Configuração da Janela (JFrame)
        super("2D SquareGame"); // Define o título aqui
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Vamos controlar o fechamento
        this.setResizable(true);

        // 2. Configuração do CardLayout para trocar de tela
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 3. Cria e adiciona o painel de login primeiro
        loginPanel = new LoginPanel(this); // Passa a referência desta janela
        mainContainer.add(loginPanel, "LOGIN");

        // 4. Adiciona o container principal à janela
        this.add(mainContainer);
        this.pack(); // Ajusta o tamanho ao LoginPanel
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // 5. Adiciona o listener de fechamento (o mesmo de antes)
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Só tenta salvar se o motor do jogo já foi iniciado
                if (gameEngine != null) {
                    System.out.println("Janela fechando, salvando pontuação...");
                    gameEngine.stop();
                } else {
                    System.out.println("Janela fechada antes de iniciar o jogo.");
                }
                // Fecha a aplicação
                System.exit(0);
            }
        });

    } // --- Fim do Construtor GameWindow() ---

    /**
     * Chamado pelo LoginPanel para iniciar o jogo.
     * @param playerID O ID do jogador que veio do banco.
     */
    // --- Método movido para FORA do construtor ---
    public void startGame(int playerID) {
        // 1. Cria os componentes do jogo
        gamePanel = new GamePanel();
        gameEngine = new GameEngine(gamePanel, playerID);

        // Linka o player criado no GameEngine ao GamePanel
        gamePanel.setPlayer(gameEngine.getPlayer());

        // 2. Adiciona o painel do jogo ao CardLayout
        mainContainer.add(gamePanel, "GAME");

        // 3. Troca a tela para o painel do jogo
        cardLayout.show(mainContainer, "GAME");

        // 4. Dá o foco ao GamePanel para que os inputs funcionem
        gamePanel.requestFocusInWindow();

        // 5. Inicia o loop do jogo
        gameEngine.start();
    }

} // --- Fim da Classe GameWindow ---