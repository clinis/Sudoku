import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;

public class Sudoku extends JFrame {
    ArrayList<QuadradinhoAvancado> cels, solu;
    int certos = 0,
        dicasRestantes = 5;
    JPanel main = new JPanel(new GridLayout(9,9));

    int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();
    String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 0));
    String[] jogoSolucao = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 1));


    Sudoku() throws IOException {
        setTitle("Sudoku");
        setSize(500,500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cels = new ArrayList<QuadradinhoAvancado>();

        for(int line = 0; line < 9; line++) {
            for(int col = 0; col < 9; col++) {
                cels.add(new QuadradinhoAvancado(col+line*9));
                desenhaRegioes(cels.get(col+line*9),line,col);
                main.add(cels.get(col+line*9));
            }
        }

        for(int line = 0; line < 9; line++) {
            for(int col = 0; col < 9; col++) {
                if(String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                    cels.get(col+line*9).b.setText(" ");
                }
                else {
                    cels.get(col+line*9).b.setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                    cels.get(col+line*9).b.setBackground(Color.YELLOW);
                    cels.get(col+line*9).editavel = false;
                }
            }
        }

        JPanel panelInferior = new JPanel();
        JButton bSolucao = new JButton("Mostrar solução"),
                bVerificar = new JButton("Verificar resposta"),
                bDica = new JButton("Dica");

        JLabel points = new JLabel();
        points.setText("Dicas restantes: " + String.valueOf(dicasRestantes));

        bSolucao.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame janelaSolucao = new JFrame("Solução");
                janelaSolucao.setSize(500,500);

                JPanel ma = new JPanel(new GridLayout(9,9));
                solu = new ArrayList<QuadradinhoAvancado>();
                for(int line = 0; line < 9; line++) {
                    for(int col = 0; col < 9; col++) {
                        solu.add(new QuadradinhoAvancado(col+line*9));
                        desenhaRegioes(solu.get(col+line*9),line,col);
                        ma.add(solu.get(col+line*9));
                    }
                }
                for(int line = 0; line < 9; line++) {
                    for(int col = 0; col < 9; col++) {
                        solu.get(col+line*9).b.setText(String.valueOf(jogoSolucao[line].charAt(col)));
                        solu.get(col+line*9).b.setBackground(Color.YELLOW);
                        solu.get(col+line*9).editavel = false;
                    }
                }
                addWindowListener(new EscutaJanela());
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
                        else if(!cels.get(col+line*9).b.getText().equals(" ")){
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
                } else {
                    certos = 0;
                }
            }
        });

        bDica.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = (int) (Math.random() * 80);

                if(cels.get(r).editavel == true && dicasRestantes > 0) {
                    cels.get(r).b.setText(String.valueOf(jogoSolucao[r / 9].charAt(r % 9)));
                    cels.get(r).b.setBackground(Color.WHITE);

                    dicasRestantes = dicasRestantes -1;
                    points.setText("Dicas restantes: " + String.valueOf(dicasRestantes));
                }
                if(dicasRestantes == 0){
                    points.setForeground(Color.red);
                    bDica.setEnabled(false);
                }
            }
        });

        panelInferior.add(bSolucao);
        panelInferior.add(bVerificar);
        panelInferior.add(bDica);
        panelInferior.add(points);

        add(main,BorderLayout.CENTER);
        add(panelInferior,BorderLayout.SOUTH);

        addWindowListener(new EscutaJanela());

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

    class EscutaJanela extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            //setVisible(false);
            dispose();
        }
    }

    class QuadradinhoAvancado extends Quadradinho {
        QuadradinhoAvancado(int a) {
            super(a);

            b.getInputMap().put(KeyStroke.getKeyStroke("R"), "r");
            b.getInputMap().put(KeyStroke.getKeyStroke("H"), "h");
            b.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if(KeyEvent.getKeyText(e.getKeyCode()).equals("R")) {
                        if(editavel == true) {
                            b.setText(String.valueOf(jogoSolucao[a/9].charAt(a%9)));
                            b.setBackground(Color.WHITE);
                        }
                    }
                    if(KeyEvent.getKeyText(e.getKeyCode()).equals("H")) {
                        if(editavel == true) {
                            int r = (int) (Math.random() * 80);
                            if(cels.get(r).editavel == true)
                                cels.get(r).b.setText(String.valueOf(jogoSolucao[r/9].charAt(r%9)));
                            b.setBackground(Color.WHITE);
                        }
                    }
                }
            });
        }
    }

    public static void main(String [] args) throws IOException {
        new Sudoku().setVisible(true);
    }
}