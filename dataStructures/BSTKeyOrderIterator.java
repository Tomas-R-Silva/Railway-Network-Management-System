/* Completed by:
 * @author Tomás Silva (68644) tri.silva@campus.fct.unl.pt
 * @author Gonçalo Guerreiro (69029) gf.guerreiro@campus.fct.unl.pt
 */

package dataStructures;

import railway.exceptions.*;;

class BSTKeyOrderIterator<K,V> implements Iterator<Entry<K,V>> {

	
	protected BSTNode<Entry<K,V>> root;

	protected Stack<BSTNode<Entry<K,V>>> p;


	BSTKeyOrderIterator(BSTNode<Entry<K,V>> root){
		this.root=root;
		rewind();
	}
	
	private void pushPathToMinimum(BSTNode<Entry<K,V>> node) {
		while(node!=null){
			p.push(node);
			node=node.getLeft();
		}
	}

	//O(1) para todos os casos
	public boolean hasNext(){
		 return !p.isEmpty();
	 }


    public Entry<K,V> next( ) throws NoSuchElementException {
    	if (!hasNext()) throw new NoSuchElementException();
    	else {
			BSTNode<Entry<K,V>> e = p.pop();
			pushPathToMinimum(e.getRight());
			return e.getElement();
    	}
    }

    public void rewind( ){
		p = new StackInList<BSTNode<Entry<K,V>>>();
    	pushPathToMinimum(root);
    }
}
