/* Dinis Areias 1120327@isep.ipp.pt
 * ISEP - Instituto Superior de Engenharia do Porto<br>
 * Mestrado em Engenharia Electrotécnica e de Computadores - Telecomunicações<br>
 * Sistemas e Aplicações Distribuídas em Telecomunicações
 * 2015/2016
 */

import java.io.*;
import java.util.ArrayList;

/**
 * <h1>Classe com métodos para leitura do jogo</h1>
 * Esta classe contém vários métodos utilizadas para obter o Jogo de Sudoku de um ficheiro local com vários jogos
 */
class obterJogo {

    /**
     * Lê os jogos que estão no ficheiro file
     *
     * @param file      ficheiro .csv com as informações (Puzzle e Solução) de vários jogos
     * @param indexJogo jogo escolhido
     * @return String com a linha (Puzzle e Solução) do jogo escolhido
     * @throws IOException
     */
    public static String readLine(InputStream file, int indexJogo) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.get(indexJogo);
    }

    /**
     * Retorna em String o Puzzle ou a Solução do jogo referido
     *
     * @param queCoisa 0 para Puzzle, 1 para Solução
     * @param jogo     jogo referido
     * @return String com o Puzzle ou a Solução em linha
     */
    public String PuzzleOUSolucao(int queCoisa, String jogo) {
        String[] coisa = jogo.split(",");
        return coisa[queCoisa];
    }

    /**
     * Converte Puzzle/Solução de uma linha para 9x9
     *
     * @param linha linha com o Puzzle ou com a Solução
     * @return Array 9x9
     */
    public static String[] deLinhaPara9x9(String linha) {
        String[] splitLine = new String[linha.length() / 9];
        for (int index = 0; index < splitLine.length; index++)
            splitLine[index] = linha.substring(index * 9, index * 9 + 9);
        return splitLine;
    }

    /**
     * Imprime na consola a solução do jogo referido
     *
     * @param jogo jogo referido
     */
    public void imprimirSolucao(String jogo) {
        String[] tem = deLinhaPara9x9(PuzzleOUSolucao(1, jogo));
        for (int i = 0; i < 9; i++) {
            System.out.println(tem[i]);
        }
    }

    /**
     * Escolhe aleatoriamente um número entre 0 e um máximo.
     * Imprime na consola a informação do número.
     *
     * @return número aleatório
     */
    public static int escolherJogoIndexAleatoriamente() {
        int lower = 0,
                upper = 24;
        int r = (int) (Math.random() * (upper - lower)) + lower;
        System.out.println("o numero random foi " + r);
        return r;
    }
}