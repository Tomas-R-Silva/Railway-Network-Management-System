/* 
 * Train Class
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;
import dataStructures.*;

public class TrainClass extends TrainNumLine implements Train{

    private OrderedDictionary<String,Station> schedule;
    private boolean doestItStartInHead;
    static final long serialVersionUID = 0l;

    public TrainClass(String lineName, int trainID){
        super(lineName,trainID);
        schedule=new BinarySearchTree<String,Station>();
    }

    @Override
    public int getTrainID(){
        return trainNumber;
    }

    @Override
    public String getLine(){
        return lineName;
    }

    @Override
    public void addSchedule(String time, Station station, Line line){
        schedule.insert(time, station);
    }

    @Override
    public void doesItStartInHead(boolean answer){
        doestItStartInHead=answer;
    }

    @Override
    public boolean getWayOfTrain(){
        return doestItStartInHead;
    }

    @Override
    public Entry<String,Station> getStartStation(){
        return schedule.minEntry();
    }

    @Override
    public Entry<String,Station> getLastStation(){
        return schedule.maxEntry();
    }

    @Override
    public Station getStation(String stationToFind){
        Iterator<Entry<String,Station>> it = schedule.iterator();
        while(it.hasNext()){
            Station station = it.next().getValue();
            if(station.getName().equals(stationToFind)){
                return station;
            }
        }
        return null;
    }

    @Override
    public String getKeyForStation(Station stationToFind) {
        Iterator<Entry<String, Station>> it = schedule.iterator();
        while (it.hasNext()) {
            Entry<String, Station> entry = it.next();
            Station station = entry.getValue();
            if (station.equals(stationToFind)) { 
                return entry.getKey();
            }
        }
        return null; 
    }

    @Override
    public Iterator<Entry<String,Station>> listStationsOfTrain(){
        return schedule.iterator();
    }

}
