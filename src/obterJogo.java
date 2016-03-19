import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class obterJogo {
    private static final String cvsSplitBy = ",";
    private static final String csvFile = "/Users/dinis/btest.csv";

    //// lê do ficheiro fileName, retorna array com cada linha
    private static String[] readLines(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    //// retorna em String a linha do jogo indexJogo
    private static String qualJogo(int indexJogo) throws IOException {
        String[] lines = readLines(csvFile);
        return lines[indexJogo];
    }

    //// retorna em String o puzzle ou a solução (queCoisa=0 ou queCoisa=1) do jogo indexJogo
    public static String JogoOUSolucao(int indexJogo, int queCoisa) throws IOException {
        String[] coisa = qualJogo(indexJogo).split(cvsSplitBy);
        return coisa[queCoisa];
    }

    //// retorna array de 9 linhas com 9 caracteres cada
    public static String[] deLinhaPara9x9(String linha){
        assert linha.length() % 9 == 0;
        String[] splitLine = new String[linha.length() / 9];
        for (int index = 0; index < splitLine.length; index++)
            splitLine[index] = linha.substring(index * 9, index * 9 + 9);
        return splitLine;
    }

    public static void imprimirSolucaoConsola(int indexJogo) throws IOException {
        String[] tem = deLinhaPara9x9(JogoOUSolucao(indexJogo,1));
        for (int i = 0; i < 9; i++) {
            System.out.println(tem[i]);
        }
    }

    public static int escolherJogoIndexAleatoriamente(){
        int lower = 0,
                upper = 3;
        int r = (int) (Math.random() * (upper - lower)) + lower;
        System.out.println("o numero random foi "+r);
        return r;
    }

    public static void main(String args[]) throws IOException {
        //System.out.println(JogoOUSolucao(0,1));
        //System.out.println(qualJogo(1));

        int asw = escolherJogoIndexAleatoriamente();
        {
            String[] tem = deLinhaPara9x9(JogoOUSolucao(asw,0));
            for (int i = 0; i < 9; i++) {
                System.out.println(tem[i]);
            }
        }
        imprimirSolucaoConsola(asw);
    }
}