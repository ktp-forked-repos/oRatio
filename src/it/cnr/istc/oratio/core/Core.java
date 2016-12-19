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
package it.cnr.istc.oratio.core;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.Network;
import it.cnr.istc.oratio.core.parser.oRatioLexer;
import it.cnr.istc.oratio.core.parser.oRatioParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
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
public class Core implements IScope, IEnv {

    public static final String BOOL = "bool";
    public static final String REAL = "real";
    public final Network network = new Network();
    final Map<ParseTree, IScope> scopes = new IdentityHashMap<>();
    final Map<String, Field> fields = new HashMap<>();
    final Map<String, Type> types = new HashMap<>();
    final Map<String, IItem> items = new HashMap<>();

    public Core() {
        types.put(BOOL, new BoolType(this));
    }

    public boolean read(String script) {
        oRatioParser parser = new oRatioParser(new CommonTokenStream(new oRatioLexer(new ANTLRInputStream(script))));
        oRatioParser.Compilation_unitContext cu = parser.compilation_unit();
        ParseTreeWalker.DEFAULT.walk(new TypeDeclarationListener(this), cu);
        ParseTreeWalker.DEFAULT.walk(new TypeRefinementListener(this), cu);
        if (!new StatementVisitor(this, this).visit(cu)) {
            return false;
        }
        return network.propagate();
    }

    public boolean read(File... files) throws IOException {
        List<File> fs = new ArrayList<>(files.length);
        for (File file : files) {
            fs.addAll(Files.walk(Paths.get(file.toURI())).filter(Files::isRegularFile).map(path -> path.toFile()).collect(Collectors.toList()));
        }
        Collection<CodeSnippet> snippets = new ArrayList<>(fs.size());
        for (File f : fs) {
            oRatioParser parser = new oRatioParser(new CommonTokenStream(new oRatioLexer(new ANTLRFileStream(f.getPath()))));
            oRatioParser.Compilation_unitContext context = parser.compilation_unit();
            snippets.add(new CodeSnippet(f, parser, context));
        }
        for (CodeSnippet snippet : snippets) {
            ParseTreeWalker.DEFAULT.walk(new TypeDeclarationListener(this), snippet.context);
            ParseTreeWalker.DEFAULT.walk(new TypeRefinementListener(this), snippet.context);
            if (!new StatementVisitor(this, this).visit(snippet.context)) {
                return false;
            }
        }
        return network.propagate();
    }

    public IBoolItem newBool() {
        return new BoolItem(this, types.get(BOOL), network.newBool());
    }

    public IBoolItem newBool(boolean val) {
        return new BoolItem(this, types.get(BOOL), network.newBool(val));
    }

    public IArithItem newReal() {
        return new ArithItem(this, types.get(REAL), network.newReal());
    }

    public IArithItem newReal(double val) {
        return new ArithItem(this, types.get(REAL), network.newReal(val));
    }

    public IEnumItem<IItem> newEnum(IItem value) {
        return new EnumItem(this, value.getType(), network.newEnum(value));
    }

    public IEnumItem<? extends IItem> newEnum(Type type, IItem... values) {
        assert values.length > 1;
        assert Stream.of(values).allMatch(item -> type.isAssignableFrom(item.getType()));
        switch (type.name) {
            case BOOL:
                return new BoolEnum(this, types.get(BOOL), network.newBool(), network.newEnum(Arrays.copyOf(values, values.length, IBoolItem[].class)));
            case REAL:
                return new ArithEnum(this, types.get(REAL), network.newReal(), network.newEnum(Arrays.copyOf(values, values.length, IArithItem[].class)));
            default:
                return new EnumItem(this, type, network.newEnum(values));
        }
    }

    public IBoolItem not(IBoolItem var) {
        return new BoolItem(this, types.get(BOOL), network.not(var.getBoolVar()));
    }

    public IBoolItem and(IBoolItem... vars) {
        return new BoolItem(this, types.get(BOOL), network.and(Stream.of(vars).map(var -> var.getBoolVar()).toArray(BoolExpr[]::new)));
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
    }
}
