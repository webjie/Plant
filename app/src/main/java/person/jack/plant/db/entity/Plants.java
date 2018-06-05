package person.jack.plant.db.entity;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Blob;
import java.util.Date;

/**
 * Created by yanxu on 2018/6/4.
 */
@DatabaseTable(tableName = "plants")
public class Plants {
    /**
     * id
     */
    @DatabaseField(columnName = "id",generatedId = true)
    private Integer id;

    /**
     * 图片
     */
    @DatabaseField (columnName = "image")
    private Integer Image;
    /**
     * 植物名称
     */
    @DatabaseField (columnName = "name")
    private String name;
    /**
     * 种植日期
     */
    @DatabaseField (columnName = "plantingDate")
    private Date plantingDate;
    /**
     * 生长阶段
     */
    @DatabaseField (columnName = "growthStage")
    private String growthStage;

    public Plants() {
    }

    public Plants(Integer id, Integer image, String name, String growthStage, Date plantingDate) {
        this.id = id;
        Image = image;
        this.name = name;
        this.plantingDate = plantingDate;
        this.growthStage = growthStage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImage() {
        return Image;
    }

    public void setImage(Integer image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(Date plantingDate) {
        this.plantingDate = plantingDate;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }
}
