/* 
 * TrainNumLine Class
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;

public class TrainNumLine implements TrainNumLineITF{ // só dois comboios podem passar à mesma hora na estacao, um num sentido e o outro no outro

    int trainNumber;
    String lineName;
    static final long serialVersionUID = 0l;

    public TrainNumLine(String lineName, int trainNumber){
        this.lineName=lineName;
        this.trainNumber=trainNumber;
    }

    @Override
    public int getTrainNumber(){
        return trainNumber;
    }

    @Override
    public String getLineName(){
        return lineName;
    }
}
