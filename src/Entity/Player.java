package Entity;

import Input.KeyInputs;
import Main.GameEngine;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {

    private final GamePanel gamePanel;
    private final GameEngine gameEngine;
    KeyInputs keyH;

    private BufferedImage sprite;

    private int playerX = 100, playerY = 450, speed = 3 ;
    private int playerLargura = 50, playerAltura = 50;

    private int plataformaX = 0;
    private int plataformaY = 500;
    private int plataformaLargura = GamePanel.LARGURA_TELA;
    private int plataformaAltura = 100;
    private String direction;

    public Player(GameEngine gameEngine , GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.gameEngine = gameEngine;
        loadSprite();
    }

    private void loadSprite() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/res/IDLE.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setKeyInputs(KeyInputs keyH) {
        this.keyH = keyH;
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPlayerLargura() {
        return playerLargura;
    }

    public void setPlayerLargura(int playerLargura) {
        this.playerLargura = playerLargura;
    }

    public int getPlayerAltura() {
        return playerAltura;
    }

    public void setPlayerAltura(int playerAltura) {
        this.playerAltura = playerAltura;
    }

    public int getPlataformaX() {
        return plataformaX;
    }

    public void setPlataformaX(int plataformaX) {
        this.plataformaX = plataformaX;
    }

    public int getPlataformaY() {
        return plataformaY;
    }

    public void setPlataformaY(int plataformaY) {
        this.plataformaY = plataformaY;
    }

    public int getPlataformaLargura() {
        return plataformaLargura;
    }

    public void setPlataformaLargura(int plataformaLargura) {
        this.plataformaLargura = plataformaLargura;
    }

    public int getPlataformaAltura() {
        return plataformaAltura;
    }

    public void setPlataformaAltura(int plataformaAltura) {
        this.plataformaAltura = plataformaAltura;
    }

    public void changePlayerX(int value) {
        this.playerX += value;
    }

    public void changePlayerY(int value) {
        this.playerY += value;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void update() {

        if (keyH == null) {
            return;
        }

        if (keyH.upPressed || keyH.downPressed || keyH.rightPressed || keyH.leftPressed) {

            if (keyH.upPressed) {
                direction = "up";
                playerY -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                playerY += speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                playerX += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                playerX -= speed;
            }
        }
    }
}