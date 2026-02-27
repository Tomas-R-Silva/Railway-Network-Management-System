/* 
 * Main Class
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

import java.util.Scanner;
import railway.*;
import railway.exceptions.*;
import dataStructures.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class Main {

    private enum Command{
        IL,RL,CL,CE,IH,RH,CH,LC,MH,TA,UNKNOWN
    }

    private static final String IL_SUCESS = "Inserção de linha com sucesso.\n";
    private static final String IL_INSUCESS = "Linha existente.\n";
    private static final String RL_SUCESS = "Remoção de linha com sucesso.\n";
    private static final String NO_LINE = "Linha inexistente.\n";
    private static final String NO_STATION = "Estação inexistente.\n";
    private static final String IH_SUCESS = "Criação de horário com sucesso.\n";
    private static final String BAD_SCHEDULE = "Horário inválido.\n";
    private static final String RH_SUCESS = "Remoção de horário com sucesso.\n";
    private static final String NO_SCHEDULE = "Horário inexistente.\n";
    private static final String NO_START_STATION = "Estação de partida inexistente.\n";
    private static final String IMPOSSIBLE_WAY = "Percurso impossível.\n";
    private static final String APP_END = "Aplicação terminada.";
    private static final String TRAIN_STATION ="Comboio %d %s\n";
    private static final String ERROR = "Unknown command %s.\n";

    private static final String DATA_FILE = "storedrs.dat";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
		RailwaySystem rs = load();

		Command cmd;
    
        do{
            String input=in.next().trim();
            cmd = getCommand(in,input);
            switch (cmd) {
                case IL ->{
                    insertLine(rs,in);
                }
                case RL ->{
                    removeLine(rs,in);
                }
                case CL ->{
                    lineStations(rs,in);
                }
                case CE ->{
                    stationLines(rs,in);
                }
                case IH ->{
                    insertSchedule(rs,in);
                }
                case RH ->{
                    removeSchedule(rs,in);
                }
                case CH ->{
                    lineSchedules(rs,in);
                }
                case LC ->{
                    trainsByStation(rs,in);
                }
                case MH ->{
                    bestSchedule(rs,in);
                }
                case TA ->{
                    save(rs);
                    System.out.println(APP_END);
                }
                default->cmd_Error(input);
            }
        }while (!cmd.equals(cmd.TA));

        in.close();

    }


    private static RailwaySystem load(){
        RailwaySystem rs=null;
        try{
            ObjectInputStream file = new ObjectInputStream(new FileInputStream(DATA_FILE));
            rs = (RailwaySystem) file.readObject();
            file.close();
        }
        catch ( IOException e ){
			return new RailwaySystemClass();
        }
        catch ( ClassNotFoundException e ){
			return new RailwaySystemClass();
        }
        return rs;
    }

    private static void save(RailwaySystem rs){
        try{
            ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
            file.writeObject(rs);
            file.close();

        }catch(IOException e){
            // falha
        }
    }
    

    private static Command getCommand(Scanner in, String input) {
        try{
            return Command.valueOf(input.toUpperCase().trim());
        }
        catch (IllegalArgumentException e){
            return Command.UNKNOWN;
        }
    }


    private static void cmd_Error(String cmd){
        System.out.printf(ERROR,cmd.toUpperCase());
    }


    private static void insertLine(RailwaySystem rs, Scanner in) {
        try {
            String lineName = in.nextLine().trim();
    
            String stationName;
            List<Entry<String,String>> tmp_stations= new DoubleList<>();
            while (!(stationName = in.nextLine().trim()).isEmpty()) {  // Stop if input is empty (user pressed enter)
                Entry<String,String> station = new EntryClass<String,String>(stationName.toUpperCase(),stationName);
                tmp_stations.add(tmp_stations.size(), station);
            }
            Line line = rs.createLine(lineName.toUpperCase(),lineName);
            rs.insertStation(lineName,line, tmp_stations);

            System.out.printf(IL_SUCESS);
        } catch (lineAlreadyExists exception) {
            System.out.printf(IL_INSUCESS);
        }
    }


    private static void removeLine(RailwaySystem rs, Scanner in){
        try {
            String lineName = in.nextLine().trim().toUpperCase();

            rs.removeLine(lineName);
            System.out.printf(RL_SUCESS);
        } catch (LineDontExists exception){
            System.out.printf(NO_LINE);
        }
    }


    private static void lineStations(RailwaySystem rs, Scanner in){
        try {
            String lineName = in.nextLine().trim().toUpperCase();
            Iterator<Station> stationsIterator = rs.lineStations(lineName);
            while(stationsIterator.hasNext()){
                System.out.println(stationsIterator.next().getFirstName());
            }
        } catch (LineDontExists exception){
            System.out.printf(NO_LINE);
        }
    }


    private static void stationLines(RailwaySystem rs, Scanner in){
    try{
        String stationName = in.nextLine().trim().toUpperCase();
        Iterator<Entry<String,String>> lines = rs.stationLines(stationName);
        
        while(lines.hasNext()){
            String lineName = lines.next().getValue();
            System.out.printf("%s\n",lineName);
        }
    }catch(NoStation exception){
        System.out.printf(NO_STATION);
    }
}


    private static void insertSchedule(RailwaySystem rs, Scanner in){
        try{
            String lineName = in.nextLine().trim().toUpperCase();
        
            int trainNumber= in.nextInt();
            in.nextLine();

            List<String> stationNames = new DoubleList<>();
            List<String> stationTimes = new DoubleList<>();
            InvertibleQueue<String> stationNamesInv = new InvertibleQueueInList<>();
            String stationNameAndTime;
            String stationName="";
            String startStation=null;
            String endStation = null;
            String[] splitInput= new String[2];
            String time;
            int index=0;


            while(!(stationNameAndTime = in.nextLine().trim()).isEmpty()){
                splitInput = stationNameAndTime.split(" ");
                for(int j=0; j<splitInput.length-1;j++){
                    stationName=(stationName+" "+splitInput[j]).trim();
                }
                time=splitInput[splitInput.length-1];

                if(index==0){
                    startStation=stationName.trim().toUpperCase();
                }
                endStation = stationName.trim().toUpperCase();

                stationNamesInv.enqueue(stationName.toUpperCase());
                stationNames.add(index, stationName.toUpperCase());
                stationTimes.add(index,time);
                stationName="";
                index++;
            } 

            rs.validateSchedule(lineName,startStation,stationNamesInv,stationTimes);
            rs.validateOvertake(lineName, stationNames, stationTimes);
            rs.createSchedule(lineName, trainNumber);
            //mudar for para iteradores

            Iterator<String> itN = stationNames.iterator();
            Iterator<String> itT = stationTimes.iterator();

            while(itN.hasNext() && itT.hasNext()){
                rs.createStationWithTime(trainNumber, lineName, itN.next(), itT.next());
            }

            rs.isTrainInOrder(lineName,trainNumber, startStation, endStation);
                System.out.printf(IH_SUCESS);
            }catch(LineDontExists exception){
                System.out.printf(NO_LINE);
            }catch(InvalidSchedule exception){ 
                System.out.printf(BAD_SCHEDULE);
            }

    }    


    private static void removeSchedule(RailwaySystem rs, Scanner in){
        String lineName= in.nextLine().trim().toUpperCase();
        String stationNameAndTime = in.nextLine().trim();
        
        String startStation ="";
        String[] splitInput=new String[2];

        splitInput = stationNameAndTime.split(" ");
        for(int j=0; j<splitInput.length-1;j++){
            startStation=(startStation+" "+splitInput[j]).trim();
        }

        String time=splitInput[splitInput.length-1];

        try{
            rs.removeSchedule(lineName,startStation.toUpperCase(),time);
            System.out.printf(RH_SUCESS);
        }catch(LineDontExists exception){
            System.out.printf(NO_LINE);
        }catch(NoScheduleExist exception){
            System.out.printf(NO_SCHEDULE);
        }
    }


    private static void lineSchedules(RailwaySystem rs, Scanner in){
        try {
            String lineName= in.nextLine().trim().toUpperCase();
            String startingStation=in.nextLine().trim().toUpperCase();
            Iterator<Train> stationsSchedule= rs.sameStartTrainsList(lineName,startingStation);
                while(stationsSchedule.hasNext()){
                    Train tmp= stationsSchedule.next();
                    System.out.printf("%d\n",tmp.getTrainID());
                    Iterator<Entry<String,Station>> it= tmp.listStationsOfTrain();
                        while(it.hasNext()){
                            Entry<String,Station> st= it.next();
                            System.out.printf("%s %s\n",st.getValue().getFirstName(),st.getKey());
                        }
                } 
        } catch (LineDontExists exception) {
            System.out.printf(NO_LINE);
        } catch(NotATerminalStation exception){
            System.out.printf(NO_START_STATION);
        }
    }


    private static void trainsByStation(RailwaySystem rs, Scanner in){
        try{
            String stationName = in.nextLine().trim().toUpperCase();
            Iterator<Entry<String,List<TrainNumLine>>> it = rs.trainsByStation(stationName);
            
            while(it.hasNext()){
                Entry<String, List<TrainNumLine>> entry = it.next();
            
                String trainStartingTime = entry.getKey();
                List<TrainNumLine> trainNumbers = entry.getValue();

                Iterator<TrainNumLine> trainIterator = trainNumbers.iterator();

                while (trainIterator.hasNext()) {
                    int trainNumber = trainIterator.next().getTrainNumber();  
                    System.out.printf(TRAIN_STATION, trainNumber, trainStartingTime);
                }
            
            }
        }catch(NoStation exception){
            System.out.printf(NO_STATION);
        }
    }


    private static void bestSchedule(RailwaySystem rs, Scanner in){
        String lineName = in.nextLine().toUpperCase().trim();
        String startStation = in.nextLine().toUpperCase().trim();
        String endStation = in.nextLine().toUpperCase().trim();
        String eta = in.nextLine();
        try{
            Entry<Integer,Iterator<Entry<String,Station>>> entry = rs.bestSchedule(lineName, startStation, endStation, eta);
            int trainID = entry.getKey();
            Iterator<Entry<String,Station>> it = entry.getValue();
            System.out.printf("%d\n",trainID); //train number
            while(it.hasNext()){
                Entry<String,Station> train = it.next();
                System.out.printf("%s %s\n",train.getValue().getFirstName(),train.getKey());
            }
        }catch(LineDontExists exception){
            System.out.printf(NO_LINE);
        }catch(StartingStationDoesNotExist exception){
            System.out.printf(NO_START_STATION);
        }catch(ImpossibleWasException exception){
            System.out.printf(IMPOSSIBLE_WAY);
        }
    }

}
