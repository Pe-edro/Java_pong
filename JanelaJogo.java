import javax.swing.JFrame;

/**
 * Janela principal da aplicação Pong.
 * Configura o contêiner JFrame e inicializa o painel CampoJogo com o modo selecionado.
 */
public class JanelaJogo extends JFrame {
    private static final long serialVersionUID = 1L;

    public JanelaJogo(int modoDeJogo) {
        String subtitulo = (modoDeJogo == CampoJogo.MODO_2P) ? " (2 Jogadores)" : " (1 Jogador)";
        setTitle("Pong em Java - POO" + subtitulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        CampoJogo campo = new CampoJogo(modoDeJogo);
        add(campo);

        pack(); // Ajusta a janela ao tamanho preferido do painel
        setLocationRelativeTo(null); // Centraliza a janela na tela do usuário
        setVisible(true);
    }
}
