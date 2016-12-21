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
package it.cnr.istc.oratio;

import it.cnr.istc.oratio.solver.Solver;
import it.cnr.istc.oratio.solver.gui.PlanningGraphJFrame;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class oRatio {

    private static final Logger LOG = Logger.getLogger(oRatio.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.info("Starting oRatio solver..");

        if (args.length == 0) {
            LOG.severe("there are no input files..");
        }

        Solver s = new Solver();

        PlanningGraphJFrame frame = new PlanningGraphJFrame(s);
        frame.setVisible(true);

        LOG.log(Level.INFO, "Reading {0} files..", args.length);
        long t0 = System.nanoTime();
        try {
            if (!s.read(Stream.of(args).map(arg -> new File(arg)).toArray(File[]::new))) {
                LOG.severe("inconsistent initial problem..");
                long t1 = System.nanoTime();
                LOG.log(Level.INFO, "Parsing time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t1 - t0));
            }
        } catch (IOException ex) {
            Logger.getLogger(oRatio.class.getName()).log(Level.SEVERE, null, ex);
        }

        long t1 = System.nanoTime();
        LOG.log(Level.INFO, "Parsing time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t1 - t0));

        LOG.info("Solving the problem..");
        if (!s.solve()) {
            LOG.severe("the problem has no solution..");
            long t2 = System.nanoTime();
            LOG.log(Level.INFO, "Solving time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t2 - t1));
            LOG.log(Level.INFO, "Total time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t2 - t0));
        } else {
            LOG.info("We have found a solution!!");
            long t2 = System.nanoTime();
            LOG.log(Level.INFO, "Solving time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t2 - t1));
            LOG.log(Level.INFO, "Total time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t2 - t0));
        }
    }
}
