package highscores;

import java.io.Serializable;

public class Score  implements Serializable {
    private long tempo;
    private String nome;

    public String getNome() {
        return nome;
    }

    public long getTempo() {
        return tempo;
    }

    public Score(String nome, long tempo) {
        this.nome = nome;
        this.tempo = tempo;
    }
}