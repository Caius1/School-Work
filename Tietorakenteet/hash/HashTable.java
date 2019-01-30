package hash;

/**
 * Own written implementation of a hash table.
 * <p>
 * Collisions are handled by creating a one way linked list where the new
 * collided node will be placed last in the list.
 * <p>
 * Used hash function: h(k) = (3k + 2) mod N,
 * where k is the integer value of the data that will be stored in the hash table.
 * 
 * @author Kai Pirttiniemi
 */
public class HashTable {
    
    private int N;  //Size of the hash
    private int n;  //Items in the hash
    public int len; //Length of the longest items in the hash    
    
    //The actual hash table that contains item of type HashNode
    public HashNode table[];
    
    /*
    Constructors
    */
    
    /**
     * Constructor for the hash, initial default size N = 10.
     */
    public HashTable() {
        N = 10;
        n = 0;
        table = new HashNode[N];
    }
    
    /**
     * Method for creating a hash table of a specified size.
     * 
     * @param size How big will the new hash table be.
     */
    private HashTable(int size) {
        N = size;
        n = 0;
        table = new HashNode[N];
    }
    
    /*
    Operational methods
    */
    
    /**
     * Method for inserting integers into the hash.
     *
     * @param data data or value to be stored in the element
     * @param detail detail, additional info that depends on the class that creates the node
     */
    public void insert(int data, int detail) {
        
        //Check if rehash is needed to prevent overfilling
        if (n/N >= 1)
            rehash();
        
        //Where will the node be placed, hash function
        int k = ((3 * data + 2) % N);
        
        HashNode newNode = new HashNode(data, detail);
        
        //Place the node
        if (table[k] == null)
            table[k] = newNode;
        else 
            handleCollision(k, newNode);
        
        //Mark that the hash contains one more node
        n++;
        
        //Check if rehash is needed
        if (n/N >= 1)
            rehash();
    }
    
    /**
     * Method for handling collisions. New node will be placed in the last spot
     * of the one way linked list.
     * 
     * @param k key, in which slot the node will be stored
     * @param newNode node to be stored
     */
    private void handleCollision(int k, HashNode newNode) {
        HashNode lastNode = table[k];
        
        //Find the last node in the linked list
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        //Set to the end of the list
        lastNode.next = newNode;
    }
    
    /**
     * Method for rehashing just to bypass size check. Same as insert-method
     * above but hash table's size will not be checked.
     * 
     * @param data data or value to be stored in the element
     * @param detail detail, additional info that depends on the class that
     * creates the node
     */
    private void insertToNew(int data, int detail) {
        
        //Where will the node be placed, hash function
        int k = ((3 * data + 2) % N);
        
        HashNode newNode = new HashNode(data, detail);
        
        //Place the node
        if (table[k] == null)
            table[k] = newNode;
        else 
            handleCollision(k, newNode);
        
        //Mark that the hash contains one more node
        n++;
    }
    
    /**
     * Remove a specific node from the hash.
     * 
     * @param data What will be removed.
     * @return True, if the removal succeeded.
     */
    public boolean remove(int data) {
        
        //Find out where the node is located
        int k = ((3 * data + 2) % N);
        
        //Node with data not found
        if (table[k] == null)
            return false;
        //Node with data not found
        else if (table[k].next == null && table[k].getData() != data)
            return false;
        //If the node is the only node in the slot
        else if (table[k].next == null)
            table[k] = null;
        //If the node-to-be-removed is not the only node in slot table[k]
        else {
            HashNode prev = null;
            HashNode curr = table[k];
            
            while (curr.getData() != data) {
                prev = curr;
                curr = curr.next;
            }
            
            //Remove the wanted node from the linked list
            prev.next = curr.next;
        }
        
        //Mark that a node has been removed
        n--;
        
        //Check if rehash is needed. Rehash is not done if the table is empty
        // (n = 0) because in that case N = 1 and n/N = 0, which in theory would
        // lead to hash table's deletion but in practice would be a problem.
        if (n > 0 && n/N <= 0.5)
            rehash();
        
        //Remove succesful
        return true;
    }
    
    /**
     * Attempts to find the given data from the hash table.
     * 
     * @param data Data or value that will be searched from the hash table
     * @return True, if the data is present in the hash table.
     */
    public boolean find(int data) {
        
        //Find out where the data/node is located
        int k = ((3 * data + 2) % N);
        
        HashNode node = table[k];

        if (node == null)
            return false;
        else
            while (node.getData() != data && node.next != null) {
                node = node.next;
            }

        return node.getData() == data;
    }
    
    /**
     * Accessor to tell the user or the calling method how many nodes
     * does the hash table contain.
     * 
     * @return Returns n, how many nodes there are in the has table.
     */
    public int getNodeSum() {
        return n;
    }
    
    /**
     * Accessor to tell the user or the calling method how big the table
     * itself is.
     * 
     * @return Returns N, how many slots for nodes there are in the has table,
     * how big the table itself is.
     */
    public int getSize() {
        return N;
    }
    
    /**
     * Method for rehashing the hash table.
     * Target is to have the fill ratio n/N = [0.5, 1].
     * Ideal fill ratio would be 0.75.
     */
    private void rehash() {
        
        //Temporary hashtable for the rehash transition
        HashTable tmpTable;
        HashNode node;
        
        //Move the hash to a larger table
        if (n/N >= 1) {
            tmpTable = new HashTable(2*N);
            
            for (int i = 0; i < N; i++) {
                node = table[i];
                if (node != null) {
                    tmpTable.insertToNew(
                            node.getData(),
                            node.getDetail());
                
                    while (node.next != null) {
                        node = node.next;
                        tmpTable.insertToNew(
                                node.getData(),
                                node.getDetail());
                    }
                }
            }
            //Replace table with the newly created table
            this.table = tmpTable.table;
            //Update the size of table
            N = 2*N;
        }
        
        //Move the hash to a smaller table
        else if (n/N <= 0.5) {
            tmpTable = new HashTable(N/2);
            
            for (int i = 0; i < N; i++) {
                node = table[i];
                if (node != null) {
                    tmpTable.insertToNew(
                            node.getData(),
                            node.getDetail());
                
                    while (node.next != null) {
                        node = node.next;
                        tmpTable.insertToNew(
                                node.getData(),
                                node.getDetail());
                    }
                }    
            }
            //Replace table with the newly created table
            table = tmpTable.table;
            //Update the size of table
            N = N/2;
        }
    }
    
    /**
     * Method that was not used or needed in the implementation but should
     * still be present in a hash table.
     * 
     * @return True, if there are no nodes in the hash.
     */
    public boolean isEmty() {
        return n == 0; }
}
