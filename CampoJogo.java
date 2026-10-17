import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CampoJogo extends JPanel implements KeyListener {
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    private final Raquete jogador;
    private final Raquete computador;
    private final Bola bola;
    private final Placar placar;
    private final Timer timer;

    private boolean teclaW, teclaS;

    private final int nivelDificuldade;
    private final int velComputador;
    private final int toleranciaIA = 12; 

    private boolean partidaEncerrada = false;

    public CampoJogo(int dificuldade) {
        this.nivelDificuldade = dificuldade;
        this.velComputador = (dificuldade == 1) ? 8 : 4; 

        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        placar = new Placar();
        jogador = new Raquete(30, (ALTURA - 100) / 2, 15, 100, 6);
        computador = new Raquete(LARGURA - 45, (ALTURA - 100) / 2, 15, 100, velComputador);
        bola = new Bola((LARGURA - 20) / 2, (ALTURA - 20) / 2, 20, 5, 5);

        timer = new Timer(16, e -> atualizarJogo());
        timer.start();
    }

    private void atualizarJogo() {
        if (partidaEncerrada) return;

        // 1. Movimento do Jogador
        if (teclaW) jogador.moverSubir();
        if (teclaS) jogador.moverDescer();
        jogador.aplicarLimites(ALTURA);

        // 2. Movimento da Inteligência Artificial
        int centroBolaY = bola.getY() + bola.getDiametro() / 2;
        int centroComputadorY = computador.getY() + computador.getAltura() / 2;

        if (Math.abs(centroBolaY - centroComputadorY) > toleranciaIA) {
            if (centroBolaY < centroComputadorY) {
                computador.moverSubir();
            } else {
                computador.moverDescer();
            }
        }
        computador.aplicarLimites(ALTURA);

        // 3. Movimento da Bola e limites verticais
        bola.mover();
        bola.rebaterTetoChao(ALTURA);

        // 4. Tratamento de colisões com as Raquetes + ACELERAÇÃO PROGRESSIVA
        if (bola.getLimites().intersects(jogador.getLimites()) && bola.getVelX() < 0) {
            bola.inverterVelX();
            // Multiplica a velocidade por 1.15 (15% mais rápido a cada batida)
            bola.setVelX((int)(bola.getVelX() * 1.15));
            bola.setVelY((int)(bola.getVelY() * 1.15));
        }
        if (bola.getLimites().intersects(computador.getLimites()) && bola.getVelX() > 0) {
            bola.inverterVelX();
            // Multiplica a velocidade por 1.15 (15% mais rápido a cada batida)
            bola.setVelX((int)(bola.getVelX() * 1.50));
            // bola.setVelY((int)(bola.getVelY() * 2.00));
        }

        // 5. Verificação de Pontos
        if (bola.getX() + bola.getDiametro() <= 0) {
            placar.marcarPontoComputador();
            verificarFimDeJogo();
            if (!partidaEncerrada) bola.reiniciar(LARGURA, ALTURA);
        } else if (bola.getX() >= LARGURA) {
            placar.marcarPontoJogador();
            verificarFimDeJogo();
            if (!partidaEncerrada) bola.reiniciar(LARGURA, ALTURA);
        }

        repaint();
    }

    private void verificarFimDeJogo() {
        if (placar.getPontosJogador() >= 10 || placar.getPontosComputador() >= 10) {
            partidaEncerrada = true;
            timer.stop();
            
            String vencedor = (placar.getPontosJogador() >= 10) ? "Jogador" : "Computador";
            int resposta = JOptionPane.showConfirmDialog(this, 
                "Fim de Jogo! O " + vencedor + " venceu!\nDeseja jogar novamente?", 
                "Partida Encerrada", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                reiniciarPartidaCompleta();
            } else {
                System.exit(0);
            }
        }
    }

    private void reiniciarPartidaCompleta() {
        placar.zerar();
        bola.reiniciar(LARGURA, ALTURA);
        partidaEncerrada = false;
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < ALTURA; i += 30) {
            g.fillRect(LARGURA / 2 - 2, i, 4, 15);
        }

        g.setColor(Color.WHITE);
        jogador.desenhar(g);
        computador.desenhar(g);
        bola.desenhar(g);

        g.setFont(new Font("Consolas", Font.BOLD, 40));
        g.drawString(String.valueOf(placar.getPontosJogador()), LARGURA / 2 - 70, 50);
        g.drawString(String.valueOf(placar.getPontosComputador()), LARGURA / 2 + 40, 50);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) teclaW = true;
        if (e.getKeyCode() == KeyEvent.VK_S) teclaS = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) teclaW = false;
        if (e.getKeyCode() == KeyEvent.VK_S) teclaS = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}

