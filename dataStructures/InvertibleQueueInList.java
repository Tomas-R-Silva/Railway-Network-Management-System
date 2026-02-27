/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */
package dataStructures;

import railway.exceptions.*;

public class InvertibleQueueInList<E> extends QueueInList<E> implements InvertibleQueue<E>{

    private boolean inverted;

    public InvertibleQueueInList(){
        super();
        inverted=false;
    }

    @Override
    public void enqueue( E element )
    {
        if(inverted){
            list.addFirst(element);
        }else {
            list.addLast(element);
        }
    }

    @Override
    public E dequeue(){
        if(inverted) {
            if (list.isEmpty())
                throw new EmptyQueueException();

            return list.removeLast();
        }else{
            if (list.isEmpty())
                throw new EmptyQueueException();

            return list.removeFirst();
        }
    }

    public boolean invert(){
        inverted=!inverted;
        return inverted;
    }
}
