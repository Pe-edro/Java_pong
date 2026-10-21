import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Representa a bola do jogo Pong.
 * Gerencia posicionamento, velocidade, limites de tela, aceleração e detecção de colisões.
 */
public class Bola {
    private int x, y;
    private final int diametro;
    private int velX, velY;
    private final int velInicialX, velInicialY;

    // Objeto Rectangle reutilizável para otimizar desempenho evitando alocações excessivas na memória
    private final Rectangle limites = new Rectangle();

    public Bola(int x, int y, int diametro, int velX, int velY) {
        this.x = x;
        this.y = y;
        this.diametro = diametro;
        this.velX = velX;
        this.velY = velY;
        this.velInicialX = velX;
        this.velInicialY = velY;
    }

    public void mover() {
        this.x += velX;
        this.y += velY;
    }

    public void rebaterTetoChao(int alturaCampo) {
        if (this.y < 0) {
            this.y = 0;
            this.velY = -this.velY;
        } else if (this.y + this.diametro > alturaCampo) {
            this.y = alturaCampo - this.diametro;
            this.velY = -this.velY;
        }
    }

    public void inverterVelX() {
        this.velX = -this.velX;
    }

    /**
     * Acelera a velocidade da bola a cada rebatida nas raquetes.
     * Garante incremento efetivo (mínimo de 1 unidade) respeitando o limite máximo para manter a jogabilidade fluida.
     */
    public void acelerar() {
        int limiteMaxX = 16;
        int limiteMaxY = 12;

        int sinalX = (velX >= 0) ? 1 : -1;
        int sinalY = (velY >= 0) ? 1 : -1;

        int absX = Math.abs(velX);
        int absY = Math.abs(velY);

        // Aumenta a velocidade horizontal em pelo menos 1 pixel ou 10%
        int novoAbsX = Math.min(limiteMaxX, Math.max(absX + 1, (int) Math.round(absX * 1.10)));
        // Aumenta ligeiramente a velocidade vertical se necessário
        int novoAbsY = Math.min(limiteMaxY, Math.max(absY, (int) Math.round(absY * 1.05)));

        this.velX = novoAbsX * sinalX;
        this.velY = novoAbsY * sinalY;
    }

    public void reiniciar(int larguraCampo, int alturaCampo) {
        this.x = (larguraCampo - diametro) / 2;
        this.y = (alturaCampo - diametro) / 2;
        // Inverte a direção de saída horizontal para o saque alternar
        this.velX = (velX > 0) ? -velInicialX : velInicialX;
        // Define uma saída vertical dinâmica para o saque
        this.velY = (Math.random() < 0.5 ? -1 : 1) * velInicialY;
    }

    public void desenhar(Graphics g) {
        g.fillOval(x, y, diametro, diametro);
    }

    public Rectangle getLimites() {
        limites.setBounds(x, y, diametro, diametro);
        return limites;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getDiametro() { return diametro; }
    public int getVelX() { return velX; }
    public void setVelX(int velX) { this.velX = velX; }

    public int getVelY() { return velY; }
    public void setVelY(int velY) { this.velY = velY; }
}
