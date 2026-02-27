/* 
 * RailwaySystem Interface
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;
import java.io.Serializable;

import dataStructures.*;
import railway.exceptions.InvalidSchedule;
import railway.exceptions.NoStation;

public interface RailwaySystem  extends Serializable     {

    /** Insert a staion on the given line
     * @param line
     * @param stationName
     */
    void insertStation(String lineName,Line line, List<Entry<String,String>> tmp_stations);


     
    /** Create a line with the given lineName (UPPERCASE) and lineNameNoFormat
     * @param lineName
     * @param lineNameNoFormat
     * @return the created line
     */
    Line createLine(String lineName, String lineNameNoFormat);


    /** Removes a line, throwa exception if line dont exists
     * @param lineName
     */
    void removeLine(String lineName);



    /** List with all stations of a line
     * @param lineName
     * @return Iterator of the stations of the give line
     */
    Iterator<Station> lineStations(String lineName);

    Iterator<Entry<String,String>> stationLines (String stationName) throws NoStation;

    /** creates schedule and adds it to the right line
     * @param lineName
     * @param trainNumber
     * @pre lineExists
     */
    void createSchedule(String lineName, int trainNumber);



    /** creates the object train with stations with its times associeted and adds it to the schedule of the train
     * @param stationName
     * @param time
     * @param lineName
     * @param trainNumber
     * @pre lineExists
     */
    void createStationWithTime(int trainNumber,String lineName,String stationName, String time);

    /** Remove a schedule if it exists
     * @param lineName
     * @param startStation
     * @param time
     */
    void removeSchedule(String lineName, String startStation, String time);


    /** list all the trains that have the same start station
     * @param lineName
     * @param startingStation
     * @return iterator with all the trains that have the same start station
     */
    Iterator<Train> sameStartTrainsList(String lineName,String startingStation);

    /** Get an entry with the best train id (key) and the list of its schedule (value)
     * @param lineName
     * @param startStation
     * @param endStation
     * @param eta
     * @return entry with the best train id (key) and the list of its schedule (value)
     */
    Entry<Integer,Iterator<Entry<String, Station>>> bestSchedule(String lineName, String startStation, String endStation, String eta);

    /** Checks if a given list of schedules (stationsToCheck) as the same order as the line
     * @param lineName
     * @param startStation
     * @param stationsToCheck
     * @param times
     */
    void validateSchedule(String lineName, String startStation, InvertibleQueue<String> stationsToCheck,List<String> times);

    /** Checks the way of a train
     * @param lineName
     * @param trainID
     * @param startStation
     * @param endStation
     * @return true if train schedule is ordered from head to tail, and false if ordered from tail to head
     */
    boolean isTrainInOrder(String lineName,int trainID, String startStation, String endStation);

    /** Gives all of the trains that pass in the given station
     * @param stationName
     * @return returns an iterator with all of the trains that pass in the station given as argument
     */
    Iterator<Entry<String,List<TrainNumLine>>> trainsByStation(String stationName);

    /** finds and returns the station with the name given as argument
     * @param stationName
     * @return returns a station object
     */
    Station getStation(String stationName);

    /** Checks if by adding a schedule that schedule overtakes other
     */
    void validateOvertake(String lineName, List<String> stationNames, List<String> stationTimes) throws InvalidSchedule;

}
