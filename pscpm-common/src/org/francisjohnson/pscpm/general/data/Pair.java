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
