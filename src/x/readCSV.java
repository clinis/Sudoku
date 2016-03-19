package x;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class readCSV {
    public static void main(String[] args) {
        readCSV obj = new readCSV();
        obj.run();
    }

    private String separaEm9(String[] linha ){
        assert linha[1].length() % 9 == 0;
        String[] splitLine = new String[linha[1].length() / 9];
        for (int index = 0; index < splitLine.length; index++)
            splitLine[index] = linha[1].substring(index * 9, index * 9 + 9);

        for (int i = 0; i < 81; i++) {
            System.out.println(splitLine[i]);
//            if (i % 9 == 0) {
//                System.out.println(" ");
//                //i = 0;
//            }
        }
    }

    public void run() {
        String csvFile = "/Users/dinis/btest.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        String[] leitura = new String[0];
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                leitura = line.split(cvsSplitBy);

                separaEm9(leitura[]);

                //System.out.println("Country [code= " + leitura[4] + " , name=" + leitura[5] + "]");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
    }
}
