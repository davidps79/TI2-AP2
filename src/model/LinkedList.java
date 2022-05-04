package model;

public class LinkedList<E> {

    private Node<E> first;
    private Node<E> last;
    private int size;
    private int adjustSpaces;

    public LinkedList(int adjustSpaces) {
        //System.out.println("Factor de ajuste: " + adjustSpaces);
        first = null;
        size = 0;
        this.adjustSpaces = adjustSpaces;
    }

    public Node<E> getFirst() {
        return first;
    }
    
    public Node<E> getLast(){
    	return last;
    }

    public void add(E e) {
        Node<E> node = new Node<E>(e, adjustSpaces);
        
        if (first==null){
            first=node;
            last=node;
        }else{    
        	last.setNext(node);
        	node.setPrevious(last);
        	last = node;
        }
        
        last.setNext(first);
        first.setPrevious(last);
        size++;
    }

    public Node<E> get(int index) {
        return get(index, first);
    }

    private Node<E> get(int index, Node<E> temp) {
        if (index == 0) return temp;
        else return get(index - 1, temp.getNext());
    }
    
    public int size() {
        return size;
    }

	public boolean isVoid() {
		return (first==null);
	}
	
    public String toStringLinks(int columns) {
        String s = "";
        Node<E> temp = first;
        for (int i=0; i<(size/columns); i++) {
            if (i%2 == 0) {
                for (int j=0; j<columns; j++) {
                    s += temp.toStringLinks() + " ";
                    temp = temp.getNext();
                }   
            } else {
                for (int j=0; j<columns-1; j++) {
                    temp = temp.getNext();
                }

                for (int j=0; j<columns; j++) {
                    s += temp.toStringLinks() + " ";
                    temp = temp.getPrevious();
                }

                for (int j=0; j<columns+1; j++) {
                    temp = temp.getNext();
                }
            }
            s += "\n";
        }
        return s;
    }

    public String toStringSeeds(int columns) {
        String s = "";
        Node<E> temp = first;
        for (int i=0; i<(size/columns); i++) {
            if (i%2 == 0) {
                for (int j=0; j<columns; j++) {
                    s += temp.toStringSeeds() + " ";
                    temp = temp.getNext();
                }   
            } else {
                for (int j=0; j<columns-1; j++) {
                    temp = temp.getNext();
                }

                for (int j=0; j<columns; j++) {
                    s += temp.toStringSeeds() + " ";
                    temp = temp.getPrevious();
                }

                for (int j=0; j<columns+1; j++) {
                    temp = temp.getNext();
                }
            }
            s += "\n";
        }
        return s;
    }
}