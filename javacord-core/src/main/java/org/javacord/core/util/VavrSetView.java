package org.javacord.core.util;

import io.vavr.collection.HashSet;
import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.TreeSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An immutable view that maps a Vavr set to a Java set.
 *
 * @param <E> The type of elements maintained by this set.
 */
public class VavrSetView<E> implements Set<E> {

    private io.vavr.collection.Set<E> vavrSet;
    private final boolean unmodifiable;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Creates a new Vavr set view.
     *
     * @param vavrSet The Vavr set.
     * @param unmodifiable Whether the view should be modifiable or not.
     */
    public VavrSetView(io.vavr.collection.Set<E> vavrSet, boolean unmodifiable) {
        this.vavrSet = vavrSet;
        this.unmodifiable = unmodifiable;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return vavrSet.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return vavrSet.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return vavrSet.contains((E) o);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>Implementation Note:</b> Modifications to the underlying set do not affect the {@link Iterator#hasNext()}
     * and {@link Iterator#next()} methods.
     */
    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new VavrSetViewIterator();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return vavrSet.toJavaArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            if (a.length < size()) {
                return (T[]) Arrays.copyOf(toArray(), size(), a.getClass());
            }
            System.arraycopy(toArray(), 0, a, 0, size());
            if (a.length > size()) {
                a[size()] = null;
            }
            return a;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        throwIfUnmodifiable();
        lock.writeLock().lock();
        try {
            int oldSize = vavrSet.size();
            vavrSet = vavrSet.add(e);
            return oldSize != vavrSet.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        throwIfUnmodifiable();
        lock.writeLock().lock();
        try {
            int oldSize = vavrSet.size();
            vavrSet = vavrSet.remove((E) o);
            return oldSize != vavrSet.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return c.stream().allMatch(this::contains);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throwIfUnmodifiable();
        lock.writeLock().lock();
        try {
            int oldSize = vavrSet.size();
            vavrSet = vavrSet.addAll(c);
            return oldSize != vavrSet.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        throwIfUnmodifiable();
        lock.writeLock().lock();
        try {
            int oldSize = vavrSet.size();
            vavrSet = vavrSet.retainAll((Collection<E>) c);
            return oldSize != vavrSet.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        throwIfUnmodifiable();
        lock.writeLock().lock();
        try {
            int oldSize = vavrSet.size();
            vavrSet = vavrSet.removeAll((Collection<E>) c);
            return oldSize != vavrSet.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        throwIfUnmodifiable();
        lock.writeLock().lock();
        try {
            if (vavrSet instanceof HashSet) {
                vavrSet = HashSet.empty();
            } else if (vavrSet instanceof TreeSet) {
                vavrSet = TreeSet.empty(((TreeSet<E>) vavrSet).comparator());
            } else if (vavrSet instanceof LinkedHashSet) {
                vavrSet = LinkedHashSet.empty();
            } else {
                vavrSet = vavrSet.removeAll(vavrSet);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void throwIfUnmodifiable() {
        if (unmodifiable) {
            throw new UnsupportedOperationException();
        }
    }

    private class VavrSetViewIterator implements Iterator<E> {

        Iterator<E> vavrIterator = vavrSet.iterator();
        E currentElement = null;

        @Override
        public boolean hasNext() {
            return vavrIterator.hasNext();
        }

        @Override
        public E next() {
            currentElement = vavrIterator.next();
            return currentElement;
        }

        @Override
        public void remove() {
            throwIfUnmodifiable();
            if (currentElement == null) {
                throw new IllegalStateException();
            }
            VavrSetView.this.remove(currentElement);
            currentElement = null;
        }
    }
}
