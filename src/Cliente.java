/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */
import javax.swing.*;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <h1>Classe principal do cliente</h1>
 * <b>Esta é a Classe principal do cliente.</b>
 * <p>
 * Esta Classe permite correr o jogo como aplicação Standalone ou como Applet.
 * <p>
 * Nesta Classe é criada a janela principal do jogo do cliente, assim como todos os outros elementos necessários.<br>
 * É criada a grelha principal do jogo, um menu, dois botões (Verificar Solução e Dica) e um indicador (tempo).
 */
public class Cliente extends JApplet {
    /** Classe Protocolo para troca de mensagens entre Cliente e Servidor */
    private Protocolo controlo = new Protocolo();
    /** Stream de entrada para comunicação com o Servidor */
    private ObjectInputStream in;
    /** Stream de saída para comunicação com o Servidor */
    private ObjectOutputStream out;
    /** socket de ligação com o Servidor */
    private Socket ligacao;

    /** janela de jogo do cliente */
    private Sudoku janelaCliente;
    /** thread para contador de tempo do lado do Cliente */
    private Thread tempocliente = new Thread();
    /** número do Cliente */
    private int soucliente = 0;
    /** String com nome do jogador */
    private String sounome = null;
    /** Puzzle do jogo escolhido */
    private String[] jogoPuzzle;
    /** número de dicas inicial */
    private final int dicasIniciais = Servidor.dicasIniciais;

    /**
     * Main que corre como Standalone ou Applet
     * @param args endereço e porta do servidor (não implementado)
     */
    public static void main(String args[]) {
        Cliente app = new Cliente();
        app.init();
    }

    /**
     *
     */
    public void init() {
        /** Ligação ao Servidor na porta 5000 */
        try{
            ligacao = new Socket("localhost", 5000);
            in = new ObjectInputStream(ligacao.getInputStream());
            out = new ObjectOutputStream(ligacao.getOutputStream());
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }

        /** Input Dialog inicial que pergunta ao jogador qual o seu nome */
        sounome = JOptionPane.showInputDialog(this,
                                             "Insira o seu nome para gravar a sua pontuação.\nCaso contrário, ficará registado como \"convidado\".",
                                             "Insira o seu nome",
                                             JOptionPane.QUESTION_MESSAGE);
        if(sounome != null)
            sounome = sounome.trim();
        /** envia ao Servidor o nome do jogador */
         try{
            controlo = new Protocolo();
            controlo.arg1 = sounome;
            controlo.envia(out);
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }

        /** recebe do Servidor o número do cliente e o Puzzle do jogo de Sudoku */
        try{
            controlo = new Protocolo();
            controlo = controlo.recebe(in);
            soucliente = (int) controlo.arg1;
            jogoPuzzle = (String []) controlo.arg2;
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }
        System.out.println("Sou o cliente "+ soucliente);
        try{
            janelaCliente = new Sudoku(jogoPuzzle);

            /** gerar o menu */
            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);
            JMenu debugMenu = new JMenu("Debug");
            JMenu jogoMenu = new JMenu("Jogo");
            JMenu exitMenu = new JMenu("Sair");
            menuBar.add(debugMenu);
            menuBar.add(jogoMenu);
            menuBar.add(exitMenu);
            JMenuItem debugPreencherTudo = new JMenuItem("Preencher Tudo");
            JMenuItem jogoHighscores = new JMenuItem("Highscores");
            JMenuItem jogoVerificar = new JMenuItem("Verificar");
            JMenuItem jogoDicas = new JMenuItem("Dicas");
            JMenuItem sairSair = new JMenuItem("Sair");
            debugMenu.add(debugPreencherTudo);
            jogoMenu.add(jogoHighscores);
            jogoMenu.add(jogoVerificar);
            jogoMenu.add(jogoDicas);
            exitMenu.add(sairSair);
            /** opcção de Debugging: preenche o jogo com os números certos */
            debugPreencherTudo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    try{
                        controlo = new Protocolo();
                        controlo.arg1 = (String) "Dbug:preencher";
                        controlo.envia(out);
                    } catch (Exception er) {
                        System.err.println(er.getMessage());
                    }
                    for (int line = 0; line < 9; line++) {
                        for (int col = 0; col < 9; col++) {
                            try{
                                controlo = new Protocolo();
                                controlo.arg1 = (String) janelaCliente.cels.get(col + line * 9).qt.getText();
                                controlo.envia(out);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }

                            try {
                                controlo = new Protocolo();
                                controlo = controlo.recebe(in);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                            janelaCliente.cels.get(col + line * 9).qt.setText((String) controlo.arg1);
                            janelaCliente.cels.get(col + line * 9).qt.setBackground(Color.WHITE);
                        }
                    }
                }
            });
            /** opção de verificar a solução */
            jogoVerificar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    verificar();
                }
            });
            /** opção de pedir dicas */
            jogoDicas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    dicas();
                    if (janelaCliente.dicasRestantes == 0)
                        jogoDicas.setEnabled(false);
                }
            });
            /** opção de mostrar uma janela com os Highscores */
            jogoHighscores.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        controlo = new Protocolo();
                        controlo.arg1 = (String) "Highscores";
                        controlo.envia(out);
                    } catch (Exception er) {
                        System.err.println(er.getMessage());
                    }

                    try {
                        controlo = new Protocolo();
                        controlo = controlo.recebe(in);
                    } catch (Exception er) {
                        System.err.println(er.getMessage());
                    }
                    System.out.println("Highscores: \n" + controlo.arg1);
                    JFrame janelaHighScores = new JFrame("Mestres do Sudoku");
                    JTextArea areaHighScores = new JTextArea();
                    areaHighScores.setEditable(false);
                    areaHighScores.setText((String) controlo.arg1);
                    janelaHighScores.add(areaHighScores);
                    janelaHighScores.pack();
                    janelaHighScores.setVisible(true);
                }
            });
            /** opção de sair do jogo */
            sairSair.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    sair();
                }
            });
            janelaCliente.add(menuBar, BorderLayout.NORTH);

            /** botão verificar */
            janelaCliente.bVerificar.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    verificar();
                }
            });
            /** botão pedir dicas */
            janelaCliente.bDica.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dicas();
                }
            });
            /** botão de fecho da janela */
            janelaCliente.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    sair();
                }
            });

        } catch (IOException er) {
            System.err.println(er.getMessage());
        }
        System.out.println("Sudokan");

        janelaCliente.setVisible(true);

        /** Thread que conta o tempo de jogo do lado do Cliente. Este tempo é meramente informativo. */
        tempocliente = new Thread(){
            public void run() {
                long startTime = System.currentTimeMillis();
                do{
                    try{
                        sleep(1000);
                    }catch (Exception er) {
                        System.err.println(er.getMessage());
                    }
                    // elapsed time in milliseconds
                    long elapsedTimeMillis = System.currentTimeMillis() - ( startTime - 5000 *(dicasIniciais-janelaCliente.dicasRestantes) ); // adiciona 5s por dica

                    DateFormat dateFormat = new SimpleDateFormat("mm:ss");
                    janelaCliente.tempo.setText(dateFormat.format(elapsedTimeMillis));
                    //System.out.println("T: "+dateFormat.format(elapsedTimeMillis));
                } while(janelaCliente.certos != 81);
            }
        };
        if(!tempocliente.isAlive()) {
            tempocliente.start();
        }
    }

    /**
     * Indica ao Servidor que o jogador pretende verificar a solução<br>
     * Percorre cada uma das células e:
     * <ul>
     *     <li> envia a célula para o Servidor</li>
     *     <li>espera resposta do Servidor:
     *       <ul>
     *          <li> se respsota negativa, assinala a célula como errada (cor vermelha)</li>
     *          <li> se resposta positiva, guarda o número de células certas</li>
     *       </ul></li>
     * </ul>
     * Se no final de percorrer todas as células, o número de células certas for igual a 81, o jogador terminou o jogo
     */
    private void verificar(){
        try{
            controlo = new Protocolo();
            controlo.arg1 = (String) "Verifica";
            controlo.envia(out);
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }
        for (int line = 0; line < 9; line++) {
            for (int col = 0; col < 9; col++) {
                try{
                    controlo = new Protocolo();
                    controlo.arg1 = (String) janelaCliente.cels.get(col+line*9).qt.getText();
                    controlo.envia(out);
                } catch (Exception er) {
                    System.err.println(er.getMessage());
                }

                try{
                    controlo = new Protocolo();
                    controlo = controlo.recebe(in);
                } catch (Exception er) {
                    System.err.println(er.getMessage());
                }
                if( (int)controlo.arg1 == -1) {
                    janelaCliente.cels.get(col + line * 9).qt.setBackground(Color.RED);
                    repaint();
                } else{
                    janelaCliente.certos = (int)controlo.arg1;
                }
            }
        }
        if (janelaCliente.certos == 81) {
            for (int line = 0; line < 9; line++) {
                for (int col = 0; col < 9; col++) {
                    janelaCliente.cels.get(col+line*9).qt.setBackground(Color.GREEN);
                }
            }
            JOptionPane.showMessageDialog(getContentPane(), "Parabéns! Chegaste ao fim do jogo.","Parabéns!", JOptionPane.INFORMATION_MESSAGE);
            sair();
        } else {
            janelaCliente.certos = 0;
        }
    }

    /**
     * Indica ao Servidor que o jogador pretende receber uma dica<br>
     * Gera um número aleatório entre 0 e 80 (de forma que calhe numa célula editável) e:
     * <ul>
     *    <li> envia essa célula para o Servidor</li>
     *    <li> espera resposta do Servidor</li>
     *    <li> preenche essa célula com a resposta do Servidor</li>
     *    <li> decrementa o número de dicas disponíveis</li>
     * </ul>
     */
    private void dicas(){
        if (janelaCliente.dicasRestantes > 0) {
            try{
                controlo = new Protocolo();
                controlo.arg1 = (String) "Dica";
                controlo.envia(out);
            } catch (Exception er) {
                System.err.println(er.getMessage());
            }
            int r;
            do {
                r = (int)(Math.random() * 80);
            } while (janelaCliente.cels.get(r).editavel == false);
            try{
                controlo = new Protocolo();
                controlo.arg1 = (int) r;
                controlo.envia(out);
            } catch (Exception er) {
                System.err.println(er.getMessage());
            }
            try{
                controlo = new Protocolo();
                controlo = controlo.recebe(in);
            } catch (Exception er) {
                System.err.println(er.getMessage());
            }
            janelaCliente.cels.get(r).qt.setText((String) controlo.arg1);
            janelaCliente.cels.get(r).qt.setBackground(Color.WHITE);

            janelaCliente.dicasRestantes = (int) controlo.arg2;
            janelaCliente.bDica.setText("Dica ("+ janelaCliente.dicasRestantes+")");
        }
        if (janelaCliente.dicasRestantes == 0) {
            janelaCliente.bDica.setEnabled(false);
        }
    }

    /**
     * Fecha a ligação com o Servidor e fecha o cliente
     */
    private void sair(){
        try {
            ligacao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        janelaCliente.dispose();
        //System.exit(1);
    }
}