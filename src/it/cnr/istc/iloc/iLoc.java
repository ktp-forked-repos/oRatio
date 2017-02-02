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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.gui.ResolutionTreeJFrame;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class iLoc {

    private static final Logger LOG = Logger.getLogger(iLoc.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Set<String> opts = new HashSet<>(Arrays.asList(args));
        opts.removeIf(o -> !o.startsWith("-"));

        if (!opts.isEmpty()) {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    try {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(iLoc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        boolean show_resolution_tree = opts.remove("-show-resolution-tree");

        if (args.length - opts.size() == 0) {
            LOG.severe("there are no input files..");
            printUsage();
            System.exit(0);
        }

        Solver s = new Solver();

        if (show_resolution_tree) {
            ResolutionTreeJFrame frame = new ResolutionTreeJFrame(s);
            frame.setVisible(true);
        }

        LOG.log(Level.INFO, "Reading {0} files..", Stream.of(args).filter(arg -> !arg.startsWith("-")).count());
        long t0 = System.nanoTime();
        try {
            if (!s.read(Stream.of(args).filter(arg -> !arg.startsWith("-")).map(arg -> new File(arg)).toArray(File[]::new))) {
                LOG.severe("The initial problem is inconsistent..");
                long t1 = System.nanoTime();
                LOG.log(Level.INFO, "Parsing time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t1 - t0));
            }
        } catch (IOException ex) {
            LOG.severe(ex.getLocalizedMessage());
            System.exit(0);
        }

        long t1 = System.nanoTime();
        LOG.log(Level.INFO, "Parsing time: {0}s", TimeUnit.NANOSECONDS.toSeconds(t1 - t0));

        if (!s.solve()) {
            LOG.severe("The problem is unsolvable..");
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

    private static void printUsage() {
        LOG.info("usage: java -jar oRatio.jar [options] <file-paths>");
        LOG.info("-show-planning-graph        Shows the generated planning graph");
    }
}
