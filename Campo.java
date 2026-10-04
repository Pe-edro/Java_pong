import javax.swing.JPanel;
import javax.swing.Timer;  
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
 
public class Campo extends JPanel{

    private Raquete raqueteJogador;
    private Raquete raqueteComputador;
    private Timer timer;

    public Campo(){
        setBackground(Color.black);
        raqueteJogador = new Raquete(20, 250, 15, 100);
        raqueteComputador = new Raquete(750, 250, 15, 100);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    raqueteJogador.moverCima();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    raqueteJogador.moverBaixo(getHeight());
                }
                repaint();
            }
        });

        timer = new Timer(10, e -> {
            int centroRaquete = raqueteComputador.getY() + (raqueteComputador.getAltura() / 2);
            raqueteComputador.moverIA(centroRaquete, getHeight());
            repaint();
        });
        timer.start();
    }
    @Override 
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.white);

        int centroX = getWidth() / 2;
        int alturaCampo = getHeight();

        int larguraLinha = 4;
        int alturaTraco = 20;
        int espacoEntreTracos = 15;

        for (int y = 0; y < alturaCampo; y += (alturaTraco + espacoEntreTracos)){
            g.fillRect(centroX - (larguraLinha / 2), y, larguraLinha, alturaTraco);
        }

        raqueteJogador.desenhar(g);
        raqueteComputador.desenhar(g);
    }
}