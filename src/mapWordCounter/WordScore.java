package mapWordCounter;

/**
 * Helper class used for sorting word scores.
 */

public class WordScore implements Comparable<WordScore> {
    private String word;
    private Double score;

    public WordScore(String word, Double score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public int compareTo(WordScore wordScore) {
        return score.compareTo(wordScore.getScore());
    }
}
