import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do jogo Pong.
 * Apresenta o menu inicial de seleção de modo e inicializa a interface gráfica.
 */
public class Main {
    public static void main(String[] args) {
        // Inicialização gráfica segura usando a thread de eventos do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            String[] opcoes = {
                "1 Jogador (Fácil)",
                "1 Jogador (Difícil)",
                "2 Jogadores"
            };

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Bem-vindo ao Pong!\nEscolha o modo de jogo para começar:",
                    "Menu Principal - Pong",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            // Se o usuário fechar a janela sem selecionar, finaliza o programa
            if (escolha == JOptionPane.CLOSED_OPTION) {
                return;
            }

            // 0 -> Fácil (1P), 1 -> Difícil (1P), 2 -> 2 Jogadores (1v1)
            new JanelaJogo(escolha);
        });
    }
}
