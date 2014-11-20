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
package org.apache.sling.scripting.sightly.impl.compiler.frontend;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.sling.scripting.sightly.impl.parser.expr.generated.SightlyLexer;
import org.apache.sling.scripting.sightly.impl.parser.expr.generated.SightlyParser;

public class ExpressionParserImpl implements ExpressionParser {

    public Interpolation parseInterpolation(String text) throws ParserException {
        SightlyParser parser = createParser(text);
        try {
            return parser.interpolation().interp;
        } catch (RecognitionException e) {
            throw new ParserException(e);
        }
    }

    private SightlyParser createParser(String str) {
        SightlyLexer lexer = new SightlyLexer(new ANTLRInputStream(str));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SightlyParser parser = new SightlyParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(new SightlyParserErrorListener());
        return parser;
    }

}
