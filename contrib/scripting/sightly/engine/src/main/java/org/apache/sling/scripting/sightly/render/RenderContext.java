/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package org.apache.sling.scripting.sightly.render;

import javax.script.Bindings;

import org.apache.sling.scripting.sightly.impl.engine.runtime.RenderUnit;

import aQute.bnd.annotation.ProviderType;

/**
 * The {@code RenderContext} defines the context for executing Sightly scripts (see {@link RenderUnit}).
 */
@ProviderType
public interface RenderContext {

    /**
     * Returns the map of script bindings available to Sightly scripts.
     *
     * @return the global bindings for a script
     */
    Bindings getBindings();

 }
