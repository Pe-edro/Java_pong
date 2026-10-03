import javax.swing.JFrame;

public class Janela extends JFrame{
    public Janela(){
        setTitle("Jogo Pong");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        Janela campo = new Janela();
        add(campo);

        setVisible(true);
    }

public static void main(String[] args){
        new Janela();
}
}