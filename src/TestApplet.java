import javax.swing.*;
import java.io.IOException;

public class TestApplet extends JApplet {
    Sudoku novaJanela = null;

    public static void main(String args[]) {
        TestApplet app = new TestApplet();
        app.init();
    }

    public void init() {
        try {
            novaJanela = new Sudoku();
            novaJanela.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}