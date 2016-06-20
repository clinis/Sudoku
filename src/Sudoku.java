/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.*;

/**
 * <h1>Classe para contruir a board do jogo</h1>
 * Cada célula é uma JPanel com um JTextArea não editável pelo utilizador no seu interior.<br>
 * Cada célula tem uma variável que indica se o número na célula pode ser mudado no decorrer
 * do jogo (i.e., se é uma célula para o jogador preencher).<br>
 * Nesta Classe definem-se o título e o tamanho da janela de jogo.<br>
 * Esta Classe tem dois contrutores:
 * <ul>
 *     <li>Um para construir a board do jogo (recebe só o Puzzle do jogo).</li>
 *     <li>Um para construir a board de solução do cliente (recebe o Puzzle do jogo e o número de cliente). Este construtor só é utilizado no Servidor</li>
 * </ul>
 * Os eventos das células são KeyListeners às teclas pressionadas pelo jogador, nomeadamente, os
 * números de 0 a 9 para colocação na grelha e algumas letras para certas funções.
 */
class Sudoku extends JFrame implements Serializable{
    /** número de dicas inicial */
    private final int dicasIniciais = Servidor.dicasIniciais;
    /** Array com todas as células da grelha principal do jogo */
    ArrayList<Quadradinho> cels = null;
    /** Variável usada para contar quantos números o jogador já acertou */
    int certos = 0;
    /** Variável que conta quantas dicas o jogador ainda pode usar (do botão de Dicas)*/
    int dicasRestantes = dicasIniciais;

    JButton bVerificar = new JButton("Verificar resposta"),
            bDica = new JButton("Dica ("+dicasRestantes+")");
    JTextArea tempo = new JTextArea("Tempo");

    /**
     * Construtor para board do jogador<br>
     * @param jogoPuzzle jogo
     * @throws IOException
     */
    Sudoku(String[] jogoPuzzle) throws IOException {
        setTitle("Sudoku");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cels = new ArrayList<Quadradinho>();

        add(desenhaGrelha(cels, jogoPuzzle), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        tempo.setEditable(false);
        panelInferior.add(tempo);
        panelInferior.add(bVerificar);
        panelInferior.add(bDica);
        add(panelInferior, BorderLayout.PAGE_END);
    }

    /**
     * Construtor para board da solução do cliente<br>
     * <b>Usado só no lado do Servidor</b>
     * @param jogoSolucao jogo
     * @param cliente numero do cliente
     */
    Sudoku(String[] jogoSolucao, int cliente) {
        JFrame janelaSolucao = new JFrame("Solução do Cliente "+cliente);
        janelaSolucao.setSize(450, 450);
        janelaSolucao.add(desenhaGrelha(new ArrayList<Quadradinho>(),jogoSolucao));
        janelaSolucao.setVisible(true);
        janelaSolucao.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                janelaSolucao.dispose();
            }
        });
    }

    /**
     * Método que desenha as grelhas 9x9 do jogo de Sudoku.<br>
     * Cria um JPanel com GridLayout 9x9 e coloca as células em cada lugar.<br>
     * Recebe uma Array com células e coloca o texo conforme o Puzzle ou a Solução do jogo
     * @return JPanel com as células que depois pode ser colocado na janela Container pretendido
     */
    private JPanel desenhaGrelha(ArrayList<Quadradinho> qe, String[] jogo) {
        JPanel grelha = new JPanel(new GridLayout(9, 9));

        for (int line = 0; line < 9; line++) {
            for (int col = 0; col < 9; col++) {
                qe.add(new Quadradinho());
                desenhaRegioes(qe.get(col+line*9), line, col);
                grelha.add(qe.get(col+line*9));

                if (String.valueOf(jogo[line].charAt(col)).equals(".")) {
                    qe.get(col+line*9).qt.setText(" ");
                } else {
                    qe.get(col+line*9).qt.setText(String.valueOf(jogo[line].charAt(col)));
                    qe.get(col+line*9).qt.setBackground(Color.YELLOW);
                    qe.get(col+line*9).editavel = false;
                }
            }
        }
        return grelha;
    }

    /**
     * Método que desenha as linhas grossas que separam as regiões.<br>
     * Nas linhas 2 e 6 coloca uma linha grossa horizontal.<br>
     * Nas colunas 2 e 6 coloca uma linha grossa vertical.
     * @param cel
     * @param line
     * @param col
     */
    private void desenhaRegioes(Quadradinho cel, int line, int col) {
        if (line == 2) {
            cel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 1, Color.BLACK));
        }
        if (line == 6) {
            cel.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.BLACK));
        }
        if (col == 2) {
            if (line == 2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.BLACK));
            }
            if (line == 6) {
                cel.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 4, Color.BLACK));
            }
            if (line != 2 && line != 6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 4, Color.BLACK));
            }
        }
        if (col == 6) {
            if (line == 2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 4, 4, 1, Color.BLACK));
            }
            if (line == 6) {
                cel.setBorder(BorderFactory.createMatteBorder(4, 4, 1, 1, Color.BLACK));
            }
            if (line != 2 && line != 6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 4, 1, 1, Color.BLACK));
            }
        }
    }
}