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

import java.util.function.Supplier;

/**
 * The different read buffer strategies.
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
@SuppressWarnings("ImmutableEnumChecker")
public enum BufferType {
    Ticket(TicketBuffer::new),
    FastFlow(FastFlowBuffer::new),
    MpscArray(MpscArrayBuffer::new),
    ManyToOne(ManyToOneBuffer::new),
    ManyToOne_spaced(ManyToOneSpacedBuffer::new),
    MpmcArray(MpmcArrayBuffer::new),
    MpscCompound(MpscCompoundBuffer::new);

    private final Supplier<ReadBuffer<Boolean>> factory;

    BufferType(Supplier<ReadBuffer<Boolean>> factory) {
        this.factory = factory;
    }

    /**
     * Returns a new buffer instance.
     */
    public ReadBuffer<Boolean> create() {
        return factory.get();
    }
}