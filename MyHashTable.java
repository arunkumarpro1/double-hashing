package com.arun;

public class MyHashTable<T> {

	private static final int DEFAULT_TABLE_SIZE = 200100;

	private T[] doubleHashArray; // The array of elements
	private int size; // Current size

	/**
	 * Construct the hash table.
	 */
	public MyHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}

	/**
	 * Construct the hash table.
	 * 
	 * @param size
	 * 
	 */
	public MyHashTable(int size) {
		formNewArray(size);
		doClear();
	}

	/**
	 * Insert into the hash table. If the item is already present, do nothing.
	 */
	public boolean insert(T x) {
		// Insert x
		int currentPos = findPos(x);
		if (doubleHashArray[currentPos] == x)
			return false;

		doubleHashArray[currentPos] = x;
		size++;

		// Rehash if the table is half full
		if (size > doubleHashArray.length / 2)
			rehash();

		return true;
	}

	/**
	 * Expand the array and rehash all the old values.
	 */
	private void rehash() {
		T[] oldArray = doubleHashArray;

		// Create a new double-sized, empty table
		formNewArray(2 * oldArray.length);
		size = 0;

		// Copy the old array
		for (T item : oldArray)
			if (item != null)
				insert(item);
	}

	/**
	 * Method that performs quadratic probing resolution.
	 * 
	 */
	private int findPos(T x) {
		int currentPos = firstHash(x);
		int probeValue = secondHash(x);

		while (doubleHashArray[currentPos] != null
				&& !doubleHashArray[currentPos].equals(x)) {
			currentPos += probeValue; // ith probe
			if (currentPos >= doubleHashArray.length)
				currentPos -= doubleHashArray.length;
		}

		return currentPos;
	}

	/**
	 * Remove from the hash table.
	 */
	public boolean remove(T x) {
		int currentPos = findPos(x);
		if (doubleHashArray[currentPos] != null) {
			doubleHashArray[currentPos] = null;
			size--;
			return true;
		} else
			return false;
	}

	/**
	 * @return the size.
	 */
	public int capacity() {
		return doubleHashArray.length;
	}

	/**
	 * Find an item in the hash table.
	 * 
	 * @param x
	 * @return
	 */
	public boolean contains(T x) {
		int currentPos = findPos(x);
		if (doubleHashArray[currentPos] != null)
			return true;
		return false;
	}

	/**
	 * Make the hash table logically empty.
	 */
	public void makeEmpty() {
		doClear();
	}

	private void doClear() {
		size = 0;
		for (int i = 0; i < doubleHashArray.length; i++)
			doubleHashArray[i] = null;
	}

	private int firstHash(T x) {
		int hashVal = Math.abs(x.hashCode());

		hashVal %= doubleHashArray.length;
		if (hashVal < 0)
			hashVal += doubleHashArray.length;

		return hashVal;
	}

	/**
	 * Second Hash function when a collision occurs
	 * 
	 * @param x
	 * @return
	 */
	private int secondHash(T x) {
		int R = previousPrime(doubleHashArray.length - 1);
		int hashVal = Math.abs(x.hashCode());
		hashVal %= R;
		// hashVal += 1;
		hashVal = R - hashVal;
		return hashVal;
	}

	/**
	 * Internal method to allocate array.
	 * 
	 * @param arraySize
	 *            the size of the array.
	 */
	@SuppressWarnings("unchecked")
	private void formNewArray(int arraySize) {
		doubleHashArray = (T[]) new Object[nextPrime(arraySize)];
	}

	/**
	 * Internal method to find a prime number that at least as large as n.
	 * 
	 */
	private static int nextPrime(int n) {
		if (n % 2 == 0)
			n++;

		for (; !isPrime(n); n += 2)
			;

		return n;
	}

	/**
	 * Internal method to find a prime number that is smaller than n.
	 * 
	 * @param n
	 */
	private static int previousPrime(int n) {
		if (n % 2 == 0)
			n--;

		for (; !isPrime(n); n -= 2)
			;

		return n;
	}

	/**
	 * Internal method to test if a number is prime. Not an efficient algorithm.
	 */
	private static boolean isPrime(int n) {
		if (n == 2 || n == 3)
			return true;

		if (n == 1 || n % 2 == 0)
			return false;

		for (int i = 3; i * i <= n; i += 2)
			if (n % i == 0)
				return false;

		return true;
	}
}
