package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    //ADICIONA O GAMEMAP AO GAMEPANEL
    private GameMap gameMap;
    //ADICIONA O CAMERAX PARA ACOMPANHAR O JOGADOR DURANTE O JOGO
    private int cameraX = 0;

    private boolean isPaused = false;

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }


    public static final int LARGURA_TELA = 1920;
    public static final int ALTURA_TELA = 1080;

    private Player player;

    public GamePanel() {

        this.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        this.setBackground(Color.BLACK);

        // RODA O MAPA PROPRIAMENTE DITO
        gameMap = new GameMap();
    }

    public void setPlayer(Player player) {
        this.player = player;

        // LIGA O PLAYER AO MAPA (ESSENCIAL)
        gameMap.setPlayer(player);
    }

    public void moveCamera(int amount) {
        cameraX += amount;
    }

    public int getCameraX() {
        return cameraX;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (player == null) {
            return;
        }

        // PARALLEX DO MAPA
        gameMap.draw((Graphics2D) g, getWidth(), getHeight(), player);

        // --- Desenha timer ---
        double gameTime = player.getGameTime();
        String timeString = String.format("Tempo: %.2f", gameTime);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(timeString, 20, 30);

        // --- Desenha o jogador ---
        if (player.getSprite() != null) {
            g.drawImage(
                    player.getSprite(),
                    player.getPlayerX(),
                    player.getPlayerY(),
                    128,
                    128,
                    null
            );
        }
        //Desenha o MENU DE PAUSE por cima de tudo ---
        if (isPaused) {
            drawPauseScreen((Graphics2D) g);
        }
    }
    //Método visual do Pause ---

    private void drawPauseScreen(Graphics2D g2) {
        // Fundo Preto Transparente (O 4º número é a transparência 0-255)
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Configura Texto
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 100));

        String text = "PAUSADO";

        // Centraliza o Texto na tela matematicamente
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = getHeight() / 2;

        g2.drawString(text, x, y);

        // Texto menor de instrução
        g2.setFont(new Font("Arial", Font.PLAIN, 40));
        String subText = "Pressione ESC para voltar";
        x = (getWidth() - g2.getFontMetrics().stringWidth(subText)) / 2;
        g2.drawString(subText, x, y + 80);
    }
}
