import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Painel principal do jogo Pong onde ocorre o loop de atualização física e renderização.
 * Suporta modo para 1 jogador (contra IA Fácil ou Difícil) e modo para 2 jogadores (1v1 local).
 */
public class CampoJogo extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;

    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    // Constantes dos modos de jogo
    public static final int MODO_1P_FACIL = 0;
    public static final int MODO_1P_DIFICIL = 1;
    public static final int MODO_2P = 2;

    private final Raquete jogador1;
    private final Raquete jogador2;
    private final Bola bola;
    private final Placar placar;
    private final Timer timer;

    // Estados dos botões pressionados para movimentação simultânea e sem atraso (lag de repetição)
    private boolean teclaW, teclaS;
    private boolean teclaCima, teclaBaixo;

    private final boolean modoDoisJogadores;
    private final int toleranciaIA;

    private boolean partidaEncerrada = false;

    public CampoJogo(int modoDeJogo) {
        this.modoDoisJogadores = (modoDeJogo == MODO_2P);

        // Velocidades ajustadas para jogabilidade rápida, ágil e fluida
        int velJogador1 = 9;
        int velJogador2 = 9;
        int velBolaX = 7;
        int velBolaY = 5;

        if (modoDoisJogadores) {
            this.toleranciaIA = 0;
        } else {
            // No modo 1 jogador, jogador2 é a IA
            if (modoDeJogo == MODO_1P_DIFICIL) {
                velJogador2 = 9;
                this.toleranciaIA = 8;
            } else {
                velJogador2 = 6;
                this.toleranciaIA = 18;
            }
        }

        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        placar = new Placar();
        jogador1 = new Raquete(30, (ALTURA - 100) / 2, 15, 100, velJogador1);
        jogador2 = new Raquete(LARGURA - 45, (ALTURA - 100) / 2, 15, 100, velJogador2);
        bola = new Bola((LARGURA - 20) / 2, (ALTURA - 20) / 2, 20, velBolaX, velBolaY);

        // Loop de animação a ~60 FPS (16ms por quadro)
        timer = new Timer(16, e -> atualizarJogo());
        timer.start();
    }

    private void atualizarJogo() {
        if (partidaEncerrada) return;

        // 1. Movimento do Jogador 1 (W / S ou Setas se for 1P)
        boolean moverCimaP1 = teclaW || (!modoDoisJogadores && teclaCima);
        boolean moverBaixoP1 = teclaS || (!modoDoisJogadores && teclaBaixo);

        if (moverCimaP1) jogador1.moverSubir();
        if (moverBaixoP1) jogador1.moverDescer();
        jogador1.aplicarLimites(ALTURA);

        // 2. Movimento do Jogador 2 (Setas Cima/Baixo) ou Computador (IA)
        if (modoDoisJogadores) {
            if (teclaCima) jogador2.moverSubir();
            if (teclaBaixo) jogador2.moverDescer();
        } else {
            // Lógica de Inteligência Artificial para o Computador
            int centroBolaY = bola.getY() + bola.getDiametro() / 2;
            int centroComputadorY = jogador2.getY() + jogador2.getAltura() / 2;

            if (Math.abs(centroBolaY - centroComputadorY) > toleranciaIA) {
                if (centroBolaY < centroComputadorY) {
                    jogador2.moverSubir();
                } else {
                    jogador2.moverDescer();
                }
            }
        }
        jogador2.aplicarLimites(ALTURA);

        // 3. Movimento da Bola e colisão com as bordas superior/inferior
        bola.mover();
        bola.rebaterTetoChao(ALTURA);

        // 4. Tratamento de colisões com as Raquetes + ACELERAÇÃO PROGRESSIVA
        tratarColisoesRaquetes();

        // 5. Verificação de Pontuação
        if (bola.getX() + bola.getDiametro() <= 0) {
            placar.marcarPontoJogador2();
            verificarFimDeJogo();
            if (!partidaEncerrada) bola.reiniciar(LARGURA, ALTURA);
        } else if (bola.getX() >= LARGURA) {
            placar.marcarPontoJogador1();
            verificarFimDeJogo();
            if (!partidaEncerrada) bola.reiniciar(LARGURA, ALTURA);
        }

        repaint();
    }

    private void tratarColisoesRaquetes() {
        // Colisão com Jogador 1 (Raqueta esquerda)
        if (bola.getLimites().intersects(jogador1.getLimites()) && bola.getVelX() < 0) {
            bola.setX(jogador1.getX() + jogador1.getLargura()); // Evita que a bola fique presa na raquete
            bola.inverterVelX();
            ajustarAnguloRebatida(jogador1);
            bola.acelerar();
        }

        // Colisão com Jogador 2 / Computador (Raqueta direita)
        if (bola.getLimites().intersects(jogador2.getLimites()) && bola.getVelX() > 0) {
            bola.setX(jogador2.getX() - bola.getDiametro()); // Evita que a bola fique presa na raquete
            bola.inverterVelX();
            ajustarAnguloRebatida(jogador2);
            bola.acelerar();
        }
    }

    /**
     * Ajusta a inclinação vertical da bola conforme o ponto de contato na raquete,
     * permitindo jogadas rápidas com efeitos angulares.
     */
    private void ajustarAnguloRebatida(Raquete raquete) {
        int centroBolaY = bola.getY() + bola.getDiametro() / 2;
        int centroRaqueteY = raquete.getY() + raquete.getAltura() / 2;
        int distancia = centroBolaY - centroRaqueteY;

        double proporcao = (double) distancia / (raquete.getAltura() / 2.0);
        int novaVelY = (int) Math.round(proporcao * 8);

        if (novaVelY == 0) {
            novaVelY = (bola.getVelY() >= 0) ? 3 : -3;
        }
        bola.setVelY(novaVelY);
    }

    private void verificarFimDeJogo() {
        if (placar.getPontosJogador1() >= 10 || placar.getPontosJogador2() >= 10) {
            partidaEncerrada = true;
            timer.stop();

            String vencedor;
            if (modoDoisJogadores) {
                vencedor = (placar.getPontosJogador1() >= 10) ? "Jogador 1" : "Jogador 2";
            } else {
                vencedor = (placar.getPontosJogador1() >= 10) ? "Jogador" : "Computador";
            }

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
        teclaW = false;
        teclaS = false;
        teclaCima = false;
        teclaBaixo = false;
        partidaEncerrada = false;
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // Ativa antialiasing para gráficos nítidos e fluidos
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Linha central pontilhada
        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < ALTURA; i += 30) {
            g2d.fillRect(LARGURA / 2 - 2, i, 4, 15);
        }

        // Desenho das raquetes e da bola
        g2d.setColor(Color.WHITE);
        jogador1.desenhar(g2d);
        jogador2.desenhar(g2d);
        bola.desenhar(g2d);

        // Legenda dos controles no topo
        g2d.setFont(new Font("Consolas", Font.PLAIN, 14));
        g2d.setColor(Color.GRAY);
        String labelP1 = modoDoisJogadores ? "Jogador 1 (W / S)" : "Jogador (W / S ou ▲ / ▼)";
        String labelP2 = modoDoisJogadores ? "Jogador 2 (▲ / ▼)" : "Computador (IA)";
        g2d.drawString(labelP1, 30, 25);
        int p2Width = g2d.getFontMetrics().stringWidth(labelP2);
        g2d.drawString(labelP2, LARGURA - 30 - p2Width, 25);

        // Exibição do placar
        g2d.setFont(new Font("Consolas", Font.BOLD, 42));
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(placar.getPontosJogador1()), LARGURA / 2 - 70, 55);
        g2d.drawString(String.valueOf(placar.getPontosJogador2()), LARGURA / 2 + 45, 55);

        // Otimização crucial: sincroniza o buffer com a tela no SO, garantindo renderização instantânea sem engasgos
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                teclaW = true;
                break;
            case KeyEvent.VK_S:
                teclaS = true;
                break;
            case KeyEvent.VK_UP:
                teclaCima = true;
                break;
            case KeyEvent.VK_DOWN:
                teclaBaixo = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                teclaW = false;
                break;
            case KeyEvent.VK_S:
                teclaS = false;
                break;
            case KeyEvent.VK_UP:
                teclaCima = false;
                break;
            case KeyEvent.VK_DOWN:
                teclaBaixo = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
