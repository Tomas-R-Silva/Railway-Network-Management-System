/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */
package dataStructures;

import java.io.Serializable;

import org.w3c.dom.Node;

import railway.exceptions.*;


/**
 * Doubly linked list Implementation 
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 * 
 */
public class DoubleList<E> implements List<E>
{   

	/**
	 * Serial Version UID of the Class
	 */
    static final long serialVersionUID = 0L;
    
    /**
     * Double List Node Implementation 
     * @author AED  Team
     * @version 1.0
     * @param <E> Generic Element
     * 
     */
    
    /**
     *  Node at the head of the list.
     */
    protected DoubleListNode<E> head;

    /**
     * Node at the tail of the list.
     */
    protected DoubleListNode<E> tail;

    /**
     * Number of elements in the list.
     */
    protected int currentSize;

    /**
     * Constructor of an empty double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public DoubleList( )
    {
        head = null;
        tail = null;
        currentSize = 0;
    }


    @Override
    public boolean isEmpty( )
    {  
        return currentSize == 0;
    }


    @Override
    public int size( )
    {
        return currentSize;
    }


    @Override
    public Iterator<E> iterator( )
    {
        return new DoubleListIterator<E>(head, tail);
    }


    @Override
    public E getFirst( ) throws EmptyListException
    {  
        if ( this.isEmpty() )
            throw new EmptyListException();

        return head.getElement();
    }


    @Override
    public E getLast( ) throws EmptyListException
    {  
    	if(isEmpty()){
            throw new EmptyListException();
        }
        return tail.getElement();
    }


    /**
     * Returns the node at the specified position in the list.
     * Pre-condition: position ranges from 0 to currentSize-1.
     * @param position - position of list element to be returned
     * @return DoubleListNode<E> at position
     */
    protected DoubleListNode<E> getNode( int position ) 
    {
        DoubleListNode<E> node;

        if ( position <= ( currentSize - 1 ) / 2 )
        {
            node = head;
            for ( int i = 0; i < position; i++ )
                node = node.getNext();
        }
        else
        {
            node = tail;
            for ( int i = currentSize - 1; i > position; i-- )
                node = node.getPrevious();

        }
        return node;
    }


    @Override    
    public E get( int position ) throws InvalidPositionException
    {
        if(position<0 || position>=currentSize){
            throw new InvalidPositionException();
        }
        return getNode(position).getElement();
    }


    @Override
    public int find( E element )
    {
        DoubleListNode<E> node = head;
        int position = 0;
        while ( node != null && !node.getElement().equals(element) )
        {
            node = node.getNext();
            position++;
        }
        if ( node == null )
            return -1;
        else
            return position;
    }


    @Override
    public void addFirst( E element )
    {
        DoubleListNode<E> newNode = new DoubleListNode<E>(element, null, head);
        if ( this.isEmpty() )
            tail = newNode;
        else
            head.setPrevious(newNode);
        head = newNode;
        currentSize++;
    }


    @Override
    public void addLast( E element )
    {
        DoubleListNode<E> newNode = new DoubleListNode<E>(element,tail,null);
        if(isEmpty()){
            head=newNode;
            tail=newNode;
        }
        else{
            tail.setNext(newNode);
            tail=newNode;
        }
        currentSize++;
    }


    /**
     * Inserts the specified element at the specified position in the list.
     * Pre-condition: position ranges from 1 to currentSize-1.
     * @param position - middle position for insertion of element
     * @param element - element to be inserted at middle position
     */
    protected void addMiddle( int position, E element )
    {
        DoubleListNode<E> prevNode = this.getNode(position - 1);
        DoubleListNode<E> nextNode = prevNode.getNext();
        DoubleListNode<E> newNode = new DoubleListNode<E>(element, prevNode, nextNode);
        
        prevNode.setNext(newNode);
        nextNode.setPrevious(newNode);

        currentSize++;
    }


    @Override
    public void add( int position, E element ) throws InvalidPositionException
    {
        if ( position < 0 || position > currentSize )
            throw new InvalidPositionException();

        if ( position == 0 )
            this.addFirst(element);
        else if ( position == currentSize )
            this.addLast(element);
        else
            this.addMiddle(position, element);
    }


    /**
     * Removes the first node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeFirstNode( )
    {
        head = head.getNext();
        if ( head == null )
            tail = null;
        else
            head.setPrevious(null);
        currentSize--;
    }


    @Override
    public E removeFirst( ) throws EmptyListException
    {
        if(isEmpty()){
            throw new EmptyListException();
        }
        if(head == null){
            return null;
        }
        E element= head.getElement();

        if(head == tail){
            head=null;
            tail=null;
        }
        else{
        head= head.getNext();
        head.setPrevious(null);
        }
        currentSize--;
        return element;
    }


    /**
     * Removes the last node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeLastNode( )
    {
    	if(isEmpty()){
            throw new EmptyListException();
        }
        if(currentSize==1){
            tail=null;
            head=null;
        }
        else{
            tail=tail.getPrevious();
            tail.setNext(null);
        }
        currentSize--;
    }


    @Override
    public E removeLast( ) throws EmptyListException
    {
        if ( this.isEmpty() ){
            throw new EmptyListException();
        }
        if(tail == null){
            return null;
        }
        E element = tail.getElement();

        if(head == tail){
            head = null;
            tail = null;
        }
        else{
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        currentSize--;
        return element;
    }


    /**
     * Removes the specified node from the list.
     * Pre-condition: the node is neither the head nor the tail of the list.
     * @param node - middle node to be removed
     */
    protected void removeMiddleNode( DoubleListNode<E> node )
    {
        DoubleListNode<E> previous=node.getPrevious();
        DoubleListNode<E> next= node.getNext();
        previous.setNext(next);                          // assim o do meio deixa de ter ligacoes e perde-se
        next.setPrevious(previous);
        currentSize--;

    }


    @Override
    public E remove( int position ) throws InvalidPositionException
    {
        if ( position < 0 || position >= currentSize )
            throw new InvalidPositionException();

        if ( position == 0 )
            return this.removeFirst();
        else if ( position == currentSize - 1 )
            return this.removeLast();
        else 
        {
        	DoubleListNode<E> nodeToBeRemoved= getNode(position);
            removeMiddleNode(nodeToBeRemoved);
        	return nodeToBeRemoved.getElement();
        }
    }


    /**
     * Returns the node with the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns null.
     * @param element - element to be searched
     * @return DoubleListNode<E> where element was found, null if not found 
     */
    protected DoubleListNode<E> findNode( E element )
    {
    	DoubleListNode<E> theOne= head;

        while(theOne!=null){  // também dá para fazer com um for mas se for à última casa (tail) vai tentar
            if(theOne.getElement().equals(element)){    //buscar o outro que é null
                return theOne;
            }
            theOne=theOne.getNext();
        }
        return null;
    }


    @Override
    public boolean remove( E element )
    {
        DoubleListNode<E> node = this.findNode(element);
        if ( node == null )
            return false;
        else
        {
            if ( node == head )
                this.removeFirstNode();
            else if ( node == tail )
                this.removeLastNode();
            else
                this.removeMiddleNode(node);
            return true;
        }
    }


    /**
     * Removes all of the elements from the specified list and
     * inserts them at the end of the list (in proper sequence).
     * @param list - list to be appended to the end of this
     */
    public void append( DoubleList<E> list )
    {
        if(this.isEmpty()){
            List<E> l= this;
            l=this;
        }
        else if(list.isEmpty()){
            // does nothing 
        }
        else if(list.isEmpty()&& this.isEmpty()){
            // does nothings
        }
        else{
        DoubleListNode<E> tailOfMyList= tail;
        DoubleListNode<E> headOfQueueToAppend= list.getNode(0);
        DoubleListNode<E> tailOfListToAppend= getNode(list.currentSize);

        tailOfMyList.setNext(headOfQueueToAppend);
        headOfQueueToAppend.setPrevious(tailOfListToAppend);
        this.tail=tailOfListToAppend;
        }

    }

    /**
     * Checks if the specified element exists in the list.
     * Traverses the list from the head to the tail, comparing each element 
     * @param element - the element to be searched in the list
     * @return true if the element is found in the list, false otherwise
     */
    @Override
    public boolean contains(E element) {
        DoubleListNode<E> current = head;
        while (current != null) {
            if (current.getElement().equals(element)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }


}   
