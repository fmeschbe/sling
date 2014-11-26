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
package org.apache.sling.scripting.sightly.impl.compiler.api.expression.node;

import org.apache.sling.scripting.sightly.ObjectModel;

/**
 * Binary operators used in expressions
 */
public enum BinaryOperator {
    // logical conjunction
    AND {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return (objectModel.toBoolean(left)) ? right : left;
        }
    },
    // logical disjunction
    OR {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return (objectModel.toBoolean(left)) ? left : right;
        }
    },
    // string concatenation
    CONCATENATE
    {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return objectModel.toString(left).concat(objectModel.toString(right));
        }
    },
    // less-than
    LT {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return lt(left, right);
        }
    },
    // less or equal
    LEQ {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return leq(left, right);
        }
    },
    // greater than
    GT {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return !leq(left, right);
        }
    },
    // greater or equal
    GEQ {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return !lt(left, right);
        }
    },
    // equal
    EQ {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return eq(left, right);
        }
    },
    // not equal
    NEQ {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return !eq(left, right);
        }

    },
    // strict version of equality, restricted to just some types
    STRICT_EQ {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return strictEq(left, right);
        }
    },
    // strict version of the not-equal operator
    STRICT_NEQ
    {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return !strictEq(left, right);
        }
    },
    // addition
    ADD {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return adjust(objectModel.toNumber(left).doubleValue()
                + objectModel.toNumber(right).doubleValue());
        }
    },

    // difference
    SUB {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return adjust(objectModel.toNumber(left).doubleValue()
                - objectModel.toNumber(right).doubleValue());
        }
    },
    // multiplication
    MUL {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return adjust(objectModel.toNumber(left).doubleValue()
                * objectModel.toNumber(right).doubleValue());
        }
    },
    // floating point division
    DIV {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return adjust(objectModel.toNumber(left).doubleValue()
                / objectModel.toNumber(right).doubleValue());
        }
    },

    // integer division
    I_DIV {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return adjust(objectModel.toNumber(left).intValue()
                / objectModel.toNumber(right).intValue());
        }
    },

    // reminder
    REM
    {
        @Override
        public Object eval(ObjectModel objectModel, Object left, Object right) {
            return adjust(objectModel.toNumber(left).intValue()
                % objectModel.toNumber(right).intValue());
        }

    };

    private static boolean eq(Object left, Object right) {
        if (left == null) {
            return right == null;
        }
        return left.equals(right);
    }

    private static boolean lt(final Object left, final Object right) {
        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() < ((Number) right).doubleValue();
        }
        throw new UnsupportedOperationException("Invalid types in comparison. Comparison is supported for Number types only");
    }

    private static boolean leq(final Object left, final Object right) {
        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() <= ((Number) right).doubleValue();
        }
        throw new UnsupportedOperationException("Invalid types in comparison. Comparison is supported for Number types only");
    }


   private static boolean strictEq(Object left, Object right) {
       if (left instanceof Number && right instanceof Number) {
           return ((Number) left).doubleValue() == ((Number) right).doubleValue();
       }
       if (left instanceof String && right instanceof String) {
           return left.equals(right);
       }
       if (left instanceof Boolean && right instanceof Boolean) {
           return left.equals(right);
       }
       if (left == null && right == null) {
           return true;
       }
       if (left == null || right == null) {
           Object notNull = (left != null) ? left : right;
           if (notNull instanceof String || notNull instanceof Boolean || notNull instanceof Number) {
               return false;
           }
       }
       throw new UnsupportedOperationException("Invalid types in comparison. Equality is supported for String, Number & Boolean types");
   }

   private static Number adjust(double x) {
       if (Math.floor(x) == x) {
           return (int)x;
       }
       return x;
   }

     public abstract Object eval(ObjectModel objectModel, Object left, Object right);
}
