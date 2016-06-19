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

public class Cliente extends JApplet {
    private final int dicasIniciais = Servidor.dicasIniciais;
    private Protocolo controlo = new Protocolo();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    Sudoku janelaCliente;
    Thread tempocliente = new Thread();
    private Socket ligacao;
    int soucliente = 0;
    private String[] jogoPuzzle;
    int r = 0;


    public static void main(String args[]) {
        Cliente app = new Cliente();
        app.init();
    }

    public void init() {
        try{
            ligacao = new Socket("localhost", 5000);
            in = new ObjectInputStream(ligacao.getInputStream());
            out = new ObjectOutputStream(ligacao.getOutputStream());
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }

        String sounome = JOptionPane.showInputDialog(this,
                                                    "Insira o seu nome para gravar a sua pontuação.\nCaso contrário, ficará registado como \"convidado\".",
                                                    "Insira o seu nome",
                                                    JOptionPane.QUESTION_MESSAGE);
        if(sounome != null)
            sounome = sounome.trim();
        try{
            controlo = new Protocolo();
            controlo.arg1 = sounome;
            controlo.envia(out);
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }

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

            // Creates a menubar for a JFrame
            JMenuBar menuBar = new JMenuBar();
            // Add the menubar to the frame
            setJMenuBar(menuBar);
            // Define and add two drop down menu to the menubar
            JMenu debugMenu = new JMenu("Debug");
            JMenu jogoMenu = new JMenu("Jogo");
            JMenu exitMenu = new JMenu("Sair");
            menuBar.add(debugMenu);
            menuBar.add(jogoMenu);
            menuBar.add(exitMenu);
            // Create and add simple menu item to one of the drop down menu
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
                            janelaCliente.cels.get(col+line*9).qt.setText((String) controlo.arg1);
                            janelaCliente.cels.get(col+line*9).qt.setBackground(Color.WHITE);
                        }
                    }
                }
            });
            jogoVerificar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    verificar();
                }
            });
            jogoDicas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    dicas();
                    if(janelaCliente.dicasRestantes == 0)
                        jogoDicas.setEnabled(false);
                }
            });
            jogoHighscores.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    System.out.println("Testingggg Highscore");
                    try{
                        controlo = new Protocolo();
                        controlo.arg1 = (String) "Highscores";
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
                    System.out.println("Highscores: \n"+ controlo.arg1);
                    JFrame janelaHighScores = new JFrame("Mestres do Sudoku");
                    JTextArea areaHighScores = new JTextArea();
                    areaHighScores.setEditable(false);
                    areaHighScores.setText((String)controlo.arg1);
                    janelaHighScores.add(areaHighScores);
                    janelaHighScores.pack();
                    janelaHighScores.setVisible(true);
                }
            });
            sairSair.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    sair();
                }
            });
            janelaCliente.add(menuBar, BorderLayout.NORTH);

            janelaCliente.bVerificar.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    verificar();
                }
            });
            janelaCliente.bDica.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dicas();
                }
            });
            janelaCliente.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    sair();
                }
            });

        }catch (IOException er) {
            System.err.println(er.getMessage());
        }
        System.out.println("Sudokan");

        janelaCliente.setVisible(true);

        tempocliente = new Thread(){
            public void run() {

                // Get current time
                long startTime = System.currentTimeMillis();
                do{
                    try{
                        sleep(1000);
                    }catch (Exception er) {
                        System.err.println(er.getMessage());
                    }
                    // Get elapsed time in milliseconds
                    long elapsedTimeMillis = System.currentTimeMillis() - ( startTime - 5000 *(dicasIniciais- janelaCliente.dicasRestantes) ); // adiciona 5s por dica

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

    void verificar(){
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
                    janelaCliente.pontuacao.setText("Pontos: "+controlo.arg2.toString() );
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
        } else {
            janelaCliente.certos = 0;
        }
    }

    void dicas(){
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

    void sair(){
        try {
            ligacao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //janelaCliente.dispose();
        System.exit(1);
    }
}