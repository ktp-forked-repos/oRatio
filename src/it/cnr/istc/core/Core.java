/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.core;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.Network;
import it.cnr.istc.core.parser.oRatioLexer;
import it.cnr.istc.core.parser.oRatioParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Core extends Network implements IScope, IEnv {

    public static final String BOOL = "bool";
    public static final String REAL = "real";
    public static final String STRING = "string";
    final Map<ParseTree, IScope> scopes = new IdentityHashMap<>();
    oRatioParser parser;
    protected final Map<String, Field> fields = new LinkedHashMap<>();
    protected final Map<String, Collection<Method>> methods = new LinkedHashMap<>();
    protected final Map<String, Predicate> predicates = new LinkedHashMap<>();
    protected final Map<String, Type> types = new LinkedHashMap<>();
    protected final Map<String, IItem> items = new LinkedHashMap<>();
    protected BoolExpr ctr_var = newBool(true);

    public Core() {
        types.put(BOOL, new BoolType(this));
        types.put(REAL, new RealType(this));
        types.put(STRING, new StringType(this));
    }

    public boolean read(String script) {
        parser = new oRatioParser(new CommonTokenStream(new oRatioLexer(new ANTLRInputStream(script))));
        oRatioParser.Compilation_unitContext cu = parser.compilation_unit();
        ParseTreeWalker.DEFAULT.walk(new TypeDeclarationListener(this), cu);
        ParseTreeWalker.DEFAULT.walk(new TypeRefinementListener(this), cu);
        if (!new StatementVisitor(this, this).visit(cu)) {
            return false;
        }
        parser = null;
        return true;
    }

    public boolean read(File... files) throws IOException {
        List<File> fs = new ArrayList<>(files.length);
        for (File file : files) {
            fs.addAll(Files.walk(Paths.get(file.toURI())).filter(Files::isRegularFile).map(path -> path.toFile()).collect(Collectors.toList()));
        }
        Collection<CodeSnippet> snippets = new ArrayList<>(fs.size());
        for (File f : fs) {
            oRatioParser c_parser = new oRatioParser(new CommonTokenStream(new oRatioLexer(new ANTLRFileStream(f.getPath()))));
            oRatioParser.Compilation_unitContext context = c_parser.compilation_unit();
            snippets.add(new CodeSnippet(f, c_parser, context));
        }
        for (CodeSnippet snippet : snippets) {
            parser = snippet.parser;
            ParseTreeWalker.DEFAULT.walk(new TypeDeclarationListener(this), snippet.context);
            ParseTreeWalker.DEFAULT.walk(new TypeRefinementListener(this), snippet.context);
            if (!new StatementVisitor(this, this).visit(snippet.context)) {
                return false;
            }
            parser = null;
        }
        return true;
    }

    /**
     * Solves the current problem returning {@code true} if a solution has been
     * found and {@code false} if the problem is unsolvable.
     *
     * @return {@code true} if a solution has been found or {@code false} if the
     * problem is unsolvable.
     */
    public abstract boolean solve();

    public IBoolItem newBoolItem() {
        return new BoolItem(this, types.get(BOOL), newBool());
    }

    public IBoolItem newBoolItem(boolean val) {
        return new BoolItem(this, types.get(BOOL), newBool(val));
    }

    public IArithItem newRealItem() {
        return new ArithItem(this, types.get(REAL), newReal());
    }

    public IArithItem newRealItem(double val) {
        return new ArithItem(this, types.get(REAL), newReal(val));
    }

    public IEnumItem newEnumItem(IItem value) {
        return new EnumItem(this, value.getType(), newEnum(value));
    }

    public IEnumItem newEnumItem(Type type, IItem... values) {
        assert values.length > 1;
        assert Stream.of(values).allMatch(item -> type.isAssignableFrom(item.getType()));
        switch (type.name) {
            case BOOL:
                return new BoolEnum(this, types.get(BOOL), newBool(), newEnum(Arrays.copyOf(values, values.length, IBoolItem[].class)));
            case REAL:
                return new ArithEnum(this, types.get(REAL), newReal(), newEnum(Arrays.copyOf(values, values.length, IArithItem[].class)));
            default:
                return new EnumItem(this, type, newEnum(values));
        }
    }

    public IStringItem newStringItem() {
        return new StringItem(this, types.get(REAL), "");
    }

    public IStringItem newStringItem(String val) {
        return new StringItem(this, types.get(REAL), val);
    }

    protected boolean newFact(Atom atom) {
        if (atom.type.scope instanceof Type) {
            if (!((Type) atom.type.scope).factCreated(atom)) {
                return false;
            }
        }
        return true;
    }

    protected boolean activateFact(Atom atom) {
        if (atom.type.scope instanceof Type) {
            if (!((Type) atom.type.scope).factActivated(atom)) {
                return false;
            }
        }
        return true;
    }

    protected boolean unifyFact(Atom unifying, Atom with) {
        assert unifying.type == with.type;
        if (unifying.type.scope instanceof Type) {
            if (!((Type) unifying.type.scope).factUnified(unifying, with)) {
                return false;
            }
        }
        return true;
    }

    protected boolean newGoal(Atom atom) {
        if (atom.type.scope instanceof Type) {
            if (!((Type) atom.type.scope).goalCreated(atom)) {
                return false;
            }
        }
        return true;
    }

    protected boolean activateGoal(Atom atom) {
        if (atom.type.scope instanceof Type) {
            if (!((Type) atom.type.scope).goalActivated(atom)) {
                return false;
            }
        }
        return true;
    }

    protected boolean unifyGoal(Atom unifying, Atom with) {
        assert unifying.type == with.type;
        if (unifying.type.scope instanceof Type) {
            if (!((Type) unifying.type.scope).goalUnified(unifying, with)) {
                return false;
            }
        }
        return true;
    }

    public boolean newDisjunction(IEnv env, Disjunction d) {
        return true;
    }

    public IBoolItem not(IBoolItem var) {
        return new BoolItem(this, types.get(BOOL), not(var.getBoolVar()));
    }

    public IBoolItem and(IBoolItem... vars) {
        return new BoolItem(this, types.get(BOOL), and(Stream.of(vars).map(var -> var.getBoolVar()).toArray(BoolExpr[]::new)));
    }

    public IBoolItem or(IBoolItem... vars) {
        return new BoolItem(this, types.get(BOOL), or(Stream.of(vars).map(var -> var.getBoolVar()).toArray(BoolExpr[]::new)));
    }

    public IBoolItem exct_one(IBoolItem... vars) {
        return new BoolItem(this, types.get(BOOL), exct_one(Stream.of(vars).map(var -> var.getBoolVar()).toArray(BoolExpr[]::new)));
    }

    public IBoolItem imply(IBoolItem left, IBoolItem right) {
        return new BoolItem(this, types.get(BOOL), imply(left.getBoolVar(), right.getBoolVar()));
    }

    public IArithItem minus(IArithItem var) {
        return new ArithItem(this, types.get(REAL), minus(var.getArithVar()));
    }

    public IArithItem sum(IArithItem... vars) {
        return new ArithItem(this, types.get(REAL), sum(Stream.of(vars).map(var -> var.getArithVar()).toArray(ArithExpr[]::new)));
    }

    public IArithItem sub(IArithItem... vars) {
        return new ArithItem(this, types.get(REAL), sub(Stream.of(vars).map(var -> var.getArithVar()).toArray(ArithExpr[]::new)));
    }

    public IArithItem mult(IArithItem... vars) {
        return new ArithItem(this, types.get(REAL), mult(Stream.of(vars).map(var -> var.getArithVar()).toArray(ArithExpr[]::new)));
    }

    public IArithItem div(IArithItem left, IArithItem right) {
        return new ArithItem(this, types.get(REAL), div(left.getArithVar(), right.getArithVar()));
    }

    public IBoolItem leq(IArithItem left, IArithItem right) {
        return new BoolItem(this, types.get(BOOL), leq(left.getArithVar(), right.getArithVar()));
    }

    public IBoolItem geq(IArithItem left, IArithItem right) {
        return new BoolItem(this, types.get(BOOL), geq(left.getArithVar(), right.getArithVar()));
    }

    public IBoolItem eq(IItem left, IItem right) {
        return new BoolItem(this, types.get(BOOL), left.eq(right));
    }

    public boolean add(IBoolItem... vars) {
        BoolExpr[] exprs = Stream.of(vars).map(var -> imply(ctr_var, var.getBoolVar())).toArray(BoolExpr[]::new);
        if (exprs.length == 0) {
            // there is nothing to add..
            return true;
        } else {
            return add(exprs);
        }
    }

    @Override
    public Core getCore() {
        return this;
    }

    @Override
    public IScope getScope() {
        return null;
    }

    @Override
    public Field getField(String name) {
        return fields.get(name);
    }

    @Override
    public Collection<Field> getFields() {
        return Collections.unmodifiableCollection(fields.values());
    }

    @Override
    public Method getMethod(String name, Type... parameterTypes) {
        boolean isCorrect;
        if (methods.containsKey(name)) {
            for (Method m : methods.get(name)) {
                if (m.parameters.length == parameterTypes.length) {
                    isCorrect = true;
                    for (int i = 0; i < m.parameters.length; i++) {
                        if (!m.parameters[i].type.isAssignableFrom(parameterTypes[i])) {
                            isCorrect = false;
                            break;
                        }
                    }
                    if (isCorrect) {
                        return m;
                    }
                }
            }
        }

        // not found
        return null;
    }

    @Override
    public Collection<Method> getMethods() {
        Collection<Method> c_methods = new ArrayList<>();
        for (Collection<Method> ms : methods.values()) {
            c_methods.addAll(ms);
        }
        return Collections.unmodifiableCollection(c_methods);
    }

    @Override
    public Predicate getPredicate(String name) {
        return predicates.get(name);
    }

    @Override
    public Collection<Predicate> getPredicates() {
        return Collections.unmodifiableCollection(predicates.values());
    }

    @Override
    public Type getType(String name) {
        return types.get(name);
    }

    @Override
    public Collection<Type> getTypes() {
        return Collections.unmodifiableCollection(types.values());
    }

    @Override
    public IEnv getEnv() {
        return null;
    }

    @Override
    public <T extends IItem> T get(String name) {
        return (T) items.get(name);
    }

    private static class CodeSnippet {

        private final File file;
        private final oRatioParser parser;
        private final oRatioParser.Compilation_unitContext context;

        CodeSnippet(File file, oRatioParser parser, oRatioParser.Compilation_unitContext context) {
            this.file = file;
            this.parser = parser;
            this.context = context;
        }
    }

    private static class BoolType extends Type {

        BoolType(Core c) {
            super(c, c, BOOL, true);
        }

        @Override
        public IItem newInstance(IEnv env) {
            return core.newBoolItem();
        }
    }

    private static class RealType extends Type {

        RealType(Core c) {
            super(c, c, REAL, true);
        }

        @Override
        public IItem newInstance(IEnv env) {
            return core.newRealItem();
        }
    }

    private static class StringType extends Type {

        StringType(Core c) {
            super(c, c, STRING, true);
        }

        @Override
        public IItem newInstance(IEnv env) {
            return core.newStringItem();
        }
    }
}
