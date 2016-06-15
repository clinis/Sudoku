/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * <h1>Classe principal</h1>
 * <b>Esta é a Classe principal.</b>
 * <p>
 * Nesta Classe é criada a janela principal do jogo, assim como todos os outros elementos necessários.
 * É criada a grelha principal do jogo, três botões (Mostrar Solução, Verificar Solução e Dica)
 * e uma janela com a Solução do jogo.
 */
class Sudoku extends JFrame {
    /** Array com todas as células da grelha principal do jogo */
    private final ArrayList<Quadradinho> cels;
    /** Variável booleana que indica se a janela de Solução está a ser mostrada */
    private boolean mostrarJanelaSolucao = false;
    /** Variável usada para contar quantos números o jogador já acertou */
    private int certos = 0;
    /** Variável que conta quantas dicas o jogador já usou (do botão de Dicas)*/
    private int dicasRestantes = 5;

    private final obterJogo obter = new obterJogo();
    /** Ficheiro .csv com vários jogos e as suas informações (Puzzle, Solução, etc.) */
    private final InputStream resourceFile = getClass().getResourceAsStream("/" + "btest.csv");
    /** Ler o ficheiro, ler uma linha aleatória e guardar as informações desse jogo numa String */
    private String jogo = obter.readLine(resourceFile, obter.escolherJogoIndexAleatoriamente());
    /** Puzzle do jogo escolhido */
    private final String[] jogoPuzzle = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(0, jogo));
    /** Solução do jogo escolhido */
    private final String[] jogoSolucao = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(1, jogo));

    /**
     * Construtor que cria a janela principal e os elementos do jogo.<br>
     * Define o título da janela principal, o seu tamanho e posição inicial.<br>
     * Cria a janela com a Solução do jogo e define a sua visibilidade como oculta.<br>
     * Cria os botões de Mostrar Solução, Verificar Solução e Dica e adiciona-os a um JPanel que é colocado no fundo da janela principal.
     * Os eventos desses botões são mouseListener para mouseClicked:
     * <ul>
     *     <li> bSolução: troca a visibilidade da janela de Solução e altera o texto do botão de acordo</li>
     *     <li> bVerificar: percorre cada uma das células e compara com a mesma posição na Solução.
     *                      Se o número estiver errado, coloca o fundo dessa célula a vermelho.
     *                      Se todas as células estiverem certas, coloca todos os fundos a verde e motra um diálogo de Parabéns</li>
     *     <li> bDica: gera um número aleatório entre 0 e 80, se a célula nessa posição for editável, preenche-a com o número certo</li>
     * </ul>
     * Imprime a solução do jogo na consola.
     * @throws IOException
     */
    Sudoku() throws IOException {
        setTitle("Sudoku");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cels = new ArrayList<Quadradinho>();
        add(desenhaGrelha(cels, null), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        JButton bSolucao = new JButton("Mostrar solução"),
                bVerificar = new JButton("Verificar resposta"),
                bDica = new JButton("Dica ("+dicasRestantes+")");

        panelInferior.add(bSolucao);
        panelInferior.add(bVerificar);
        panelInferior.add(bDica);

        add(panelInferior, BorderLayout.PAGE_END);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        String hostName = "localhost";
        int portNumber = 1331;

        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ( (fromServer = in.readLine()) != null ) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        }

        obter.imprimirSolucao(jogo);
    }

    /**
     * Método que desenha as grelhas 9x9 do jogo de Sudoku.<br>
     * Cria um JPanel com GridLayout 9x9 e coloca as células em cada lugar.
     * Conforme os parâmetros que recebe:
     * <ul>
     *     <li>Se receber uma Array com células com eventos, coloca o texo conforme o Puzzle do jogo</li>
     *     <li>Se receber uma Array com células sem eventos, coloca o texto conforme a Solução do jogo</li>
     * </ul>
     * @param qe Array com células com eventos
     * @param q Array com células sem eventos
     * @return JPanel com as células que depois pode ser colocado na janela Container pretendido
     */
    private JPanel desenhaGrelha(ArrayList<Quadradinho> qe, ArrayList<Quadradinho> q) {
        JPanel grelha = new JPanel(new GridLayout(9, 9));
        if (qe != null) {
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    qe.add(new Quadradinho(line, col, jogoSolucao, cels));
                    desenhaRegioes(qe.get(col+line*9), line, col);
                    grelha.add(qe.get(col+line*9));
                }
            }
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                        qe.get(col+line*9).qt.setText(" ");
                    } else {
                        qe.get(col+line*9).qt.setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                        qe.get(col+line*9).qt.setBackground(Color.YELLOW);
                        qe.get(col+line*9).editavel = false;
                    }
                }
            }
        } else if (q != null) {
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    q.add(new Quadradinho());
                    desenhaRegioes(q.get(col+line*9), line, col);
                    grelha.add(q.get(col+line*9));
                }
            }
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    q.get(col+line*9).qt.setText(String.valueOf(jogoSolucao[line].charAt(col)));
                    q.get(col+line*9).qt.setBackground(Color.YELLOW);
                    q.get(col+line*9).editavel = false;
                }
            }
        }
        return grelha;
    }

    /**
     * Método que desenha as linhas grossas que separam as regiões.<br>
     * Nas linhas 2 e 6 coloca uma linha grossa horizontal.<br>
     * Nas colunas 2 e 6 coloca uma linha grossa vertical.
     * @param cel
     * @param line
     * @param col
     */
    private void desenhaRegioes(Quadradinho cel, int line, int col) {
        if (line == 2) {
            cel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 1, Color.BLACK));
        }
        if (line == 6) {
            cel.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.BLACK));
        }
        if (col == 2) {
            if (line == 2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.BLACK));
            }
            if (line == 6) {
                cel.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 4, Color.BLACK));
            }
            if (line != 2 && line != 6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 4, Color.BLACK));
            }
        }
        if (col == 6) {
            if (line == 2) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 4, 4, 1, Color.BLACK));
            }
            if (line == 6) {
                cel.setBorder(BorderFactory.createMatteBorder(4, 4, 1, 1, Color.BLACK));
            }
            if (line != 2 && line != 6) {
                cel.setBorder(BorderFactory.createMatteBorder(1, 4, 1, 1, Color.BLACK));
            }
        }
    }
}