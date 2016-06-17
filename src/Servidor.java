import java.io.*;
import java.net.*;

public class Servidor {
    protected final int DIM = 5;
    protected ObjectOutputStream oos[] = new ObjectOutputStream[DIM];
    protected ObjectInputStream in[] = new ObjectInputStream[DIM];
    protected ServerSocket SocketEscuta;
    protected Socket[] meias = new Socket[5];
    protected Protocolo[] jogos = new Protocolo[5];
    int n = -1;

    public Servidor() {
    }

    public Servidor(int listenPort) throws IOException {
        SocketEscuta = new ServerSocket(listenPort, DIM);
    }

    public void waitForClients() {
        try {
            while( n < DIM ) {
                meias[n+1] = SocketEscuta.accept();
                n++;
                //System.out.println(n);

                oos[n] = new ObjectOutputStream(meias[n].getOutputStream());
                in[n] = new ObjectInputStream(meias[n].getInputStream());

                Thread one = new Thread() {
                    public void run() {
                        int dicas = 5;

                        /** Puzzle do jogo escolhido */
                        final String[] jogoPuzzle;
                        /** Solução do jogo escolhido */
                        final String[] jogoSolucao;
                        String jogo = null;
                        InputStream ficheiroJogos = null;
                        try{
                            ficheiroJogos = new FileInputStream("./res/btest.csv");
                        }catch (Exception er) {
                            System.err.println(er.getMessage());
                        }
                        obterJogo obter = new obterJogo();
                        int meuclient = n;
                        String omeunome;
                        int count = 0;
                        try{
                            jogos[meuclient] = new Protocolo();
                            jogos[meuclient].arg1 = meuclient;
                            jogos[meuclient].envia(oos[meuclient]);
                            System.out.println("Cliente "+meuclient+" ligado.");
                        } catch (Exception er) {
                            System.err.println(er.getMessage());
                        }

                        // Get current time
                        long start = System.currentTimeMillis();

                        try{
                            jogo = obter.readLine(ficheiroJogos, obter.escolherJogoIndexAleatoriamente());
                        } catch (Exception er) {
                            System.err.println(er.getMessage());
                        }
                        jogoPuzzle = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(0, jogo));
                        jogoSolucao = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(1, jogo));

                        try{
                            jogos[meuclient] = new Protocolo();
                            jogos[meuclient].arg1 = jogoPuzzle;
                            jogos[meuclient].envia(oos[meuclient]);

                            System.out.println("Cliente "+meuclient+" com jogo atribuido. Solução:");
                            //obter.imprimirSolucao(jogo);
                            ///new Sudoku(jogoSolucao, meuclient);
                        } catch (Exception er) {
                            System.err.println(er.getMessage());
                        }

                        do{
                            try{
                                jogos[meuclient] = new Protocolo();
                                jogos[meuclient] = jogos[meuclient].recebe(in[meuclient]);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());

                                System.out.println("sssss close");
                                try {
                                    meias[meuclient].close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }

                            String botaotipo = (String) jogos[meuclient].arg1;
                            if(botaotipo.equals("Verifica")) {
                                for (int line = 0; line < 9; line++) {
                                    for (int col = 0; col < 9; col++) {
                                        try{
                                            jogos[meuclient] = new Protocolo();
                                            jogos[meuclient] = jogos[meuclient].recebe(in[meuclient]);
                                        }catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                        String valores = (String) jogos[meuclient].arg1;

                                        if (valores.equals(String.valueOf(jogoSolucao[line].charAt(col)))) {
                                            try{
                                                jogos[meuclient] = new Protocolo();
                                                jogos[meuclient].arg1 = 2;
                                                jogos[meuclient].envia(oos[meuclient]);
                                            }catch (Exception er) {
                                                System.err.println(er.getMessage());
                                            }
                                        } else {
                                            try{
                                                jogos[meuclient] = new Protocolo();
                                                jogos[meuclient].arg1 = null;
                                                jogos[meuclient].envia(oos[meuclient]);
                                            } catch (Exception er) {
                                                System.err.println(er.getMessage());
                                            }
                                        }
                                    }
                                }
                                // Get elapsed time in milliseconds
                                long elapsedTimeMillis = System.currentTimeMillis()-start;

                                // Get elapsed time in seconds
                                float elapsedTimeSec = elapsedTimeMillis/1000F;

                                // Get elapsed time in minutes
                                float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
                                int presentSec = (int) elapsedTimeSec;
                                while( presentSec >= 60 ) {
                                    presentSec = presentSec - 60;
                                }
                                if( presentSec < 10 ) {
                                    System.out.println("Cliente "+meuclient+" verificou aos: " + (int) elapsedTimeMin + ":0" + (int) presentSec);
                                } else {
                                    System.out.println("Cliente "+meuclient+" verificou aos: " + (int) elapsedTimeMin + ":" + (int) presentSec);
                                }
                            }
                            if(botaotipo.equals("Dica")) {
                                System.out.println("Cliente "+meuclient+" recebeu a dica "+(5-dicas));
                                try{
                                    jogos[meuclient] = new Protocolo();
                                    jogos[meuclient] = jogos[meuclient].recebe(in[meuclient]);
                                } catch (Exception er) {
                                    System.err.println(er.getMessage());
                                }
                                int r = (int) jogos[meuclient].arg1;

                                String celuladica = String.valueOf(jogoSolucao[r / 9].charAt(r % 9));
                                start = start-5000;
                                dicas--;
                                try{
                                    jogos[meuclient] = new Protocolo();
                                    jogos[meuclient].arg1 = celuladica;
                                    jogos[meuclient].arg2 = dicas;
                                    jogos[meuclient].envia(oos[meuclient]);
                                }catch (Exception er) {
                                    System.err.println(er.getMessage());
                                }
                            }
                        } while(true);
                    }
                };
                if(!one.isAlive()) {
                    one.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor(5000);
            servidor.waitForClients();
        } catch (Exception e) {
            System.out.println("Erro:");
            e.printStackTrace();
        }
    }
}