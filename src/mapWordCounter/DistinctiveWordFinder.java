package mapWordCounter;

import edu.macalester.graphics.CanvasWindow;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Interesting word finder.
 * To use this class, first call countWords(), then call findDistinctive.
 *
 * @author Bret, original based on a 124 lab written by shilad
 *
 */
public class DistinctiveWordFinder {
    // Word counts from each file.
    private Map<String, Integer> primaryCounts;
    private Map<String, Integer> secondaryCounts;

    /**
     * Constructs a new word counter.
     */
    public DistinctiveWordFinder() {
        //TODO: initialize primaryCount and secondaryCount

    }

    /**
     * Counts the words in two files.
     * @param primaryFile
     * @param secondaryFile
     */
    public void countWords(File primaryFile, File secondaryFile) {
        countWordsInOneFile(primaryFile, primaryCounts);
        countWordsInOneFile(secondaryFile, secondaryCounts);
    }

    /**
     * Counts the words in a single file.  The counts should be tallied
     * in the passed-in counts object.
     *
     * @param file1
     * @param counter
     */
    private void countWordsInOneFile(File file1, Map<String, Integer> counter){
        counter.clear();
        try {
            BufferedReader r = new BufferedReader(new FileReader(file1));
            while (true) {
                String line = r.readLine();
                if (line == null) {
                    break;
                }
                for (String w : splitLine(line)) {
                    w = cleanWord(w);
                    if (w.equals("")){
                        continue; // if the word is just punctuation then cleaning it will produce the empty string
                    }

                    //TODO: if the counter map contains the word, increment the associated count
                    // if it does not contain the word, add it with a count of 1




                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes excess punctuation from a word, and turns it to lower case.
     * @param word
     * @return
     */
    private static String cleanWord(String word) {
        return word.toLowerCase().replaceAll("[^a-zA-Z0-9]+", "");
    }

    /**
     * Finds words in the primary word counter that are interesting.
     * Words are interesting if they appear frequently in the primary counts but not in the secondary
     */
    public List<WordScore> findDistinctive() {

        // This list will be used to sort the word scores;
        List<WordScore> scores = new ArrayList<>(primaryCounts.size());

        // TODO: Iterate over the primaryCounts map.
        //  For each word in the map, get the count for the number of times the word appears in the secondaryCounts map.
        //  Find the distinctive score based on the primary and secondary counts (Hint: look at existing methods) and
        //  add the word and corresponding score to the scores list
        // Hint: the word may not appear in the secondaryCounts map. Think about how to handle this case.






        Collections.sort(scores);
        Collections.reverse(scores); // we want them in descending order

        return scores;
    }

    /**
     * Displays the distinctive wordscores
     * @param scores in sorted order from most interesting to least.
     */
    public void displayDistinctive(List<WordScore> scores){
        for(WordScore wordscore : scores) {
            String word = wordscore.getWord();
            int c1 = primaryCounts.get(word);
            int c2 = 0;
            if (secondaryCounts.containsKey(word)) {
                c2 = secondaryCounts.get(word);
            }
            System.out.println("word: " + word + ", primary=" + c1 + ", secondary=" + c2 + ", score="+wordscore.getScore());
        }
    }

    /**
     * Returns a "score" indicating how interesting a word is.
     * @param primaryCount  The count for a specific word for the primary candidate (i.e. Trump)
     * @param secondaryCount The count for a specific word for the secondary candidate (i.e. Clinton)
     * @return
     */
    private double getDistinctiveScore(int primaryCount, int secondaryCount) {
        return ((primaryCount) / (secondaryCount + primaryCount + 1.0));
    }

    /**
     * Splits a line into words.
     * @param line
     * @return An array containing the words.
     */
    private String[] splitLine(String line) {
        return line.split("[^a-zA-Z0-9']+");
    }

    public static File getFile(String resourceName){
        try {
            URL url = DistinctiveWordFinder.class.getResource("/" + resourceName);
            if (url != null) {
                return new File(url.toURI());
            }
            else {
                System.out.println("Cannot find file with name "+resourceName);
                return null;
            }
        } catch (URISyntaxException syntaxException){
            syntaxException.printStackTrace();
            return null;
        }
    }

    public static void main(String args[]) {
        File f1 = getFile("PresidentialDebate2024/trump.txt");
        File f2 = getFile("PresidentialDebate2024/harris.txt");

        System.out.println("Donald Trump's Distinctive Words:");
        DistinctiveWordFinder finder = new DistinctiveWordFinder();
        finder.countWords(f1, f2);
        List<WordScore> scores = finder.findDistinctive();
        finder.displayDistinctive(scores);

        Wordle wordle = new Wordle(scores, Color.RED, 0,0);
        wordle.doLayout();
        Rectangle2D bounds = wordle.getBounds();
        int padding = 50;
        wordle.setCenter(bounds.getWidth()/2.0+padding, bounds.getHeight()/2.0+padding);

        System.out.println("\nKamala Harris' Distinctive Words:");
        finder = new DistinctiveWordFinder();
        finder.countWords(f2, f1);
        scores = finder.findDistinctive();
        finder.displayDistinctive(scores);

        Wordle wordle2 = new Wordle(scores, Color.BLUE, 0,0);
        wordle2.doLayout();
        Rectangle2D bounds2 = wordle2.getBounds();
        wordle2.setCenter(bounds.getWidth()+bounds2.getWidth()/2.0 + (2*padding), bounds2.getHeight()/2.0+padding);

        CanvasWindow canvas = new CanvasWindow("Wordle", (int)(bounds.getWidth()+bounds2.getWidth()+(3*padding)),
                (int)((bounds.getHeight() > bounds2.getHeight())?  bounds.getHeight()+(2*padding) : bounds2.getHeight()+(2*padding)));
        canvas.add(wordle);
        canvas.add(wordle2);
    }
}