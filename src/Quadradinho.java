import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Quadradinho extends JPanel {
    public boolean editavel = true;
    public JTextArea qt;

    Quadradinho(int posicao) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        qt = new JTextArea(Integer.toString(posicao));
        qt.setEditable(false);
        qt.setFont(new Font("Verdana", Font.BOLD, 40));

        setVisible(true);
        //qt.requestFocus();
        add(qt,BorderLayout.CENTER);
    }

    Quadradinho(int posicao, String[] jogo, ArrayList<Quadradinho> cels) {
        this(posicao);

        qt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //System.out.println(a + "::l" + a/9 + ":c" + a%9); // imprime informacoes do quadradinho clicado
                if(editavel == true)
                    qt.setBackground(Color.LIGHT_GRAY);
            }
        });
        qt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
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
                        qt.setText(String.valueOf(jogo[posicao/9].charAt(posicao%9)));
                        qt.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("H")) {
                    if(editavel == true) {
                        int r = (int) (Math.random() * 80);
                        if(cels.get(r).editavel == true) {
                            cels.get(r).qt.setText(String.valueOf(jogo[r/9].charAt(r%9)));
                            cels.get(r).qt.setBackground(Color.WHITE);
                        }
                        qt.setBackground(Color.WHITE);
                    }
                }
            }
        });
    }
}