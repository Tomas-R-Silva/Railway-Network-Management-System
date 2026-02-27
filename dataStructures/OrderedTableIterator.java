/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package dataStructures;

public class OrderedTableIterator<K,V> implements Iterator<Entry<K,V>> {

    static final long serialVersionUID = 0L;

    protected Dictionary<K,V>[] table;
    protected int arrayIndex;
    protected Iterator<Entry<K,V>> curIterator;


   
   public OrderedTableIterator(Dictionary<K,V>[] table){
    this.table = table;
    this.arrayIndex = 0;
    this.curIterator = getNextIterator(arrayIndex);
   }
   
    @Override
    public boolean hasNext() {
       while( curIterator == null || !curIterator.hasNext() && arrayIndex < table.length-1){ // se o curIterator for null( provavelmente nunca vai estar a null e sim devolver um iterator com 0 elementos mas para prevenir) ou se já não tiver nada e se ainda estiver dentro do array 
            arrayIndex++;                                                                 // vai aumentar o index do array e buscar o próximo iterator doubleList no inded indicado. table.length -1 porque senão tentar aceder a posiçao errada
            curIterator = getNextIterator(arrayIndex);
       }
       return curIterator != null && curIterator.hasNext();                              // se for diferente de null e se tiver next dá true, se chegar ao final, como falha no arrayIndex< table.length o curIterator hasNext() vai dar falso dizendo que já não há mais para iterar
    }

    @Override
    public Entry<K,V> next() {
       return curIterator.next();
    }

    @Override
    public void rewind() {
        
    }

    private Iterator<Entry<K,V>> getNextIterator( int index){
        return table[index].iterator();
    }
    
}
