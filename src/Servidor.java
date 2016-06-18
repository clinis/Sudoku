import java.io.*;
import java.net.*;

public class Servidor {
    protected final int DIM = 5;
    protected ServerSocket SocketEscuta;

    public Servidor() {
    }

    public Servidor(int listenPort) {
        try {
            SocketEscuta = new ServerSocket(listenPort);
            System.out.println("Servidor à espera de ligações...");
            while (true) {
                Socket socket = SocketEscuta.accept();
                System.out.println("Ligado: "+socket.getPort());
                Atendimento atendedor = new Atendimento(socket);
                atendedor.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Servidor(5000);
    }


    class Atendimento extends Thread {
        private Socket soquete;
        ObjectOutputStream oos = null;
        ObjectInputStream in = null;

        public Atendimento(Socket socket) {
            this.soquete = socket;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Protocolo controlo = new Protocolo();
                    int dicas = 5;
                    final String[] jogoPuzzle;
                    final String[] jogoSolucao;
                    String jogo = null;
                    InputStream ficheiroJogos = null;
                    try {
                        ficheiroJogos = new FileInputStream("./res/btest.csv");
                    } catch (Exception er) {
                        System.err.println(er.getMessage());
                    }
                    obterJogo obter = new obterJogo();
                    try {
                        controlo = new Protocolo();
                        controlo.arg1 = soquete.getPort();
                        controlo.envia(oos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Get current time
                    long start = System.currentTimeMillis();

                    try {
                        jogo = obter.readLine(ficheiroJogos, obter.escolherJogoIndexAleatoriamente());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    jogoPuzzle = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(0, jogo));
                    jogoSolucao = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(1, jogo));

                    try{
                        controlo = new Protocolo();
                        controlo.arg1 = jogoPuzzle;
                        controlo.envia(oos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    do {
                        try {
                            controlo = new Protocolo();
                            controlo = controlo.recebe(in);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("Cliente "+soquete.getPort()+" closed");
                            try {
                                oos.close();
                                in.close();
                                soquete.close();
                                join();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        String botaotipo = (String) controlo.arg1;
                        if (botaotipo.equals("Verifica")) {
                            for (int line = 0; line < 9; line++) {
                                for (int col = 0; col < 9; col++) {
                                    try {
                                        controlo = new Protocolo();
                                        controlo = controlo.recebe(in);
                                    } catch (Exception er) {
                                        System.err.println(er.getMessage());
                                    }
                                    String valores = (String) controlo.arg1;

                                    if (valores.equals(String.valueOf(jogoSolucao[line].charAt(col)))) {
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = 2;
                                            controlo.envia(oos);
                                        } catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                    } else {
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = null;
                                            controlo.envia(oos);
                                        } catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                    }
                                }
                            }
                            // Get elapsed time in milliseconds
                            long elapsedTimeMillis = System.currentTimeMillis() - start;

                            // Get elapsed time in seconds
                            float elapsedTimeSec = elapsedTimeMillis / 1000F;

                            // Get elapsed time in minutes
                            float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
                            int presentSec = (int) elapsedTimeSec;
                            while (presentSec >= 60) {
                                presentSec = presentSec - 60;
                            }
                            if (presentSec < 10) {
                                System.out.println("Cliente " + soquete.getPort() + " verificou aos: " + (int) elapsedTimeMin + ":0" + (int) presentSec);
                            } else {
                                System.out.println("Cliente " + soquete.getPort() + " verificou aos: " + (int) elapsedTimeMin + ":" + (int) presentSec);
                            }
                        }
                        if (botaotipo.equals("Dica")) {
                            System.out.println("Cliente " + soquete.getPort() + " recebeu a dica " + (5 - dicas));
                            try {
                                controlo = new Protocolo();
                                controlo = controlo.recebe(in);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                            int r = (int) controlo.arg1;

                            String celuladica = String.valueOf(jogoSolucao[r / 9].charAt(r % 9));
                            start = start - 5000;
                            dicas--;
                            try {
                                controlo = new Protocolo();
                                controlo.arg1 = celuladica;
                                controlo.arg2 = dicas;
                                controlo.envia(oos);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                        }
                    } while (true);
                } finally {
                    try {
                        soquete.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
