package person.jack.plant.db.entity;

/**
 * Created by yanxu on 2018/6/4.
 */

/**
 * 设置阈值
 */
public class ValueSet {
    /**
     *植物名称
     */
    private String name;
    /**
     * 最小值
     */
    private String min;
    /**
     * 最大值
     */
    private String max;

    public ValueSet() {
    }

    public ValueSet(String name, String min, String max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
