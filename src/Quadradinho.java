/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

class Quadradinho extends JPanel {
    /** Varíavel booleana que indica se o número na célula pode ser mudado no decorrer do jogo
     * (i.e., se é uma célula para o utilizador preencher) */
    public boolean editavel = true;
    /** JTextArea não editável pelo utilizador e que mostra o número da célula */
    public final JTextArea qt;

    Quadradinho() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        qt = new JTextArea();
        qt.setEditable(false);
        qt.setFont(new Font("Verdana", Font.BOLD, 40));
        add(qt, BorderLayout.CENTER);

        setVisible(true);
    }

    Quadradinho(int line, int col, ArrayList<Quadradinho> cels){
        this();

        qt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //System.out.println("posicao: "+(col+line*9)+" l:"+line+" c:"+col); // imprime informacoes do quadradinho clicado
                if (editavel == true)
                    qt.setBackground(Color.LIGHT_GRAY);
            }
        });
        qt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                String tecla = KeyEvent.getKeyText(e.getKeyChar()); // getKeyChar porque também permite apanhar os números do keyPad
                //System.out.println(tecla);
                if (tecla.equals("1") ||
                    tecla.equals("2") ||
                    tecla.equals("3") ||
                    tecla.equals("4") ||
                    tecla.equals("5") ||
                    tecla.equals("6") ||
                    tecla.equals("7") ||
                    tecla.equals("8") ||
                    tecla.equals("9")) {
                    if (editavel == true) {
                        qt.setText(tecla);
                        qt.setBackground(Color.WHITE);
                    }
                }
                if (tecla.equals("Delete")      ||
                    tecla.equals("Backspace")   ||
                    tecla.equals("⌫")) {
                    if (editavel == true) {
                        qt.setText(" ");
                        qt.setBackground(Color.WHITE);
                    }
                }
//                if (KeyEvent.getKeyText(e.getKeyCode()).equals("R")) {
//                    if (editavel == true) {
//                        qt.setText(String.valueOf(jogo[line].charAt(col)));
//                        qt.setBackground(Color.WHITE);
//                    }
//                }
//                if (KeyEvent.getKeyText(e.getKeyCode()).equals("H")) {
//                    if (editavel == true) {
//                        int r = (int)(Math.random() * 80);
//                        if (cels.get(r).editavel == true) {
//                            cels.get(r).qt.setText(String.valueOf(jogo[r/9].charAt(r%9)));
//                            cels.get(r).qt.setBackground(Color.WHITE);
//                        }
//                        qt.setBackground(Color.WHITE);
//                    }
//                }
            }
        });
    }
}