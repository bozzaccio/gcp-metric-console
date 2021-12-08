package model;

import java.io.Serializable;

public class InternalLogMetric implements Serializable {

    private static final long serialVersionUID = 8158900388286870267L;

    private String name;
    private String description;
    private String filter;

    public InternalLogMetric() {
    }

    public InternalLogMetric(String name, String description, String filter) {
        this.name = name;
        this.description = description;
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
