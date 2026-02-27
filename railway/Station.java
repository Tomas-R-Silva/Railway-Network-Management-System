/* 
 * Station Interface
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

 package railway;

import java.io.Serializable;
import dataStructures.*;

public interface Station extends Serializable {

    /** Getter for the name of the station
     * @return station name
     */
    String getName();

    /** Getter for the first name of this station
     * @return First apperance
     */
    String getFirstName();


    /**
     * @param line
     * Adds to a collection the lines through which the train passes.
     */
    void addLine(String lineName,String lineNoFormat);

    /**
     * @param line
     * Removes a line from the line collection
     */
    void removeLine(String lineName);

    /**
     * @return true id the station has lines, false if dont
     */
    boolean stationHasLines();

     /** Gets an iterator of lines that belong to the station
     * @return an iterator with the names of the lines that the station is in
     */
    Iterator<Entry<String,String>> getLinesOfSt();

    /**
     * adds a train to eh binaryTree that contains all of the trains that pass in that station and uses its time as a key
     */
    public void addTrain(String time, TrainNumLine train);

    /** Gets the iterator of trains that belong in the station
     * @return an iterator with the id of all the trains that pass the station and its associeted time
     */
    Iterator<Entry<String,List<TrainNumLine>>> getTrainsST();

    /**
     * removes a train from the station
     */
    void removeTrain(String time, int trainID);

    /**
     * removes a line from the station
     */
    public void removeTrainsOfLine( String lineName);

    
}
