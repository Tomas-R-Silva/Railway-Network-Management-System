/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package dataStructures;
import java.io.Serializable;
import railway.exceptions.*;

public class OrderedDoubleList<K extends Comparable<K>,V> implements OrderedDictionary<K,V>{

    static final long serialVersionUID = 0L;

    protected DoubleListNode<Entry<K,V>> head;
    protected DoubleListNode<Entry<K,V>> tail;
    protected int currentSize;

    public OrderedDoubleList(){
        this.head=null;
        this.tail=null;
        this.currentSize=0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize==0;
    }

    @Override
    public int size(){
        return currentSize;
    }

    @Override
    public V find(K key) {
        DoubleListNode<Entry<K,V>> node = findNode(key);
        if(node!=null && node.getElement().getKey().equals(key)){
            return node.getElement().getValue();
        }
        return null;
    }

    private DoubleListNode<Entry<K,V>> findNode(K key){
        DoubleListNode<Entry<K,V>> node = head;
        while(node!=null && node.getElement().getKey().compareTo(key)<0){
            node=node.getNext();
        }
        return node;
    }

    @Override
    public V insert(K key, V value) {
        if(head==null){ //Se o dicionário nao tiver elementos, adiciona em primeiro
            DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key,value),null,null); //cria node
            head=newNode; //1º e único elem do dicionário, logo é a head
            tail=newNode; //1º e único elem do dicionário, logo é a tail
            currentSize++;
            return null;
        }else{ //Vai encontrar o lugar e inserir
            DoubleListNode<Entry<K,V>> node = findNode(key);
            if(node==null){ //Na tail, pois nao ha nenhum node maior que o a inserir
                DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key,value),node,null); //cria node
                node=tail; //Guarda a tail (antiga) que vai passar a penultimo elem
                newNode.setPrevious(tail); //set previous da nova tail = antiga tail
                node.setNext(newNode); //set next da antiga tail = nova tail
                tail=newNode; //atualiza para a nova tail
                currentSize++;
                return null;
            }else if(node.getElement().getKey().equals(key)){ //findNode retorna um no com chave igual
                V oldValue = node.getElement().getValue(); //guarda o antigo valor para dar return
                DoubleListNode<Entry<K,V>> prevNode = node.getPrevious(); //busca o previous node, para poder fazer a copia
                DoubleListNode<Entry<K,V>> nextNode = node.getNext(); //busca o next node, para poder fazer a copia
                node.setPrevious(null); //elimina o node a ser substituido (1/2)
                node.setNext(null); //elimina o node a ser substituido (2/2)
                DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key,value),prevNode,nextNode); //cria node
                if(node==head)
                    head=newNode; //se era a head, atualiza a head com a copia
                if(node==tail)
                    tail=newNode; //se era a tail, atualiza a tail com a copia
                if(prevNode!=null)
                    prevNode.setNext(newNode); //se tinha previous, redireciona a ligaçao para a copia
                if(nextNode!=null)
                    nextNode.setPrevious(newNode); //se tinha next, redireciona a ligaçao para a copia
                currentSize++;
                return oldValue;
            }else { // findNode retorna um no com chave maior
                DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key,value),null,node);
                if(node==head){
                    node.setPrevious(newNode);
                    head=newNode;
                    currentSize++;
                    return null;
                }else{
                    DoubleListNode<Entry<K,V>> prevNode = node.getPrevious();
                    prevNode.setNext(newNode);
                    node.setPrevious(newNode);
                    newNode.setPrevious(prevNode);
                    currentSize++;
                    return null;
                }
            }
        }
    }

    @Override
    public V remove(K key) throws EmptyDictionaryException, NoSuchElementException{ // se encontra o no , remove e devolve V
        if(head==null){
            throw new EmptyDictionaryException();
        }
        DoubleListNode<Entry<K,V>> node = findNode(key);
        if(node==null||!node.getElement().getKey().equals(key)){
            throw new NoSuchElementException();
        }else{
            V oldValue = node.getElement().getValue();
            DoubleListNode<Entry<K,V>> prevNode = node.getPrevious();
            DoubleListNode<Entry<K,V>> nextNode = node.getNext();
            node.setPrevious(null);
            node.setNext(null);
            if(node==head)
                head=nextNode;
            if(node==tail)
                tail=prevNode;
            if(prevNode!=null)
                prevNode.setNext(nextNode);
            if(nextNode!=null)
                nextNode.setPrevious(prevNode);
            currentSize--;
            return oldValue;
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator(){
        return new DoubleListIterator<Entry<K,V>>(head, tail);
    }

    @Override
    public Entry<K,V> minEntry() throws EmptyDictionaryException{
        if(head==null){
            throw new EmptyDictionaryException();
        }
        return head.getElement();
    }

    @Override
    public Entry<K,V> maxEntry() throws EmptyDictionaryException{
        if(tail==null){
            throw new EmptyDictionaryException();
        }
        return tail.getElement();
    }
}
