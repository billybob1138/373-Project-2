package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;


import java.util.Iterator;
import java.util.NoSuchElementException;

//CSE 373 Peter Schultz and Armin Rouz
//10/4/17
/**
 * Note: For more info on the expected behavior of your methods, see the source
 * code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
	// You may not rename these fields or change their types.
	// We will be inspecting these in our private tests.
	// You also may not add any additional fields.
	private Node<T> front;
	private Node<T> back;
	private int size;

	public DoubleLinkedList() {
		this.front = null;
		this.back = null;
		this.size = 0;
	}

	@Override
	public void add(T item) {
		Node<T> newNode = new Node<T>(item);

		if (size == 0) {
			front = newNode;
			back = newNode;
		} else {
			back.next = newNode;
			newNode.prev = back;
			back = newNode;
		}
		size++;
	}

	@Override
	public T remove() {
		if (size == 0) {
			throw new EmptyContainerException();
		}
		if (size == 1) {
			T temp = front.data;
			back = null;
			front = null;
			size--;
			return temp;
		}
		Node<T> temp = back;
		back = back.prev;
		back.next = null;
		size--;
		return temp.data;
	}

	@Override
	public T get(int index) {
		if (index > size - 1 || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			return front.data;
		}
		Node<T> cur = front.next;
		for (int i = 1; i < index; i++) {
			cur = cur.next;
		}
		return cur.data;
	}

	@Override
	public void set(int index, T item) {
		if (index > size - 1 || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> newNode = new Node<T>(item);
		Node<T> cur = front;

		if (index == 0) {
			if (size == 1) {
				front = newNode;
				back = newNode;
			} else {
				newNode.next = front.next;
				newNode.next.prev = newNode;
				front.next = null;
				front = newNode;
			}
		} else if (index == this.size - 1) {
			cur = back.prev;
			back.prev = null;
			cur.next = newNode;
			newNode.prev = cur;
			back = newNode;
		} else {
			// happy case
			for (int i = 0; i < index; i++) {
				cur = cur.next;
			}
			Node<T> curPrev = cur.prev;
			Node<T> curNext = cur.next;
			cur.prev = null;
			cur.next = null;
			curPrev.next = newNode;
			curNext.prev = newNode;
			newNode.prev = curPrev;
			newNode.next = curNext;
		}
	}

	@Override
	public void insert(int index, T item) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> newNode = new Node<T>(item);
		if (size == 0) {
			add(item);
		} else {
			Node<T> cur = front;
			if (index == 0) { // front case
				newNode.next = front;
				front.prev = newNode;
				front = newNode;

			} else if (index == size) { // back case
				back.next = newNode;
				newNode.prev = back;
				back = newNode;
			} else { // happy case
				if (index > size / 2) {
					cur = back;
					for (int i = 0; i < size - index; i++) {
						cur = cur.prev;
					}
				} else {
					for (int i = 0; i < index; i++) {
						cur = cur.next;
					}
				}
				Node<T> curPrev = cur.prev;
				newNode.next = cur;
				newNode.prev = curPrev;
				curPrev.next = newNode;
				cur.prev = newNode;
			}
			size++;
		}

	}

	@Override
	public T delete(int index) {
		if (index > size - 1 || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		if (size == 0) {
			return null;
		}
		Node<T> cur = front;
		if (index == 0) {
			if (size > 1) {
				front = front.next;
				front.prev = null;
				size--;
				return cur.data;
			} else {
				T temp = front.data;
				front = null;
				back = null;
				size--;
				return temp;
			}
		} else if (index == size - 1) { // back case
			return remove();
		} else { // happy case
			if (index < size / 2) {
				for (int i = 0; i < index; i++) {
					cur = cur.next;
				}
			} else {
				cur = back;
				for (int i = 0; i < size - index - 1; i++) {
					cur = cur.prev;
				}
			}
			Node<T> curPrev = cur.prev;
			Node<T> curNext = cur.next;
			curPrev.next = curNext;
			curNext.prev = curPrev;
			cur.prev = null;
			cur.next = null;
			size--;
			return cur.data;
		}
	}

	@Override
	public int indexOf(T item) {
		Node<T> cur = front;
		for (int i = 0; i < size; i++) {
			if (cur == null) {
				return -1;
			}
			if (item != null) {
				if (cur.data.equals(item)) {
					return i;
				}

			} else {
				if (cur.data == null) {
					return i;
				}
			}
			cur = cur.next;
		} // what if item does not exist
		return -1;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean contains(T other) {
		Node<T> cur = front;
		for (int i = 0; i < size; i++) {
			if (cur.data == null || cur.data.equals(other)) {
				return true;
			}
			cur = cur.next;
		}
		return false;

	}

	@Override
	public Iterator<T> iterator() {
		// Note: we have provided a part of the implementation of
		// an iterator for you. You should complete the methods stubs
		// in the DoubleLinkedListIterator inner class at the bottom
		// of this file. You do not need to change this method.
		return new DoubleLinkedListIterator<>(this.front);
	}

	private static class Node<E> {
		// You may not change the fields in this node or add any new fields.
		public final E data;
		public Node<E> prev;
		public Node<E> next;

		public Node(Node<E> prev, E data, Node<E> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}

		public Node(E data) {
			this(null, data, null);
		}

		// Feel free to add additional constructors or methods to this class.
	}

	private static class DoubleLinkedListIterator<T> implements Iterator<T> {
		// You should not need to change this field, or add any new fields.
		private Node<T> current;

		public DoubleLinkedListIterator(Node<T> current) {
			// You do not need to make any changes to this constructor.
			this.current = current;
		}

		/**
		 * Returns 'true' if the iterator still has elements to look at; returns
		 * 'false' otherwise.
		 */
		public boolean hasNext() {
			// if (current.next == null) {
			// return false;
			// } else {
			// return true;
			// }
			return current != null;
			// throw new NotYetImplementedException();
		}

		/**
		 * Returns the next item in the iteration and internally updates the
		 * iterator to advance one element forward.
		 *
		 * @throws NoSuchElementException
		 *             if we have reached the end of the iteration and there are
		 *             no more elements to look at.
		 */
		public T next() { // needs to be different for first call??
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T temp = current.data;
			this.current = current.next;
			return temp;
		}
	}
}
