/* 
 * Train Interface
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;
import java.io.Serializable;

import dataStructures.*;

public interface Train extends Serializable{

    /** Getter for the train ID
     * @return Train ID
     */
    int getTrainID();

    /** Getter for the line associated with the train
     * @return Name of that line
     */
    String getLine();

    /** Add a station and his time to the schedule
     * @param time
     * @param station
     */
    void addSchedule(String time, Station station, Line line);

    /** Getter for the first station of this train
     * @return <time the line starts,start station>
     */
    Entry<String,Station> getStartStation();

    /** Getter for the last station of this train
     * @return <time the line ends,last station>
     */
    Entry<String,Station> getLastStation();

    /** Getter for the iterator for the stations of a train
     * @return iterator for the stations of a train
     */
    Iterator<Entry<String,Station>> listStationsOfTrain(); 

    /** Getter for the station
     * @param stationName
     * @return station with that given name
     */
    Station getStation(String stationName);

    /** Getter for the key of a station
     * @param stationToFind
     * @return the key(string) of the station that is given
     */
    String getKeyForStation(Station stationToFind);

    /** sets the variable of the train that says if the trains stars in head or in tail
     * @param answer
     */
    void doesItStartInHead(boolean answer);

    /** Gives the way of the train
     * @return Returns true if a train starts in head, or false if a train starts in last
     */
    boolean getWayOfTrain();
}
