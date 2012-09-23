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

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckTypes extends CommandLineRunner {
    private static final String ANY = ".*";

    protected static String[] includes;
    protected static String[] excludes;

    // treat non-flag options as js source files
    // ex: a.js b.js c.js
    // ex: --some-flag2 1 --some-flag2 2 -- a.js b.js c.js
    static String[] parseCommandLine(String[] args) {
        boolean dashdashEncountered = false;
        List<String> beforeDashDash = new ArrayList<String>();
        List<String> afterDashDash = new ArrayList<String>();

        List<String> inc = new ArrayList<String>();
        List<String> exc = new ArrayList<String>();

        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if ("--report-include".equals(arg)) {
                i++;
                String regex = ANY + args[i] + ANY;
                System.err.println("Including in report: " + regex);
                inc.add(regex);
            } else if ("--report-exclude".equals(arg)) {
                i++;
                String regex = ANY + args[i] + ANY;
                System.err.println("Excluding from report: " + regex);
                exc.add(regex);
            } else if ("--".equals(arg)) {
                dashdashEncountered = true;
            } else {
                if (dashdashEncountered) {
                    afterDashDash.add(arg);
                } else {
                    beforeDashDash.add(arg);
                }
            }
            i++;
        }

        List<String> files;
        List<String> flags;
        if (dashdashEncountered) {
            flags = beforeDashDash;
            files = afterDashDash;
        } else {
            flags = new ArrayList<String>();
            files = beforeDashDash;
        }

        flags.add("--jscomp_off");
        flags.add("missingProperties");
        flags.add("--compilation_level");
        flags.add("ADVANCED_OPTIMIZATIONS");
        List<String> newArgs = new ArrayList<String>(flags);
        //beforeDashDash.add("--jscomp_error");
        //beforeDashDash.add("checkTypes");

        for (String file: files) {
            newArgs.add("--js");
            newArgs.add(file);
        }

        includes = inc.toArray(new String[inc.size()]);
        excludes = exc.toArray(new String[exc.size()]);
        return newArgs.toArray(new String[newArgs.size()]);
    }

    FilteredPrintStreamErrorManager errorManager;
    Compiler compiler;

    CheckTypes(String[] args) {
        super(parseCommandLine(args));
        this.errorManager = new FilteredPrintStreamErrorManager(getErrorPrintStream(), includes, excludes);
    }

    @Override
    protected Compiler createCompiler() {
        if (this.compiler == null) {
            this.compiler = new Compiler(getErrorPrintStream()) {
                @Override
                public String toSource() {
                    return "";
                }
            };
            this.compiler.setErrorManager(errorManager);
        }
        return this.compiler;
    }

//    protected void findRValueSource(SymbolTable st, Node symbol) {
//        System.err.println(symbol);
//        if (symbol.isName()) {
//            try {
//                SymbolTable.Symbol s = st.getEnclosingScope(symbol).getOwnSlot(symbol.getString());
//
//                // iterate over all references to the symbol
//                for (SymbolTable.Reference ref: st.getReferences(s)) {
//
//                    // inspect assignments
//                    Node parent = ref.getNode().getParent();
//
//                    if (parent != null && parent.isAssign()) {
//                        Node assignment = ref.getNode().getParent();
//                        System.err.println("ASSIGNMENT: " + assignment);
//                        int len = assignment.getChildCount();
//                        for (int i = 0; i < len; i++) {
//                            System.err.println("\t" + assignment.getChildAtIndex(i));
//                        }
//
//                        Node assigned = assignment.getLastChild();
//
//                        if (assigned.isGetProp()) {
//                            SymbolTable.SymbolScope scope = st.getGlobalScope();
//                            SymbolTable.Symbol sc = null;
//                            Node curNode = assigned;
//
//                            while (curNode.isGetProp()) {
//                                System.err.println(curNode);
//                                Node left = curNode.getFirstChild();
//                                Node right = curNode.getLastChild();
//                                System.err.println(left);
//                                System.err.println(right);
//                                System.err.println(scope);
//                                sc = scope.getOwnSlot(left.getString());
//                                System.err.println(sc);
//                                scope = sc.getPropertyScope();
//                                curNode = right;
//                            }
//
//                            System.err.println(scope); // property scope is null
//                            System.err.println(curNode.toString());
//                            System.err.println(scope.getOwnSlot(curNode.toString())); // npe
//
//                            System.err.println(st.getGlobalScope().getOwnSlot("$"));
//                            System.err.println(st.getGlobalScope().getOwnSlot("$").getPropertyScope());
//                            //System.err.println(st.getScope(st.getGlobalScope().getOwnSlot("$")));
//                            for (SymbolTable.Symbol sy: st.getAllSymbols()) {
//                                String sn = sy.getName();
//                                if ("window".equals(sn)) {
//                                    System.err.println(sy.getPropertyScope());
//                                    System.err.println(sy.getDeclaration());
//                                    System.err.println(st.getScope(sy));
//                                }
//                                if ("$".equals(sn)) {
//                                    System.err.println(sy.getPropertyScope());
//                                    System.err.println(sy.getDeclaration());
//                                    System.err.println(st.getScope(sy));
//                                }
//                            }
//                            //.getOwnSlot("Deferred"));
//
//                            System.err.println(scope);
//                            System.err.println(sc);
//                        } else if (assigned.isCall()) {
//                            Node assignedName = assigned.getLastChild().getFirstChild();
//                            System.err.println(assignedName);
//                            SymbolTable.Symbol assignedSymbol = st.getEnclosingScope(assignedName).getOwnSlot(assignedName.getString());
//                            System.err.println(assignedSymbol);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    protected int doRun() throws FlagUsageException, IOException {
//        int result = super.doRun();
//        SymbolTable st = compiler.buildKnownSymbolTable();
//        System.err.println(errorManager.propErrors.size());
//        for (Node n: errorManager.propErrors) {
//            // get the symbol that is being assigned to
//            findRValueSource(st, n.getFirstChild());
//        }
//        return result;
//    }

    @Override
    protected CompilerOptions createOptions() {
        CompilerOptions options = super.createOptions();
        options.setWarningLevel(DiagnosticGroups.CHECK_TYPES, CheckLevel.ERROR);
        //options.setCheckGlobalNamesLevel(CheckLevel.OFF);
        //options.setWarningLevel(DiagnosticGroups.UNDEFINED_NAMES, CheckLevel.OFF);
        //options.setWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES, CheckLevel.OFF);
        //options.setWarningLevel(DiagnosticGroups.MISSING_PROPERTIES, CheckLevel.OFF);
        //options.setWarningLevel(DiagnosticGroup.forType(DiagnosticType.disabled(
        //                                                "JSC_INEXISTENT_PROPERTY",
        //                                                "Property {0} never defined on {1}")), CheckLevel.OFF);
        options.setWarningLevel(DiagnosticGroup.forType(DiagnosticType.disabled(
                "JSC_NOT_A_CONSTRUCTOR",
                "cannot instantiate non-constructor")), CheckLevel.OFF);
        return options;
    }

    public static void main(String[] args) {
        CheckTypes runner = new CheckTypes(args);
        if (runner.shouldRunCompiler()) {
            runner.run();
        } else {
            System.exit(-1);
        }
    }
}
