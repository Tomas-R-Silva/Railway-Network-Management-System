/* 
 * Station Class
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

 package railway;
 import dataStructures.*;

public class StationClass implements Station{

    private String stationName;
    private String firstName; // repetição do nome da estaçao, uma para pesquisa e outra para iterações
    private OrderedDictionary<String,String> lines;
    private OrderedDictionary<String,List<TrainNumLine>> trains;
    static final long serialVersionUID = 0l;

    public StationClass(String stationName, String firstName){
        this.stationName = stationName;
        this.lines= new BinarySearchTree<>();
        this.trains= new BinarySearchTree<>();
        this.firstName= firstName;
    }

    @Override
    public String getName(){
        return stationName;
    }

    @Override
    public String getFirstName(){
        return firstName;
    }

    @Override
    public void addLine(String lineName,String lineNoFormat){
        lines.insert(lineName.toUpperCase(),lineNoFormat);
    }

    @Override
    public void addTrain(String time, TrainNumLine train) {
        List<TrainNumLine> trainList = trains.find(time);

        if (trainList == null) {
            trainList = new DoubleList<>();
            trains.insert(time,trainList);
        }
        //if(!trainList.contains(trainNumber)){
        trainList.add(trainList.size(),train);
       // }
        sortTrainList(trainList);
    }

    private void sortTrainList(List<TrainNumLine> trainList){
        if(trainList.size()==1){
            // não é preciso ordenar
        }
        else{  // só podem existir dois comboios com o mesmo tempo um num sentido e o outro no outro
           for(int i=0;i<trainList.size()-1;i++){
                for(int j=i+1;j<trainList.size();j++){
                    TrainNumLine train1 = trainList.get(i);
                    TrainNumLine train2 = trainList.get(j);

                    if(train1.getTrainNumber()> train2.getTrainNumber()){
                        trainList.remove(j);
                        trainList.remove(i);

                        trainList.add(i, train2);
                        trainList.add(j,train1);
                    }
                }
           }
        }
    }

    @Override
    public void removeLine(String lineName){
        lines.remove(lineName);
    }

    @Override
    public boolean stationHasLines(){
        return !lines.isEmpty();
    }

    @Override
    public Iterator<Entry<String,String>> getLinesOfSt(){
        return lines.iterator();
    }

    @Override
    public Iterator<Entry<String,List<TrainNumLine>>> getTrainsST(){
        return trains.iterator();
    }

    @Override
    public void removeTrain(String time, int trainNumber) {
        List<TrainNumLine> trainList = trains.find(time);

        if (trainList != null) {
            for(int i=0; i<trainList.size();i++){
                if(trainList.get(i).getTrainNumber()== trainNumber){
                    trainList.remove(i);
                    break;
                }
            }

            if (trainList.isEmpty()) {
                trains.remove(time);
            }
        }
    }

    @Override
    public void removeTrainsOfLine( String lineName){
        Iterator<Entry<String,List<TrainNumLine>>> tri = trains.iterator();
        while(tri.hasNext()){
            List<TrainNumLine> listTrains = tri.next().getValue();
            for(int i=0; i< listTrains.size();i++){
                if(listTrains.get(i).getLineName().equals(lineName)){
                    listTrains.remove(i);
                    i--;
                }
            }
        }
    }
}

