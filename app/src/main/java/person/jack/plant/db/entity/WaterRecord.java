package person.jack.plant.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 灌溉记录
 * Created by yanxu on 2018/6/4.
 */
@DatabaseTable(tableName = "water_record")
public class WaterRecord {
    /**
     * id
     */
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;

    /**
     * 植物名称
     */
    @DatabaseField (columnName = "name")
    private String name;
    /**
     * 灌溉时间
     */
    @DatabaseField (columnName = "waterDate")
    private Date waterDate;

    public WaterRecord() {
    }

    public WaterRecord(int id, String name, Date waterDate) {
        this.id = id;
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
