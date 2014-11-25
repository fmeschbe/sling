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

package org.apache.sling.scripting.sightly.impl.compiler.api.expression;

import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.ArrayLiteral;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.BinaryOperation;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.BooleanConstant;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.Identifier;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.MapLiteral;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.NullLiteral;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.NumericConstant;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.PropertyAccess;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.RuntimeCall;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.StringConstant;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.TernaryOperator;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.UnaryOperation;

/**
 * Visitor for all expression nodes
 */
public interface NodeVisitor<T> {

    T evaluate(PropertyAccess propertyAccess);

    T evaluate(Identifier identifier);

    T evaluate(StringConstant text);

    T evaluate(BinaryOperation binaryOperation);

    T evaluate(BooleanConstant booleanConstant);

    T evaluate(NumericConstant numericConstant);

    T evaluate(UnaryOperation unaryOperation);

    T evaluate(TernaryOperator ternaryOperator);

    T evaluate(RuntimeCall runtimeCall);

    T evaluate(MapLiteral mapLiteral);

    T evaluate(ArrayLiteral arrayLiteral);

    T evaluate(NullLiteral nullLiteral);

}
