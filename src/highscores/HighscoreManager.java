package highscores;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class HighscoreManager {
    private ArrayList<Score> scores;

    private static final String HIGHSCORE_FILE = "./_highscores.dat";
    private static final String HIGHSCORE_FILE_txt = "./_highscores.txt";

    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    public HighscoreManager() {
        scores = new ArrayList<Score>();
    }

    private ArrayList<Score> getScores() {
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

    private void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] " + e.getMessage() + "criar ficheiro novo");
        } catch (IOException e) {
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

    private void writeScoreTextFile() {
        try {
            FileWriter pw = new FileWriter(HIGHSCORE_FILE_txt, false);
            pw.write(getHighscoreString());
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("[Update]" + e.getMessage() + "criar ficheiro novo");
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
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
            DateFormat dateFormat = new SimpleDateFormat("mm:ss");
            highscoreString += (i + 1) + ".\t" + dateFormat.format(scores.get(i).getTempo()) + "\t" + scores.get(i).getPuzzle() + "\t" + scores.get(i).getNome() + "\n";
            i++;
        }
        return highscoreString;
    }
}