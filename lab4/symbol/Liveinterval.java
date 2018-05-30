package symbol;

public class Liveinterval implements Comparable<Liveinterval> {
    // this stores TEMP id and its start lines and end lines in a procedure
    public int tmpnum;
    public int start;
    public int end;
    public Liveinterval(int t, int s, int e) {
        tmpnum = t;
        start = s;
        end = e;
    }
    // will sort according to its start
    public int compareTo(Liveinterval other) {
        return (start == other.start)?(end - other.end):(start - other.start);
    }
    public String toString() {
        return "t: " + tmpnum + " s: " + start + " t: " + end;
    }
}
