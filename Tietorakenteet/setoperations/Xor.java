package setoperations;

import hash.HashTable;
import java.util.Arrays;

/**
 * Performs the set operation XOR on sets A and B.
 * <p>
 * If the number is present in set A or B but is not in both, it will
 * be added to the results.
 *
 * @author Kai Pirttiniemi
 */
public class Xor {
    
    /**
     * Hash table that contains the results of set operation XOR
     */
    private final HashTable result = new HashTable();
    
    /**
     * Performs the set operation OR on sets A and B. Operation utilises
     * the method binarySearch from Arrays class. On each number, the number
     * will be searched from the other set using binarySearch and if it is not
     * found it will be added to the results.
     * 
     * @param setA Set of integers sorted to an ascending order.
     * @param setB Set of integers sorted to an ascending order.
     * @return Hash table that contains the results of setA XOR setB.
     */
    public HashTable operate(int[] setA, int[] setB) {
        
        //These variables make a small helping effort in comparisons
        int a; int b;
        
        //Go through the set and add to hash if not found in setB
        for (int i = 0; i < setA.length; i++) {
            a = setA[i];
            if (a > setB[setB.length - 1])
                while (i < setA.length) {
                    result.insert(setA[i], 1);
                    i++;
                }
            else if (Arrays.binarySearch(setB, a) < 0)
                result.insert(a, 1);
            
        }
        //Same for setB
        for (int j = 0; j < setB.length; j++) {
            b = setB[j];
            if (b > setA[setA.length - 1])
                while (j < setB.length) {
                    result.insert(setB[j], 2);
                    j++;
                }
            else if (Arrays.binarySearch(setA, b) < 0)
                result.insert(b, 2);
        }
        
        return result;
    }
}
