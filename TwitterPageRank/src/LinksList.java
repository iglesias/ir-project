/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  


import java.util.LinkedList;
import java.io.Serializable;

/**
 *   A list of postings for a given word.
 */
public class LinksList implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	/** The postings list as a linked list. */
    private LinkedList<LinksEntry> list;
    
    public boolean empty(){
    return list.isEmpty();
    }
    
    public LinksEntry removeFirst(){
    return list.removeFirst();
    }


    /**  Number of postings in this list  */
    public int size() {
	return list.size();
    }

    /**  Returns the ith posting */
    public LinksEntry get( int i ) {
	return list.get( i );
    }
    //
    //  YOUR CODE HERE
    //
    
    public void add(String name)
    {
    	LinksEntry newpe = new LinksEntry(name);
    	list.add(newpe);
    }

    
    public void clearup(){
    	list.clear();
    }

	public LinksList()
	{
		list = new LinkedList<LinksEntry>();
	}

    
}