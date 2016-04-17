/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Classe com métodos para leitura do jogo</h1>
 * Esta classe contém vários métodos utilizadas para obter o Jogo de Sudoku de um ficheiro local com vários jogos
 */
class obterJogo {
    /** Separador de dados num ficheiro .csv */
    private static final String cvsSplitBy = ",";

    /** Ficheiro .csv com vários jogos e as suas informações (Puzzle, Solução, etc.) */
    private static final String csvFile = "C:/Users/dinis/Documents/GitHub/Sudoku/res/btest.csv";
    //private static final String csvFile = "./res/btest.csv";


    /**
     * Lê os jogos que estão no ficheiro csvFile
     * @return Array com cada linha do ficheiro
     * @throws IOException
     */
    private static String[] readLines() throws IOException {
        FileReader fileReader = new FileReader(csvFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * Retorna em String toda a linha (jogo, solução, etc.) do jogo referido
     * @param indexJogo é qual o jogo
     * @return linha em String
     * @throws IOException
     */
    private static String qualJogo(int indexJogo) throws IOException {
        String[] lines = readLines();
        return lines[indexJogo];
    }

    /**
     * Retorna em String o Puzzle ou a Solução do jogo referido
     * @param indexJogo é qual o jogo
     * @param queCoisa 0 para Puzzle, 1 para Solução
     * @return String com o Puzzle ou a Solução em linha
     * @throws IOException
     */
    public static String PuzzleOUSolucao(int indexJogo, int queCoisa) throws IOException {
        String[] coisa = qualJogo(indexJogo).split(cvsSplitBy);
        return coisa[queCoisa];
    }

    /**
     * Converte Puzzle/Solução de uma linha para 9x9
     * @param linha linha com o Puzzle ou com a Solução
     * @return Array 9x9
     */
    public static String[] deLinhaPara9x9(String linha) {
        //assert linha.length() % 9 == 0;
        String[] splitLine = new String[linha.length() / 9];
        for (int index = 0; index < splitLine.length; index++)
            splitLine[index] = linha.substring(index * 9, index * 9 + 9);
        return splitLine;
    }

    /**
     * Imprime na consola a solução do jogo referido
     * @param indexJogo é qual o jogo
     * @throws IOException
     */
    public static void imprimirSolucao(int indexJogo) throws IOException {
        String[] tem = deLinhaPara9x9(PuzzleOUSolucao(indexJogo, 1));
        for (int i = 0; i < 9; i++) {
            System.out.println(tem[i]);
        }
    }

    /**
     * Escolhe aleatoriamente um número entre 0 e um máximo.
     * Imprime na consola a informação do número.
     * @return número aleatório
     */
    public static int escolherJogoIndexAleatoriamente() {
        int lower = 0,
            upper = 24;
        int r = (int)(Math.random() * (upper - lower)) + lower;
        System.out.println("o numero random foi " + r);
        return r;
    }
}