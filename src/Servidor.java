import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;

public class Servidor {
    protected final int DIM = 5;
    protected ArrayList<ObjectOutputStream> oos = new ArrayList <ObjectOutputStream>();
    protected ArrayList<ObjectInputStream> in = new ArrayList <ObjectInputStream>();
    protected ServerSocket SocketEscuta;
    protected ArrayList<Socket> meias = new ArrayList<Socket>();
    protected Protocolo[] jogos = new Protocolo[5];
    int n = -1;

    public Servidor() {
    }

    public Servidor(int listenPort) throws IOException {
        SocketEscuta = new ServerSocket(listenPort, DIM);
    }

    public void waitForClients() {
        try {
            while( n+1 < DIM ) {
                Socket supermeia=SocketEscuta.accept();
                meias.add(supermeia);
                n++;

                oos.add(new ObjectOutputStream(meias.get(n).getOutputStream()));
                in.add(new ObjectInputStream(meias.get(n).getInputStream()));

                Thread one = new Thread() {
                    public void run() {
                        int meuclient = meias.indexOf(supermeia);
                        System.out.println(meuclient);
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
                        try{
                            jogos[meuclient] = new Protocolo();
                            jogos[meuclient].arg1 = meuclient;
                            jogos[meuclient].envia(oos.get(meuclient));
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
                            jogos[meuclient].envia(oos.get(meuclient));

                            System.out.println("Cliente "+meuclient+" com jogo atribuido. Solução:");
                            //obter.imprimirSolucao(jogo);
                            ///new Sudoku(jogoSolucao, meuclient);
                        } catch (Exception er) {
                            System.err.println(er.getMessage());
                        }

                        do{
                            try{
                                jogos[meuclient] = new Protocolo();
                                jogos[meuclient] = jogos[meuclient].recebe(in.get(meuclient));
                            } catch (Exception er) {
                                //System.err.println(er.getMessage());
                                System.out.println("Cliente "+meuclient+" closed");
                                try {
                                    meias.remove(meuclient);
                                    oos.remove(meuclient);
                                    in.remove(meuclient);
                                    n=meuclient;
                                    join();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //break;
                            }

                            String botaotipo = (String) jogos[meuclient].arg1;
                            if(botaotipo.equals("Verifica")) {
                                for (int line = 0; line < 9; line++) {
                                    for (int col = 0; col < 9; col++) {
                                        try{
                                            jogos[meuclient] = new Protocolo();
                                            jogos[meuclient] = jogos[meuclient].recebe(in.get(meuclient));
                                        }catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                        String valores = (String) jogos[meuclient].arg1;

                                        if (valores.equals(String.valueOf(jogoSolucao[line].charAt(col)))) {
                                            try{
                                                jogos[meuclient] = new Protocolo();
                                                jogos[meuclient].arg1 = 2;
                                                jogos[meuclient].envia(oos.get(meuclient));
                                            }catch (Exception er) {
                                                System.err.println(er.getMessage());
                                            }
                                        } else {
                                            try{
                                                jogos[meuclient] = new Protocolo();
                                                jogos[meuclient].arg1 = null;
                                                jogos[meuclient].envia(oos.get(meuclient));
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
                                    jogos[meuclient] = jogos[meuclient].recebe(in.get(meuclient));
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
                                    jogos[meuclient].envia(oos.get(meuclient));
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