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

public class Cliente extends JApplet {

    Protocolo game = new Protocolo();
    private ObjectInputStream in;
    private ObjectOutputStream out;

    int meuclient = 0,
        meujogo = 0;

    Sudoku novaJanela;
    Thread one = new Thread();
    private Socket client;

    private String[] jogoPuzzle;


    public static void main(String args[]) {
        Cliente app = new Cliente();
        app.init();
    }

    public void init() {
        try{
            client = new Socket("localhost", 5000);
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }

        try{
            game = new Protocolo();
            game = game.recebe(in);
            meuclient = (int) game.arg1;
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }
        System.out.println("Sou o cliente "+meuclient);

        try{
            game = new Protocolo();
            game = game.recebe(in);
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }
        jogoPuzzle = (String []) game.arg1;

        one = new Thread(){
            public void run() {
                try{
                    novaJanela = new Sudoku(jogoPuzzle);
                }catch (IOException er) {
                    System.err.println(er.getMessage());
                }
                System.out.println("Sudokan");

                novaJanela.bVerificar.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        try{
                            game = new Protocolo();
                            game.arg1 = (String) "Verifica";
                            game.envia(out);
                        } catch (Exception er) {
                            System.err.println(er.getMessage());
                        }
                        for (int line = 0; line < 9; line++) {
                            for (int col = 0; col < 9; col++) {
                                try{
                                    game = new Protocolo();
                                    game.arg1 = (String) novaJanela.cels.get(col+line*9).qt.getText();
                                    game.envia(out);
                                } catch (Exception er) {
                                    System.err.println(er.getMessage());
                                }

                                try{
                                    game = new Protocolo();
                                    game = game.recebe(in);
                                } catch (Exception er) {
                                    System.err.println(er.getMessage());
                                }

                                if(game.arg1 != null)
                                    novaJanela.certos++;
                                else
                                    novaJanela.cels.get(col+line*9).qt.setBackground(Color.RED);
                                repaint();
                            }
                        }
                        if (novaJanela.certos == 81) {
                            for (int line = 0; line < 9; line++) {
                                for (int col = 0; col < 9; col++) {
                                    novaJanela.cels.get(col+line*9).qt.setBackground(Color.GREEN);
                                }
                            }
                            JOptionPane.showMessageDialog(getContentPane(), "Parabéns! Chegaste ao fim do jogo.","Parabéns!", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            novaJanela.certos = 0;
                        }
                    }
                });
                novaJanela.bDica.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (novaJanela.dicasRestantes > 0) {
                            try{
                                game = new Protocolo();
                                game.arg1 = (String) "Dica";
                                game.envia(out);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }

                            int r;
                            do {
                                r = (int)(Math.random() * 80);
                            } while (novaJanela.cels.get(r).editavel == false);
                            try{
                                game = new Protocolo();
                                game.arg1 = (int) r;
                                game.envia(out);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }

                            try{
                                game = new Protocolo();
                                game = game.recebe(in);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                            novaJanela.cels.get(r).qt.setText((String) game.arg1);
                            novaJanela.cels.get(r).qt.setBackground(Color.WHITE);

                            novaJanela.dicasRestantes = (int) game.arg2;
                            novaJanela.bDica.setText("Dica ("+novaJanela.dicasRestantes+")");
                        }
                        if (novaJanela.dicasRestantes == 0) {
                            novaJanela.bDica.setEnabled(false);
                        }
                    }
                });
                novaJanela.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent we) {
                        try {
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //novaJanela.dispose();
                        System.exit(1);
                    }
                });

                novaJanela.setVisible(true);


                // Get current time
                long start = System.currentTimeMillis();

                do{
                    try{
                        sleep(1000);
                    }catch (Exception er) {
                        System.err.println(er.getMessage());
                    }
                    // Get elapsed time in milliseconds
                    long elapsedTimeMillis = System.currentTimeMillis()-start+5000*(5-novaJanela.dicasRestantes);

                    // Get elapsed time in seconds
                    float elapsedTimeSec = elapsedTimeMillis/1000F;

                    // Get elapsed time in minutes
                    float elapsedTimeMin = elapsedTimeMillis/(60*1000F);

                    int presentSec=(int) elapsedTimeSec;
                    while(presentSec >= 60)
                        presentSec = presentSec - 60;
                    if(presentSec<10) {
                        novaJanela.tempo.setText("Tempo: "+(int) elapsedTimeMin+":0"+(int) presentSec);
                        System.out.println("T: "+(int) elapsedTimeMin+":0"+(int) presentSec);
                    } else {
                        novaJanela.tempo.setText("Tempo: "+(int) elapsedTimeMin+":"+(int) presentSec);
                        System.out.println("T: "+(int) elapsedTimeMin+":"+(int) presentSec);
                    }
                } while(novaJanela.certos != 81);
                       /*      try{
                                game.arg1=novaJanela.certos;
                                game.envia(out);
                            } catch (Exception er) {
                              System.err.println(er.getMessage());
                            }
                            try{
                            int time=in.read();
                            System.out.println(time);
                            } catch (IOException e) {
                                System.err.println(e.getMessage());
                                System.exit(1);
                            }*/
            }
        };

        if(!one.isAlive()) {
            one.start();
        }
    }
}