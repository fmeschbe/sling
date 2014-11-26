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

import org.apache.sling.scripting.sightly.Record;
import org.apache.sling.scripting.sightly.render.RenderContext;

/**
 * Basic unit of rendering. This also extends the record interface.
 * The properties for a unit are the sub-units
 */
public interface RenderUnit extends Record<RenderUnit> {

    /**
     * Render the main script template
     * @param renderContext - the rendering context
     * @param arguments - the arguments for this unit
     */
    void render(RenderContext renderContext, Bindings arguments);

}
