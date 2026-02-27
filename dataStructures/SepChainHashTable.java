/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package dataStructures;

/**
 * Separate Chaining Hash table implementation
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key, must extend comparable
 * @param <V> Generic Value 
 */

 public class SepChainHashTable<K extends Comparable<K>, V> extends HashTable<K,V> 
 { 
     /**
      * Serial Version UID of the Class.
      */
     static final long serialVersionUID = 0L;
 
     /**
      * The array of dictionaries.
      */
     protected Dictionary<K,V>[] table;
 
 
     /**
      * Constructor of an empty separate chaining hash table,
      * with the specified initial capacity.
      * Each position of the array is initialized to a new ordered list
      * maxSize is initialized to the capacity.
      * @param capacity defines the table capacity.
      */
     @SuppressWarnings("unchecked")
     public SepChainHashTable( int capacity )
     {
         int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
         // Compiler gives a warning.
         table = (Dictionary<K,V>[]) new Dictionary[arraySize];
         for ( int i = 0; i < arraySize; i++ )
              table[i] = new OrderedDoubleList<K,V>();
         maxSize = capacity;
         currentSize = 0;
     }                                      
 
 
     public SepChainHashTable( )
     {
         this(DEFAULT_CAPACITY);
     }                                                                
 
     /**
      * Returns the hash value of the specified key.
      * @param key to be encoded
      * @return hash value of the specified key
      */
     protected int hash( K key )
     {
         return Math.abs( key.hashCode() ) % table.length;
     }
 
     @Override
     public V find( K key )
     {
         return table[ this.hash(key) ].find(key);
     }
 
     @Override
     public V insert( K key, V value )
     {
         if ( this.isFull() ){
             rehash();
         }
         int hashKey= hash(key);
         if(table[hashKey].find(key) == null){
            table[hashKey].insert(key, value);
            currentSize++;
         }
         else{
            table[hashKey].insert(key, value);
         }
         return null;
     }
 
     @Override
     public V remove( K key )
     {
         int hashkey = hash(key);
         Dictionary<K,V> list = table[hashkey];
 
         V removedItem = list.remove(key);
         if(removedItem != null){
             currentSize--;
         }
         return removedItem;
         
     }
 
     @Override
     public Iterator<Entry<K,V>> iterator( )
     {
        return new OrderedTableIterator<>(table);
     } 
 
 
     private void rehash(){
         HashTable<K,V> newHash = new SepChainHashTable<>(2*maxSize);
         for(int i=0;i<table.length;i++){
             if(table[i].isEmpty()){
                 // vai passar à frente
             }
             else{
                 Dictionary<K,V> list = table[i];
                     Iterator<Entry<K,V>> it = list.iterator();
                     while(it.hasNext()){
                         Entry<K,V> ent = it.next();  // dá erro porque falta a exceção
                         newHash.insert(ent.getKey(), ent.getValue());
                     }
                 }
              }
         // trocar para o novo objecto
         table = ((SepChainHashTable<K,V>) newHash).table;
         maxSize= newHash.maxSize;
         currentSize= newHash.currentSize;
 
     }
 }
 
