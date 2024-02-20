package dataStructres;

import java.util.Comparator;

/**
 * Created by Roi on 18/02/2015.
 */
public class Pair<A, B> {

    A first;
    B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {}

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair pair = (Pair) o;

        if (!first.equals(pair.first)) return false;
        if (!second.equals(pair.second)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

}
