package Main;

import Entity.Player;
import Input.KeyInputs;

import java.awt.*;
import java.io.Serial;

public class GameEngine extends Canvas implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Player player;
    private KeyInputs keyInputs;

    @Serial
    private static final long serialVersionUID = 1L;

    //Controle Main.Game Loop
    private Thread gameThread;
    private volatile boolean running = false;

    //Controle FPS

    public static final int FPS = 60;

    //Intervalo de tempo para cada atualização em segundos.
    public static final float SECONDS_PER_UPDATE = 1.0f / FPS;

    public GameEngine() {

        gamePanel = new GamePanel();
        player = new Player(this, gamePanel);
        keyInputs = new KeyInputs();

        gamePanel.setPlayer(player);
        player.setKeyInputs(keyInputs);

        gamePanel.addKeyListener(keyInputs);
        gamePanel.setFocusable(true);

        gameWindow = new GameWindow(gamePanel);

        gamePanel.requestFocus();
        start();
    }

    protected void updatePlayer() {
        if (player != null) {
            player.update();
        }
    }

    private void update(float secondsPerUpdate) {
    }

    protected void render(float interpolation) {}

    // Inicia o game loop em uma nova thread
    public synchronized void start() {
        if(running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Para o game loop de forma segura
    public synchronized void stop() {
        if(!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Usa o tempo de alta complexidade do sistema (nanosegundos)
        long lastTime = System.nanoTime();
        double timeAccumulator = 0.0;

        //Contadores para debugar
        long timer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while(running) {
            long now = System.nanoTime();
            // Calcula o tempo que passou desde a última verificação em segundos
            double elapsedTime = (now - lastTime) / 1000000000.0;
            lastTime = now;

            //Adiciona o tempo decorrido ao acumulador
            timeAccumulator += elapsedTime;


            // Lógica de atulização usando o Fixed Timestep
            while (timeAccumulator >= SECONDS_PER_UPDATE) {
                update(SECONDS_PER_UPDATE);
                updatePlayer();
                timeAccumulator -= SECONDS_PER_UPDATE;
                updates++; // Para debugar o FPS
            }

            // Lógica de Renderização com Variable Timestep com Interpolação
            final float interpolation = (float) (timeAccumulator / SECONDS_PER_UPDATE);
            render(interpolation);
            gamePanel.repaint();
            frames++;

            // Exibe o FPS e UPS
            if (System.currentTimeMillis() - timer > 1000) {
                System.out.printf("UPS: %d, FPS: %d%n", updates, frames);
                updates = 0;
                frames = 0;
                timer += 1000;
            }

            // Sistema de pausa para máquinas muito fortes de hardware
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
