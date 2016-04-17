/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */
import javax.swing.*;
import java.io.IOException;

/**
 * <h1>Standalone ou Applet</h1>
 * <b>Esta Classe permite correr o jogo como aplicação Standalone ou como Applet</b>
 * <p>
 * Usa como HTML o ficheiro ./res/a.html, que tem o seguinte código:
 * <pre>{@code
<html>
<head>
    <title>SADT</title>
</head>
<body>
    <div align="center">
    <h1>Ola!</h1>
    <object width="400" height="400" code="TestApplet.java"></object>
    </div>
</body>
</html>}</pre>
 */
public class TestApplet extends JApplet {

    public static void main(String args[]) {
        TestApplet app = new TestApplet();
        app.init();
    }

    public void init() {
        try {
            Sudoku novaJanela = new Sudoku();
            novaJanela.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}