package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DatabaseConnection {

    // Substitua com seus dados
    private static final String DB_URL = "jdbc:mysql://localhost:3306/game_data"; // Altere "nome_do_seu_banco"
    private static final String DB_USER = "root"; // Altere "seu_usuario"
    private static final String DB_PASS = "root";   // Altere "sua_senha"

    public static Connection getConnection() {
        try {
            // Isso registra o driver, embora em JDBC 4.0+ seja muitas vezes automático
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tenta estabelecer a conexão
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Conexão com o banco de dados estabelecida!");
            return connection;

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL não encontrado! Verifique se o .jar está no projeto.");
            e.printStackTrace();
            return null;
        }
    }

    // Metodo simples para testar a conexão (opcional)
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada com sucesso.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveScore(double tempoCorrido) {
        String sql = "INSERT INTO tbl_pontuacao (id_jogador, tempo_corrido) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("Não foi possível salvar a pontuação: conexão nula.");
                return;
            }

            // 2. Definir os parâmetros
            pstmt.setInt(1, 1); // Define id_jogador como 1 (ou qualquer ID de jogador que você queira)
            pstmt.setDouble(2, tempoCorrido); // Define tempo_corrido

            pstmt.executeUpdate();

            System.out.printf("Pontuação salva no DB! Tempo: %.2f segundos%n", tempoCorrido);

        } catch (SQLException e) {
            System.err.println("Erro ao salvar pontuação:");
            e.printStackTrace();
        }
    }
}