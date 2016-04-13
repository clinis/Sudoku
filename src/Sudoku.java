import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;

public class Sudoku extends JFrame {
    ArrayList<Quadradinho> cels, solu;
    int certos = 0;

    JPanel main = new JPanel(new GridLayout(9,9));

    int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();
    String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 0));
    String[] jogoSolucao = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 1));


    Sudoku() throws IOException {
        setTitle("Sudoku");
        setSize(500,500);
        setLayout(new BorderLayout());

        cels = new ArrayList<Quadradinho>();

        for(int line = 0; line < 9; line++) {
            for(int col = 0; col < 9; col++) {
                cels.add(new Quadradinho(col+line*9));
                desenhaRegioes(cels.get(col+line*9),line,col);
                main.add(cels.get(col+line*9));
            }
        }

        for(int line = 0; line < 9; line++) {
            for(int col = 0; col < 9; col++) {
                if(String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                    cels.get(col+line*9).b.setText("   ");
                }
                else {
                    cels.get(col+line*9).b.setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                    cels.get(col+line*9).b.setBackground(Color.YELLOW);
                    cels.get(col+line*9).editavel = false;
                }
            }
        }

        JPanel panelInferior = new JPanel();
        JButton bSolucao = new JButton("Mostrar solução");
        JButton bVerificar = new JButton("Verificar resposta");

        bSolucao.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame janelaSolucao = new JFrame("Solução");
                janelaSolucao.setSize(500,500);

                JPanel ma = new JPanel(new GridLayout(9,9));
                solu = new ArrayList<Quadradinho>();
                for(int line = 0; line < 9; line++) {
                    for(int col = 0; col < 9; col++) {
                        solu.add(new Quadradinho(col+line*9));
                        desenhaRegioes(solu.get(col+line*9),line,col);
                        ma.add(solu.get(col+line*9));
                    }
                }
                for(int line = 0; line < 9; line++) {
                    for(int col = 0; col < 9; col++) {
                        solu.get(col+line*9).b.setText(String.valueOf(jogoSolucao[line].charAt(col)));
                        solu.get(col+line*9).b.setBackground(Color.YELLOW);
                    }
                }
                janelaSolucao.add(ma);
                janelaSolucao.setVisible(true);
            }
        });

        bVerificar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for(int line = 0; line < 9; line++) {
                    for(int col = 0; col < 9; col++) {
                        if(cels.get(col+line*9).b.getText().equals(String.valueOf(jogoSolucao[line].charAt(col)))){
                            certos++;
                        }
                        else {
                            cels.get(col + line * 9).b.setBackground(Color.RED);
                        }
                    }
                }
                if(certos == 81) {
                    for(int line = 0; line < 9; line++) {
                        for(int col = 0; col < 9; col++) {
                            cels.get(col+line*9).b.setBackground(Color.GREEN);
                        }
                    }
                }
                else {
                    certos = 0;
                }
            }
        });

        panelInferior.add(bSolucao);
        panelInferior.add(bVerificar);

        add(main,BorderLayout.CENTER);
        add(panelInferior,BorderLayout.SOUTH);
        obterJogo.imprimirSolucao(jogoIndex);
    }

    public void desenhaRegioes(Quadradinho cel, int line, int col) {
        if(line == 2) {
            cel.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
        }
        if(line == 6) {
            cel.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.BLACK));
        }
        if(col == 2) {
            if(line == 2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
            }
            if(line == 6) {
                cel.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.BLACK));
            }
            if(line != 2 && line != 6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
            }
        }
        if(col == 6) {
            if(line == 2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.BLACK));
            }
            if(line == 6) {
                cel.setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.BLACK));
            }
            if(line != 2 && line != 6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK));
            }
        }
    }

    public static void main(String [] args) throws IOException {
        new Sudoku().setVisible(true);
    }
}