public class Placar {
    private int pontosJogador;
    private int pontosComputador;

    public Placar() {
        this.pontosJogador = 0;
        this.pontosComputador = 0;
    }

    public void marcarPontoJogador() {
        this.pontosJogador++;
    }

    public void marcarPontoComputador() {
        this.pontosComputador++;
    }

    public void zerar() {
        this.pontosJogador = 0;
        this.pontosComputador = 0;
    }

    public int getPontosJogador() {
        return pontosJogador;
    }

    public int getPontosComputador() {
        return pontosComputador;
    }
}
