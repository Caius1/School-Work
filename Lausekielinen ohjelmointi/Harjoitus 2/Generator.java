import java.util.Random;

/* 
 * The second assignment work: A helper class for maze generation.
 *  
 * High Level Programming II, Fall 2015.
 *
 * Jorma Laurikkala, jorma.laurikkala@uta.fi.
 *
 * DO NOT MODIFY THIS CLASS.
 *
 * DO NOT COPY OPERATIONS FROM THIS CLASS TO YOUR PROGRAM.
 *
 * Version 1.0.
 *
 * Last modified 26.11.2015.
 *
 */

public class Generator {

   /*__________________________________________________________________________
    *
    * 1. Public constant class variables.
    *
    */

   // A character symbolising passage.
   public static final char PASSAGE = ' ';

   // A character symbolising horizontal wall.
   public static final char HORIZONTALWALL = '.';
   
   // A character symbolising vertical wall.
   public static final char VERTICALWALL = '.';
   
   // Minimum length for the side of the maze.
   public static final int MINSIDELENGTH = 3;
   
   // The row index of the maze entry.
   public static final int ENTRYROWIND = 0;

   // The column index of the maze entry.
   public static final int ENTRYCOLIND = 1;

   /*__________________________________________________________________________
    *
    * 2. Private constant class variables.
    *
    */

   // A character symbolising an unvisited vertex.
   public static final char UNVISITED = 'x';

   /*__________________________________________________________________________
    *
    * 3. Private class variables.
    *
    */

   // A pseudorandom number generator.
   private static Random generator;

   // True, if the maze generator has been initialised, that is, the initialise
   // operation has been called.
   private static boolean initialised = false;

   /*__________________________________________________________________________
    *
    * 4. Private class methods.
    *
    */

   /* Return true, if the size of a nrows x ncols matrix is valid.
    */
   private static boolean mazeSizeValid(int nrows, int ncols) {
      return nrows >= MINSIDELENGTH && ncols >= MINSIDELENGTH && nrows % 2 == 1
      && ncols % 2 == 1;
   }

   /* Create a nrows x ncols maze and initialise it with unvisited vertices
    * surrounded by walls. None of the vertices is connected after initialisation.
    * It is assumed that the parameters are valid. Operation returns a reference
    * to the maze.
    */
   private static char[][] initialiseMaze(int nrows, int ncols) {
      // Create a maze.
      char[][] maze = new char[nrows][ncols];

      // Initialise all of the maze locations. UNVISITED location represent
      // the vertices of a spanning tree.
      for (int rowind = 0; rowind < nrows; rowind++)
         for (int colind = 0; colind < ncols; colind++) {
            // Insert vertex.
            if (rowind % 2 == 1 && colind % 2 == 1)
               maze[rowind][colind] = UNVISITED;
            // Insert alternatively vertical and horizontal walls.
            else {
               // Print full horizontal wall, if the row is the first or the last one.
               if (rowind == 0 || rowind == nrows - 1)
                  maze[rowind][colind] = HORIZONTALWALL;                  
               // Start and end the rows between with a vertical wall.
               else if (colind == 0 || colind == ncols - 1)
                  maze[rowind][colind] = VERTICALWALL;
               // Print vertical wall, if on a odd line.
               else if (rowind % 2 == 1)
                  maze[rowind][colind] = VERTICALWALL;                                 
               // Print horizontal wall, if on a even line.
               else if (rowind % 2 == 0)
                  maze[rowind][colind] = HORIZONTALWALL;               
            }
         }
         
      // Return a reference.
      return maze;
   }

   /* Select randomly a neighbour of the vertex located at given indices
    * in a nrows x ncols matrix and return the indices of the neighbour.
    * It is assumed that the indices of a vertex are received.
    */
   private static int[] selectNeighbour(int[] indices, int nrows, int ncols) {
      // Create an array for the indices of the neighbours in north, east,
      // south and west.
      int[][] neighbourIndices =
      { { indices[0] - 2, indices[1] }, { indices[0], indices[1] + 2},
      { indices[0] + 2, indices[1] }, { indices[0], indices[1] - 2 } };
      
      // Select randomly until the neighbour is vertex.
      boolean isVertex = false;
      int rowind = 0;
      do {
         // Select neighbour randomly.
         rowind = generator.nextInt(neighbourIndices.length);

         // Vertex was found.
         isVertex = neighbourIndices[rowind][0] > 0
         && neighbourIndices[rowind][1] > 0
         && neighbourIndices[rowind][0] < nrows - 1
         && neighbourIndices[rowind][1] < ncols - 1;
      }
      while (!isVertex);
            
      // Return a reference to the indices.
      return neighbourIndices[rowind];
   }

   /* Add an edge between the current vertex and its neighbour by removing
    * wall between the locations of the vertices. It is assumed that the parameters
    * are valid.
    */
   private static void addEdge(char[][] maze, int[] indices, int[] neighbourIndices) {
      // Remove northern wall.
      if (indices[0] > neighbourIndices[0])
         maze[indices[0] - 1][indices[1]] = PASSAGE;
      // Remove eastern wall.
      else if (indices[1] < neighbourIndices[1]) 
         maze[indices[0]][indices[1] + 1] = PASSAGE;
      // Remove southern wall.
      else if (indices[0] < neighbourIndices[0])
         maze[indices[0] + 1][indices[1]] = PASSAGE;
      // Remove western wall.
      else if (indices[1] > neighbourIndices[1]) 
         maze[indices[0]][indices[1] - 1] = PASSAGE;

   }
   
   /*__________________________________________________________________________
    *
    * 5. Public class operations called for from the assignment program.
    *
    */

   /* This operation initialises the maze generator. The pseudonumber generator
    * is initialised using the seed number. The operation can be called only once.
    * If the operation is called again, an exection is raised.
    */
   public static final void initialise(int seed) {
      // The first call initialises the generator.
      if (!initialised) {
         // Create a pseudorandom number generator and initialise it with a known
         // seed number to allow the repeatition of a sequence of random numbers.
         generator = new Random(seed);

         // The maze and pseudonumber generators have been initialised.
         initialised = true;
      }

      // The second call raises an exception.
      else
         throw new IllegalArgumentException("Maze generator already initialised!");
   }

   /* This operation generates a perfect (or simply connected) maze using
    * the Aldous-Broder algorithm. The maze has no loops or parts which cannot
    * be accessed. The algorithm is very simple and selects the spanning tree
    * by a random walk. The maze is represented as a two-dimensional array
    * consisting of characters. The return value is a reference to the array,
    * if the numbers of rows and columns are valid. Otherwise, the null value
    * is returned. The operation can be called only after the maze generator
    * has been initialised. Premature call raises an exception.
    */
   public static final char[][] generate(int nrows, int ncols) {
      // Raise an exception, if the pseudonumber generator has not been initialised.
      if (!initialised)
         throw new IllegalArgumentException("Maze generator has not been initialised!");

      // Declare a reference and intialise it with the error code.
      char[][] maze = null;

      // Not all maze sizes are acceptable.
      if (mazeSizeValid(nrows, ncols)) {
         // Create an array for the maze and intialise it.
         maze = initialiseMaze(nrows, ncols);
                  
         // Select the first vertex randomly.
         int[] indices = { 2 * generator.nextInt(nrows / 2) + 1,
         2 * generator.nextInt(ncols / 2) + 1 };

         // Mark the first vertex as visited.
         maze[indices[0]][indices[1]] = PASSAGE;

         // Walk randomly until the rest of the vertices have been visited.
         int nunvisited = (nrows / 2) * (ncols / 2) - 1;
         while (nunvisited > 0) {                        
            // Select randomly a neighbour.
            int[] neighbourIndices = selectNeighbour(indices, nrows, ncols);

            // An unvisited vertex was found.
            if (maze[neighbourIndices[0]][neighbourIndices[1]] == UNVISITED) {
               // Create edge between the current vertex and its neighbour.
               addEdge(maze, indices, neighbourIndices);

               // Mark the vertex and the neighbour as visited.
               maze[neighbourIndices[0]][neighbourIndices[1]] = PASSAGE;
               
               // One more vertex has been been visited.
               nunvisited--;
            }
                        
            // Move to the next vertex.
            indices[0] = neighbourIndices[0];
            indices[1] = neighbourIndices[1];
         }

         // Carve the entrance and the exit into the outer wall.
         maze[ENTRYROWIND][ENTRYCOLIND] = PASSAGE;
         maze[maze.length - 1][maze[0].length - 2] = PASSAGE;
      }
      
      // Return the reference to the maze.
      return maze;
   }
}
