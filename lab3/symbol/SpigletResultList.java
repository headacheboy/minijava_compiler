package symbol;
import java.util.*;

public class SpigletResultList extends Allsp {
    public Vector<SpigletResult> nodes;
    public SpigletResultList() {
        nodes = new Vector<SpigletResult>();
    }
    public void addResult(SpigletResult res) {
        nodes.add(res);
    }
    public String toString() {
        String retStr = "";
        for (SpigletResult iter : nodes) {
            retStr += (iter.toString() + " ");
        }
        return retStr;
    }
}
