import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;

public class Sudoku extends JFrame {
    ArrayList<QuadradinhoEventos> cels;
    ArrayList<Quadradinho> solu;
    boolean mostrarJanelaSolucao = false;
    int certos = 0,
        dicasRestantes = 5;

    int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();
    String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 0));
    String[] jogoSolucao = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 1));


    Sudoku() throws IOException {
        setTitle("Sudoku");
        setSize(500,500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cels = new ArrayList<QuadradinhoEventos>();
        add(desenhaGrelha(cels,null),BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        JButton bSolucao = new JButton("Mostrar solução"),
                bVerificar = new JButton("Verificar resposta"),
                bDica = new JButton("Dica ("+dicasRestantes+")");

        JFrame janelaSolucao = new JFrame("Solução");
        janelaSolucao.setSize(500,500);
        janelaSolucao.add(desenhaGrelha(null,new ArrayList<Quadradinho>()));
        janelaSolucao.setVisible(false);
        janelaSolucao.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                janelaSolucao.dispose();
                mostrarJanelaSolucao = false;
                bSolucao.setText("Mostrar Solução");
            }
        });
        bSolucao.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(mostrarJanelaSolucao == false) {
                    janelaSolucao.setVisible(true);
                    mostrarJanelaSolucao = true;
                    bSolucao.setText("Ocultar solução");
                }
                else {
                    janelaSolucao.setVisible(false);
                    mostrarJanelaSolucao = false;
                    bSolucao.setText("Mostrar Solução");
                }
            }
        });

        bVerificar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for(int line = 0; line < 9; line++) {
                    for(int col = 0; col < 9; col++) {
                        if(cels.get(col+line*9).qt.getText().equals(String.valueOf(jogoSolucao[line].charAt(col)))){
                            certos++;
                        }
                        else if(!cels.get(col+line*9).qt.getText().equals(" ")){
                            cels.get(col + line * 9).qt.setBackground(Color.RED);
                        }
                    }
                }
                if(certos == 81) {
                    for(int line = 0; line < 9; line++) {
                        for(int col = 0; col < 9; col++) {
                            cels.get(col+line*9).qt.setBackground(Color.GREEN);
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
                    cels.get(r).qt.setText(String.valueOf(jogoSolucao[r / 9].charAt(r % 9)));
                    cels.get(r).qt.setBackground(Color.WHITE);

                    dicasRestantes = dicasRestantes - 1;
                    bDica.setText("Dica ("+dicasRestantes+")");
                }
                if(dicasRestantes == 0){
                    bDica.setEnabled(false);
                }
            }
        });

        panelInferior.add(bSolucao);
        panelInferior.add(bVerificar);
        panelInferior.add(bDica);

        add(panelInferior,BorderLayout.PAGE_END);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });


        obterJogo.imprimirSolucao(jogoIndex);
    }

    public JPanel desenhaGrelha(ArrayList<QuadradinhoEventos> qe, ArrayList<Quadradinho> q) {
        JPanel w = new JPanel(new GridLayout(9,9));
        if (qe != null ) {
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    qe.add(new QuadradinhoEventos(col+line*9));
                    desenhaRegioes(qe.get(col+line*9), line, col);
                    w.add(qe.get(col+line*9));
                }
            }
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                        qe.get(col+line*9).qt.setText(" ");
                    } else {
                        qe.get(col+line*9).qt.setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                        qe.get(col+line*9).qt.setBackground(Color.YELLOW);
                        qe.get(col+line*9).editavel = false;
                    }
                }
            }
        }
        else if(q != null){
            for(int line = 0; line < 9; line++) {
                for(int col = 0; col < 9; col++) {
                    q.add(new Quadradinho(col+line*9));
                    desenhaRegioes(q.get(col+line*9),line,col);
                    w.add(q.get(col+line*9));
                }
            }
            for(int line = 0; line < 9; line++) {
                for(int col = 0; col < 9; col++) {
                    q.get(col+line*9).qt.setText(String.valueOf(jogoSolucao[line].charAt(col)));
                    q.get(col+line*9).qt.setBackground(Color.YELLOW);
                    q.get(col+line*9).editavel = false;
                }
            }
        }
        return w;
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

    class QuadradinhoEventos extends Quadradinho {
        QuadradinhoEventos(int a) {
            super(a);
            //qt.requestFocus();

            qt.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    //System.out.println(a + "::l" + a/9 + ":c" + a%9); // imprime informacoes do quadradinho clicado
                    if(editavel == true)
                        qt.setBackground(Color.LIGHT_GRAY);
                }
            });
            qt.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    String tecla = KeyEvent.getKeyText(e.getKeyChar());
                    //System.out.println(tecla);
                    if( tecla.equals("1") ||
                        tecla.equals("2") ||
                        tecla.equals("3") ||
                        tecla.equals("4") ||
                        tecla.equals("5") ||
                        tecla.equals("6") ||
                        tecla.equals("7") ||
                        tecla.equals("8") ||
                        tecla.equals("9") ) {
                        if(editavel == true) {
                            qt.setText(tecla);
                            qt.setBackground(Color.WHITE);
                        }
                    }
                    if( tecla.equals("Delete") ||
                        tecla.equals("Backspace") ||
                        tecla.equals("⌫") ) {
                        if(editavel == true) {
                            qt.setText(" ");
                            qt.setBackground(Color.WHITE);
                        }
                    }
                    if(KeyEvent.getKeyText(e.getKeyCode()).equals("R")) {
                        if(editavel == true) {
                            qt.setText(String.valueOf(jogoSolucao[a/9].charAt(a%9)));
                            qt.setBackground(Color.WHITE);
                        }
                    }
                    if(KeyEvent.getKeyText(e.getKeyCode()).equals("H")) {
                        if(editavel == true) {
                            int r = (int) (Math.random() * 80);
                            if(cels.get(r).editavel == true) {
                                cels.get(r).qt.setText(String.valueOf(jogoSolucao[r/9].charAt(r%9)));
                                cels.get(r).qt.setBackground(Color.WHITE);
                            }
                            qt.setBackground(Color.WHITE);
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