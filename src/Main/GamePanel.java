package Main;

import Entity.Player;
import Input.KeyInputs;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    //Constantes para a largura e altura da tela.
    public static final int LARGURA_TELA = 900;
    public static final int ALTURA_TELA = 800;

    Player player;

    public GamePanel() {
        this.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha o jogador
        g.setColor(Color.WHITE);
        g.fillRect(player.getPlayerX(), player.getPlayerY(), player.getPlataformaLargura(), player.getPlataformaAltura());

        // Desenha a plataforma
        g.setColor(Color.GREEN);
        g.fillRect(player.getPlataformaX(), player.getPlataformaY(), player.getPlataformaLargura(), player.getPlataformaAltura());
    }
}
