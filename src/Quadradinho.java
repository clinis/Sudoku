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
 * A cada célula é atribuida um número de 0 a 80 (esquerda para a direita, cima para baixo) conforme
 * a sua posição na grelha e são também definidas as suas margens e o estilo da letra dos números.<br>
 * Cada célula tem uma variável que indica se o número na célula pode ser mudado no decorrer
 * do jogo (i.e., se é uma célula para o utilizador preencher).<br>
 * Esta Classe tem dois construtores:
 * <ul>
 *     <li>Um para células sem eventos (e.g. grelha da janela Solução).</li>
 *     <li>Um para células com eventos (e.g. grelha da janela principal do jogo).</li>
 * </ul>
 * Os eventos das células são KeyListeners às teclas pressionadas pelo jogador, nomeadamente, os
 * números de 0 a 9 para colocação na grelha e algumas letras para certas funções.
 */
class Quadradinho extends JPanel {
    /** Varíavel booleana que indica se o número na célula pode ser mudado no decorrer do jogo
     * (i.e., se é uma célula para o utilizador preencher) */
    public boolean editavel = true;
    /** JTextArea não editável pelo utilizador e que mostra o número da célula */
    public final JTextArea qt;

    /**
     * Construtor para células <b>sem eventos</b><br>
     * <i>e.g. grelha da Janela Solução</i><br>
     * Define as margens da célula, adiciona um JTextArea, define um estilo para as letras e coloca a célula visivel.
     * @param posicao posição da célula na grelha. De 0 a 81 (esquerda para a direita, cima para baixo)
     */
    Quadradinho(int posicao) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        qt = new JTextArea(Integer.toString(posicao));
        qt.setEditable(false);
        qt.setFont(new Font("Verdana", Font.BOLD, 40));
        add(qt, BorderLayout.CENTER);
        //qt.requestFocus();

        setVisible(true);
    }

    /**
     * Construtor para células <b>com eventos</b><br>
     * <i>e.g. grelha da janela de jogo</i><br>
     * Chama o construtor anterior e adiciona Eventos ao JTextField qt.<br>
     * <ul>
     *     <li>MouseListener: muda a cor de fundo da célula que o utilizador clicar com o rato para cinzento</li>
     *     <li>KeyListener: teclas pressionadas pelo utilizador
     *     <ul>
     *          <li>1, 2, 3, 4, 5, 6, 7, 8, 9: muda o número da célula para o número pressionado pelo utilizador</li>
     *          <li>Delete ou Backspace: elimina o número da célulo</li>
     *          <li>R: coloca o número certo na célula selecionada</li>
     *          <li>H: coloca um número certo numa célula aleatória</li>
     *     </ul></li>
     * </ul>
     * @param posicao posição da célula na grelha. De 0 a 81 (esquerda para a direita, cima para baixo)
     * @param jogo grelha em questão. Pode ser a grelha de Puzzle ou de Solução.
     *             Necessário para fazer comparações na funções de R e H.
     * @param cels número colocado pelo utilizador na célula em questão.
     *             Necessário para fazer comparações na funções de R e H.
     */
    Quadradinho(int posicao, String[] jogo, ArrayList<Quadradinho> cels) {
        this(posicao);

        qt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //System.out.println(posicao + "::l" + posicao/9 + ":c" + posicao%9); // imprime informacoes do quadradinho clicado
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
                if (tecla.equals("Delete") ||
                    tecla.equals("Backspace") ||
                    tecla.equals("⌫")) {
                    if (editavel == true) {
                        qt.setText(" ");
                        qt.setBackground(Color.WHITE);
                    }
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("R")) {
                    if (editavel == true) {
                        qt.setText(String.valueOf(jogo[posicao/9].charAt(posicao%9)));
                        qt.setBackground(Color.WHITE);
                    }
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("H")) {
                    if (editavel == true) {
                        int r = (int)(Math.random() * 80);
                        if (cels.get(r).editavel == true) {
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