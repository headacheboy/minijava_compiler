package symbol;

public class SpigletResult extends Allsp {
    private String result;
    private boolean simple;

    public SpigletResult(String res, boolean sim) {
        super();
        result = res;
        simple = sim;
    }
    public void addStr(String str) {
        result += str;
    }
    public String toString() {
        return result;
    }
    public boolean isTemp() {
        return result.startsWith("TEMP");
    }
    public boolean isSimple() {
        return simple;
    }
}
