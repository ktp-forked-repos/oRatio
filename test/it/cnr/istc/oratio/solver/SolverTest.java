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
package it.cnr.istc.oratio.solver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class SolverTest {

    private static final Logger LOG = Logger.getLogger(SolverTest.class.getName());

    @Test
    public void test_sv_0() {
        Solver s = new Solver();

        long t0 = System.nanoTime();
        try {
            boolean read = s.read(new File("examples/test/test_sv_0.rddl"));
            Assert.assertTrue(read);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Assert.fail(ex.getLocalizedMessage());
        }

        long t1 = System.nanoTime();

        boolean solve = s.solve();
        Assert.assertTrue(solve);

        long t2 = System.nanoTime();

        LOG.log(Level.INFO, "parsing time: {0}s; solving time: {1}s; total time: {2}", new Object[]{TimeUnit.NANOSECONDS.toSeconds(t1 - t0), TimeUnit.NANOSECONDS.toSeconds(t2 - t1), TimeUnit.NANOSECONDS.toSeconds(t2 - t0)});
    }

    @Test
    public void test_sv_1() {
        Solver s = new Solver();

        long t0 = System.nanoTime();
        try {
            boolean read = s.read(new File("examples/test/test_sv_1.rddl"));
            Assert.assertTrue(read);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Assert.fail(ex.getLocalizedMessage());
        }

        long t1 = System.nanoTime();

        boolean solve = s.solve();
        Assert.assertTrue(solve);

        long t2 = System.nanoTime();

        LOG.log(Level.INFO, "parsing time: {0}s; solving time: {1}s; total time: {2}", new Object[]{TimeUnit.NANOSECONDS.toSeconds(t1 - t0), TimeUnit.NANOSECONDS.toSeconds(t2 - t1), TimeUnit.NANOSECONDS.toSeconds(t2 - t0)});
    }

    @Test
    public void test_sv_2() {
        Solver s = new Solver();

        long t0 = System.nanoTime();
        try {
            boolean read = s.read(new File("examples/test/test_sv_2.rddl"));
            Assert.assertTrue(read);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Assert.fail(ex.getLocalizedMessage());
        }

        long t1 = System.nanoTime();

        boolean solve = s.solve();
        Assert.assertTrue(solve);

        long t2 = System.nanoTime();

        LOG.log(Level.INFO, "parsing time: {0}s; solving time: {1}s; total time: {2}", new Object[]{TimeUnit.NANOSECONDS.toSeconds(t1 - t0), TimeUnit.NANOSECONDS.toSeconds(t2 - t1), TimeUnit.NANOSECONDS.toSeconds(t2 - t0)});
    }

    @Test
    public void test_rr_0() {
        Solver s = new Solver();

        long t0 = System.nanoTime();
        try {
            boolean read = s.read(new File("examples/test/test_rr_0.rddl"));
            Assert.assertTrue(read);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Assert.fail(ex.getLocalizedMessage());
        }

        long t1 = System.nanoTime();

        boolean solve = s.solve();
        Assert.assertTrue(solve);

        long t2 = System.nanoTime();

        LOG.log(Level.INFO, "parsing time: {0}s; solving time: {1}s; total time: {2}", new Object[]{TimeUnit.NANOSECONDS.toSeconds(t1 - t0), TimeUnit.NANOSECONDS.toSeconds(t2 - t1), TimeUnit.NANOSECONDS.toSeconds(t2 - t0)});
    }
}
