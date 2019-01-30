package setoperations;

import hash.HashTable;

/**
 * Performs the set operation OR on sets A and B.
 * <p>
 * If number is present in either or both of the sets, it will be added to the
 * results.
 *
 * @author Kai Pirttiniemi
 */
public class Or {
    
    /**
     * Hash table that contains the results of set operation OR
     */
    private final HashTable result = new HashTable();
    
    /**
     * Performs the set operation OR on sets A and B.
     * 
     * @param setA Set of integers sorted to an ascending order.
     * @param setB Set of integers sorted to an ascending order.
     * @return Hash table that contains the union/OR of setA and setB.
     */
    public HashTable operate(int[] setA, int[] setB) {
        
        //Helping variables and counters
        int a = 0; int b = 0;
        int i = 0;  //Counter for tracking the position in setA.
        int j = 0;  //Position in setB.
        int aN = 1; //How many times a number is found in setA.
        int bN = 1; //Found in setB.
        
        //For avoiding ArrayIndexOutOfBoundsException and to measure when to
        // stop comparisons.
        int setALen = setA.length;
        int setBLen = setB.length;
        
        do {
            
            if (i < setALen)
                a = setA[i];
            if(j < setBLen)
                b = setB[j];
            
            //Count how many times a and b are found in either set.
            //Counting is done: when first arriving at a number, go through them
            // until the next number is not the same. (Great explanetion)
            while (i < setALen-1 && setA[i+1] == a) {
                i++; aN++;
            }
            while (j < setBLen-1 && setB[j+1] == b) {
                j++; bN++;
            }
            
            //setA has ended
            if (i >= setALen) {
                result.insert(b, bN);
                j++; bN = 1;
            }
            //setB has ended
            else if (j >= setBLen) {
                result.insert(a, aN);
                i++; aN = 1;
            }
            else if (a <= b) {
                if (a == b) {
                    result.insert(a, (aN+bN));
                    i++; j++;
                    aN = bN = 1;
                }
                else {
                    result.insert(a, aN);
                    i++; aN = 1;
                }
            }
            //a > b
            else {
                result.insert(b, bN);
                j++; bN = 1;
            }
        }
        while (i < setALen || j < setBLen);
        
        return result;
    }
}
