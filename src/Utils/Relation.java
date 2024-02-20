
package Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Relation<I, J, K> {
    Map<I, Map<J, K>> map = new HashMap();

    public Relation() {
    }

    public void setValue(I key1, J key2, K value) {
        Map<J, K> jkMap = (Map)this.map.get(key1);
        if (jkMap == null) {
            jkMap = new HashMap();
            this.map.put(key1, jkMap);
        }

        ((Map)jkMap).put(key2, value);
    }

    public Set<I> getAllFirstKeys() {
        return this.map.keySet();
    }

    public K getValue(I key1, J key2) {
        Map<J, K> jkMap = (Map)this.map.get(key1);
        return jkMap == null ? null : jkMap.get(key2);
    }

    public Set<J> getSecondKeys(I key1) {
        return ((Map)this.map.get(key1)).keySet();
    }
}