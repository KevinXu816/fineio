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

import com.fineio.v3.cache.core.CacheLoader;
import com.fineio.v3.cache.core.Caffeine;
import com.fineio.v3.cache.core.Ticker;
import java.lang.SuppressWarnings;

/**
 * <em>WARNING: GENERATED CODE</em>
 *
 * A cache that provides the following features:
 * <ul>
 *   <li>RefreshWrite
 *   <li>WeakKeys (inherited)
 *   <li>InfirmValues (inherited)
 *   <li>Listening (inherited)
 *   <li>Stats (inherited)
 *   <li>MaximumSize (inherited)
 * </ul>
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
@SuppressWarnings({"unchecked", "MissingOverride", "NullAway"})
final class WILSMSR<K, V> extends WILSMS<K, V> {
  final Ticker ticker;

  volatile long refreshAfterWriteNanos;

  WILSMSR(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
    super(builder, cacheLoader, async);
    this.ticker = builder.getTicker();
    this.refreshAfterWriteNanos = builder.getRefreshAfterWriteNanos();
  }

  public Ticker expirationTicker() {
    return ticker;
  }

  protected boolean refreshAfterWrite() {
    return true;
  }

  protected long refreshAfterWriteNanos() {
    return refreshAfterWriteNanos;
  }

  protected void setRefreshAfterWriteNanos(long refreshAfterWriteNanos) {
    this.refreshAfterWriteNanos = refreshAfterWriteNanos;
  }
}