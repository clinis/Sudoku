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
import java.util.Calendar;

public class Cliente extends JApplet {
    private Protocolo controlo = new Protocolo();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    Sudoku novaJanela;
    Thread tempocliente = new Thread();
    private Socket ligacao;
    int soucliente = 0;
    private String[] jogoPuzzle;


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

        String sounome = JOptionPane.showInputDialog("Insira o seu nome");
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
            novaJanela = new Sudoku(jogoPuzzle);
            novaJanela.bVerificar.addMouseListener(new trataVerificar());
            novaJanela.bDica.addMouseListener(new trataDicas());
            novaJanela.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    try {
                        ligacao.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //novaJanela.dispose();
                    System.exit(0);
                }
            });
        }catch (IOException er) {
            System.err.println(er.getMessage());
        }
        System.out.println("Sudokan");

        novaJanela.setVisible(true);

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
                    long elapsedTimeMillis = System.currentTimeMillis() - ( startTime - 5000 *(5-novaJanela.dicasRestantes) ); // adiciona 5s por dica

                    DateFormat dateFormat = new SimpleDateFormat("mm:ss");
                    novaJanela.tempo.setText(dateFormat.format(elapsedTimeMillis));
                    //System.out.println("T: "+dateFormat.format(elapsedTimeMillis));
                } while(novaJanela.certos != 81);
            }
        };
        if(!tempocliente.isAlive()) {
            tempocliente.start();
        }
    }

    class trataVerificar extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
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
                        controlo.arg1 = (String) novaJanela.cels.get(col+line*9).qt.getText();
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
                    if(controlo.arg1 == "certo")
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
    }

    class trataDicas extends MouseAdapter{
        public void mouseClicked(MouseEvent e) {
            if (novaJanela.dicasRestantes > 0) {
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
                } while (novaJanela.cels.get(r).editavel == false);
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
                novaJanela.cels.get(r).qt.setText((String) controlo.arg1);
                novaJanela.cels.get(r).qt.setBackground(Color.WHITE);

                novaJanela.dicasRestantes = (int) controlo.arg2;
                novaJanela.bDica.setText("Dica ("+novaJanela.dicasRestantes+")");
            }
            if (novaJanela.dicasRestantes == 0) {
                novaJanela.bDica.setEnabled(false);
            }
        }
    }
}