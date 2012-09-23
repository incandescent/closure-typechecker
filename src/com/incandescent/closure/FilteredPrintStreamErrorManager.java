/**
 * Copyright 2012 Incandescent Software LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.incandescent.closure;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.PrintStreamErrorManager;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.UnknownType;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Filters errors by source file
 */
public class FilteredPrintStreamErrorManager extends PrintStreamErrorManager {
    protected List<Pattern> includePatterns = new ArrayList<Pattern>();
    protected List<Pattern> excludePatterns = new ArrayList<Pattern>();

    public List<Node> propErrors = new ArrayList<Node>();

    public FilteredPrintStreamErrorManager(PrintStream stream, String[] include, String[] exclude) {
        super(stream);
        addRegexes(include, includePatterns);
        addRegexes(exclude, excludePatterns);
    }

    protected void addRegexes(String[] regexes, List<Pattern> patterns) {
        if (regexes != null) {
            for (String s: regexes) {
                patterns.add(Pattern.compile(s, Pattern.CASE_INSENSITIVE));
            }
        }
    }

    protected boolean match(List<Pattern> patterns, String str) {
        for (Pattern pattern: patterns) {
            if (pattern.matcher(str).matches()) return true;
        }
        return false;
    }

    @Override
    public void report(CheckLevel level, JSError error) {
        if (error.sourceName != null) {
            if (includePatterns.size() > 0) {
                if (!match(includePatterns, error.sourceName)) {
                    return;
                }
            }
            if (excludePatterns.size() > 0) {
                if (match(excludePatterns, error.sourceName)) {
                    return;
                }
            }
        }

        /*try {
            Field f = JSError.class.getDeclaredField("node");
            f.setAccessible(true);
            Node node = (Node) f.get(error);
            if (node.getType() == Token.GETPROP) {
                propErrors.add(node);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        super.report(level, error);
    }
}
