import java.util.Random;

/**
 * The class that creates the hashtables
 * 
 * @author Alina Rozenbaum
 * 
 */
public class Hashtables {
	private int[] table1;
	private int[] table2;
	private int[] table3;
	private int[] table4;

	int key = 0;
	int h = 0;

	private static final int SIZE = 1019;

	/**
	 * Constructs the four hashtables
	 */
	public Hashtables() {

		table1 = new int[SIZE];
		table2 = new int[SIZE];
		table3 = new int[SIZE];
		table4 = new int[SIZE];

	}

	/**
	 * Creates and hashes an array with the appropriate number of values, with
	 * linear probing, and then returns it
	 * 
	 * @param table
	 *            - The array being looked at to hash into
	 * @param loadFact
	 *            - The number of values that the hashtable should have
	 * @return - The final hashed array
	 */
	protected int[] createLinear(int[] table, int loadFact) {

		initialize(table);
		linearProbe(table, loadFact);
		key = h = 0;
		return table;

	}// end createLinear

	/**
	 * Creates and hashes an array with the appropriate number of values, with
	 * quadratic probing, and then returns it
	 * 
	 * @param table
	 *            - The array being looked at to hash into
	 * @param loadFact
	 *            - The number of values that the hashtable should have
	 * @return - The final hashed array
	 */
	protected int[] createQuad(int[] table, int loadFact) {

		initialize(table);
		quadProbe(table, loadFact);
		key = h = 0;
		return table;

	}// end createQuad

	/**
	 * Hashing values into an array with linear probing
	 * 
	 * @param table
	 *            - The array being looked at to hash into
	 * @param loadFact
	 *            - The number of values that the hashtable should have
	 * @param key
	 *            - The value being hashed into the array and being put into the
	 *            hashfunction
	 * @param h
	 *            - The result for the index under consideration after the key
	 *            goes through the hashfunction
	 * @param gen
	 *            - Generator for a value between 1 - 10,000
	 */
	private void linearProbe(int[] table, int loadFact) {

		int temp = 1;

		// Makes sure that the appropriate number of values are hashed
		while (temp <= loadFact) {

			genValue(table);

			// As long as the h indexed spot is full,
			// the index h is incremented by one continuously
			// until and empty spot is found
			while (checkEmpty(table, h) == false) {

				if (h == (table.length - 1)) {
					h = 0;
				} else {
					h++;
				}// end if-else
			}// end while

			// Hashes the key into the array at index h
			// and moves on to the next value to be hashed
			table[h] = key;
			temp++;
		}// end while
	}// end linearProbe

	/**
	 * Hashing values into an array with quadratic probing
	 * 
	 * @param table
	 *            - The array being looked at to hash into
	 * @param loadFact
	 *            - The number of values that the hashtable should have
	 * @param key
	 *            - The value being hashed into the array and being put into the
	 *            hashfunction
	 * @param h
	 *            - The result for the index under consideration after the key
	 *            goes through the hashfunction
	 * @param gen
	 *            - Generator for a value between 1 - 10,000
	 */
	private void quadProbe(int[] table, int loadFact) {
		int temp = 1;

		// Makes sure that the appropriate number of values are hashed
		while (temp <= loadFact) {

			genValue(table);

			int i = 1;

			boolean flag = checkEmpty(table, h);
			// As long as the h indexed spot is full AND the offset being
			// squared is within the range, a new h will be generated for the
			// key to go into
			while (flag == false && (i < ((table.length - 1) / 2))) {

				int turn = 0;
				// Decides whether to add or subtract the squared offset
				while (turn < 2) {
					// Add squared offset
					if (turn == 0) {
						overflowPos(i, table, flag, turn);
						// Subtract squared offset
					} else {
						overflowNeg(i, table, flag, turn);
					}// end if-else
					turn++;
				}// end while
				i++;
			}// end while
		}// end while

		// Hashes the key into the array at index h
		// and moves on to the next value to be hashed
		table[h] = key;
		temp++;
	}// end quadProbe

	/**
	 * Initializes the hashtable to all empty and sets the SIZE of the table --
	 * empty space == -1
	 * 
	 * 
	 * @param table
	 *            - Hashtable to be initialized
	 */
	private void initialize(int[] table) {

		for (int i = 0; i < table.length; i++) {
			table[i] = -1;
		}// end for

	}// end initialize

	/**
	 * Checks if the space in the hashtable is empty or not
	 * 
	 * @param table
	 * @param i
	 * @return - False if the spot isn't empty -- true if the spot if empty
	 */
	private boolean checkEmpty(int[] table, int i) {

		if (table[i] != -1)
			return false;
		else
			return true;
	}// end checkEmpty

	/**
	 * Generates a value between 1 - 10,000 which becomes the key, and checks
	 * the array for that value. If the value already exists in the hashtable,
	 * generates another value. This repeats until a unique value is found.
	 * 
	 * @param table
	 *            - The array being looked at to hash into
	 * @param gen
	 *            - Generator for a value between 1 - 10,000
	 * @param key
	 *            - The value being hashed into the array and being put into the
	 *            hashfunction
	 * @param h
	 *            - The result for the index under consideration after the key
	 *            goes through the hashfunction
	 */
	private void genValue(int[] table) {

		// Generates and creates the key and hash index
		Random gen = new Random();
		key = gen.nextInt(10000);
		h = key % table.length;

		// Checks if the hashtable already has this value in it
		// If it does, run the method again for a new value and check it
		for (int i = 0; i < table.length; i++) {

			if (table[i] == key) {
				genValue(table);
			}// end if
		}// end for
	}// end genValue

	/**
	 * Checks to see if adding the square for the quadratic probe causes the
	 * index h to go off the array (greater than the largest index of the
	 * array). If so, then adjusts it to the right location. If not, applies
	 * quadratic probing regularly.
	 * 
	 * @param i
	 *            - The value being squared and added (the offset for the
	 *            hashfunction)
	 * @param h
	 *            - The result for the index under consideration after the key
	 *            goes through the hashfunction
	 * @param table
	 *            - The array being looked at to hash into
	 */
	private void overflowPos(int i, int[] table, boolean flag, int turn) {

		h = h + (i ^ 2) % SIZE;

		flag = checkEmpty(table, h);

		if (flag == true) {

			turn = 1;
		}

	}// end overlowPos

	/**
	 * Checks to see if subtracting the square for the quadratic probe causes
	 * the index h to go off the array (become negative). If so, then adjusts it
	 * to the right location. If not, applies quadratic probing regularly.
	 * 
	 * @param i
	 *            - The value being squared and subtracted (the offset for the
	 *            hashfunction)
	 * @param h
	 *            - The result for the index under consideration after the key
	 *            goes through the hashfunction
	 * @param table
	 *            - The array being looked at to hash into
	 */
	private void overflowNeg(int i, int[] table, boolean flag, int turn) {

		// If there is overflow, adjust accordingly
		if ((h - (i ^ 2)) < 0) {
			
			h = (table.length + h) - (i ^ 2);
		// Otherwise just hash with a normal quadratic probing function
		} else {
			
			h = h - (i ^ 2);

		}// end if-else

		if (flag == true) {

			turn = 1;
		}

	}// end overflowNeg

	/**
	 * Returns a the first hashtable
	 * 
	 * @return Hashtable1
	 */
	public int[] getTable1() {
		return table1;
	}// end getTable1

	/**
	 * Returns a the second hashtable
	 * 
	 * @return Hashtable2
	 */
	public int[] getTable2() {
		return table2;
	}// end getTable2

	/**
	 * Returns a the third hashtable
	 * 
	 * @return Hashtable3
	 */
	public int[] getTable3() {
		return table3;
	}// end getTable3

	/**
	 * Returns a the fourth hashtable
	 * 
	 * @return Hashtable4
	 */
	public int[] getTable4() {
		return table4;
	}// end getTable4

}// end class Hashtable
