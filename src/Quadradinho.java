import java.awt.*;
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
}