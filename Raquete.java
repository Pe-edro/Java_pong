import java.awt.Graphics;
import java.awt.Rectangle;

public class Raquete {
    private int x, y;
    private final int largura, altura;
    private final int velocidade;

    public Raquete(int x, int y, int largura, int altura, int velocidade) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.velocidade = velocidade;
    }

    public void moverSubir() { this.y -= velocidade; }
    public void moverDescer() { this.y += velocidade; }

    public void aplicarLimites(int alturaCampo) {
        if (this.y < 0) this.y = 0;
        if (this.y + this.altura > alturaCampo) this.y = alturaCampo - this.altura;
    }

    public void desenhar(Graphics g) { g.fillRect(x, y, largura, altura); }
    public Rectangle getLimites() { return new Rectangle(x, y, largura, altura); }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
}
