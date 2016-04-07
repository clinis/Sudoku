package x;

import java.applet.*;
import java.io.IOException;

public class TestApplet extends Applet {
    GUIi novaJanela = null;

    public static void main(String args[]) {
        TestApplet app = new TestApplet();
        app.init();
    }

    public void init() {
        try {
            novaJanela = new GUIi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}