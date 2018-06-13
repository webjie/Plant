package person.jack.plant.model;

import java.io.Serializable;
import java.util.Map;

/**
 *可序列化map可以实现intent传递
 * Created by kakee on 2018/6/13.
 */

public class SerializableMap implements Serializable {

    private Map<String,int[]> map;

    public Map<String, int[]> getMap() {
        return map;
    }

    public void setMap(Map<String, int[]> map) {
        this.map = map;
    }
}
