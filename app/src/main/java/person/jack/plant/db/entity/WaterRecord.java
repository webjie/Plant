package person.jack.plant.db.entity;

import java.util.Date;

/**
 * 灌溉记录
 * Created by yanxu on 2018/6/4.
 */

public class WaterRecord {
    /**
     * 植物名称
     */
    private String name;
    /**
     * 灌溉时间
     */
    private Date waterDate;

    public WaterRecord() {
    }

    public WaterRecord(String name, Date waterDate) {
        this.name = name;
        this.waterDate = waterDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getWaterDate() {
        return waterDate;
    }

    public void setWaterDate(Date waterDate) {
        this.waterDate = waterDate;
    }
}
