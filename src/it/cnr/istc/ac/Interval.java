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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Interval implements Domain {

    double lb, ub;

    public Interval() {
        this.lb = Double.NEGATIVE_INFINITY;
        this.ub = Double.POSITIVE_INFINITY;
    }

    public Interval(double val) {
        this.lb = val;
        this.ub = val;
    }

    public Interval(double lb, double ub) {
        this.lb = lb;
        this.ub = ub;
    }

    public Interval(Interval domain) {
        this.lb = domain.lb;
        this.ub = domain.ub;
    }

    public double getLb() {
        return lb;
    }

    public double getUb() {
        return ub;
    }

    @Override
    public boolean isSingleton() {
        return lb == ub;
    }

    public boolean intersects(Interval domain) {
        return ub >= domain.lb && lb <= domain.ub;
    }

    public boolean contains(Interval domain) {
        return lb <= domain.lb && ub >= domain.ub;
    }

    public boolean neq(Interval right) {
        return ub < right.lb || lb > right.ub;
    }

    public boolean neq(double right) {
        return ub < right || lb > right;
    }

    public boolean lt(Interval right) {
        return ub < right.lb;
    }

    public boolean lt(double right) {
        return ub < right;
    }

    public boolean leq(Interval right) {
        return ub <= right.lb;
    }

    public boolean leq(double right) {
        return ub <= right;
    }

    public boolean eq(Interval right) {
        return isSingleton() && right.isSingleton() && lb == right.lb;
    }

    public boolean eq(double right) {
        return isSingleton() && lb == right;
    }

    public boolean gt(Interval right) {
        return lb > right.ub;
    }

    public boolean gt(double right) {
        return lb > right;
    }

    public boolean geq(Interval right) {
        return lb >= right.ub;
    }

    public boolean geq(double right) {
        return lb >= right;
    }

    public static Interval negate(Interval lhs) {
        return new Interval(-lhs.ub, -lhs.lb);
    }

    public static Interval add(Interval lhs, Interval rhs) {
        return new Interval(lhs.lb + rhs.lb, lhs.ub + rhs.ub);
    }

    public static Interval add(Interval lhs, double rhs) {
        return new Interval(lhs.lb + rhs, lhs.ub + rhs);
    }

    public static Interval add(double lhs, Interval rhs) {
        return new Interval(lhs + rhs.lb, lhs + rhs.ub);
    }

    public static Interval subtract(Interval lhs, Interval rhs) {
        return new Interval(lhs.lb - rhs.ub, lhs.ub - rhs.lb);
    }

    public static Interval subtract(Interval lhs, double rhs) {
        return new Interval(lhs.lb - rhs, lhs.ub - rhs);
    }

    public static Interval subtract(double lhs, Interval rhs) {
        return new Interval(lhs - rhs.ub, lhs - rhs.lb);
    }

    public static Interval multiply(Interval lhs, Interval rhs) {
        Interval result = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for (double val : new double[]{lhs.lb * rhs.lb, lhs.lb * rhs.ub, lhs.ub * rhs.lb, lhs.ub * rhs.ub}) {
            if (val < result.lb) {
                result.lb = val;
            }
            if (val > result.ub) {
                result.ub = val;
            }
        }
        return result;
    }

    public static Interval multiply(Interval lhs, double rhs) {
        if (rhs >= 0) {
            return new Interval(lhs.lb * rhs, lhs.ub * rhs);
        } else {
            return new Interval(lhs.ub * rhs, lhs.lb * rhs);
        }
    }

    public static Interval multiply(double lhs, Interval rhs) {
        if (lhs >= 0) {
            return new Interval(lhs * rhs.lb, lhs * rhs.ub);
        } else {
            return new Interval(lhs * rhs.ub, lhs * rhs.lb);
        }
    }

    public static Interval divide(Interval lhs, Interval rhs) {
        if (rhs.lb <= 0 && rhs.ub >= 0) {
            // 0 appears in the denominator..
            return new Interval();
        } else {
            Interval result = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
            for (double val : new double[]{lhs.lb / rhs.lb, lhs.lb / rhs.ub, lhs.ub / rhs.lb, lhs.ub / rhs.ub}) {
                if (val < result.lb) {
                    result.lb = val;
                }
                if (val > result.ub) {
                    result.ub = val;
                }
            }
            return result;
        }
    }

    public static Interval divide(Interval lhs, double rhs) {
        if (rhs >= 0) {
            return new Interval(lhs.lb / rhs, lhs.ub / rhs);
        } else {
            return new Interval(lhs.ub / rhs, lhs.lb / rhs);
        }
    }

    public static Interval divide(double lhs, Interval rhs) {
        if (lhs >= 0) {
            return new Interval(lhs / rhs.lb, lhs / rhs.ub);
        } else {
            return new Interval(lhs / rhs.ub, lhs / rhs.lb);
        }
    }

    public Interval add(Interval rhs) {
        lb += rhs.lb;
        ub += rhs.ub;
        return this;
    }

    public Interval add(double rhs) {
        lb += rhs;
        ub += rhs;
        return this;
    }

    public Interval subtract(Interval rhs) {
        lb -= rhs.ub;
        ub -= rhs.lb;
        return this;
    }

    public Interval subtract(double rhs) {
        lb -= rhs;
        ub -= rhs;
        return this;
    }

    public Interval multiply(Interval rhs) {
        double c_lb = Double.POSITIVE_INFINITY;
        double c_ub = Double.NEGATIVE_INFINITY;
        for (double val : new double[]{lb * rhs.lb, lb * rhs.ub, ub * rhs.lb, ub * rhs.ub}) {
            if (val < c_lb) {
                c_lb = val;
            }
            if (val > c_ub) {
                c_ub = val;
            }
        }
        lb = c_lb;
        ub = c_ub;
        return this;
    }

    public Interval multiply(double rhs) {
        if (rhs >= 0) {
            lb *= rhs;
            ub *= rhs;
        } else {
            double c_lb = lb;
            lb = ub * rhs;
            ub = c_lb * rhs;
        }
        return this;
    }

    public Interval divide(Interval rhs) {
        if (rhs.lb <= 0 && rhs.ub >= 0) {
            // 0 appears in the denominator..
            lb = Double.NEGATIVE_INFINITY;
            ub = Double.POSITIVE_INFINITY;
        } else {
            double c_lb = Double.POSITIVE_INFINITY;
            double c_ub = Double.NEGATIVE_INFINITY;
            for (double val : new double[]{lb / rhs.lb, lb / rhs.ub, ub / rhs.lb, ub / rhs.ub}) {
                if (val < c_lb) {
                    c_lb = val;
                }
                if (val > c_ub) {
                    c_ub = val;
                }
            }
            lb = c_lb;
            ub = c_ub;
        }
        return this;
    }

    public Interval divide(double rhs) {
        if (rhs >= 0) {
            lb /= rhs;
            ub /= rhs;
        } else {
            double c_lb = lb;
            lb = ub / rhs;
            ub = c_lb / rhs;
        }
        return this;
    }

    @Override
    public String toString() {
        if (isSingleton()) {
            return Double.toString(lb);
        } else {
            return "[" + Double.toString(lb) + ", " + Double.toString(ub) + "]";
        }
    }
}
