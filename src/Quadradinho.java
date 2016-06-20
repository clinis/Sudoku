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

/**
 * <h1>Classe com as características de cada célula</h1>
 * Cada célula é uma JPanel com um JTextArea não editável pelo utilizador no seu interior.<br>
 * Cada célula tem uma variável que indica se o número na célula pode ser mudado no decorrer
 * do jogo (i.e., se é uma célula para o jogador preencher).<br>
 * Os eventos das células são KeyListeners às teclas pressionadas pelo jogador, nomeadamente, os
 * números de 0 a 9 para colocação na grelha e algumas letras para certas funções.
 */
class Quadradinho extends JPanel {
    /** Varíavel booleana que indica se o número na célula pode ser mudado no decorrer do jogo
     * (i.e., se é uma célula para o utilizador preencher) */
    boolean editavel = true;
    /** JTextArea não editável pelo utilizador e que mostra o número da célula */
    final JTextArea qt;

    /**
     * Construtor das células<br>
     * Define as margens da célula, adiciona um JTextArea, define um estilo para as letras e coloca a célula visivel.
     * Adiciona Eventos ao JTextField qt.<br>
     * <ul>
     *     <li>MouseListener: muda a cor de fundo da célula que o utilizador clicar com o rato para cinzento</li>
     *     <li>KeyListener: teclas pressionadas pelo utilizador
     *     <ul>
     *          <li>1, 2, 3, 4, 5, 6, 7, 8, 9: muda o número da célula para o número pressionado pelo utilizador</li>
     *          <li>Delete ou Backspace: elimina o número da célula</li>
     *     </ul></li>
     * </ul>
     */
    Quadradinho() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        qt = new JTextArea();
        qt.setEditable(false);
        qt.setFont(new Font("Verdana", Font.BOLD, 40));
        add(qt, BorderLayout.CENTER);

        setVisible(true);

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
            }
        });
    }
}