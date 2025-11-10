package Main;

import Entity.Player;
import Input.KeyInputs;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    //Constantes para a largura e altura da tela.
    public static final int LARGURA_TELA = 1920;
    public static final int ALTURA_TELA = 1080;

    Player player;

    public GamePanel() {
        this.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        this.setBackground(Color.BLACK);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (player == null) {
            return;
        }

        // --- NOVO: Preparar o texto do tempo ---
        // 1. Pegamos o tempo (em segundos) do player
        double gameTime = player.getGameTime();

        // 2. Formatamos para exibir apenas 2 casas decimais
        String timeString = String.format("Tempo: %.2f", gameTime);

        // 3. Definimos a fonte e a cor do texto
        g.setColor(Color.WHITE); // Cor do texto
        g.setFont(new Font("Arial", Font.BOLD, 24)); // Fonte, Estilo, Tamanho

        // 4. Desenhamos o tempo no canto superior esquerdo (x=20, y=30)
        g.drawString(timeString, 20, 30);
        // --- Fim da NOVIDADE ---

        // Desenha o jogador (código original)
        if (player.getSprite() != null) {
            g.drawImage(player.getSprite(), player.getPlayerX(), player.getPlayerY(),
                    128 , 128, null);
        }

        // Desenha a plataforma (código original)
        g.setColor(Color.GREEN);
        g.fillRect(player.getPlataformaX(), player.getPlataformaY(), player.getPlataformaLargura(), player.getPlataformaAltura());
    }
}
