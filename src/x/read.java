package x;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class read {


    private static void separaEm9(String[] linha ){
        assert linha[1].length() % 9 == 0;
        String[] splitLine = new String[linha[1].length() / 9];
        for (int index = 0; index < splitLine.length; index++)
            splitLine[index] = linha[1].substring(index * 9, index * 9 + 9);

        for (int i = 0; i < splitLine[0].length(); i++) {
            System.out.println(splitLine[i]);
//            if (i % 9 == 0) {
//                System.out.println(" ");
//                //i = 0;
//            }
        }
    }

    private static void separaEm92(ArrayList<String[]> linha ){
        //assert linha.get(1).length() % 9 == 0;
        String[] tam = linha.get(1);
        String[] splitLine = new String[ tam.length / 9];
        for (int index = 0; index < splitLine.length; index++)
            splitLine[index] = linha.get(1).substring(index * 9, index * 9 + 9);

        for (int i = 0; i < splitLine[0].length(); i++) {
            System.out.println(splitLine[i]);
//            if (i % 9 == 0) {
//                System.out.println(" ");
//                //i = 0;
//            }
        }
    }


    public static ArrayList<String[]> getLinhas(String fileName, String cvsSplitBy) {
        String[] pedacos = new String[0];
        ArrayList<String[]> ar = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                pedacos = line.split(cvsSplitBy);
                ar.add(pedacos);
                //System.out.println(pedacos[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ar;
    }

    public static String[] FileArrayProvider(String fileName, String cvsSplitBy) throws IOException {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] linw = line.split(cvsSplitBy);
                lines.add(line);
            }
            bufferedReader.close();
            return lines.toArray(new String[lines.size()]);
    }

    public static void main(String args[]) {

        String fileName = "/Users/dinis/btest.csv";
        String cvsSplitBy = ",";

        //String[] pedacos = getLinhas(fileName, cvsSplitBy);
        separaEm92(getLinhas(fileName, cvsSplitBy));
        //getLinhas(fileName, cvsSplitBy);
    }
}
