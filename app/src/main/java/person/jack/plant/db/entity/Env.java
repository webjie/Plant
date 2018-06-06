package person.jack.plant.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yanxu on 2018/6/6.
 */
@DatabaseTable (tableName = "env_record")
public class Env {
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;
    @DatabaseField(columnName = "humidity")
    private int humidity;
    @DatabaseField(columnName = "temperature")
    private int temperature;
    @DatabaseField(columnName = "light")
    private int light;

    public Env(int id, int humidity, int temperature, int light) {
        this.id = id;
        this.humidity = humidity;
        this.temperature = temperature;
        this.light = light;
    }

    public Env() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
