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

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.oratio.core.parser.oRatioBaseListener;
import it.cnr.istc.oratio.core.parser.oRatioParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeRefinementListener extends oRatioBaseListener {

    private final Core core;
    private IScope scope;

    TypeRefinementListener(Core core) {
        this.core = core;
    }

    @Override
    public void enterCompilation_unit(oRatioParser.Compilation_unitContext ctx) {
        scope = core.scopes.get(ctx);
    }

    @Override
    public void enterEnum_declaration(oRatioParser.Enum_declarationContext ctx) {
        EnumType et = (EnumType) core.scopes.get(ctx);
        for (oRatioParser.Enum_constantsContext c : ctx.enum_constants()) {
            if (c.type() != null) {
                et.enums.add((EnumType) new TypeVisitor(core).visit(c.type()));
            }
        }
    }

    @Override
    public void enterClass_declaration(oRatioParser.Class_declarationContext ctx) {
        // we set the type superclasses..
        scope = core.scopes.get(ctx);
        if (ctx.type_list() != null) {
            Type t = (Type) scope;
            for (oRatioParser.TypeContext st : ctx.type_list().type()) {
                t.superclasses.add(new TypeVisitor(core).visit(st));
            }
        }
    }

    @Override
    public void exitClass_declaration(oRatioParser.Class_declarationContext ctx) {
        // if the current type has no constructor..
        Type t = (Type) scope;
        if (t.constructors.isEmpty()) {
            t.constructors.add(new Constructor(core, scope) {
                @Override
                public boolean invoke(IItem item, IItem... expressions) {
                    assert expressions.length == 0;

                    // we invoke superclasses constructors..
                    for (Type sc : t.superclasses) {
                        sc.getConstructor().invoke(item, expressions);
                    }

                    // we initialize the fields..
                    for (Field field : fields.values()) {
                        if (!field.synthetic) {
                            if (field instanceof InstantiatedField) {
                                ((BaseEnv) item).items.put(field.name, new ExpressionVisitor(core, item).visit(((InstantiatedField) field).expr));
                            } else if (field.type.primitive) {
                                ((BaseEnv) item).items.put(field.name, field.type.newInstance(item));
                            } else {
                                ((BaseEnv) item).items.put(field.name, field.type.newExistential());
                            }
                        }
                    }

                    return true;
                }
            });
        }

        scope = scope.getScope();
    }

    @Override
    public void enterField_declaration(oRatioParser.Field_declarationContext ctx) {
        // we add a field to the current scope..
        Type t = new TypeVisitor(core).visit(ctx.type());
        for (oRatioParser.Variable_decContext dec : ctx.variable_dec()) {
            if (dec.expr() != null) {
                if (scope == core) {
                    core.fields.put(dec.name.getText(), new InstantiatedField(t, dec.name.getText(), dec.expr()));
                } else {
                    ((BaseScope) scope).fields.put(dec.name.getText(), new InstantiatedField(t, dec.name.getText(), dec.expr()));
                }
            } else {
                if (scope == core) {
                    core.fields.put(dec.name.getText(), new Field(t, dec.name.getText()));
                } else {
                    ((BaseScope) scope).fields.put(dec.name.getText(), new Field(t, dec.name.getText()));
                }
            }
        }
    }

    @Override
    public void enterConstructor_declaration(oRatioParser.Constructor_declarationContext ctx) {
        // we add a new constructor to the current type..
        // these are the parameters of the new constructor..
        Collection<Field> args = new ArrayList<>();
        if (ctx.typed_list() != null) {
            for (int i = 0; i < ctx.typed_list().type().size(); i++) {
                args.add(new Field(new TypeVisitor(core).visit(ctx.typed_list().type(i)), ctx.typed_list().ID(i).getText()));
            }
        }

        Constructor c = new Constructor(core, scope, args.toArray(new Field[args.size()])) {
            @Override
            public boolean invoke(IItem item, IItem... expressions) {
                for (Field field : ((BaseScope) scope).fields.values()) {
                    if (field instanceof InstantiatedField) {
                        ((BaseEnv) item).items.put(field.name, new ExpressionVisitor(core, item).visit(((InstantiatedField) field).expr));
                    }
                }

                BaseEnv c_env = new BaseEnv(core, item);
                c_env.items.put(THIS, item);
                for (int i = 0; i < parameters.length; i++) {
                    c_env.items.put(parameters[i].name, expressions[i]);
                }

                for (oRatioParser.Initializer_elementContext el : ctx.initializer_element()) {
                    if (((BaseScope) scope).fields.containsKey(el.name.getText())) {
                        // field instantiation..
                        ((BaseEnv) item).items.put(el.name.getText(), new ExpressionVisitor(core, c_env).visit(el.expr_list().expr(0)));
                    } else {
                        // base constructor invocation..
                        Collection<Type> types = new ArrayList<>();
                        Collection<IItem> exprs = new ArrayList<>();
                        if (el.expr_list() != null) {
                            for (oRatioParser.ExprContext expr : el.expr_list().expr()) {
                                IItem it = new ExpressionVisitor(core, c_env).visit(expr);
                                types.add(it.getType());
                                exprs.add(it);
                            }
                        }
                        if (!getType(el.name.getText()).getConstructor(types.toArray(new Type[types.size()])).invoke(item, exprs.toArray(new IItem[exprs.size()]))) {
                            return false;
                        }
                    }
                }

                for (Field field : ((BaseScope) scope).fields.values()) {
                    if (field.type.primitive) {
                        ((BaseEnv) item).items.put(field.name, field.type.newInstance(item));
                    } else {
                        ((BaseEnv) item).items.put(field.name, field.type.newExistential());
                    }
                }

                return new StatementVisitor(core, c_env).visit(ctx.block());
            }
        };

        ((Type) scope).constructors.add(c);
        core.scopes.put(ctx, c);
        scope = c;
    }

    @Override
    public void exitConstructor_declaration(oRatioParser.Constructor_declarationContext ctx) {
        // we restore the scope as the enclosing scope of the current scope..
        scope = scope.getScope();
    }

    @Override
    public void enterVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx) {
        // we add a new method without return type to the current scope..
        // these are the parameters of the new method..
        Collection<Field> args = new ArrayList<>();
        if (ctx.typed_list() != null) {
            for (int i = 0; i < ctx.typed_list().type().size(); i++) {
                args.add(new Field(new TypeVisitor(core).visit(ctx.typed_list().type(i)), ctx.typed_list().ID(i).getText()));
            }
        }

        Method m = new Method(core, scope, ctx.name.getText(), null, args.toArray(new Field[args.size()])) {
            @Override
            public boolean invoke(IEnv env, IItem... expressions) {
                BaseEnv c_env = new BaseEnv(core, env);
                if (env instanceof IItem) {
                    c_env.items.put(THIS, (IItem) env);
                }
                for (int i = 0; i < parameters.length; i++) {
                    c_env.items.put(parameters[i].name, expressions[i]);
                }
                return new StatementVisitor(core, c_env).visit(ctx.block());
            }
        };

        core.scopes.put(ctx, m);

        if (scope == core) {
            if (!core.methods.containsKey(m.name)) {
                core.methods.put(m.name, new ArrayList<>());
            }
            core.methods.get(m.name).add(m);
        } else {
            if (!((Type) scope).methods.containsKey(m.name)) {
                ((Type) scope).methods.put(m.name, new ArrayList<>());
            }
            ((Type) scope).methods.get(m.name).add(m);
        }

        scope = m;
    }

    @Override
    public void exitVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx) {
        // we restore the scope as the enclosing scope of the current scope..
        scope = scope.getScope();
    }

    @Override
    public void enterType_method_declaration(oRatioParser.Type_method_declarationContext ctx) {
        // we add a new method with a return type to the current scope..
        // these are the parameters of the new method..
        Type return_type = new TypeVisitor(core).visit(ctx.type());

        Collection<Field> args = new ArrayList<>();
        if (ctx.typed_list() != null) {
            for (int i = 0; i < ctx.typed_list().type().size(); i++) {
                args.add(new Field(new TypeVisitor(core).visit(ctx.typed_list().type(i)), ctx.typed_list().ID(i).getText()));
            }
        }

        Method m = new Method(core, scope, ctx.name.getText(), return_type, args.toArray(new Field[args.size()])) {
            @Override
            public boolean invoke(IEnv env, IItem... expressions) {
                BaseEnv c_env = new BaseEnv(core, env);
                if (env instanceof IItem) {
                    c_env.items.put(THIS, (IItem) env);
                }
                for (int i = 0; i < parameters.length; i++) {
                    c_env.items.put(parameters[i].name, expressions[i]);
                }
                return new StatementVisitor(core, c_env).visit(ctx.block());
            }
        };

        core.scopes.put(ctx, m);

        if (scope == core) {
            if (!core.methods.containsKey(m.name)) {
                core.methods.put(m.name, new ArrayList<>());
            }
            core.methods.get(m.name).add(m);
        } else {
            if (!((Type) scope).methods.containsKey(m.name)) {
                ((Type) scope).methods.put(m.name, new ArrayList<>());
            }
            ((Type) scope).methods.get(m.name).add(m);
        }

        scope = m;
    }

    @Override
    public void exitType_method_declaration(oRatioParser.Type_method_declarationContext ctx) {
        // we restore the scope as the enclosing scope of the current scope..
        scope = scope.getScope();
    }

    @Override
    public void enterPredicate_declaration(oRatioParser.Predicate_declarationContext ctx) {
        // we add a new predicate to the current scope..
        // these are the parameters of the new predicate..
        Collection<Field> args = new ArrayList<>();
        if (ctx.typed_list() != null) {
            for (int i = 0; i < ctx.typed_list().type().size(); i++) {
                args.add(new Field(new TypeVisitor(core).visit(ctx.typed_list().type(i)), ctx.typed_list().ID(i).getText()));
            }
        }

        Predicate p = new Predicate(core, scope, ctx.name.getText(), args.toArray(new Field[args.size()])) {
            @Override
            public boolean apply(Atom atom) {
                for (Type sp : superclasses) {
                    if (!((Predicate) sp).apply(atom)) {
                        return false;
                    }
                }

                BaseEnv c_env = new BaseEnv(core, atom);
                c_env.items.put(THIS, atom);
                return new StatementVisitor(core, c_env).visit(ctx.block());
            }
        };

        if (ctx.type_list() != null) {
            for (oRatioParser.TypeContext t : ctx.type_list().type()) {
                p.superclasses.add(new TypeVisitor(core).visit(t));
            }
        }

        core.scopes.put(ctx, p);

        Set<Type> types = new HashSet<>();
        LinkedList<Type> queue = new LinkedList<>();
        if (scope == core) {
            core.predicates.put(p.name, p);
        } else {
            ((Type) scope).predicates.put(p.name, p);
            queue.add((Type) scope);
        }
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            if (!types.contains(c_type)) {
                types.add(c_type);
                queue.addAll(c_type.superclasses);
            }
        }

        for (Type type : types) {
            type.predicateDefined(p);
        }

        scope = p;
    }

    @Override
    public void exitPredicate_declaration(oRatioParser.Predicate_declarationContext ctx) {
        // we restore the scope as the enclosing scope of the current scope..
        scope = scope.getScope();
    }

    @Override
    public void enterDisjunction_statement(oRatioParser.Disjunction_statementContext ctx) {
        Disjunction d = new Disjunction(core, scope);

        core.scopes.put(ctx, d);

        scope = d;
    }

    @Override
    public void exitDisjunction_statement(oRatioParser.Disjunction_statementContext ctx) {
        // we restore the scope as the enclosing scope of the current scope..
        scope = scope.getScope();
    }

    @Override
    public void enterConjunction(oRatioParser.ConjunctionContext ctx) {
        Conjunction c = new Conjunction(core, scope, ctx.cost != null ? (ArithExpr) new ExpressionVisitor(core, core).visit(ctx.cost) : core.network.newReal(1)) {
            @Override
            public boolean apply(IEnv env) {
                return new StatementVisitor(core, env).visit(ctx.block());
            }
        };

        ((Disjunction) scope).conjunctions.add(c);

        core.scopes.put(ctx, c);

        scope = c;
    }

    @Override
    public void exitConjunction(oRatioParser.ConjunctionContext ctx) {
        // we restore the scope as the enclosing scope of the current scope..
        scope = scope.getScope();
    }

    @Override
    public void enterLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterAssignment_statement(oRatioParser.Assignment_statementContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterExpression_statement(oRatioParser.Expression_statementContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterFormula_statement(oRatioParser.Formula_statementContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterReturn_statement(oRatioParser.Return_statementContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterQualified_id(oRatioParser.Qualified_idContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx) {
        core.scopes.put(ctx, scope);
    }

    @Override
    public void enterFunction_expression(oRatioParser.Function_expressionContext ctx) {
        core.scopes.put(ctx, scope);
    }
}
