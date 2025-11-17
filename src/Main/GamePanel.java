package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
//ADICIONA O GAMEMAP AO GAMEPANEL
    private GameMap gameMap;
    //ADICIONA O CAMERAX PARA ACOMPANHAR O JOGADOR DURANTE O JOGO
    private int cameraX = 0;

    public void moveCamera(int amount) {
        cameraX += amount;
    }
    public int getCameraX() {
        return cameraX;
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
    }
}
