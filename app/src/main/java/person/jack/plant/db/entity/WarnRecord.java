package person.jack.plant.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 报警记录类
 * Created by yanxu on 2018/6/4.
 */
@DatabaseTable(tableName = "warn_record")
public class WarnRecord {
    /**
     * id
     */
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;

    /**
     *植物名称
     */
    @DatabaseField (columnName = "name")
    private String name;
    /**
     * 报警类型
     */
    @DatabaseField (columnName = "type")
    private String type;
    /**
     * 数值
     */
    @DatabaseField (columnName = "value")
    private Integer value;
    /**
     * 报警时间
     */
    @DatabaseField (columnName = "warnDate")
    private Date warnDate;

    public WarnRecord() {
    }

    public WarnRecord(int id, String name, String type, Integer value, Date warnDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.warnDate = warnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
