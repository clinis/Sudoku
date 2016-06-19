package highscores;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class HighscoreManager {
    // An arraylist of the type "score" we will use to work with the scores inside the class
    private ArrayList<Score> scores;

    // The name of the file where the highscores will be saved
    private static final String HIGHSCORE_FILE = "./HighScores/scores.dat";
    private static final String HIGHSCORE_FILE_txt = "./HighScores/scores.txt";

    //Initialising an in and outputStream for working with the file
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public HighscoreManager() {
        //initialising the scores-arraylist
        scores = new ArrayList<Score>();
    }

    ////////////////////////////////////////////////
    public ArrayList<Score> getScores() {
        loadScoreFile();
        sort();
        return scores;
    }
    private void sort() {
        ScoreComparator comparator = new ScoreComparator();
        Collections.sort(scores, comparator);
    }
    public void addScore(String name, long score, int puzzle) {
        loadScoreFile();
        scores.add(new Score(name, score, puzzle));
        updateScoreFile();
        writeScoreTextFile();
    }
    public void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Laad] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Laad] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Laad] CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            }
        }
    }
    public void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }
    public void writeScoreTextFile() {
        try {
            FileWriter pw = new FileWriter(HIGHSCORE_FILE_txt, false);
            pw.write(getHighscoreString());
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        }
    }
    public String getHighscoreString() {
        String highscoreString = "";
        int max = 10;

        ArrayList<Score> scores;
        scores = getScores();

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            //highscoreString += (i + 1) + ".\t" + scores.get(i).getNome() + "\t\t" + scores.get(i).getTempo() + "\n";
            DateFormat dateFormat = new SimpleDateFormat("mm:ss");
            highscoreString += (i + 1) + ".\t" + dateFormat.format(scores.get(i).getTempo()) + "\t" + scores.get(i).getPuzzle() + "\t" + scores.get(i).getNome() + "\n";
            i++;
        }
        return highscoreString;
    }
}