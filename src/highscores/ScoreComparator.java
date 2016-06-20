package highscores;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Score> {
    public int compare(Score score1, Score score2) {

        long sc1 = score1.getTempo();
        long sc2 = score2.getTempo();

        if (sc1 > sc2) {
            return +1;
        } else if (sc1 < sc2) {
            return -1;
        } else {
            return 0;
        }
    }
}