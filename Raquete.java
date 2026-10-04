import java.awt.Color;
import java.awt.Graphics;

public class Raquete {
    private int x;
    private int y;
    private int largura;
    private int altura;
    private int velocidade;

    public Raquete(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.velocidade = 10;
    }

    public void desenhar(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, largura, altura);
    }


    public void moverCima() {
        if (y - velocidade >= 0) {
            y -= velocidade;
        }
    }

    public void moverBaixo(int alturaJanela) {
        if (y + altura + velocidade <= alturaJanela) {
            y += velocidade;
        }
    }   

    public void moverIA(int alvoY, int alturaCampo){
        int centroRaquete = y + altura / 2;
        if(centroRaquete < alvoY){
            moverBaixo(alturaCampo);
        } else if(centroRaquete > alvoY){
            moverCima();
        }
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public int getLargura(){return largura;}
    public int getAltura(){return altura;}
}
