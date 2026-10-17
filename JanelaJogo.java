import javax.swing.JFrame;

public class JanelaJogo extends JFrame {
    public JanelaJogo(int dificuldade) {
        setTitle("Pong em Java - POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        CampoJogo campo = new CampoJogo(dificuldade);
        add(campo);

        pack(); // Ajusta a janela ao tamanho do painel interno
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setVisible(true);
    }
}
