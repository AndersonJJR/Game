package Entity;

import Input.KeyInputs;
import Main.GameEngine;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import audio.AudioManager;


public class Player {

    private final GamePanel gamePanel;
    private final GameEngine gameEngine;
    KeyInputs keyH;

    // Sistema de Sprites
    private BufferedImage idleSpriteSheet;
    private BufferedImage runSpriteSheet;
    private BufferedImage[] idleSprites;
    private BufferedImage[] runSprites;
    private BufferedImage currentSprite;

    // Controle de animação
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final long ANIMATION_VELOCIDADE = 100;

    // Dimensões da spritesheet
    private final int SPRITE_SIZE = 32;
    private int idleTotalFrames = 0;
    private int runTotalFrames = 0;

    // POSIÇÃO NO MUNDO
    private int worldX = 0;
    public int getWorldX() { return worldX; }

    // Estados de animação
    private AnimationState currentState = AnimationState.IDLE;
    private AnimationState previousState = AnimationState.IDLE;

    public enum AnimationState {
        IDLE,
        RUN
    }

    // posição na tela (calculada)
    private int playerX = 100, playerY = 450;
    private final int Velocidade = 2;

    private String direction = "idle";

    public Player(GameEngine gameEngine, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.gameEngine = gameEngine;
        loadSprites();
        lastFrameTime = System.currentTimeMillis();
    }

    public int getSpeedX() {
        if (keyH == null) return 0;
        if (keyH.rightPressed) return Velocidade;
        if (keyH.leftPressed) return -Velocidade;
        return 0;
    }

    private void loadSprites() {
        try {
            idleSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/IDLE.png"));
            if (idleSpriteSheet != null) {
                idleTotalFrames = idleSpriteSheet.getWidth() / SPRITE_SIZE;
                idleSprites = new BufferedImage[idleTotalFrames];
                for (int i = 0; i < idleTotalFrames; i++) {
                    idleSprites[i] = idleSpriteSheet.getSubimage(i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
                }
            }

            runSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/RUN.png"));
            if (runSpriteSheet != null) {
                runTotalFrames = runSpriteSheet.getWidth() / SPRITE_SIZE;
                runSprites = new BufferedImage[runTotalFrames];
                for (int i = 0; i < runTotalFrames; i++) {
                    runSprites[i] = runSpriteSheet.getSubimage(i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
                }
            }

            if (idleSprites != null && idleSprites.length > 0) currentSprite = idleSprites[0];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAnimation() {
        if (currentState != previousState) {
            currentFrame = 0;
            previousState = currentState;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= ANIMATION_VELOCIDADE) {
            BufferedImage[] activeSprites;
            int totalFrames;

            if (currentState == AnimationState.RUN) {
                activeSprites = runSprites;
                totalFrames = runTotalFrames;
            } else {
                activeSprites = idleSprites;
                totalFrames = idleTotalFrames;
            }

            if (activeSprites != null && totalFrames > 0) {
                currentFrame = (currentFrame + 1) % totalFrames;
                currentSprite = activeSprites[currentFrame];
                lastFrameTime = currentTime;
            }
        }
    }

    private void updateAnimationState() {
        boolean isMoving = keyH != null && (keyH.rightPressed || keyH.leftPressed);
        currentState = isMoving ? AnimationState.RUN : AnimationState.IDLE;
    }

    public void setKeyInputs(KeyInputs keyH) {
        this.keyH = keyH;
    }

    public BufferedImage getSprite() { return currentSprite; }

    // ====================================================================
    // MOVIMENTO PRINCIPAL (ESTILO MARIO)
    // ====================================================================
    public void update() {
        if (keyH == null) return;

        updateAnimationState();
        updateAnimation();

        int inputSpeed = getSpeedX();

        // DEADZONE (meio da tela)
        int deadzone = gamePanel.getWidth() / 2 - 50;

        // Atualiza worldX SEMPRE que o player anda
        worldX += inputSpeed;

        if (worldX < 0) worldX = 0;

        // Quando worldX > deadzone → move o mapa
        if (worldX > deadzone) {
            gamePanel.moveCamera(inputSpeed);
        }

        // PlayerX é sempre baseado no mundo - câmera
        int camX = gamePanel.getCameraX();
        playerX = worldX - camX;

        // Movimento vertical simples
        if (keyH.upPressed) playerY -= Velocidade;
        if (keyH.downPressed) playerY += Velocidade;

        // Mantém o player na tela (opcional)
        if (playerX < 0) playerX = 0;
        if (playerX > gamePanel.getWidth() - 50) playerX = gamePanel.getWidth() - 50;
    }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }

    public double getGameTime() {
        return gameEngine.getElapsedGameTimeSeconds();
    }


// ===================================
// SOM DE MORTE – você coloca aqui ↓
// ===================================
public void playDeathSound() {
    AudioManager.stopBackgroundMusic();
    AudioManager.playBackgroundMusic("death.wav");
}
 }