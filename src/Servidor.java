import highscores.HighscoreManager;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Servidor {
    protected final int DIM = 5;
    static final int dicasIniciais = 5;
    private ServerSocket SocketEscuta;

    public Servidor() {
    }

    /**
     * Construtor que cria um ServerSocket que escuta a porta fornecida à espera de ligações.<br>
     * Quando recebe uma ligação, atende o cliente criando uma Thread para tal.
     * @param listenPort porta onde o servidor vai estar á escuta
     */
    private Servidor(int listenPort) {
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

    /**
     * Thread de atendimento de cada cliente.<br>
     *  Cria as streams de comunicação com o Cliente.<br>
     *  Abre o ficheiro com os jogos (Puzzle e Solução) de Sudoku.<br>
     *  Recebe o nome do Cliente. So o nome for nulo, atribui o nome "Visitante"<br>
     *  Envia o jogo ao Cliente e fica a guardar pedidos do Cliente
     */
    private class Atendimento extends Thread {
        private Socket soquete;
        ObjectOutputStream oos = null;
        ObjectInputStream in = null;

        Protocolo controlo = new Protocolo();
        int dicasRestantes = dicasIniciais;
        String[] jogoPuzzle = null;
        String[] jogoSolucao = null;
        int jogoindex = 0;
        String jogo = null;
        InputStream ficheiroJogos = null;
        int clientenum = -1;
        Object clientenome;
        int clientecertos = 0,
            desconto = 0;
        HighscoreManager scoreboard = new HighscoreManager();

        Atendimento(Socket socket) {
            this.soquete = socket;
            this.clientenum = soquete.getPort();
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                ficheiroJogos = getClass().getResourceAsStream("/"+"btest.csv");
                obterJogo obter = new obterJogo();
                jogoindex = obterJogo.escolherJogoIndexAleatoriamente();
                jogo = obterJogo.readLine(ficheiroJogos, jogoindex);
                jogoPuzzle = obterJogo.deLinhaPara9x9(obter.PuzzleOUSolucao(0, jogo));
                jogoSolucao = obterJogo.deLinhaPara9x9(obter.PuzzleOUSolucao(1, jogo));
                for (int line = 0; line < 9; line++) {
                    for (int col = 0; col < 9; col++) {
                        if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(String.valueOf(jogoSolucao[line].charAt(col)))) {
                            desconto++;
                        }
                    }
                }
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
                        clientenome =  (String) controlo.arg1;
                    } catch (Exception er) {
                        //System.err.println(er.getMessage());
                    }
                    //System.out.print(clientenome);
                    if ( clientenome == null || ((String) clientenome).length() < 1){
                        clientenome = "Visitante";
                    }
                    System.out.println("Cliente "+clientenum+" com jogador "+clientenome);

                    try {
                        controlo = new Protocolo();
                        controlo.arg1 = clientenum;
                        controlo.arg2 = jogoPuzzle;
                        controlo.envia(oos);
                        System.out.println("Cliente "+ clientenum +" com jogo atribuido");
                        //obter.imprimirSolucao(jogo);
                        //new Sudoku(jogoSolucao, clientenum);
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
                        String pedido = (String) controlo.arg1;

                        /** Pedido de verificação de solução por parte do Cliente */
                        if (pedido.equals("Verifica")) {
                            clientecertos = 0;
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
                                        clientecertos++;
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = clientecertos;
                                            controlo.envia(oos);
                                        } catch (Exception er) {
                                            System.err.println(er.getMessage());
                                        }
                                    } else {
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = (int) -1;
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

                            if(clientecertos == 81){
                                System.out.println("Cliente " + clientenum + " GANHOU aos: "+dateFormat.format(elapsedTimeMillis));
                                scoreboard.addScore((String)clientenome,elapsedTimeMillis,jogoindex);
                                System.out.print(scoreboard.getHighscoreString());
                            }
                        }

                        /** Pedido de dicas por parte do Cliente */
                        if (pedido.equals("Dica")) {
                            System.out.println("Cliente " + clientenum + " pediu a dica " + (1 + dicasIniciais - dicasRestantes));
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

                        /** Pedido de highscores por parte do Cliente */
                        if (pedido.equals("Highscores")) {
                            try{
                                controlo = new Protocolo();
                                controlo.arg1 = scoreboard.getHighscoreString();
                                controlo.envia(oos);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                        }

                        /** Pedido de debug */
                        if (pedido.equals("Dbug:preencher")) {
                            for (int line = 0; line < 9; line++) {
                                for (int col = 0; col < 9; col++) {
                                    try {
                                        controlo = new Protocolo();
                                        controlo = controlo.recebe(in);
                                    } catch (Exception er) {
                                        System.err.println(er.getMessage());
                                    }
                                    try {
                                        controlo = new Protocolo();
                                        controlo.arg1 = String.valueOf(jogoSolucao[line].charAt(col));
                                        controlo.envia(oos);
                                    } catch (Exception er) {
                                        System.err.println(er.getMessage());
                                    }
                                }
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
