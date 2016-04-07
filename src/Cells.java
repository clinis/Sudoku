import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Cells extends JPanel{

    public int regionro = -1;
    public int i;
    public JTextArea b;

    Cells(int a){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setVisible(true);

        b = new JTextArea(Integer.toString(a));
        b.setEditable(false);
        b.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                regionro = a;
                System.out.println(regionro);
            }
        });
        b.getInputMap().put(KeyStroke.getKeyStroke("1"), "um");
        b.getInputMap().put(KeyStroke.getKeyStroke("2"), "dois");
        b.getInputMap().put(KeyStroke.getKeyStroke("3"), "tres");
        b.getInputMap().put(KeyStroke.getKeyStroke("4"), "quatro");
        b.getInputMap().put(KeyStroke.getKeyStroke("5"), "cinco");
        b.getInputMap().put(KeyStroke.getKeyStroke("6"), "seis");
        b.getInputMap().put(KeyStroke.getKeyStroke("7"), "sete");
        b.getInputMap().put(KeyStroke.getKeyStroke("8"), "oito");
        b.getInputMap().put(KeyStroke.getKeyStroke("9"), "nove");
        b.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("1")){
                    b.setText("1");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("2")){
                    b.setText("2");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("3")){
                    b.setText("3");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("4")){
                    b.setText("4");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("5")){
                    b.setText("5");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("6")){
                    b.setText("6");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("7")){
                    b.setText("7");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("8")){
                    b.setText("8");
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("9")){
                    b.setText("9");
                }
            }
        });
        b.requestFocus();
        add(b);
    }
}