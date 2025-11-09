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
    private BufferedImage idleSpriteSheet;
    private BufferedImage runSpriteSheet;
    private BufferedImage[] idleSprites;
    private BufferedImage[] runSprites;
    private BufferedImage currentSprite;

    // Controle de animação
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final long ANIMATION_SPEED = 100; // 100ms por frame (animação mais rápida)

    // Dimensões da spritesheet
    private final int SPRITE_SIZE = 32; // Cada sprite tem 32x32 pixels
    private int idleTotalFrames = 0;
    private int runTotalFrames = 0;

    // Estados de animação
    private AnimationState currentState = AnimationState.IDLE;
    private AnimationState previousState = AnimationState.IDLE;

    // Enum para controlar estados de animação
    public enum AnimationState {
        IDLE,
        RUN
    }

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
        lastFrameTime = System.currentTimeMillis();
    }

    private void loadSprites() {
        try {
            // Carrega a spritesheet IDLE
            idleSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/IDLE.png"));

            if (idleSpriteSheet != null) {
                idleTotalFrames = idleSpriteSheet.getWidth() / SPRITE_SIZE;
                idleSprites = new BufferedImage[idleTotalFrames];

                for (int i = 0; i < idleTotalFrames; i++) {
                    idleSprites[i] = idleSpriteSheet.getSubimage(
                            i * SPRITE_SIZE,
                            0,
                            SPRITE_SIZE,
                            SPRITE_SIZE
                    );
                }
                System.out.println("IDLE Spritesheet carregada! Frames: " + idleTotalFrames);
            }

            // Carrega a spritesheet RUN
            runSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/RUN.png"));

            if (runSpriteSheet != null) {
                runTotalFrames = runSpriteSheet.getWidth() / SPRITE_SIZE;
                runSprites = new BufferedImage[runTotalFrames];

                for (int i = 0; i < runTotalFrames; i++) {
                    runSprites[i] = runSpriteSheet.getSubimage(
                            i * SPRITE_SIZE,
                            0,
                            SPRITE_SIZE,
                            SPRITE_SIZE
                    );
                }
                System.out.println("RUN Spritesheet carregada! Frames: " + runTotalFrames);
            }

            // Define o primeiro sprite IDLE como atual
            currentSprite = idleSprites[0];

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar as spritesheets!");
        }
    }

    /**
     * Atualiza a animação do sprite baseado no tempo e estado
     */
    private void updateAnimation() {
        // Verifica se mudou de estado
        if (currentState != previousState) {
            currentFrame = 0; // Reinicia a animação ao mudar de estado
            previousState = currentState;
            System.out.println("Estado mudou para: " + currentState);
        }

        long currentTime = System.currentTimeMillis();

        // Verifica se já passou tempo suficiente para trocar de frame
        if (currentTime - lastFrameTime >= ANIMATION_SPEED) {
            // Seleciona o array de sprites correto baseado no estado
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
                // Avança para o próximo frame
                currentFrame = (currentFrame + 1) % totalFrames;
                currentSprite = activeSprites[currentFrame];
                lastFrameTime = currentTime;
            }
        }
    }

    /**
     * Define o estado de animação baseado no movimento
     */
    private void updateAnimationState() {
        // Verifica se está se movendo
        boolean isMoving = keyH.rightPressed || keyH.leftPressed;

        if (isMoving) {
            currentState = AnimationState.RUN;
        } else {
            currentState = AnimationState.IDLE;
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
        // Verifica se o sistema de input está funcionando
        if (keyH == null) {
            return;
        }

        // Atualiza o estado da animação baseado no input
        updateAnimationState();

        // Atualiza a animação
        updateAnimation();

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
