import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Quadradinho extends JPanel {
    public boolean editavel = true;
    public JTextArea b;

    Quadradinho(int a) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setVisible(true);

        b = new JTextArea(Integer.toString(a));
        b.setEditable(false);
        b.setFont(new Font("Verdana", Font.BOLD, 40));

        b.getInputMap().put(KeyStroke.getKeyStroke("1"), "um");
        b.getInputMap().put(KeyStroke.getKeyStroke("2"), "dois");
        b.getInputMap().put(KeyStroke.getKeyStroke("3"), "tres");
        b.getInputMap().put(KeyStroke.getKeyStroke("4"), "quatro");
        b.getInputMap().put(KeyStroke.getKeyStroke("5"), "cinco");
        b.getInputMap().put(KeyStroke.getKeyStroke("6"), "seis");
        b.getInputMap().put(KeyStroke.getKeyStroke("7"), "sete");
        b.getInputMap().put(KeyStroke.getKeyStroke("8"), "oito");
        b.getInputMap().put(KeyStroke.getKeyStroke("9"), "nove");
        //b.getInputMap().put(KeyStroke.getKeyStroke("⌫"), "delete");

        b.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("1")) {
                    if(editavel == true) {
                        b.setText("1");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("2")) {
                    if(editavel == true) {
                        b.setText("2");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("3")) {
                    if(editavel == true) {
                        b.setText("3");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("4")) {
                    if(editavel == true) {
                        b.setText("4");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("5")) {
                    if(editavel == true) {
                        b.setText("5");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("6")) {
                    if(editavel == true) {
                        b.setText("6");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("7")) {
                    if(editavel == true) {
                        b.setText("7");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("8")) {
                    if(editavel == true) {
                        b.setText("8");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("9")) {
                    if(editavel == true) {
                        b.setText("9");
                        b.setBackground(Color.WHITE);
                    }
                }
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("⌫")) {
                    if(editavel == true) {
                        b.setText(" ");
                        b.setBackground(Color.WHITE);
                    }
                }
            }
        });

        b.requestFocus();
        add(b,BorderLayout.CENTER);
    }
}