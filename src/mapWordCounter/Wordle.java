package mapWordCounter;

import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Point;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by bjackson on 10/12/2015.
 */
public class Wordle extends GraphicsGroup {

    public static final int MAX_WORDS = 30;
    public static final int SMALLEST_FONT_SIZE = 10;
    public static final int BIGGEST_FONT_SIZE = 90;
    private GraphicsText[] labels;
    private Random rgen;

    public Wordle(List<WordScore> scores, Color color, double x, double y) {
        super(x,y);
        rgen = new Random();

        int maxIndex = Math.min(scores.size() - 1, MAX_WORDS - 1);
        labels = new GraphicsText[maxIndex];
        double lowScore = scores.get(maxIndex).getScore();
        double highScore = scores.get(0).getScore();

        for (int i = 0; i < maxIndex; i++) {
            labels[i] = new GraphicsText(scores.get(i).getWord(), highScore, lowScore);
            double fontSize = ((scores.get(i).getScore()-lowScore)/(highScore-lowScore)*(BIGGEST_FONT_SIZE-SMALLEST_FONT_SIZE))+SMALLEST_FONT_SIZE;
            labels[i].setFont(FontStyle.PLAIN, fontSize);
            labels[i].setFillColor(color);
        }
    }

    /**
     * Positions each WordleGLabel according to the wordle algorithm.
     */
    public void doLayout() {
        Point center = new Point(0.0, 0.0);
        for(int i=0; i < labels.length; i++){
            Point initialPos = makeInitialPosition(center);
            GraphicsText current = labels[i];
            current.setPosition(initialPos.getX(), initialPos.getY());
            add(current);


            final double DELTA_ANGLE = Math.random() < 0.5 ? 1 : -1;
            double angle = DELTA_ANGLE;

            while(checkIntersections(current)){
                updatePosition(current, initialPos, angle);
                angle += DELTA_ANGLE;
            }

            //pause(1000); // You could uncomment this line if you want to slow down the placing so that it is easier to see what is going on.
        }
    }

    /**
     * Returns a random point within a gaussian distribution centered on center.
     * Used to set the initial position of a word.
     * @param center of the distribution
     * @return random gaussian point.
     */
    private Point makeInitialPosition(Point center){
        final double STDDEV = 25;
        double x = rgen.nextGaussian()* STDDEV + center.getX();
        double y = rgen.nextGaussian() * STDDEV + center.getY();
        return  new Point(x, y);
    }

    /**
     * Updates the position of a label using a spiral pattern centered on initialPos.
     * @param label to position
     * @param initialPos for label, used as the center of the spiral
     * @param angle current angle around the spiral
     */
    private void updatePosition(GraphicsText label, Point initialPos, double angle){
        double x = initialPos.getX() + angle * Math.cos(angle);
        double y = initialPos.getY() + angle * Math.sin(angle);
        label.setPosition(x, y);
    }

    /**
     * Checks for intersections between the label and all other WordleGLabels that are already added to the canvas
     * @param label
     * @return true if an intersection is found.
     */
    private boolean checkIntersections(GraphicsText label){
        Iterator it = this.iterator();
        while(it.hasNext()){
            Object obj = it.next();
            if (obj instanceof GraphicsText) {
                GraphicsText other = (GraphicsText) obj;
                if (label != other && label.intersects(other)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Pauses the program for milliseconds
     * @param milliseconds
     */
    public void pause(long milliseconds){
        try{
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // Empty
        }
    }
}
