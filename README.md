# Pong em Java - Programação Orientada a Objetos

Projeto desenvolvido para a disciplina de **Técnicas de Programação**, sob orientação do **Prof. Renato Pierre**.

Este repositório contém uma implementação do clássico jogo **Pong**, desenvolvida em Java utilizando a biblioteca gráfica `Swing`. O objetivo do projeto é consolidar na prática os conceitos fundamentais da **Programação Orientada a Objetos (POO)**.

---

## Conceitos de POO Aplicados

### O que são Classes e Objetos?

* **Classe:** Funciona como um **molde ou projeto** que define as características (atributos) e os comportamentos (métodos) que determinado elemento do programa terá.
  * *Exemplo no projeto:* A classe `Raquete` define que qualquer raquete terá largura, altura, posição $(x, y)$, velocidade e capacidade de se mover.
* **Objeto:** É a **instanciação (criação concreta)** baseada em uma classe.
  * *Exemplo no projeto:* Com base na classe `Raquete`, criamos dois objetos distintos na memória: `jogador1` (raquete da esquerda) e `jogador2` (raquete da direita, controlada por outro jogador ou pela IA).

---

## Estrutura do Código e Componentes Principais

O projeto é dividido em classes com responsabilidades bem definidas:

### 1. `Main.java`
Ponto de entrada da aplicação.
* Exibe a caixa de diálogo inicial (`JOptionPane`) para o jogador escolher o modo de jogo:
  * **1 Jogador (Fácil)**
  * **1 Jogador (Difícil)**
  * **2 Jogadores (1v1 Local)**
* Utiliza `SwingUtilities.invokeLater` para garantir a inicialização segura da interface gráfica dentro da thread do Swing (EDT).

### 2. `JanelaJogo.java` (extende `JFrame`)
Gerencia a janela principal da aplicação.
* Configura o título dinâmico com o modo atual, fechamento da aplicação, tamanho fixo e centralização na tela.
* Instancia e adiciona o painel do campo de jogo (`CampoJogo`).

### 3. `CampoJogo.java` (extende `JPanel`, implementa `KeyListener`)
O "coração" do jogo, responsável pela lógica do loop e renderização.
* **Loop de Animação a 60 FPS:** Utiliza um `javax.swing.Timer` executado a cada 16ms para atualizar o estado da física e redesenhar a tela (`repaint()`).
* **Suporte a 2 Jogadores:** No modo 2P, gerencia comandos simultâneos de ambos os jogadores sem conflito ou bloqueio de teclas.
* **Inteligência Artificial (IA):** No modo 1 Jogador, controla a raquete direita com velocidade e tolerância ajustadas conforme o nível selecionado.
* **Física e Otimizações de Desempenho:**
  * **Aceleração progressiva:** A bola ganha velocidade a cada rebatida nas raquetes.
  * **Efeito angular:** O ângulo de rebatida varia de acordo com o ponto de contato da raquete (centro ou extremidades).
  * **Anti-sticking:** Reposicionamento automático da bola na borda da raquete para evitar que a bola fique presa ou atravesse.
  * **Sincronização Gráfica:** Uso de `Toolkit.getDefaultToolkit().sync()` para eliminar stuttering e garantir taxa de quadros e resposta em tempo real.
  * **Zero GC Overhead:** Reutilização de objetos `Rectangle` nos testes de colisão para evitar criação desnecessária de lixo na memória.
* **Controle de Placar e Reinício:** Detecta pontuação, atualiza o placar e anuncia o vencedor quando alguém atinge 10 pontos.

### 4. `Bola.java`
Modela a esfera do jogo.
* Armazena coordenadas, diâmetro e vetores de velocidade (`velX`, `velY`).
* **Funções-chave:** `mover()`, `rebaterTetoChao()`, `inverterVelX()`, `acelerar()` (incremento progressivo e dinâmico de velocidade), `reiniciar()` (reposiciona a bola no centro com saque alternado) e `getLimites()` (retorna a caixa de colisão).

### 5. `Raquete.java`
Modela as raquetes dos jogadores.
* Controla a movimentação vertical e restringe os limites para que a raquete não saia da tela (`aplicarLimites()`).
* Velocidade de deslocamento ágil e balanceada para respostas instantâneas.
* Fornece o método `getLimites()`, retornando uma caixa de colisão (`Rectangle`) otimizada.

### 6. `Placar.java`
Encapsula o estado da pontuação.
* Armazena os pontos de ambos os competidores de forma encapsulada (`pontosJogador1`, `pontosJogador2`), oferecendo métodos seguros de incremento e reinicialização (`zerar()`).

---

## Como Executar o Projeto

1. Certifique-se de ter o **JDK (Java Development Kit)** instalado (versão 8 ou superior).
2. Acesse o diretório do projeto e compile os arquivos:
   ```bash
   javac *.java
   ```
3. Execute a classe principal:
   ```bash
   java Main
   ```

---

## Controles

### Modo 1 Jogador (vs Computador)
* **W** ou **Seta para Cima (▲):** Move a raquete para cima.
* **S** ou **Seta para Baixo (▼):** Move a raquete para baixo.

### Modo 2 Jogadores (1 vs 1 Local)
* **Jogador 1 (Esquerda):**
  * **W:** Move para cima.
  * **S:** Move para baixo.
* **Jogador 2 (Direita):**
  * **Seta para Cima (▲):** Move para cima.
  * **Seta para Baixo (▼):** Move para baixo.
