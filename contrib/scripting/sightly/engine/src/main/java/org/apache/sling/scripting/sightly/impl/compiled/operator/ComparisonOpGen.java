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

package org.apache.sling.scripting.sightly.impl.compiled.operator;

import org.apache.sling.scripting.sightly.ObjectModel;
import org.apache.sling.scripting.sightly.impl.compiled.ExpressionTranslator;
import org.apache.sling.scripting.sightly.impl.compiled.JavaSource;
import org.apache.sling.scripting.sightly.impl.compiled.SourceGenConstants;
import org.apache.sling.scripting.sightly.impl.compiled.Type;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.ExpressionNode;
import org.apache.sling.scripting.sightly.impl.compiler.api.expression.node.BinaryOperator;
import org.apache.sling.scripting.sightly.impl.compiler.util.expression.SideEffectVisitor;

/**
 * Generator for logical operators
 */
public class ComparisonOpGen implements BinaryOpGen {

    private final String javaOperator;
    private final boolean inverted;
    private final String runtimeMethod;

    public ComparisonOpGen(BinaryOperator operator) {
        switch (operator) {
            case LT: runtimeMethod = ObjectModel.LT; inverted = false; javaOperator = "<"; break;
            case GT: runtimeMethod = ObjectModel.LEQ; inverted = true; javaOperator = ">"; break;
            case LEQ: runtimeMethod = ObjectModel.LEQ; inverted = false; javaOperator = "<="; break;
            case GEQ: runtimeMethod = ObjectModel.LT; inverted = true; javaOperator = ">="; break;
            default: throw new IllegalArgumentException("Operator is not a comparison operator: " + operator);
        }
    }

    @Override
    public Type returnType(Type left, Type right) {
        return Type.BOOLEAN;
    }

    @Override
    public void generate(JavaSource source, ExpressionTranslator visitor, TypedNode left, TypedNode right) {
        Type type = OpHelper.sameType(left, right);
        if (OpHelper.isNumericType(type)) {
            generateWithOperator(source, visitor, left.getNode(), right.getNode());
        } else {
            generateGeneric(source, visitor, left.getNode(), right.getNode());
        }
    }

    private void generateGeneric(JavaSource source, SideEffectVisitor visitor, ExpressionNode leftNode, ExpressionNode rightNode) {
        if (inverted) {
            source.negation().startExpression();
        }
        source.startMethodCall(SourceGenConstants.OBJ_MODEL_INSTANCE, runtimeMethod);
        leftNode.accept(visitor);
        source.separateArgument();
        rightNode.accept(visitor);
        source.endCall();
        if (inverted) {
            source.endExpression();
        }
    }

    private void generateWithOperator(JavaSource source, SideEffectVisitor visitor,
                                      ExpressionNode leftNode, ExpressionNode rightNode) {
        leftNode.accept(visitor);
        source.append(javaOperator);
        rightNode.accept(visitor);
    }

}
