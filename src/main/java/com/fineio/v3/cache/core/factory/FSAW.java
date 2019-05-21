// Copyright 2019 Ben Manes. All Rights Reserved.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.fineio.v3.cache.core.factory;

import com.fineio.v3.cache.base.UnsafeAccess;
import com.fineio.v3.cache.core.Node;
import java.lang.Object;
import java.lang.SuppressWarnings;
import java.lang.ref.ReferenceQueue;

/**
 * <em>WARNING: GENERATED CODE</em>
 *
 * A cache entry that provides the following features:
 * <ul>
 *   <li>ExpireWrite
 *   <li>WeakKeys (inherited)
 *   <li>StrongValues (inherited)
 *   <li>ExpireAccess (inherited)
 * </ul>
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
@SuppressWarnings({"unchecked", "PMD.UnusedFormalParameter", "MissingOverride", "NullAway"})
class FSAW<K, V> extends FSA<K, V> {
  protected static final long WRITE_TIME_OFFSET = UnsafeAccess.objectFieldOffset(FSAW.class, LocalCacheFactory.WRITE_TIME);

  volatile long writeTime;

  Node<K, V> previousInWriteOrder;

  Node<K, V> nextInWriteOrder;

  FSAW() {
  }

  FSAW(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
    super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, now);
  }

  FSAW(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
    super(keyReference, value, valueReferenceQueue, weight, now);
    UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, now);
  }

  public Node<K, V> getPreviousInVariableOrder() {
    return previousInWriteOrder;
  }

  public void setPreviousInVariableOrder(Node<K, V> previousInWriteOrder) {
    this.previousInWriteOrder = previousInWriteOrder;
  }

  public Node<K, V> getNextInVariableOrder() {
    return nextInWriteOrder;
  }

  public void setNextInVariableOrder(Node<K, V> nextInWriteOrder) {
    this.nextInWriteOrder = nextInWriteOrder;
  }

  public long getVariableTime() {
    return UnsafeAccess.UNSAFE.getLong(this, WRITE_TIME_OFFSET);
  }

  public void setVariableTime(long writeTime) {
    UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, writeTime);
  }

  public boolean casVariableTime(long expect, long update) {
    return (writeTime == expect)
        && UnsafeAccess.UNSAFE.compareAndSwapLong(this, WRITE_TIME_OFFSET, expect, update);
  }

  public final long getWriteTime() {
    return UnsafeAccess.UNSAFE.getLong(this, WRITE_TIME_OFFSET);
  }

  public final void setWriteTime(long writeTime) {
    UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, writeTime);
  }

  public final Node<K, V> getPreviousInWriteOrder() {
    return previousInWriteOrder;
  }

  public final void setPreviousInWriteOrder(Node<K, V> previousInWriteOrder) {
    this.previousInWriteOrder = previousInWriteOrder;
  }

  public final Node<K, V> getNextInWriteOrder() {
    return nextInWriteOrder;
  }

  public final void setNextInWriteOrder(Node<K, V> nextInWriteOrder) {
    this.nextInWriteOrder = nextInWriteOrder;
  }

  public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
    return new FSAW<>(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
  }

  public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
    return new FSAW<>(keyReference, value, valueReferenceQueue, weight, now);
  }
}
