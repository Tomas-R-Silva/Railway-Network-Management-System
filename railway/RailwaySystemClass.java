/* 
 * RailwaySystem Class
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;
import dataStructures.*;
import railway.exceptions.*;

public class RailwaySystemClass implements RailwaySystem {

    private Dictionary<String,Line> lines;
    private Dictionary<String,Station> stations;
    static final long serialVersionUID = 0l;

    public RailwaySystemClass(){
        this.lines=new SepChainHashTable<String,Line>();
        this.stations=new SepChainHashTable<String,Station>();
    }

    
    @Override
    public Line createLine(String lineName, String lineNameNoFormat) throws lineAlreadyExists{
        if(!lines.isEmpty() && lines.find(lineName)!=null){
            throw new lineAlreadyExists();
        }
        Line line = new LineClass(lineName,lineNameNoFormat);
        lines.insert(lineName, line);
        return line;
    }



    @Override
    public void removeLine(String lineName) throws LineDontExists{
        if(lines.find(lineName)==null){ //Se a linha nao existe lança exceçao
            throw new LineDontExists();
        }
        Line lineToRemove = lines.find(lineName); //Encontra a linha
        Iterator<Station> stationsIterator = lineToRemove.iteratorStations(); //Vai buscar as estaçoes da linha
        while(stationsIterator.hasNext()){
            Station station = stationsIterator.next();
            station.removeLine(lineName); //Remove a linha que foi removida das linhas da estaçao
            station.removeTrainsOfLine(lineName);
            if(!station.stationHasLines()){
                stations.remove(station.getName()); //Se era a única linha dessa estaçao, elimina a estaçao tambem
            }
        }
        lines.remove(lineName); //Remove a linha
    }



    @Override
    public void insertStation(String lineName,Line line, List<Entry<String,String>> tmp_stations){ //Também adiciona a linha à estacao
        Station stationToAdd;
        Iterator<Entry<String,String>> it= tmp_stations.iterator();
        while(it.hasNext()){
            Entry<String,String> stationNames = it.next();
            String stationName = stationNames.getKey();
            String noFormatStationName = stationNames.getValue();
            if(!stations.isEmpty() && stations.find(stationName)!=null){  // verifica se ja existe
                stationToAdd=stations.find(stationName); // encontra a estacao
            }
            else{ //senao existe cria a estaçao
                String firstName = getFirstNameStation(stationName);
                if(firstName!=null){
                    stationToAdd= new StationClass(stationName,firstName);
                }else{
                    stationToAdd= new StationClass(stationName, noFormatStationName);   
                }
                stations.insert(stationName, stationToAdd); // adiciona a estacao à colecao de estacao do railway
            }
            stationToAdd.addLine(lineName,line.getNameToIterate());         // adiciona a linha à estacao e a estacao à linha
            line.addStation(stationToAdd);
        }
    }

    private String getFirstNameStation( String stationName){
        Station st = stations.find(stationName);
        if(st != null){
            return st.getFirstName();
        }
        return null;
    }


    @Override
    public Iterator<Station> lineStations(String lineName) throws LineDontExists{
        if(lines.isEmpty() || lines.find(lineName)==null){ //Se a linha nao existe lança exceçao
            throw new LineDontExists();
        }
        return lines.find(lineName).iteratorStations();
    }

    @Override
    public Iterator<Entry<String,String>> stationLines (String stationName) throws NoStation{
        Station station = stations.find(stationName);
        if(station == null){
            throw new NoStation();
        }
        return station.getLinesOfSt();
    }


    @Override
    public void createSchedule(String lineName, int trainNumber){ // cria e adiciona à linha o comboio (schedule)
        Train train = new TrainClass(lineName, trainNumber);
        lines.find(lineName).addTrain(trainNumber, train);
    }


    @Override
    public void createStationWithTime(int trainNumber,String lineName,String stationName, String time)throws NoStation{ // numberTimes serve para só adicionar o comboio com o tempo de partida à binarytree da estacao
        Line line= lines.find(lineName);
        Train train= line.getTrain(trainNumber);
        train.addSchedule(time,stations.find(stationName),line);
        Station station = stations.find(stationName);
        if(station == null){
            throw new NoStation();
        }
        TrainNumLine trainNumWithLine = new TrainNumLine(lineName, trainNumber); //  precisa de ter a linha para depois se a linha for removida também poder remover isso
        station.addTrain(time,trainNumWithLine);
    }

    @Override
    public Station getStation(String stationName){
        return stations.find(stationName);
    }

    private void removeTrainFromStation(Train train, Iterator<Entry<String,Station>> itsSs){
        while(itsSs.hasNext()){
            Entry<String,Station> tmp= itsSs.next();
            Station station = tmp.getValue();
            station.removeTrain(tmp.getKey(), train.getTrainID());
        }
    }

    @Override
    public void removeSchedule(String lineName, String startStation, String time) throws LineDontExists, NoScheduleExist{
        boolean removed=false;
        if(lines.find(lineName)==null){ //Se a linha nao existe lança exceçao
            throw new LineDontExists();
        }
        Line line = lines.find(lineName);
        Iterator<Entry<Integer,Train>> itT = line.iteratorTrains();
        while(itT.hasNext()){
            Train train = itT.next().getValue();
            Iterator<Entry<String,Station>> itS = train.listStationsOfTrain();
            Iterator<Entry<String,Station>> itSS = train.listStationsOfTrain(); // para poder passar ao método de remocão inalterada
            Entry<String,Station> tmp= itS.next();
            if(tmp.getKey().equals(time) && tmp.getValue().getName().equals(startStation)){
                line.removeTrain(train.getTrainID());
                removeTrainFromStation(train, itSS);
                removed=true;
            }
        }
        if(!removed){
            throw new NoScheduleExist();
        }
    }

    
    @Override
    public void validateSchedule(String lineName, String startStation, InvertibleQueue<String> stationsToCheck,List<String> times) throws LineDontExists{
        if(lines.find(lineName)==null){ //Se a linha nao existe lança exceçao
            throw new LineDontExists();
        }
        validateStationOrderInv(lineName,startStation,stationsToCheck);
        validateTimeOrder(times);
    }


    private void validateTimeOrder(List<String> times) throws InvalidSchedule{
        int counter=0;
        while(counter<times.size()-1 && times.get(counter).compareTo(times.get(counter+1))<0){
            counter++;
        }
        if(counter!=times.size()-1){
            throw new InvalidSchedule();
        }
    }


    private void validateStationOrderInv(String lineName,String startStation ,InvertibleQueue<String> stationsToCheck){
        Line line= lines.find(lineName);
        Iterator<Station> lineStationsIterator = line.iteratorStations();
        int terminalStation=line.isTerminalStation(startStation);
        if(terminalStation==1){ //se a primeira estação é a ultima estacao terminal inverte senao ja esta pela mesma  ordem que a lista de estacoes da linha
            stationsToCheck.invert();
        }
        if(terminalStation==-1){
            throw new InvalidSchedule();  
        }
        checkStationsOrder(lineStationsIterator, stationsToCheck);
        
    }


    private void checkStationsOrder(Iterator<Station> lineStationsIterator, InvertibleQueue<String> stationsToCheck){
        boolean found=false;

        while(!stationsToCheck.isEmpty()){
            found=false;
            String stationName= stationsToCheck.dequeue();
            while(lineStationsIterator.hasNext()){
                String lineStation=lineStationsIterator.next().getName();
                if(stationName.equals(lineStation)){
                    found=true;
                    break;
                }
            }
            if(!found){
                throw new InvalidSchedule();
            }
        }
    }

    @Override
    public void validateOvertake(String lineName, List<String> stationNames, List<String> stationTimes) throws InvalidSchedule{
        Line line= lines.find(lineName);
        int dif = 0; //new - exist = dif, if not is invalid

        Iterator<Entry<Integer,Train>> itT = line.iteratorTrains();
        
        while(itT.hasNext()){
            Train train = itT.next().getValue();

            if(train.getStartStation().getValue().getName().compareTo(stationNames.getFirst())!=0) continue; //se os horários nao estao na msm direção nao precisa verificar nada

            Iterator<Entry<String,Station>> itS = train.listStationsOfTrain();

            if(train.getStartStation().getKey().compareTo(stationTimes.getFirst())==0){ //if start at the same time
                throw new InvalidSchedule();
            }else{
                dif=(stationTimes.getFirst().compareTo(train.getStartStation().getKey())<0 ? -1 : 1);
            }


            while(itS.hasNext()){
                Entry<String,Station> schedule = itS.next();
                
                for(int i=0;i<stationNames.size();i++){
                    if(stationNames.get(i).equals(schedule.getValue().getName())){  
                        if(stationTimes.get(i).compareTo(schedule.getKey())*dif<=0){ //Se dif=1, ent é invalid0 se x<0, se dif=-1, ent é invalido se x>0 = -x<0 = x*dif<0
                            throw new InvalidSchedule();
                        }
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public Iterator<Train> sameStartTrainsList(String lineName,String startingStation){
        if(lines.find(lineName)==null){ //Se a linha nao existe lança exceçao
            throw new LineDontExists();
        }
        Line line= lines.find(lineName);
        if(line.isTerminalStation(startingStation)==-1){
            throw new NotATerminalStation();// não é uma estaçao terminal da linha
        }
        List<Train> trains= new DoubleList<>();
        Iterator<Entry<Integer,Train>> trainsOfLine= line.iteratorTrains();
    
        while(trainsOfLine.hasNext()){
            Train tmp = trainsOfLine.next().getValue();
            String station= tmp.getStartStation().getValue().getName();
            if(station.equals(startingStation)){
                trains.add(trains.size(), tmp);
            }
        }

        return orderSchedulesDepartureTime(trains);
    }


    private Iterator<Train> orderSchedulesDepartureTime(List<Train> trains){
        List<Train> sortedTrainsbyTime = new DoubleList<>();
        Train[] arrayTrains= new Train[trains.size()];

        for(int i=0;i<trains.size();i++){
            arrayTrains[i]=trains.get(i);
        }
        for(int i=0;i<arrayTrains.length-1;i++){
            for(int j=i+1;j<arrayTrains.length;j++){
                if(arrayTrains[j].getStartStation().getKey().compareTo(arrayTrains[i].getStartStation().getKey())<0){
                    Train tmp= arrayTrains[i];
                    arrayTrains[i]= arrayTrains[j];
                    arrayTrains[j]= tmp; 
                }
            }
        }

        for(int i=0;i<arrayTrains.length;i++){
            sortedTrainsbyTime.add(sortedTrainsbyTime.size(), arrayTrains[i]);
        }
        return sortedTrainsbyTime.iterator();
    }
    

    @Override
    public Entry<Integer,Iterator<Entry<String, Station>>> bestSchedule(String lineName, String startStation, String endStation, String etaString) throws LineDontExists,StartingStationDoesNotExist,ImpossibleWasException{
        if(lines.find(lineName)==null){
            throw new LineDontExists();
        }
        Line line = lines.find(lineName);
        if(stations.find(startStation)==null || !line.haveStation(startStation)){
            throw new StartingStationDoesNotExist();
        }
        
        Iterator<Train> possibleTrains = sameStartTrainsListForBestSchedule(lineName,startStation, endStation);
        if(!possibleTrains.hasNext()){
            throw new ImpossibleWasException();
        }
        Train best = null;
        
        best = bestTrainForSchedule(startStation, etaString, endStation, possibleTrains);
        if(best == null){
            throw new ImpossibleWasException();
        }

        Entry<Integer,Iterator<Entry<String, Station>>> idAndStations = new EntryClass<>(best.getTrainID(),best.listStationsOfTrain());
        return idAndStations;
    }

    private Iterator<Train> sameStartTrainsListForBestSchedule(String lineName,String startingStation,String endStation){ // porque o best schedule pode não ter a estacao terminal como inicial a ser dada, alterar depois
        Line line= lines.find(lineName);
        List<Train> trains= new DoubleList<>();
        Iterator<Entry<Integer,Train>> trainsOfLine= line.iteratorTrains();

        while(trainsOfLine.hasNext()){
            Train tmp = trainsOfLine.next().getValue();
            Iterator<Entry<String,Station>> it= tmp.listStationsOfTrain();
            boolean startingFound = false;
            while(it.hasNext()){
                Entry<String,Station> st= it.next();
                if(st.getValue().getName().equals(startingStation)){
                    startingFound= true;
                }
                if(startingFound && st.getValue().getName().equals(endStation)){
                    trains.add(trains.size(),tmp);
                }
            }
        }
        return orderSchedulesDepartureTime(trains);
    }

    private Train bestTrainForSchedule( String startStation,String expectedHour,String endStation, Iterator<Train> possibleTrains){
        Train bestTrain = null;
        String bestTime = null;

        while(possibleTrains.hasNext()){
            Train train = possibleTrains.next();
                Station station = train.getStation(endStation);
                String timeKey = train.getKeyForStation(station);
                    if(station!=null){
                        if(timeKey.compareTo(expectedHour)<=0){
                            if(bestTime == null || timeKey.compareTo(bestTime) > 0){
                                bestTrain = train;
                                bestTime = timeKey;
                            }
                        }
                    }
             }
        return bestTrain;
    }

    @Override
    public boolean isTrainInOrder(String lineName,int trainID, String startStation, String endStation){ // retorna true se estiver ordenada da head para a tail e falso se estiver ordenanda da tail para head
        Station start = stations.find(startStation);
        Station end = stations.find(endStation);
        Line line =lines.find(lineName);
        Train train= line.getTrain(trainID);
        boolean foundStart = false;
        Iterator<Entry<String,Station>> it = train.listStationsOfTrain();

        while (it.hasNext()) {
            Station currentStation = it.next().getValue();
    
            if (currentStation.equals(end) && !foundStart) {
                train.doesItStartInHead(false);
                return false;
            }
            if (currentStation.equals(start)) {
                foundStart = true;
            }
            if (currentStation.equals(end) && foundStart) {
                train.doesItStartInHead(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Entry<String,List<TrainNumLine>>> trainsByStation(String stationName){
        Station station = stations.find(stationName);
        if(station == null){
            throw new NoStation();
        }
        return station.getTrainsST();
    }
    
}
