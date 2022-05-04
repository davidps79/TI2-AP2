package model;

import java.io.Serializable;

public class Node<E> implements Serializable{

	private static final long serialVersionUID = 1L;
	private E item;
    private Node<E> next;
    private Node<E> previous;
    private int adjustSpaces;
    
    public Node(E e, int adjustSpaces) {
        this.item = e;
        this.next = null;
        this.previous=null;
        this.adjustSpaces = adjustSpaces;
    }

    public Node<E> getPrevious() {
        return this.previous;
    }

    public void setPrevious(Node<E> previous) {
        this.previous = previous;
    }

    public void setItem(E item) {
        this.item = item;
    }

    public E getItem() {
        return item;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }
    
    public String toStringSeeds() {
        return adjustString("[" + ((Square) getItem()).toStringSeeds() + "]");
    }
    
    public String toStringLinks() {
        return adjustString("[" + ((Square) getItem()).toStringLinks() + "]");
    }
    
    public Node<E> movePlayerForward(int positions) {
    	if (positions>0) return next.movePlayerForward(positions-1);
    	else return this;
    }
    
    public Node<E> movePlayerBackward(int positions) {
    	if (positions>0) return previous.movePlayerBackward(positions-1);
    	else return this;
    }

    public String adjustString(String s) {
        for (int i=0; i<adjustSpaces-s.length(); i++) s += " ";
        return s;
    }
}
