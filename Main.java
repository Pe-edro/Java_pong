import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicialização gráfica segura usando a thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            String[] opcoes = {"Fácil", "Difícil"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha o nível de dificuldade para começar:",
                    "Menu Principal - Pong",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            // Se o usuário fechar o diálogo sem escolher, adota o padrão Fácil (0)
            if (escolha == JOptionPane.CLOSED_OPTION) {
                escolha = 0;
            }

            new JanelaJogo(escolha);
        });
    }
}
