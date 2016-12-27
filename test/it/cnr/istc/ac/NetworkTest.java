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
package it.cnr.istc.ac;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class NetworkTest {

    /**
     * Test of newBool method, of class Network.
     */
    @Test
    public void testNewBool_0args() {
        Network n = new Network();
        BoolVar bv = n.newBool();
        assertNotNull(bv);
        assertEquals(LBool.L_UNKNOWN, bv.domain);
    }

    /**
     * Test of newBool method, of class Network.
     */
    @Test
    public void testNewBool_boolean() {
        Network n = new Network();
        BoolConst true_bv = n.newBool(true);
        assertNotNull(true_bv);
        assertEquals(LBool.L_TRUE, true_bv.val);
        BoolConst false_bv = n.newBool(false);
        assertNotNull(false_bv);
        assertEquals(LBool.L_FALSE, false_bv.val);
    }

    /**
     * Test of newReal method, of class Network.
     */
    @Test
    public void testNewReal_0args() {
        Network n = new Network();
        ArithVar av = n.newReal();
        assertNotNull(av);
        assertEquals(Double.NEGATIVE_INFINITY, av.domain.lb, 0);
        assertEquals(Double.POSITIVE_INFINITY, av.domain.ub, 0);
    }

    /**
     * Test of newReal method, of class Network.
     */
    @Test
    public void testNewReal_double() {
        Network n = new Network();
        ArithConst av_0 = n.newReal(0);
        assertNotNull(av_0);
        assertEquals(0, av_0.val, 0);
        ArithConst av_1 = n.newReal(1);
        assertNotNull(av_1);
        assertEquals(1, av_1.val, 0);
    }

    @Test
    public void testSimplex0() {
        Network n = new Network();
        ArithVar x0 = n.newReal();
        ArithVar x1 = n.newReal();
        ArithExpr s1 = n.sum(n.minus(x0), x1);
        ArithExpr s2 = n.sum(x0, x1);

        BoolExpr leq_0 = n.leq(x0, n.newReal(-4));
        BoolExpr geq_0 = n.geq(x0, n.newReal(-8));
        BoolExpr leq_1 = n.leq(s1, n.newReal(1));
        BoolExpr geq_1 = n.geq(s2, n.newReal(-3));

        assertNotNull(leq_0);
        assertNotNull(geq_0);
        assertNotNull(leq_1);
        assertNotNull(geq_1);

        boolean add = n.add(leq_0, geq_0, leq_1, geq_1);
        assertTrue(add);
        boolean sat = n.propagate();
        assertFalse(sat);
    }

    @Test
    public void testSimplex1() {
        Network n = new Network();
        ArithVar x0 = n.newReal();
        ArithVar x1 = n.newReal();

        BoolExpr leq_0 = n.leq(n.sum(x0, n.newReal(1)), x1);
        BoolExpr leq_1 = n.leq(n.sum(x1, n.newReal(1)), x0);

        boolean add = n.add(leq_0, leq_1);
        assertTrue(add);
        boolean sat = n.propagate();
        assertFalse(sat);
    }

    @Test
    public void testSimplex2() {
        boolean propagate;
        Network n = new Network();
        ArithVar x0 = n.newReal();
        ArithVar x1 = n.newReal();
        n.add(n.geq(x0, n.newReal(0)), n.leq(x0, x1));

        propagate = n.propagate();
        assertTrue(propagate);

        ArithVar x2 = n.newReal();
        ArithVar x3 = n.newReal();
        boolean add = n.add(n.geq(x0, x0), n.leq(x2, x1), n.eq(x3, n.sub(x2, x0)), n.geq(x3, n.newReal(0)), n.geq(x3, n.newReal(10)));
        assertTrue(add);

        propagate = n.propagate();
        assertTrue(propagate);

        ArithVar x4 = n.newReal();
        ArithVar x5 = n.newReal();
        add = n.add(n.geq(x4, x0), n.leq(x1, x1), n.eq(x5, n.sub(x1, x4)), n.geq(x5, n.newReal(0)), n.geq(x5, n.newReal(10)));
        assertTrue(add);

        propagate = n.propagate();
        assertTrue(propagate);

        ArithVar x6 = n.newReal();
        ArithVar x7 = n.newReal();
        add = n.add(n.geq(x6, x0), n.leq(x4, x1), n.eq(x7, n.sub(x4, x6)), n.geq(x7, n.newReal(0)), n.geq(x7, n.newReal(10)));
        assertTrue(add);

        propagate = n.propagate();
        assertTrue(propagate);

        ArithVar x8 = n.newReal();
        ArithVar x9 = n.newReal();
        add = n.add(n.geq(x8, x0), n.leq(x6, x1), n.eq(x9, n.sub(x6, x8)), n.geq(x9, n.newReal(0)), n.geq(x9, n.newReal(10)));
        assertTrue(add);

        propagate = n.propagate();
        assertTrue(propagate);

        add = n.add(n.eq(x8, x0), n.eq(x6, x2), n.eq(x9, x3));
        assertTrue(add);

        propagate = n.propagate();
        assertTrue(propagate);
    }

    @Test
    public void testSimplex3() {
        Network n = new Network();
        ArithVar x0 = n.newReal();
        ArithVar x1 = n.newReal();
        boolean add = n.add(n.geq(x0, n.newReal(0)), n.leq(x0, x1));
        assertTrue(add);

        ArithVar x2 = n.newReal();
        ArithVar x3 = n.newReal();
        add = n.add(n.geq(x0, x0), n.leq(x2, x1), n.eq(x3, n.sub(x2, x0)), n.geq(x3, n.newReal(0)), n.geq(x3, n.newReal(10)));
        assertTrue(add);

        ArithVar x4 = n.newReal();
        ArithVar x5 = n.newReal();
        add = n.add(n.geq(x4, x0), n.leq(x1, x1), n.eq(x5, n.sub(x1, x4)), n.geq(x5, n.newReal(0)), n.geq(x5, n.newReal(10)));
        assertTrue(add);

        ArithVar x6 = n.newReal();
        ArithVar x7 = n.newReal();
        add = n.add(n.geq(x6, x0), n.leq(x4, x1), n.eq(x7, n.sub(x4, x6)), n.geq(x7, n.newReal(0)), n.geq(x7, n.newReal(10)));
        assertTrue(add);

        ArithVar x8 = n.newReal();
        ArithVar x9 = n.newReal();
        add = n.add(n.geq(x8, x0), n.leq(x6, x1), n.eq(x9, n.sub(x6, x8)), n.geq(x9, n.newReal(0)), n.geq(x9, n.newReal(10)));
        assertTrue(add);

        add = n.add(n.eq(x8, x0), n.eq(x6, x2), n.eq(x9, x3));
        assertTrue(add);

        boolean propagate = n.propagate();
        assertTrue(propagate);
    }

    @Test
    public void testNoGood() {
        Network n = new Network();
        BoolVar b0 = n.newBool();
        BoolVar b1 = n.newBool();
        BoolVar b2 = n.newBool();
        BoolVar b3 = n.newBool();
        BoolVar b4 = n.newBool();
        BoolVar b5 = n.newBool();
        BoolVar b6 = n.newBool();
        BoolVar b7 = n.newBool();
        BoolVar b8 = n.newBool();

        boolean add = n.add(n.or(b0, b1));
        assertTrue(add);
        add = n.add(n.or(b0, b2, b6));
        assertTrue(add);
        add = n.add(n.or(n.not(b1), n.not(b2), b3));
        assertTrue(add);
        add = n.add(n.or(n.not(b3), b4, b7));
        assertTrue(add);
        add = n.add(n.or(n.not(b3), b5, b8));
        assertTrue(add);
        add = n.add(n.or(n.not(b4), n.not(b5)));
        assertTrue(add);

        boolean assign;
        assign = n.assign(n.not(b6));
        assertTrue(assign);
        assign = n.assign(n.not(b7));
        assertTrue(assign);
        assign = n.assign(n.not(b8));
        assertTrue(assign);

        assign = n.assign(n.not(b0));
        assertFalse(assign);

        Collection<BoolVar> unsat_core = n.getUnsatCore();
        Collection<BoolVar> ng_vars = new ArrayList<>(unsat_core.size());
        for (BoolVar v : unsat_core) {
            ng_vars.add((BoolVar) n.not(v).to_var(n));
        }
        BoolExpr no_good = n.or(ng_vars.toArray(new BoolVar[ng_vars.size()]));
        while (no_good.evaluate() == LBool.L_FALSE) {
            n.pop();
        }
        n.add(no_good);

        boolean propagate = n.propagate();
        assertTrue(propagate);
    }
}
