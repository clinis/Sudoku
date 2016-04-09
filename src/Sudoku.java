import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;

public class Sudoku extends JFrame {
    ArrayList<Cells> cels,solu;
    int correcto = 0;
    JPanel main = new JPanel(new GridLayout(9,9));

    int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();
    String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 0));
    String[] stringsol = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 1));


    Sudoku() throws IOException {
        setTitle("Sudoku");
        setSize(500,500);
        setLayout(new BorderLayout());

        cels=new ArrayList<Cells>();

        for(int line=0;line<9;line++) {
            for(int col=0;col<9;col++) {
                cels.add(new Cells(col+line*9));
                createRegion(cels.get(col+line*9),line,col);
                main.add(cels.get(col+line*9));
            }
        }
        for(int line=0;line<9;line++) {
            for(int col=0;col<9;col++) {
                if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                    cels.get(col+line*9).b.setText("   ");
                }
                else {
                    cels.get(col+line*9).b.setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                    cels.get(col+line*9).b.setBackground(Color.YELLOW);
                    cels.get(col+line*9).edita=false;
                }
            }
        }

        JButton solve = new JButton("Show Solution");
        JButton verifica = new JButton("Verify your Solution!");
        JPanel solver = new JPanel();
        solve.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame f = new JFrame("Solution");
                JPanel ma = new JPanel(new GridLayout(9,9));
                f.setSize(500,500);
                solu = new ArrayList<Cells>();
                for(int line=0;line<9;line++) {
                    for(int col=0;col<9;col++) {
                        solu.add(new Cells(col+line*9));
                        createRegion(solu.get(col+line*9),line,col);
                        ma.add(solu.get(col+line*9));
                    }
                }
                for(int line=0;line<9;line++) {
                    for(int col=0;col<9;col++) {
                        solu.get(col+line*9).b.setText(String.valueOf(stringsol[line].charAt(col)));
                        solu.get(col+line*9).b.setBackground(Color.YELLOW);
                    }
                }
                f.add(ma);
                f.setVisible(true);
            }
        });
        verifica.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for(int line=0;line<9;line++) {
                    for(int col=0;col<9;col++) {
                        if(cels.get(col+line*9).b.getText().equals(String.valueOf(stringsol[line].charAt(col)))){
                            correcto++;
                        }
                        else {
                            cels.get(col + line * 9).b.setBackground(Color.RED);
                        }
                    }
                }
                if(correcto==81) {
                    for(int line=0;line<9;line++) {
                        for(int col=0;col<9;col++) {
                            cels.get(col+line*9).b.setBackground(Color.GREEN);
                        }
                    }
                }
                else {
                    correcto = 0;
                }
            }
        });
        solver.add(solve);
        solver.add(verifica);
        add(main,BorderLayout.CENTER);
        add(solver,BorderLayout.SOUTH);
        obterJogo.imprimirSolucao(jogoIndex);
    }

    public void createRegion(Cells cel,int line,int col) {
        if(line==2) {
            cel.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
        }
        if(line==6) {
            cel.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.BLACK));
        }
        if(col==2) {
            if(line==2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
            }
            if(line==6) {
                cel.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.BLACK));
            }
            if(line!=2 && line!=6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
            }
        }
        if(col==6) {
            if(line==2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.BLACK));
            }
            if(line==6) {
                cel.setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.BLACK));
            }
            if(line!=2 && line!=6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK));
            }
        }
    }

    public static void main(String [] args) throws IOException {
        new Sudoku().setVisible(true);
    }
}