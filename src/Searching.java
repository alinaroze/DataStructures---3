/**
 * Main class that builds and searches the hashtables
 * 
 * @author Alina Rozenbaum
 * 
 */
public class Searching {

	private static final int SIXTY = 611, EIGHTY = 815;

	/**
	 * Main method to run the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Searching hash = new Searching();
		hash.run();

	}

	/**
	 * Runs the program: creates and searches the hashtables
	 */
	private void run() {

		Hashtables hash = new Hashtables();

		//Builds the hashtables
		hash.createLinear(hash.getTable1(), SIXTY);
		hash.createLinear(hash.getTable2(), EIGHTY);
		hash.createLinear(hash.getTable3(), SIXTY);
		hash.createLinear(hash.getTable4(), EIGHTY);

		//Searches the hashtables
		///and prints out the result
		System.out
				.println("The results for a hashtable 60% full with linear probing are:\n\n");
		search(hash.getTable1(), 1);

		System.out
				.println("The results for a hashtable 80% full with linear probing are:\n\n");
		search(hash.getTable2(), 1);

		System.out
				.println("The results for a hashtable 60% full with quadratic probing are:\n\n");
		search(hash.getTable3(), 2);

		System.out
				.println("The results for a hashtable 80% full with quadratic probing are:\n\n");
		search(hash.getTable4(), 2);

	}//end run

	/**
	 * Searches the hashtables and puts the results into an array and then
	 * prints the results for the user
	 * 
	 * @param table
	 *            - Hashtable to be searched
	 */
	private void search(int[] table, int which) {

		// double[] results holds the number of:

		// 0: probes for a successful search
		// 1: probes for an unsuccessful search
		// 2: Whether key was found - 1
		// or not - 0

		// double[] output displays number of:

		// 0: total probes for a successful search
		// 1: total probes for an unsuccessful search
		// 2: total successful searches
		// 3: total unsuccessful searches

		// The array of all the outputs stated above
		double[] output = new double[4];

		for (int k = 1; k <= 10000; k++) {

			// temporary array that holds the outputs
			// for each key value
			double[] temp = found(table, k, which);

			// successful search
			if (temp[2] == 1) {

				output[0] = output[0] + temp[0];
				output[2]++;

				// unsuccessful searches
			} else {

				output[1] = output[1] + temp[1];
				output[3]++;
			}//end if-else
		}//end for

		// stores the output for the probe averages
		double[] averages = findAvg(output);

		// uses the display method to show the user the averages and output
		display(averages);

	}// end search

	/**
	 * Finds the average of the total successful and unsuccessful probes
	 * 
	 * @param out
	 *            - The array with the results from the search
	 * @return - The array with the averages for a successful and unsuccessful
	 */
	private double[] findAvg(double[] out) {
		out[0] = out[0] / out[2];
		out[1] = out[1] / out[3];

		return out;
	}

	/**
	 * Decides whether the array is linear or quadratic probing and then applies
	 * the appropriate searching function to the hashtable
	 * 
	 * @param table
	 *            - The hashtable to be searched
	 * @param k
	 *            - The key that is being searched for
	 * @param which
	 *            - Whether the hashfunction is linear or quadratic
	 * @return - The array with the results for finding the key
	 */
	private double[] found(int[] table, int k, int which) {

		// double[] results holds the number of:

		// 0: probes for a successful search
		// 1: probes for an unsuccessful search
		// 2: Whether key was found - 1
		// or not - 0
		double[] results = new double[3];

		// Calculated hash value
		int h = k % table.length;

		// If linear or quadratic
		if (which == 1) {

			results = foundLinear(table, results, k, h);

		} else {

			results = foundQuad(table, results, k, h);
		}// end if-else

		return results;
	}// end found

	/**
	 * Goes through the hashtable linearly to find the keys
	 * 
	 * @param table
	 *            - The hashtable to be searched
	 * @param results
	 *            - The array where the results are to be stored
	 * @param k
	 *            - The key to be searched for
	 * @param h
	 *            - The hash index to start at
	 * @return - The array with the results stored
	 */
	private double[] foundLinear(int[] table, double[] results, int k, int h) {

		for (int i = h; i < table.length; i++) {

			// If the key is found, change the found value to 1
			// and change the
			if (k == table[i]) {
				results[0] = results[1] + 1;
				results[2] = 1;
				return results;
			} else {
				results[1]++;
			}// end if-else
		}// end for
		return results;

	}// end foundLinear

	/**
	 * Goes through the hashtable quadratically to find the keys
	 * 
	 * @param table
	 *            - The hashtable to be searched
	 * @param results
	 *            - The array where the results are to be stored
	 * @param k
	 *            - The key to be searched for
	 * @param h
	 *            - The hash index to start at
	 * @return - The array with the results stored
	 */
	private double[] foundQuad(int[] table, double[] results, int k, int h) {

		// Starts at a 0*0 offset and goes until the the key is found (or not at
		// all)
		for (int i = 0; i < ((table.length - 1) / 2); i++) {

			int turn = 0;

			// Decides whether to add or subtract the squared offset
			while (turn != 2) {

				// Add squared offset
				if (turn == 0) {

					// If there is overflow, adjust accordingly
					if ((h + (i ^ 2)) > table.length - 1) {

						h = (h + (i ^ 2)) - (table.length - 1);
						if (k == table[h]) {
							results[0] = results[1] + 1;
							results[2] = 1;
							return results;
						} else {
							results[1]++;
						}

						// Otherwise just hash with a normal quadratic
						// probing function
					} else {

						h = h + (i ^ 2);
						if (k == table[h]) {
							results[0] = results[1] + 1;
							results[2] = 1;
							return results;
						} else {
							results[1]++;
						}

					}// end if-else

					// Subtract squared offset
				} else {

					// If there is overflow, adjust accordingly
					if ((h - (i ^ 2)) < 0) {

						h = (table.length + h) - (i ^ 2);
						if (k == table[h]) {
							results[0] = results[1] + 1;
							results[2] = 1;
							return results;
						} else {
							results[1]++;
						}

						// Otherwise just hash with a normal quadratic
						// probing function
					} else {

						h = h - (i ^ 2);
						if (k == table[h]) {
							results[0] = results[1] + 1;
							results[2] = 1;
							return results;
						} else {
							results[1]++;
						}

					}// end if-else

				}// end if-else
				turn++;
			}// end while

		}// end for
		return results;
	}// end foundQuad

	/**
	 * Prints the results for the user to see
	 * 
	 * @param out
	 *            - The array of results to be printed
	 */
	private void display(double[] out) {

		System.out
				.println("The number of successful probes was:"
						+ out[2]
						+ "\n"
						+ "The number of unsuccessful probes was:"
						+ out[3]
						+ "\n"
						+ "The average number of probes for a successful search was:"
						+ out[0]
						+ "\n"
						+ "The average number of probes for an unsuccessful search was:"
						+ out[1] + "\n\n\n");
	}// end display

}