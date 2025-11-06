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

    // Sistema de Sprites
    private BufferedImage spriteSheet;
    private BufferedImage[] idleSprites;
    private BufferedImage currentSprite;
    
    // Controle de animação
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final long ANIMATION_SPEED = 1000; // 1 segundo por frame (1000ms)
    
    // Dimensões da spritesheet
    private final int SPRITE_SIZE = 32; // Cada sprite tem 64x64 pixels
    private int totalFrames = 0;

    private int playerX = 100, playerY = 450, speed = 2;
    private int playerLargura = 50, playerAltura = 50;

    private int plataformaX = 0;
    private int plataformaY = 500;
    private int plataformaLargura = GamePanel.LARGURA_TELA;
    private int plataformaAltura = 100;
    private String direction;

    public Player(GameEngine gameEngine, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.gameEngine = gameEngine;
        loadSprites();
        lastFrameTime = System.currentTimeMillis(); // Inicializa o timer
    }

    private void loadSprites() {
        try {
            // Carrega a spritesheet completa
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/IDLE.png"));
            
            if (spriteSheet != null) {
                // Calcula quantos frames existem na spritesheet (assumindo que é horizontal)
                totalFrames = spriteSheet.getWidth() / SPRITE_SIZE;
                
                // Cria array para armazenar cada frame individual
                idleSprites = new BufferedImage[totalFrames];
                
                // Recorta cada frame da spritesheet usando subImage
                for (int i = 0; i < totalFrames; i++) {
                    idleSprites[i] = spriteSheet.getSubimage(
                        i * SPRITE_SIZE,  // x position
                        0,                // y position (assumindo que todos os frames estão na primeira linha)
                        SPRITE_SIZE,      // width
                        SPRITE_SIZE       // height
                    );
                }
                
                // Define o primeiro sprite como atual
                currentSprite = idleSprites[0];
                
                System.out.println("Spritesheet carregada com sucesso! Total de frames: " + totalFrames);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a spritesheet!");
        }
    }
    
    /**
     * Atualiza a animação do sprite baseado no tempo
     */
    private void updateAnimation() {
        if (idleSprites == null || totalFrames == 0) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Verifica se já passou tempo suficiente para trocar de frame
        if (currentTime - lastFrameTime >= ANIMATION_SPEED) {
            // Avança para o próximo frame
            currentFrame = (currentFrame + 1) % totalFrames;
            
            // Atualiza o sprite atual
            currentSprite = idleSprites[currentFrame];
            
            // Atualiza o tempo do último frame
            lastFrameTime = currentTime;
            
            // Debug: mostra qual frame está sendo exibido
            System.out.println("Frame atual: " + (currentFrame + 1) + "/" + totalFrames);
        }
    }
    
    /**
     * Método alternativo para controlar a velocidade da animação
     * @param animationSpeedMs velocidade em milissegundos
     */
    public void setAnimationSpeed(long animationSpeedMs) {
        // Este método pode ser usado para mudar a velocidade durante o jogo
        // Por exemplo: setAnimationSpeed(500) para animação mais rápida
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

    /**
     * Retorna o sprite atual para renderização
     */
    public BufferedImage getSprite() {
        return currentSprite;
    }
    
    /**
     * Método principal de atualização do player
     */
    public void update() {
        // Atualiza a animação primeiro
        updateAnimation();
        
        // Verifica se o sistema de input está funcionando
        if (keyH == null) {
            return;
        }

        // Processa movimento baseado nas teclas pressionadas
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