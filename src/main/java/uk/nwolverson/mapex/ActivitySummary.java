package uk.nwolverson.mapex;

import javastrava.model.StravaActivity;

public class ActivitySummary {
    private Long id;
    private String name;

    ActivitySummary(StravaActivity act) {
        this.id = act.getId();
        this.name = act.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
