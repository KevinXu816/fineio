/*
 * Copyright 2014 Ben Manes. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fineio.v3.cache.core;

import com.fineio.v3.cache.core.WriteOrderDeque.WriteOrder;


import java.util.Deque;

/**
 * A linked deque implementation used to represent a write-order queue.
 *
 * @param <E> the type of elements held in this collection
 * @author ben.manes@gmail.com (Ben Manes)
 */
public final class WriteOrderDeque<E extends WriteOrder<E>> extends AbstractLinkedDeque<E> {

    @Override
    public boolean contains(Object o) {
        return (o instanceof WriteOrder<?>) && contains((WriteOrder<?>) o);
    }

    // A fast-path containment check
    boolean contains(WriteOrder<?> e) {
        return (e.getPreviousInWriteOrder() != null)
                || (e.getNextInWriteOrder() != null)
                || (e == first);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        return (o instanceof WriteOrder<?>) && remove((E) o);
    }

    // A fast-path removal
    public boolean remove(E e) {
        if (contains(e)) {
            unlink(e);
            return true;
        }
        return false;
    }

    @Override
    public E getPrevious(E e) {
        return e.getPreviousInWriteOrder();
    }

    @Override
    public void setPrevious(E e, E prev) {
        e.setPreviousInWriteOrder(prev);
    }

    @Override
    public E getNext(E e) {
        return e.getNextInWriteOrder();
    }

    @Override
    public void setNext(E e, E next) {
        e.setNextInWriteOrder(next);
    }

    /**
     * An element that is linked on the {@link Deque}.
     */
    public interface WriteOrder<T extends WriteOrder<T>> {

        /**
         * Retrieves the previous element or <tt>null</tt> if either the element is unlinked or the first
         * element on the deque.
         */
        T getPreviousInWriteOrder();

        /**
         * Sets the previous element or <tt>null</tt> if there is no link.
         */
        void setPreviousInWriteOrder(T prev);

        /**
         * Retrieves the next element or <tt>null</tt> if either the element is unlinked or the last
         * element on the deque.
         */
        T getNextInWriteOrder();

        /**
         * Sets the next element or <tt>null</tt> if there is no link.
         */
        void setNextInWriteOrder(T next);
    }
}