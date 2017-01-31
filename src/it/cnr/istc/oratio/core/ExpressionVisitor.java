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

import static it.cnr.istc.oratio.core.IScope.RETURN;
import static it.cnr.istc.oratio.core.IScope.THIS;
import it.cnr.istc.oratio.core.parser.oRatioBaseVisitor;
import it.cnr.istc.oratio.core.parser.oRatioParser;
import java.util.ArrayList;
import java.util.Collection;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ExpressionVisitor extends oRatioBaseVisitor<IItem> {

    private final Core core;
    private final IEnv env;

    ExpressionVisitor(Core core, IEnv env) {
        this.core = core;
        this.env = env;
    }

    @Override
    public IItem visitLiteral_expression(oRatioParser.Literal_expressionContext ctx) {
        if (ctx.literal().numeric != null) {
            return core.newRealItem(Double.parseDouble(ctx.literal().numeric.getText()));
        } else if (ctx.literal().string != null) {
            return core.newStringItem(ctx.literal().string.getText());
        } else if (ctx.literal().t != null) {
            return core.newBoolItem(true);
        } else if (ctx.literal().f != null) {
            return core.newBoolItem(false);
        } else {
            throw new AssertionError("the primitive type has not been found..");
        }
    }

    @Override
    public IItem visitParentheses_expression(oRatioParser.Parentheses_expressionContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public IItem visitMultiplication_expression(oRatioParser.Multiplication_expressionContext ctx) {
        Collection<IArithItem> exprs = new ArrayList<>(ctx.expr().size());
        for (oRatioParser.ExprContext expr : ctx.expr()) {
            exprs.add((IArithItem) visit(expr));
        }
        return core.mult(exprs.toArray(new IArithItem[exprs.size()]));
    }

    @Override
    public IItem visitDivision_expression(oRatioParser.Division_expressionContext ctx) {
        return core.div((IArithItem) visit(ctx.expr(0)), (IArithItem) visit(ctx.expr(1)));
    }

    @Override
    public IItem visitAddition_expression(oRatioParser.Addition_expressionContext ctx) {
        Collection<IArithItem> exprs = new ArrayList<>(ctx.expr().size());
        for (oRatioParser.ExprContext expr : ctx.expr()) {
            exprs.add((IArithItem) visit(expr));
        }
        return core.sum(exprs.toArray(new IArithItem[exprs.size()]));
    }

    @Override
    public IItem visitSubtraction_expression(oRatioParser.Subtraction_expressionContext ctx) {
        Collection<IArithItem> exprs = new ArrayList<>(ctx.expr().size());
        for (oRatioParser.ExprContext expr : ctx.expr()) {
            exprs.add((IArithItem) visit(expr));
        }
        return core.sub(exprs.toArray(new IArithItem[exprs.size()]));
    }

    @Override
    public IItem visitMinus_expression(oRatioParser.Minus_expressionContext ctx) {
        return core.minus((IArithItem) visit(ctx.expr()));
    }

    @Override
    public IItem visitNot_expression(oRatioParser.Not_expressionContext ctx) {
        return core.not((IBoolItem) visit(ctx.expr()));
    }

    @Override
    public IItem visitQualified_id(oRatioParser.Qualified_idContext ctx) {
        IEnv c_env = env;
        if (ctx.t != null) {
            c_env = c_env.get(THIS);
        }
        for (TerminalNode id : ctx.ID()) {
            c_env = c_env.get(id.getText());
            if (c_env == null) {
                core.parser.notifyErrorListeners(id.getSymbol(), "cannot find symbol..", null);
            }
        }
        return (IItem) c_env;
    }

    @Override
    public IItem visitQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx) {
        return visit(ctx.qualified_id());
    }

    @Override
    public IItem visitFunction_expression(oRatioParser.Function_expressionContext ctx) {
        IEnv c_env = env;
        Collection<Type> types = new ArrayList<>();
        Collection<IItem> exprs = new ArrayList<>();
        if (ctx.expr_list() != null) {
            for (oRatioParser.ExprContext expr : ctx.expr_list().expr()) {
                IItem it = new ExpressionVisitor(core, c_env).visit(expr);
                types.add(it.getType());
                exprs.add(it);
            }
        }

        Method m = null;
        if (ctx.object != null) {
            m = visit(ctx.object).getType().getMethod(ctx.function_name.getText(), types.toArray(new Type[types.size()]));
        } else {
            m = core.scopes.get(ctx).getMethod(ctx.function_name.getText(), types.toArray(new Type[types.size()]));
        }
        if (m == null) {
            core.parser.notifyErrorListeners(ctx.function_name, "cannot find method..", null);
        }

        boolean invoke = m.invoke(c_env, exprs.toArray(new IItem[exprs.size()]));
        if (!invoke) {
            core.parser.notifyErrorListeners(ctx.function_name, "functions are not supposed to create inconsistencies..", null);
        }
        return c_env.get(RETURN);
    }

    @Override
    public IItem visitRange_expression(oRatioParser.Range_expressionContext ctx) {
        IArithItem var = core.newRealItem();
        IArithItem min = (IArithItem) visit(ctx.min);
        IArithItem max = (IArithItem) visit(ctx.max);
        if (!core.add(core.geq(var.getArithVar(), min.getArithVar()), core.leq(var.getArithVar(), max.getArithVar()))) {
            core.parser.notifyErrorListeners(ctx.getStart(), "invalid range expression..", null);
        }
        return var;
    }

    @Override
    public IItem visitConstructor_expression(oRatioParser.Constructor_expressionContext ctx) {
        Type t = new TypeVisitor(core).visit(ctx.type());
        Collection<Type> types = new ArrayList<>();
        Collection<IItem> exprs = new ArrayList<>();
        if (ctx.expr_list() != null) {
            for (oRatioParser.ExprContext expr : ctx.expr_list().expr()) {
                IItem it = new ExpressionVisitor(core, env).visit(expr);
                types.add(it.getType());
                exprs.add(it);
            }
        }
        Constructor c = t.getConstructor(types.toArray(new Type[types.size()]));
        if (c == null) {
            core.parser.notifyErrorListeners(ctx.type().start, "cannot find constructor..", null);
        }
        return c.newInstance(env, exprs.toArray(new IItem[exprs.size()]));
    }

    @Override
    public IItem visitLt_expression(oRatioParser.Lt_expressionContext ctx) {
        throw new UnsupportedOperationException("strict inequalities are not supported yet..");
    }

    @Override
    public IItem visitLeq_expression(oRatioParser.Leq_expressionContext ctx) {
        return core.leq((IArithItem) visit(ctx.expr(0)), (IArithItem) visit(ctx.expr(1)));
    }

    @Override
    public IItem visitEq_expression(oRatioParser.Eq_expressionContext ctx) {
        return core.eq(visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IItem visitGeq_expression(oRatioParser.Geq_expressionContext ctx) {
        return core.geq((IArithItem) visit(ctx.expr(0)), (IArithItem) visit(ctx.expr(1)));
    }

    @Override
    public IItem visitGt_expression(oRatioParser.Gt_expressionContext ctx) {
        throw new UnsupportedOperationException("strict inequalities are not supported yet..");
    }

    @Override
    public IItem visitNeq_expression(oRatioParser.Neq_expressionContext ctx) {
        return core.not(core.eq(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IItem visitImplication_expression(oRatioParser.Implication_expressionContext ctx) {
        return core.imply((IBoolItem) visit(ctx.expr(0)), (IBoolItem) visit(ctx.expr(1)));
    }

    @Override
    public IItem visitConjunction_expression(oRatioParser.Conjunction_expressionContext ctx) {
        Collection<IBoolItem> exprs = new ArrayList<>(ctx.expr().size());
        for (oRatioParser.ExprContext expr : ctx.expr()) {
            exprs.add((IBoolItem) visit(expr));
        }
        return core.and(exprs.toArray(new IBoolItem[exprs.size()]));
    }

    @Override
    public IItem visitDisjunction_expression(oRatioParser.Disjunction_expressionContext ctx) {
        Collection<IBoolItem> exprs = new ArrayList<>(ctx.expr().size());
        for (oRatioParser.ExprContext expr : ctx.expr()) {
            exprs.add((IBoolItem) visit(expr));
        }
        return core.or(exprs.toArray(new IBoolItem[exprs.size()]));
    }

    @Override
    public IItem visitExtc_one_expression(oRatioParser.Extc_one_expressionContext ctx) {
        Collection<IBoolItem> exprs = new ArrayList<>(ctx.expr().size());
        for (oRatioParser.ExprContext expr : ctx.expr()) {
            exprs.add((IBoolItem) visit(expr));
        }
        return core.exct_one(exprs.toArray(new IBoolItem[exprs.size()]));
    }
}
