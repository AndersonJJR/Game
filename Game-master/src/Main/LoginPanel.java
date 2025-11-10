package Main;

import Util.DatabaseConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginPanel extends JPanel {

    private JTextField nameField;
    private JButton startButton;
    private GameWindow gameWindow; // Referência à janela principal

    // Variável para guardar nossa imagem de fundo
    private BufferedImage backgroundImage;

    public LoginPanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        // 1. Carrega a imagem de fundo
        this.backgroundImage = loadBackgroundImage();

        // 2. Define o layout deste painel
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento

        // 3. Rótulo "Digite seu nome:"
        JLabel nameLabel = new JLabel("Digite seu nome:");
        nameLabel.setForeground(Color.BLACK); // Cor do texto (BRANCO para ser visível)
        nameLabel.setFont(new Font("Arial", Font.BOLD, 42));
        // Opcional: adicionar uma sombra ou contorno para legibilidade
        // (mas isso é mais complexo)

        gbc.gridx = 0;
        gbc.gridy = 0; // Posição 0 (centralizado, podemos adicionar mais coisas)
        this.add(nameLabel, gbc);

        // 4. Campo de texto para o nome
        nameField = new JTextField(20); // 20 colunas de largura
        nameField.setPreferredSize(new Dimension(230,32));
        nameField.setFont(new Font("Arial", Font.BOLD,20));
        gbc.gridx = 0;
        gbc.gridy = 1; // Posição Y mudou para 1 (abaixo do rótulo)
        this.add(nameField, gbc);

        // 5. Botão "Iniciar Jogo"
        startButton = new JButton("Iniciar Jogo");
        startButton.setPreferredSize( new Dimension(180,40));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2; // Posição Y mudou para 2 (abaixo do campo)
        this.add(startButton, gbc);

        // 6. Ação do botão (continua igual)
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText();

                if (playerName == null || playerName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Por favor, digite um nome.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int playerID = DatabaseConnection.findOrCreatePlayer(playerName.trim());

                if (playerID != -1) {
                    gameWindow.startGame(playerID);
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Erro ao salvar jogador no banco de dados.", "Erro de DB", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Carrega a imagem de login da pasta /res
     */
    private BufferedImage loadBackgroundImage() {
        try {
            // Mude "login_wallpaper.png" para o nome exato da sua imagem
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/res/login_wallpaper.png"));
            if (img == null) {
                throw new IOException("Imagem não encontrada no caminho /res/login_wallpaper.png");
            }
            return img;

        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de wallpaper:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sobrescrevemos paintComponent para desenhar o wallpaper.
     * Os componentes (botão, etc.) são desenhados DEPOIS disso.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha a imagem de fundo
        if (backgroundImage != null) {
            // Estica a imagem para preencher o painel (1280x720)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            // Se a imagem falhar ao carregar, desenha um fundo preto
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Sobrescrevemos para definir o tamanho preferido
    // Ele já pega os valores atualizados (1280x720) do GamePanel
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GamePanel.LARGURA_TELA, GamePanel.ALTURA_TELA);
    }
}