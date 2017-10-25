package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    int size;
    int numElements;
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
    	size = 0;
    	numElements = 0;
        chains = makeArrayOfChains(10);
    }

    private void resize() {
    	IDictionary<K,V>[] oldChains = chains;
    	// NEED TO MAKE A NEW ARRAY OF CHAINS AND PUT IN THE CONTENTS OF THE OLD CHAINS
    	// ARRAY IN THE NEW SPOTS BASED ON THE NEW HASH FUNCTION!
    	chains = makeArrayOfChains(oldChains.length * 2);
    	for(int i = 0; i < oldChains.length; i++) {
    		if(chains[i] != null) {
	    		for(KVPair<K,V> pair : chains[i]) {
	    			put(pair.getKey(), pair.getValue());
	    		}
    		}
    	}
    }
    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
    	if(key == null) {
    		return chains[0].get(key);
    	}
    	if(containsKey(key)) {
    		return chains[key.hashCode() % chains.length].get(key);
    	} else {
    		throw new NoSuchKeyException();
    	}
    }

    @Override
    public void put(K key, V value) {
    	if(numElements >= chains.length * 10) {
    		resize();
    	}
    	int index;
    	if(key == null) {
    		index = 0;
    	} else {
    		index = key.hashCode() % chains.length;
    	}
    	if(chains[index] == null) {
    		size++;
    		chains[index] = new ArrayDictionary<K,V>();
    	}
    	numElements++;
    	chains[index].put(key, value);
        
    }

    @Override
    public V remove(K key) {
    	if(containsKey(key)) {
    		V value;
    		if(key == null) {
        		value = chains[0].remove(key);
        		
        	} else {
        		value = chains[key.hashCode() % chains.length].remove(key);
        	}
    		return value;
    	} else {
    		throw new NoSuchKeyException();
    	}
    }

    @Override
    public boolean containsKey(K key) {
    	for(int i = 0; i < chains.length; i++) {
    		
			if(chains[i] != null && chains[i].containsKey(key)) {
				return true;
			}
    		
    	}
    	return false;
    }

    @Override
    public int size() {
    	return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int iteratorIndex = -1;
        private IDictionary<K,V> current;
        private Iterator<KVPair<K,V>> iterator;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            current = new ArrayDictionary<K,V>();
            iterator = current.iterator();
        }

        @Override
        public boolean hasNext() {
        	if(iterator.hasNext()) {
        		return true;
        	}
        	
            for(int i = iteratorIndex; i < chains.length; i++) {
            	
            	if(!chains[i].isEmpty()) {
            		iteratorIndex = i;
            		return true;
            	}
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
        	if(!iterator.hasNext()) {
        		if(hasNext()) {
        			iterator = chains[iteratorIndex].iterator();
        		} else {
        			throw new NoSuchElementException();
        		}
	            
        	} 
        	return iterator.next();
        }
    }
}
