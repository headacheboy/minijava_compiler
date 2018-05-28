package symbol;

public class Liveinterval implements Comparable<Liveinterval> {
    public int tmpnum;
    public int start;
    public int end;
    public Liveinterval(int t, int s, int e) {
        tmpnum = t;
        start = s;
        end = e;
    }
    public int compareTo(Liveinterval other) {
        return (start == other.start)?(other.end - end):(start - other.start);
    }
    public String toString() {
        return "t: " + tmpnum + " s: " + start + " t: " + end;
    }
}
