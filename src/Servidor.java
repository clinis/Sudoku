import highscores.HighscoreManager;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Servidor {
    protected final int DIM = 5;
    public static final int dicasIniciais = 5;
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
        int dicasRestantes = dicasIniciais;
        String[] jogoPuzzle = null;
        String[] jogoSolucao = null;
        int jogoindex = 0;
        String jogo = null;
        InputStream ficheiroJogos = null;
        int clientenum = -1;
        Object clientenome;
        int clientecertos = 0,
            clientepontos = 0,
            desconto = 0;
        HighscoreManager hm = new HighscoreManager();

        public Atendimento(Socket socket) {
            this.soquete = socket;
            this.clientenum = soquete.getPort();
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                ficheiroJogos = new FileInputStream("./res/btest.csv");
                obterJogo obter = new obterJogo();
                jogoindex = obter.escolherJogoIndexAleatoriamente();
                jogo = obter.readLine(ficheiroJogos, jogoindex);
                jogoPuzzle = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(0, jogo));
                jogoSolucao = obter.deLinhaPara9x9(obter.PuzzleOUSolucao(1, jogo));
                for (int line = 0; line < 9; line++) {
                    for (int col = 0; col < 9; col++) {
                        if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(String.valueOf(jogoSolucao[line].charAt(col)))) {
                            desconto++;
                        }
                    }
                }
                clientepontos = 0-desconto;
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
                    //System.out.print("\n\n---\n"+clientenome+"\n---\n\n");
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
                                        clientepontos = (clientecertos-desconto-(dicasIniciais-dicasRestantes)); // pontuacao = certos - os certos do puzzle - as dicas
                                        try {
                                            controlo = new Protocolo();
                                            controlo.arg1 = clientecertos;
                                            controlo.arg2 = clientepontos;
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
                                hm.addScore((String)clientenome,elapsedTimeMillis,jogoindex);
                                System.out.print(hm.getHighscoreString());
                            }
                        }
                        if (botaotipo.equals("Dica")) {
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
                        if (botaotipo.equals("Highscores")) {
                            try{
                                controlo = new Protocolo();
                                controlo.arg1 = hm.getHighscoreString();
                                controlo.envia(oos);
                            } catch (Exception er) {
                                System.err.println(er.getMessage());
                            }
                        }
                        if (botaotipo.equals("Dbug:preencher")) {
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
