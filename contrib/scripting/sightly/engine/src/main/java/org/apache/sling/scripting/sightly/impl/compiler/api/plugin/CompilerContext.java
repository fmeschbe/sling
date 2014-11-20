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
package org.apache.sling.scripting.sightly.impl.compiler.api.plugin;

import org.apache.sling.scripting.sightly.impl.compiler.api.expression.Expression;

/**
 * Compiler context for plugins
 */
public interface  CompilerContext {

    /**
     * Generate a unique variable name
     * @param hint - a hint to be added to the variable name. Leave null
     *             for no hint
     * @return the variable name
     */
    String generateVariable(String hint);

    /**
     * Adjust the expression node to the specifics of the given context
     * @param expression - the expression to be changed
     * @param context - the render context
     * @return - the adjusted expression
     */
    Expression adjustToContext(Expression expression, MarkupContext context);
}
