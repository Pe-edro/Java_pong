import java.awt.Graphics;
import java.awt.Rectangle;

public class Bola {
    private int x, y;
    private final int diametro;
    private int velX, velY;
    private final int velInicialX, velInicialY;

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

public void acelerar() {
        int limiteX = velInicialX * 3;
        int limiteY = velInicialY * 3;

        // Calcula a nova velocidade acrescida (+15%)
        int novaVelX = (int)(this.velX * 1.15);
        int novaVelY = (int)(this.velY * 1.15);

        // Aplica o limitador respeitando a direção (sinal positivo/negativo)
        if (Math.abs(novaVelX) <= limiteX) {
            this.velX = novaVelX;
        } else {
            this.velX = (this.velX > 0) ? limiteX : -limiteX;
        }

        if (Math.abs(novaVelY) <= limiteY) {
            this.velY = novaVelY;
        } else {
            this.velY = (this.velY > 0) ? limiteY : -limiteY;
        }
    }

    public void reiniciar(int larguraCampo, int alturaCampo) {
        this.x = (larguraCampo - diametro) / 2;
        this.y = (alturaCampo - diametro) / 2;
        // Restaura a velocidade original para o novo saque
        this.velX = (velX > 0) ? -velInicialX : velInicialX;
        this.velY = velInicialY;
    }

    public void desenhar(Graphics g) {
        g.fillOval(x, y, diametro, diametro);
    }

    public Rectangle getLimites() {
        return new Rectangle(x, y, diametro, diametro);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDiametro() { return diametro; }
    public int getVelX() { return velX; }
    public int getVelY() { return velY; }

    // Métodos novos para permitir a aceleração
    public void setVelX(int velX) { this.velX = velX; }
    public void setVelY(int velY) { this.velY = velY; }
}
