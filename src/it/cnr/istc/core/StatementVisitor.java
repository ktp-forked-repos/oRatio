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

import static it.cnr.istc.core.IScope.RETURN;
import static it.cnr.istc.core.IScope.SCOPE;
import it.cnr.istc.core.parser.oRatioBaseVisitor;
import it.cnr.istc.core.parser.oRatioParser;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StatementVisitor extends oRatioBaseVisitor<Boolean> {

    private final Core core;
    private final IEnv env;

    StatementVisitor(Core core, IEnv env) {
        this.core = core;
        this.env = env;
    }

    @Override
    public Boolean visitCompilation_unit(oRatioParser.Compilation_unitContext ctx) {
        for (oRatioParser.StatementContext s : ctx.statement()) {
            if (!visit(s)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean visitBlock(oRatioParser.BlockContext ctx) {
        for (oRatioParser.StatementContext s : ctx.statement()) {
            if (!visit(s)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean visitConjunction(oRatioParser.ConjunctionContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public Boolean visitAssignment_statement(oRatioParser.Assignment_statementContext ctx) {
        IEnv e = env;
        if (ctx.object != null) {
            e = new ExpressionVisitor(core, env).visit(ctx.object);
        }
        if (e == core) {
            core.items.put(ctx.field.getText(), new ExpressionVisitor(core, env).visit(ctx.expr()));
        } else {
            ((BaseEnv) e).items.put(ctx.field.getText(), new ExpressionVisitor(core, env).visit(ctx.expr()));
        }
        return true;
    }

    @Override
    public Boolean visitLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx) {
        Type t = new TypeVisitor(core).visit(ctx.type());
        for (oRatioParser.Variable_decContext dec : ctx.variable_dec()) {
            if (env == core) {
                if (dec.expr() != null) {
                    core.items.put(dec.name.getText(), new ExpressionVisitor(core, env).visit(dec.expr()));
                } else if (t.primitive) {
                    core.items.put(dec.name.getText(), t.newInstance(env));
                } else {
                    core.items.put(dec.name.getText(), t.newExistential());
                }
            } else {
                if (dec.expr() != null) {
                    ((BaseEnv) env).items.put(dec.name.getText(), new ExpressionVisitor(core, env).visit(dec.expr()));
                } else if (t.primitive) {
                    ((BaseEnv) env).items.put(dec.name.getText(), t.newInstance(env));
                } else {
                    ((BaseEnv) env).items.put(dec.name.getText(), t.newExistential());
                }
            }
        }
        return true;
    }

    @Override
    public Boolean visitExpression_statement(oRatioParser.Expression_statementContext ctx) {
        IBoolItem expr = (IBoolItem) new ExpressionVisitor(core, env).visit(ctx.expr());
        return core.add(expr);
    }

    @Override
    public Boolean visitFormula_statement(oRatioParser.Formula_statementContext ctx) {
        Predicate p;
        Map<String, IItem> assignments = new LinkedHashMap<>();
        if (ctx.object != null) {
            IItem i = new ExpressionVisitor(core, env).visit(ctx.object);
            p = i.getType().getPredicate(ctx.predicate.getText());
            if (i instanceof IEnumItem) {
                assignments.put(SCOPE, i);
            } else {
                assignments.put(SCOPE, core.newEnumItem(i));
            }
        } else {
            p = core.scopes.get(ctx).getPredicate(ctx.predicate.getText());
            if (p.scope != core) {
                assignments.put(SCOPE, env.get(SCOPE));
            }
        }

        if (ctx.assignment_list() != null) {
            for (oRatioParser.AssignmentContext a : ctx.assignment_list().assignment()) {
                assignments.put(a.field.getText(), new ExpressionVisitor(core, env).visit(a.expr()));
            }
        }

        Atom a = p.newInstance(assignments.get(SCOPE));
        a.items.putAll(assignments);

        Set<Type> types = new HashSet<>();
        LinkedList<Type> queue = new LinkedList<>();
        queue.add(p);
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            if (!types.contains(c_type)) {
                types.add(c_type);
                queue.addAll(c_type.superclasses);
            }
        }

        for (Type t : types) {
            for (Field f : t.fields.values()) {
                if (!f.synthetic && !a.items.containsKey(f.name)) {
                    if (f.type.primitive) {
                        a.items.put(f.name, f.type.newInstance(env));
                    } else {
                        a.items.put(f.name, f.type.newExistential());
                    }
                }
            }
        }

        if (ctx.fact != null) {
            if (!core.newFact(a)) {
                return false;
            }
        } else if (ctx.goal != null) {
            if (!core.newGoal(a)) {
                return false;
            }
        }

        if (env == core) {
            core.items.put(ctx.name.getText(), a);
        } else {
            ((BaseEnv) env).items.put(ctx.name.getText(), a);
        }
        return true;
    }

    @Override
    public Boolean visitReturn_statement(oRatioParser.Return_statementContext ctx) {
        ((BaseEnv) env).items.put(RETURN, new ExpressionVisitor(core, env).visit(ctx.expr()));
        return true;
    }

    @Override
    public Boolean visitDisjunction_statement(oRatioParser.Disjunction_statementContext ctx) {
        return core.newDisjunction(env, (Disjunction) core.scopes.get(ctx));
    }
}
