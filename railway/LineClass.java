/* 
 * Line Class
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;
import dataStructures.*;

public class LineClass implements Line{
    
    String lineName,lineNameNoFormat;  // repetição do nome da linha, uma para pesquisa e outra para iterações
    private List<Station> stations;
    private OrderedDictionary<Integer,Train> trains;
    static final long serialVersionUID = 0l;
    


    public LineClass(String lineName,String lineNameNoFormat){
        this.lineName=lineName;
        this.lineNameNoFormat=lineNameNoFormat;
        this.stations = new DoubleList<Station>();
        this.trains=new BinarySearchTree<Integer,Train>();
    }

    @Override
    public String getName(){
        return lineName;
    }

    @Override
    public String getNameToIterate(){
        return lineNameNoFormat;
    }

    @Override
    public void addStation(Station station){
        stations.add(stations.size(),station);
    }

    @Override
    public Iterator<Station> iteratorStations(){
        return stations.iterator();
    }

    @Override
    public Iterator<Entry<Integer,Train>> iteratorTrains(){
        return trains.iterator();
    }

    @Override
    public void addTrain(int trainID, Train train){
        trains.insert(trainID,train);
    }

    @Override
    public void removeTrain(int trainID){
        trains.remove(trainID);
    }

    @Override
    public Train getTrain(int trainID){
       return trains.find(trainID);
    }

    @Override
    public int isTerminalStation(String stationName){
        if(stations.getFirst().getName().equals(stationName) || stations.getLast().getName().equals(stationName)){
            if(stations.getFirst().getName().equals(stationName)){
                return 0;
            }else{
                return 1;
            }
        }
        return -1;
    }


    @Override
    public boolean haveStation(String stationName){
        boolean found=false;
        int counter=0;
        while(counter<stations.size() && !found){
            if(stations.get(counter).getName().equals(stationName)){
                found=true;
            }
            counter++;
        }
        return found;
    }
    
}
