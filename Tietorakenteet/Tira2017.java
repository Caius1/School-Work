import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import setoperations.*;
import hash.*;

/**
 * This is the course work for the course "Data Structures 2017" held in
 * University of Tampere.
 * <p>
 * This work is about performing set operations AND, OR and XOR on to given sets.
 * The sets are read from a file and the set operations are performed using a
 * self-made hash table as a data structure. After the results are done, they
 * will be written into corresponding files called "and.txt", "or.txt" and
 * "xor.txt".
 *
 * @author Kai Pirttiniemi
 */
public class Tira2017 {

    private static int[] setA;
    private static int[] setB;
    
    /**
     * How many characters does the largest number in setA or setB have.
     * Will be used when writing the output. Default value is 1 because every
     * number has at least 1 character in it.*/
    private int len = 0;
    
    /**
     * This method will read the files that contain the sets on which the set
     * operations will be performed on.
     */
    private void readInput() {
        String line;
        int i;
        BufferedReader br;
        
        //Read setA
        try {
            br = new BufferedReader( new FileReader("setA.txt"));
            i = 0;
            while ((line = br.readLine()) != null) {
                i++;
            }
            br.close();
            
            setA = new int[i];
            
            //Reset reader and move the file contents into hash table
            br = new BufferedReader( new FileReader("setA.txt"));
            i = 0;
            line = br.readLine();
            while (line != null) {
                setA[i] = Integer.parseInt(line.trim().replaceAll("\n ", ""));
                line = br.readLine();
                i++;
            }
        }
        catch (IOException e) {
            System.out.println("File not found.");
        }
        
        //Read setB
        try {
            br = new BufferedReader( new FileReader("setB.txt"));
            i = 0;
            while ((line = br.readLine()) != null) {
                i++;
            }
            br.close();
            
            setB = new int[i];
            
            //Reset reader and move the file contents into hash table
            br = new BufferedReader( new FileReader("setB.txt"));
            i = 0;
            line = br.readLine();
            while (line != null) {
                setB[i] = Integer.parseInt(line.trim().replaceAll("\n ", ""));
                line = br.readLine();
                i++;
            }
        }
        catch (IOException e) {
            System.out.println("File not found.");
        }
    }
    
    /**
     * Method that will write the set operation result in a text file.
     * 
     * @param s Set operation (AND/OR/XOR) result that will be written into file.
     * @param file Name of the file that will be written.
     */
    private void writeOutput(HashTable s, String file) {
        String output = "";
        int nodesLeft = s.getNodeSum();
        int setSize = s.getSize();
        int i = 0; int j = 0;
        HashNode node;
        String data;
        String detail;
        BufferedWriter bw;
        int[][] set = new int[s.getNodeSum()][2];
        
        //Create an array in which the data will be sorted in to an ascending
        // order so that they would be in order in the print.
        do {

            if (s.table[i] != null) {
                node = s.table[i];

                //Get the numbers out of the node and into String form
                set[j][0] = node.getData();
                set[j][1] = node.getDetail();
                
                //Move to the next slot in the help array
                j++;

                //One less node to write in the file
                nodesLeft--;

                //Enter the loop if there are more than nodes in slot table[i]
                while (node.next != null) {

                    //Get the next node that will be written in the file
                    node = node.next;
                    set[j][0] = node.getData();
                    set[j][1] = node.getDetail();
                    
                    j++;
                    nodesLeft--;
                }
            }
            //Move to the next element/slot in the hash table array
            i++;
        }
        while (i < setSize && nodesLeft > 0);
        
        //Sort the array set to ascending order according to the first value
        Arrays.parallelSort(set, (int[] a, int[] b) -> Integer.compare(a[0], b[0]));
        
        //Write the file from the sorted array set
        try {
            System.out.println("Writing file...");
            bw = new BufferedWriter( new FileWriter(file));
            
            for (int[] set1 : set) {
                data = Integer.toString(set1[0]);
                detail = Integer.toString(set1[1]);
                //Format the output
                output = formatOutput(data, detail);
                //Write the output
                bw.write(output);
                bw.newLine();
            }
            
            bw.close();
            
            System.out.println("File writing succesful.\n");
        }
        catch (IOException e) {
            System.err.format("Error writing file.\nIOException: %s%n\n", e);
        }
        catch (Exception e) {
            System.err.format("Error writing file.\nException: %s%n\n", e);
        }
    }
    
    /**
     * Method that adds white spaces to the beginning of a string until
     * the string is of predetermined length.
     * 
     * @param data String that will be formatted by padding.
     * @param detail String that will be formatted by padding.
     * @return Formated output that will be written in the file.
     */
    private String formatOutput(String data, String detail) {
        String output;
        
        //Add spaces to the beginning as padding
        while (data.length() < len)
            data = " " + data;
        //Same for detail
        while (detail.length() < len)
            detail = " " + detail;
        
        output = data + " " + detail;
        
        return output;
    }
    
    /**
     * Method that executes the set operations and output writing.
     * <p>
     * Before writing the results of the set operations, the user is asked if
     * they want to remove any elements (data) from the result.
     */
    private void executeSets() {
        Scanner scan = new Scanner(System.in);
        String s = ""; //Scanner, remove data or not?
        int data;   //Scanner, data to remove
        final String REMOVE = "Y";      //Yes, remove data
        final String NOREMOVE = "N";    //No, don't remove data
        boolean removed;
        
        /*
        AND
        */
        
        And a = new And();
        HashTable setAnd = a.operate(setA, setB);
        while (!s.equalsIgnoreCase(NOREMOVE)) {
            System.out.println("Hash table AND contains " + 
                                setAnd.getNodeSum() + " nodes.");
            System.out.println("Remove element from hash table AND?\nY/es or N/o");
            s = scan.next();
            
            if (s.equalsIgnoreCase(REMOVE)) {
                System.out.println("Which element should be removed?");
                data = Integer.parseInt(scan.next());
                removed = setAnd.remove(data);
                System.out.println("Element found and removed: " + removed);
            }
        }
        
        //AND needs to have the sets unsorted but OR and XOR do not so here
        //  the sets are sorted to an ascending order to make operations easier
        Arrays.parallelSort(setA);
        Arrays.parallelSort(setB);
        calculateLen('A');
        
        writeOutput(setAnd, "and.txt");
        s = "";
        
        /*
        OR
        */
        
        Or o = new Or();
        HashTable setOr = o.operate(setA, setB);
        while (!s.equalsIgnoreCase(NOREMOVE)) {
            System.out.println("Hash table OR contains " + 
                                setOr.getNodeSum() + " nodes.");
            System.out.println("Remove element from hash table OR?\nY/es or N/o");
            s = scan.next();
            
            if (s.equalsIgnoreCase(REMOVE)) {
                System.out.println("Which element should be removed?");
                data = Integer.parseInt(scan.next());
                removed = setOr.remove(data);
                System.out.println("Element found and removed: " + removed);
            }
        }
        calculateLen('O');
        writeOutput(setOr, "or.txt");
        s = "";
        
        /*
        XOR
        */
        
        Xor x = new Xor();
        HashTable setXor = x.operate(setA, setB);
        while (!s.equalsIgnoreCase(NOREMOVE)) {
            System.out.println("Hash table XOR contains " + 
                                setXor.getNodeSum() + " nodes.");
            System.out.println("Remove element from hash table XOR?\nY/es or N/o");
            s = scan.next();
            
            if (s.equalsIgnoreCase(REMOVE)) {
                System.out.println("Which element should be removed?");
                data = Integer.parseInt(scan.next());
                removed = setXor.remove(data);
                System.out.println("Element found and removed: " + removed);
            }
        }
        calculateLen('X');
        writeOutput(setXor, "xor.txt");
    }
    
    /**
     * Calculate the length of the longest number in the set. The result "len"
     * will be used when formatting output.
     * 
     * @param set Character that will tell the method how the longest number
     * is determined
     */
    private void calculateLen(char set) {
        int longest = 0;
        len = 0;
        switch (set) {
            case 'A':
                if (setA[setA.length-1] > setA.length)
                    longest = setA[setA.length-1];
                else
                    longest = setA.length;
                break;
            case 'O':
                if (setA[setA.length-1] > (setA.length + setB.length))
                    longest = setA[setA.length-1];
                else if (setB[setB.length-1] > (setA.length + setB.length))
                    longest = setB[setB.length-1];
                else
                    longest = setA.length + setB.length;
                break;
            case 'X':
                if (setA[setA.length-1] > setB[setB.length-1])
                    longest = setA[setA.length-1];
                else
                    longest = setB[setB.length-1];
                break;
        }
        
        do {
            len++;
            longest = longest / 10;
        }
        while (longest > 0);
    }
    
    /**
     * Main method. Does not do that much. Greets the user and sets the
     * the operation in motion.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Tira2017 ht = new Tira2017();
        
        System.out.println("Data structures 2017, course work\nKai Pirttiniemi\n");
        System.out.println("This program can be given large integers.\n");
        
        //Read files setA.txt and setB.txt into corresponding arrays
        ht.readInput();
        
        //Execute set operations AND, OR, XOR
        ht.executeSets();
    }
}
