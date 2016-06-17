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

class Sudoku extends JFrame implements Serializable{
    /** Array com todas as células da grelha principal do jogo */
    ArrayList<Quadradinho> cels = null;
    /** Variável usada para contar quantos números o jogador já acertou */
    public int certos = 0;
    /** Variável que conta quantas dicas o jogador ainda pode usar (do botão de Dicas)*/
    int dicasRestantes = 5;

    JButton bVerificar = new JButton("Verificar resposta"),
            bDica = new JButton("Dica ("+dicasRestantes+")");
    JTextArea tempo = new JTextArea("Time :");

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

//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent we) {
//                dispose();
//            }
//        });
        //setVisible(true);
    }

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

    public JPanel desenhaGrelha(ArrayList<Quadradinho> qe, String[] jogo) {
        JPanel grelha = new JPanel(new GridLayout(9, 9));

        for (int line = 0; line < 9; line++) {
            for (int col = 0; col < 9; col++) {
                qe.add(new Quadradinho(line, col, cels));
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