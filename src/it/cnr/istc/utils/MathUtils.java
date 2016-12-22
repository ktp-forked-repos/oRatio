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
package it.cnr.istc.utils;

import java.math.BigInteger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class MathUtils {

    public static final double log2 = Math.log(2);

    public static double log2(double a) {

        return Math.log(a) / log2;
    }

    public static long safeAdd(long a, long b) {
        long r = a + b;
        return ((a ^ r) & (b ^ r)) < 0 ? a > 0 ? Long.MAX_VALUE : Long.MIN_VALUE : r;
    }

    public static long safeSub(long a, long b) {
        if (b == Long.MAX_VALUE || a == Long.MIN_VALUE) {
            return Long.MIN_VALUE;
        } else if (a == Long.MAX_VALUE) {
            assert b != Long.MIN_VALUE;
            return Long.MAX_VALUE;
        } else {
            return safeAdd(a, -b);
        }
    }

    public static long safeMult(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        long r = a * b;
        return ((a ^ b) ^ r) < 0 ? (a ^ b) > 0 ? Long.MIN_VALUE : Long.MAX_VALUE : r;
    }

    public static BigInteger factorial(int x) {
        BigInteger f = BigInteger.ONE;
        for (int i = 2; i <= x; i++) {
            f = f.multiply(BigInteger.valueOf(i));
        }
        return f;
    }

    public static int combinations(int n, int k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n - k))).intValue();
    }

    private MathUtils() {
    }
}
