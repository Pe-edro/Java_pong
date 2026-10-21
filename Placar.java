/**
 * Responsável pelo controle e encapsulamento dos pontos da partida.
 * Suporta tanto partidas contra o computador quanto partidas de 2 jogadores.
 */
public class Placar {
    private int pontosJogador1;
    private int pontosJogador2;

    public Placar() {
        this.pontosJogador1 = 0;
        this.pontosJogador2 = 0;
    }

    public void marcarPontoJogador1() {
        this.pontosJogador1++;
    }

    public void marcarPontoJogador2() {
        this.pontosJogador2++;
    }

    // Métodos legados mantidos para compatibilidade com versões anteriores
    public void marcarPontoJogador() {
        marcarPontoJogador1();
    }

    public void marcarPontoComputador() {
        marcarPontoJogador2();
    }

    public void zerar() {
        this.pontosJogador1 = 0;
        this.pontosJogador2 = 0;
    }

    public int getPontosJogador1() {
        return pontosJogador1;
    }

    public int getPontosJogador2() {
        return pontosJogador2;
    }

    public int getPontosJogador() {
        return pontosJogador1;
    }

    public int getPontosComputador() {
        return pontosJogador2;
    }
}
