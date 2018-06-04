package person.jack.plant.model;

import java.io.Serializable;

import person.jack.plant.common.NotObfuscateInterface;

/**
 * Created by kakee on 2018/6/4.
 */

public class Plants implements Serializable, NotObfuscateInterface {
    private String name;
    private String logo;

    public Plants() {
    }

    public Plants(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }
}
