package symbol;

public class Place extends Allsp {
    // to show the exp in which stmt
    // or whether the exp in another exp
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
