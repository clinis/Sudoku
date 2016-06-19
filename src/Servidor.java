import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

        Protocolo controlo = new Protocolo();
        int dicasRestantes = 5;
        String[] jogoPuzzle = null;
        String[] jogoSolucao = null;
        String jogo = null;
        InputStream ficheiroJogos = null;
        int clientenum = -1;
        Object clientenome;

        public Atendimento(Socket socket) {
            this.soquete = socket;
            this.clientenum = soquete.getPort();
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                ficheiroJogos = new FileInputStream("./res/btest.csv");
                obterJogo obter = new obterJogo();
                jogo = obter.readLine(ficheiroJogos, obter.escolherJogoIndexAleatoriamente());
                jogoPuzzle = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(0, jogo));
                jogoSolucao = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(1, jogo));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    try {
                        controlo = new Protocolo();
                        controlo = controlo.recebe(in);
                        clientenome =  controlo.arg1;
                    } catch (Exception er) {
                        //System.err.println(er.getMessage());
                    }
                    System.out.println(clientenome);
                    if ( clientenome == null || ((String) clientenome).length() < 1){
                        clientenome = "Visitante";
                        System.out.println(clientenome);
                    }
                    System.out.println("Cliente "+clientenum+" com jogador "+clientenome);

                    try {
                        controlo = new Protocolo();
                        controlo.arg1 = clientenum;
                        controlo.arg2 = jogoPuzzle;
                        controlo.envia(oos);
                        System.out.println("Cliente "+ clientenum +" com jogo atribuido");
                        //obter.imprimirSolucao(jogo);
                        ////new Sudoku(jogoSolucao, clientenum);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Get current time
                    long startTime = System.currentTimeMillis();


                    do {
                        try {
                            controlo = new Protocolo();
                            controlo = controlo.recebe(in);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("Cliente "+ clientenum +" closed");
                            try {
                                oos.close();
                                in.close();
                                soquete.close();
                                join();
                            } catch (IOException | InterruptedException e1) {
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
                                    String celulaAVerificar = (String) controlo.arg1;

                                    if (celulaAVerificar.equals(String.valueOf(jogoSolucao[line].charAt(col)))) {
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = "certo";
                                            controlo.envia(oos);
                                        } catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                    } else {
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = "errado";
                                            controlo.envia(oos);
                                        } catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                    }
                                }
                            }
                            // Get elapsed time in milliseconds
                            long elapsedTimeMillis = System.currentTimeMillis() - startTime;
                            DateFormat dateFormat = new SimpleDateFormat("mm:ss");
                            System.out.println("Cliente " + clientenum + " verificou aos: "+dateFormat.format(elapsedTimeMillis));
                        }
                        if (botaotipo.equals("Dica")) {
                            System.out.println("Cliente " + clientenum + " pediu a dica " + (5 - dicasRestantes));
                            try {
                                controlo = new Protocolo();
                                controlo = controlo.recebe(in);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                            int r = (int) controlo.arg1;

                            String dicaSolucao = String.valueOf(jogoSolucao[r / 9].charAt(r % 9));
                            startTime = startTime - 5000; // acrescenta 5s
                            dicasRestantes--;
                            try {
                                controlo = new Protocolo();
                                controlo.arg1 = dicaSolucao;
                                controlo.arg2 = dicasRestantes;
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
