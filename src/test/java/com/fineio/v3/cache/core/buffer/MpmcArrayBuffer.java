/*
 * Copyright 2015 Ben Manes. All Rights Reserved.
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
package com.fineio.v3.cache.core.buffer;


import com.fineio.v3.cache.core.ReadBuffer;
import org.jctools.queues.MpmcArrayQueue;

import java.util.function.Consumer;

/**
 * @author ben.manes@gmail.com (Ben Manes)
 */
final class MpmcArrayBuffer<E> extends ReadBuffer<E> {
  final MpmcArrayQueue<E> queue;
  long drained;

  MpmcArrayBuffer() {
    queue = new MpmcArrayQueue<>(BUFFER_SIZE);
  }

  @Override
  public int offer(E e) {
    return queue.offer(e) ? SUCCESS : FULL;
  }

  @Override
  public void drainTo(Consumer<E> consumer) {
    E e = null;
    while ((e = queue.poll()) != null) {
      consumer.accept(e);
      drained++;
    }
  }

  @Override
  public int reads() {
    return (int) drained;
  }

  @Override
  public int writes() {
    return drained() + queue.size();
  }
}