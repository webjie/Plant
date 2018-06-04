package person.jack.plant.model;

/**
 * Created by kakee on 2018/6/4.
 */

public class Plants {
    private String name;
    private String logo;

    public Plants() {
    }

    public Plants(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }
}
