package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;
//CSE 373 Peter Schultz and Armin Rouz
//10/4/17
/**
 * See IDictionary for more details on what this class should do
 */
// TEST TEST TEST asdfjasdf;lkjasdf
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
	// You may not change or rename this field: we will be inspecting
	// it using our private tests.
	private Pair<K, V>[] pairs; // this is an array of pairs
	private int size;
	private int sizeUsed;
	// You're encouraged to add extra fields (and helper methods) though!

	public ArrayDictionary() {
		size = 10;
		sizeUsed = 0;
		pairs = makeArrayOfPairs(size);
	}

	/**
	 * This method will return a new, empty array of the given size that can
	 * contain Pair<K, V> objects.
	 *
	 * Note that each element in the array will initially be null.
	 */
	@SuppressWarnings("unchecked")
	private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
		// It turns out that creating arrays of generic objects in Java
		// is complicated due to something known as 'type erasure'.
		//
		// We've given you this helper method to help simplify this part of
		// your assignment. Use this helper method as appropriate when
		// implementing the rest of this class.
		//
		// You are not required to understand how this method works, what
		// type erasure is, or how arrays and generics interact. Do not
		// modify this method in any way.
		return (Pair<K, V>[]) (new Pair[arraySize]);

	}

	@Override
	public V get(K key) {
		//if(sizeUsed == 0) return null;
		int index = findKey(key);
		if(index == -1) throw new NoSuchKeyException();
		return pairs[index].value;
	}

	@Override
	public void put(K key, V value) {
		reallocate();
		if (containsKey(key)) {
			pairs[findKey(key)].value = value;
		} else {
			Pair<K, V> newPair = new Pair<K, V>(key, value);
			pairs[sizeUsed] = newPair;
			sizeUsed++;
		}
	}

	@Override
	public V remove(K key) {
		int index = findKey(key);
		if(index == -1) throw new NoSuchKeyException();
		Pair<K, V> temp = pairs[index];
		shift(index);
		return temp.value;
	}

	@Override
	public boolean containsKey(K key) {
		if (findKey(key) == -1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int size() {
		return sizeUsed;
	}

	private void reallocate() {
		if (sizeUsed == size) {
			Pair<K, V>[] replacement = makeArrayOfPairs(size * 2);
			for (int i = 0; i < sizeUsed; i++) {
				replacement[i] = pairs[i];
			}
			pairs = replacement;
			size = size * 2;
		}
	}

	private void shift(int index) {
		for (int i = index; i < sizeUsed; i++) {
			pairs[i] = pairs[i + 1];
		}
		sizeUsed--;
	}

	private int findKey(K key) {
		if (sizeUsed == 0)
			return -1;
		for(int i = 0; i < sizeUsed; i++) {
			if(key == null && pairs[i].key == key) {
				return i;
			} 
			if(pairs[i].key.equals(key)) {
				return i;
			}
		}
		return -1;
	}

	private static class Pair<K, V> {
		public K key;
		public V value;

		// You may add constructors and methods to this class as necessary.
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return this.key + "=" + this.value;
		}
	}
}
