/* 
 * TrainNumLine Interface
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package railway;

import java.io.Serializable;

public interface TrainNumLineITF extends Serializable{

    /** Getter for the trainID
     * @return trainID
     */
    int getTrainNumber();

    /** Getter for the lineName
     * @return lineName
     */
    String getLineName();
}
