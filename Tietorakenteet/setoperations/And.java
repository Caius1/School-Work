package setoperations;
import hash.HashTable;
import java.util.Arrays;

/**
 * Performs the set operation AND for sets A and B.
 * <p>
 * In the method operate() the sets are first moved to new arrays that also
 * contains each element's row number. Then the sets are sorted to an ascending
 * order according to the number. After this preparation the sets are passed on
 * to method compareAnd() that performs the actual operation.
 * 
 * @author Kai Pirttiniemi
 */
public class And {
    
    /**
     * Hash table that contains the results of set operation AND
     */
    private final HashTable result = new HashTable();
    
    /**
     * Performs the set operation AND for sets A and B.
     * 
     * @param setA1 Set of integers sorted to an ascending order.
     * @param setB Set of integers sorted to an ascending order.
     * @return Hash table that contains the result of set operation AND.
     * Node data is the value that was given by the user in the original set on
     * integers and detail is the number of the row on which the data was first found.
     */
    public HashTable operate(int[] setA1, int[] setB) {
        
        //Move the sets to new 2d arrays that contain the given value and on
        // which row the value is located. r = row number
        int[][] setA2 = new int[setA1.length][2];
        for (int r = 0; r < setA1.length; r++) {
            setA2[r][0] = setA1[r];
            setA2[r][1] = r+1;
        }
        
        //Sort the new arrays to ascending order to make the comparing easier
        Arrays.parallelSort(setA2, (int[] o1, int[] o2) -> (Integer.valueOf(o1[0]).compareTo(o2[0])));
        Arrays.parallelSort(setB);
        
        //Do the operation
        compareAnd(setA2, setB);
        
        return result;
    }
    
    /**
     * Method that goes through sets A and B and adds the number from setA to hash
     * if the same number is found in setB. The number is not added if it is
     * already present in the hash table.
     * 
     * @param setA Set of integers in an array with their respective row numbers.
     * @param setB Set of integers in an array with their respective row numbers.
     */
    private void compareAnd(int[][] setA, int[] setB) {
        
        //Variables for the arrays' contents when comparing
        int a, b; int c, d;
        //Simple helping counters
        int i = 0; int j = 0;
        int data, detail;
        
        //Do the comparing until the end of setA is reached
        do {
            a = setA[i][0];
            b = setB[j];
            c = setA[i][1];
            
            if (a == b) {
                data = a;
                
                //Find the first row on which the number was found
                while (i < setA.length-1 && a == setA[i+1][0]) {
                    d = setA[i+1][1];
                    
                    if (c < d)
                        c = d;
                    
                    i++;
                }
                
                //Check if the number is already inserted
                if (!result.find(a))
                    result.insert(a, c);
                
                i++; j++;
            }
            else if (a < b)
                i++;
            else
                j++;
        }
        while (i < setA.length);
    }
}
