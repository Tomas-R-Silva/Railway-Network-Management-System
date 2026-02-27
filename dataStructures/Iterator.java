/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */
package dataStructures;

import java.io.Serializable;
import railway.exceptions.*;

/**
 * Iterator Abstract Data Type 
 * Includes description of general methods for one way iterator.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 * 
 */
public interface Iterator<E> extends Serializable
{

    /** 
     * Returns true if next would return an element 
     * rather than throwing an exception.
     * @return true iff the iteration has more elements
     */
	boolean hasNext( );

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    E next( ) throws NoSuchElementException;

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty,
     * next will return the first element in the iteration.
     */
    void rewind( );

}
