/* 
 * Copyright (C) 2018 Francis Johnson
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
package org.francisjohnson.pscpm.general.data;

public class Pair<One, Two> {
    private One one;
    private Two two;

    public Pair() {
    }

    public Pair(One one, Two two) {
        setOne(one);
        setTwo(two);
    }

    public void setOne(One one) {
        this.one = one;
    }

    public One getOne() {
        return one;
    }

    public void setTwo(Two two) {
        this.two = two;
    }

    public Two getTwo() {
        return two;
    }

    public One one() {
        return getOne();
    }

    public Two two() {
        return getTwo();
    }

    public One obj1() {
        return getOne();
    }

    public Two obj2() {
        return getTwo();
    }

    @Override
    public int hashCode() {
        return (getOne() == null ? 0 : getOne().hashCode()) &
            (getTwo() == null ? 0 : getTwo().hashCode());
    }

    @Override
    public boolean equals(Object rhsO) {
        Pair<?, ?> rhs = rhsO instanceof Pair ? (Pair<?, ?>)rhsO : null;
        return rhs != null && rhs.getOne() != null && rhs.getTwo() != null &&
            getOne() != null && getTwo() != null &&
            getOne().equals(rhs.getOne()) && getTwo().equals(rhs.getTwo());
    }
}
