/* 
 * Line Interface
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;
import java.io.Serializable;

import dataStructures.*;

public interface Line extends Serializable {
    
    /** Getter for the name of the line
     * @return name of the line
     */
    String getName();

    String getNameToIterate();

    /** Add a station to the line
     * @param station
     */
    void addStation(Station station);

    /** List all the station of the line
     * @return Stations iterator
     */
    Iterator<Station> iteratorStations();

    /** List all the trains of the line
     * @return <Train ID, Train> iterator of all trains
     */
    Iterator<Entry<Integer,Train>> iteratorTrains();


    /** Add a train to the line
     * @param trainID
     * @param train
     */
    void addTrain(int trainID, Train train);

    /** Remove a train from the line
     * @param trainID
     */
    void removeTrain(int trainID);

    /** Getter for a train using its ID
     * @param trainID
     * @return Train with the given ID
     */
    Train getTrain(int trainID);

    /**
     * @param stationName
     * @return -1 if is not a terminal station, 0 is is the head and 1 it is the tail
     */
    int isTerminalStation(String stationName);

    /**
     * @param stationName
     * @return true if the line has that station, false if not
     */
    boolean haveStation(String stationName);
}
