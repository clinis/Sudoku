package highscores;

import java.io.Serializable;

public class Score implements Serializable {
    private long tempo;
    private String nome;
    private int puzzle;

    public String getNome() {
        return nome;
    }

    public long getTempo() {
        return tempo;
    }

    public int getPuzzle() {
        return puzzle;
    }

    public Score(String nome, long tempo, int puzzle) {
        this.nome = nome;
        this.tempo = tempo;
        this.puzzle = puzzle;
    }
}