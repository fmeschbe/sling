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

package org.apache.sling.scripting.sightly.impl.engine.runtime;

import javax.script.Bindings;

import org.apache.sling.scripting.sightly.SightlyRuntime;
import org.apache.sling.scripting.sightly.render.RenderContext;
import org.apache.sling.scripting.sightly.render.RenderUnit;

/**
 * Rendering context for Sightly rendering units.
 * @see RenderUnit
 */
public class RenderContextImpl implements RenderContext {

    private final Bindings bindings;
    private final SightlyRuntime runtime;

    public RenderContextImpl(Bindings bindings, SightlyRuntime runtime) {
        this.bindings = bindings;
        this.runtime = runtime;
    }

    /**
     * Provide the bindings for this script
     * @return - the list of global bindings available to the script
     */
    @Override
    public Bindings getBindings() {
        return bindings;
    }

    /**
     * Get the available Sightly runtime
     * @return - an instance of the Sightly runtime
     */
    @Override
    public SightlyRuntime getRuntime() {
        return runtime;
    }

}
