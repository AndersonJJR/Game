package Main;

import Entity.Player;
import Input.KeyInputs;

import java.awt.*;
import java.io.Serial;

public class GameEngine extends Canvas implements Runnable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Variáveis
    private GamePanel gamePanel;
    private Player player;
    private KeyInputs keyInputs;

    private double elapsedGameTimeSeconds = 0; // O tempo de jogo acumulado

    // Controle do Loop
    private Thread gameThread;
    private volatile boolean running = false;

    // Controle FPS
    public static final int FPS = 60;
    public static final float SECONDS_PER_UPDATE = 1.0f / FPS;

    private int playerID;

    public GameEngine(GamePanel gamePanel, int playerID) {
        this.gamePanel = gamePanel;
        this.playerID = playerID;

        this.player = new Player(this, gamePanel);
        this.keyInputs = new KeyInputs();

        // Configurações
        gamePanel.setPlayer(player);
        player.setKeyInputs(keyInputs);
        gamePanel.addKeyListener(keyInputs);
        gamePanel.setFocusable(true);
    }

    // --- PAUSE: Checa se apertou ESC ---
    private void checkPauseInput() {
        if (keyInputs.escPressed) {
            boolean estadoAtual = gamePanel.isPaused();
            gamePanel.setPaused(!estadoAtual); // Inverte
            keyInputs.escPressed = false;      // Consome o clique
        }
    }

    // --- UPDATE: Lógica de movimento (Só roda se não estiver pausado) ---
    private void update() {
        if (player != null) {
            player.update();
        }
        // updateInimigos(); // Adicione outras lógicas aqui
    }

    protected void render(float interpolation) {
        // Reservado para interpolação visual
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        Util.DatabaseConnection.saveScore(this.playerID, elapsedGameTimeSeconds);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double timeAccumulator = 0.0;

        long timer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while (running) {
            long now = System.nanoTime();
            double elapsedTime = (now - lastTime) / 1000000000.0;
            lastTime = now;

            // 1. Verifica Pause (SEMPRE RODA)
            checkPauseInput();

            // 2. Lógica do Jogo (SÓ RODA SE NÃO TIVER PAUSADO)
            if (!gamePanel.isPaused()) {

                // Acumula o tempo de jogo (Relógio)
                elapsedGameTimeSeconds += elapsedTime;

                // Acumula a física (para o Fixed Timestep)
                timeAccumulator += elapsedTime;

                // Loop de Física (Fixed Timestep)
                while (timeAccumulator >= SECONDS_PER_UPDATE) {
                    update(); // Chama a lógica de movimento do jogo
                    timeAccumulator -= SECONDS_PER_UPDATE;
                    updates++;
                }
            }

            // 3. Renderiza (SEMPRE RODA - desenha o jogo ou a tela de pause)
            float interpolation = (float) (timeAccumulator / SECONDS_PER_UPDATE);
            render(interpolation);
            gamePanel.repaint(); // Um único repaint por frame
            frames++;

            // Debug FPS
            if (System.currentTimeMillis() - timer > 1000) {
                System.out.printf("UPS: %d, FPS: %d, Tempo: %.2f s%n", updates, frames, elapsedGameTimeSeconds);
                updates = 0;
                frames = 0;
                timer += 1000;
            }

            // Sleep para aliviar CPU
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getElapsedGameTimeSeconds() {
        return elapsedGameTimeSeconds;
    }
}