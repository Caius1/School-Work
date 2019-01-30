package hash;

/**
 * Node class of a linked list structure that is used in the hash table.
 *
 * @author Kai Pirttiniemi
 */
public class HashNode {
    
    private int data;
    private int detail;
    public HashNode next;
    
    /**
     * Constructor for single nodes in the hash
     * 
     * @param da data or value to be stored in the element
     * @param de detail, additional info that depends on the class that creates the node
     */
    public HashNode(int da, int de) {
        next = null;
        data = da;
        detail = de;
    }
    
    /**
     * Accessor that sets the node's data.
     * @param d Data that will be set in the node.
     */
    public void setData(int d) {
        data = d;
    }
    /**
     * Accessor that gets the node's data.
     * @return Data that is contained in the node.
     */
    public int getData() {
        return data;
    }
    
    /**
     * Accessor that sets the node's detail.
     * @param d Detail that will be set in the node.
     */
    public void setDetail(int d) {
        detail = d;
    }
    /**
     * Accessor that gets the node's detail.
     * @return Detail that is contained in the node.
     */
    public int getDetail() {
        return detail;
    }
}
