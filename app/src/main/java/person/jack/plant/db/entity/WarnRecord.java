package person.jack.plant.db.entity;

import java.util.Date;

/**
 * 报警记录
 * Created by yanxu on 2018/6/4.
 */

public class WarnRecord {
    /**
     *植物名称
     */
    private String name;
    /**
     * 报警类型
     */
    private String type;
    /**
     * 数值
     */
    private Integer value;
    /**
     * 报警时间
     */
    private Date warnDate;

    public WarnRecord() {
    }

    public WarnRecord(String name, String type, Integer value, Date warnDate) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.warnDate = warnDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getWarnDate() {
        return warnDate;
    }

    public void setWarnDate(Date warnDate) {
        this.warnDate = warnDate;
    }
}
