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
package com.fineio.v3.cache.core.testing;

import com.fineio.v3.cache.core.CacheWriter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A test method with this annotation has indicated that no {@link CacheWriter} operations
 * occurred and the {@link CacheValidationListener} should verify that expectation.
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
@Target(METHOD) @Retention(RUNTIME)
public @interface CheckNoWriter {}