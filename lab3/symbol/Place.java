package symbol;

public class Place extends Allsp {
    public String stmt;
    public boolean exp;

    public Place(String s, boolean in) {
        stmt = s;
        exp = in;
    }
    public boolean inExp() {
        return (exp == true);
    }
}
