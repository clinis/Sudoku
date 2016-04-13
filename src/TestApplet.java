import java.applet.*;
import java.io.IOException;

public class TestApplet extends Applet {
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