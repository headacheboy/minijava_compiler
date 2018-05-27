package symbol;

import java.util.*;
import java.util.Map.*;

public class RegAlloc {
    HashMap<String, FlowGraph> flowGraphHashMap;
    public RegAlloc(HashMap<String, FlowGraph> flowGraphHashMap_) {
        flowGraphHashMap = flowGraphHashMap_;
    }
    public void updateInterval() {
        for (FlowGraph fg : flowGraphHashMap.values()) {
            ProcedureBlock pBlock = fg.pBlock;
            for (Entry<Integer, FlowBlock> entry : fg.mBlock.entrySet()) {
                int curLine = entry.getKey();
                HashSet<Integer> IN = entry.getValue().In;
                for (Integer integer : IN) {
                    if (integer == 0) {
                        // entry blockid is 0, skip
                        continue;
                    }
                    if (curLine <= pBlock.tmpMap.get(integer).start) {
                        pBlock.tmpMap.get(integer).start = curLine;
                    }
                    if (curLine >= pBlock.tmpMap.get(integer).end) {
                        pBlock.tmpMap.get(integer).end = curLine;
                    }
                }
            }
        }
    }
    ProcedureBlock curpBlock;
    Vector<Integer> curactive;
    Vector<Liveinterval> curlives;
    boolean[] curusedR;
    int curspillIdx;
    public void expireOld(int intervalId) {
        Liveinterval curInterval = curlives.get(intervalId);
        int cursize = curactive.size();
        for (int j=0, realj=0; j<cursize; j++, realj++) {
            int curj = curactive.get(realj);
            if (curlives.get(curj).end < curInterval.end) {
                curactive.removeElement(realj);
                realj -= 1;
                curusedR[curj] = false;
            }
        }
    }
    public void spillInterval(int intervalId) {
        Liveinterval spill = curlives.get(curactive.get(0));
        int spill_idx = 0;
        for (int i : curactive) {
            Liveinterval mapInterval = curlives.get(i);
            if (mapInterval.end > spill.end) {
                spill = mapInterval;
                spill_idx = i;
            }
        }
        Liveinterval curInterval = curlives.get(intervalId);
        if (spill.end > curInterval.end) {
            curpBlock.regCandi.put(curInterval.tmpnum, curpBlock.regCandi.get(spill.tmpnum));
            curpBlock.regCandi.remove(spill.tmpnum);
            curpBlock.regStack.put(spill.tmpnum, "SPILLARG " + curspillIdx);
            curactive.removeElement(spill_idx);
            curactive.add(intervalId);
        } else {
            curpBlock.regStack.put(curInterval.tmpnum, "SPILLARG " + curspillIdx);
            curspillIdx ++;
        }
    }
    public void alloc() {
        updateInterval();
        for (FlowGraph fg : flowGraphHashMap.values()) {
            Vector<Liveinterval> lives = new Vector<Liveinterval>();
            curpBlock = fg.pBlock;
            curlives = lives;
            for (Liveinterval liint : curpBlock.tmpMap.values()) {
                curlives.add(liint);
            }
            Collections.sort(curlives);

            Vector<Integer> active = new Vector<Integer>();
            curactive = active;
            int curIntervalnum = 0;
            curspillIdx = 0;
            int R = 18; // t0-9; s0-7
            boolean[] usedR = new boolean[R];
            curusedR = usedR;
            for (Liveinterval liint : curlives) { // increasing start point
                expireOld(curIntervalnum);
                if (curactive.size() == R) {
                    spillInterval(curIntervalnum);
                } else {
                    for (int r=0; r<R; ++r) {
                        if (!curusedR[r]) {
                            curusedR[r] = true;
                            String regname;
                            if (r <= 10) {
                                regname = "t" + r;
                            } else {
                                regname = "s" + (r-10);
                            }
                            curpBlock.regCandi.put(liint.tmpnum, regname);
                        }
                    }
                    curactive.add(curIntervalnum);
                }
                curIntervalnum++;
            }
            curpBlock.useStack = curspillIdx + curpBlock.regCandi.size();
        }
    }
}
