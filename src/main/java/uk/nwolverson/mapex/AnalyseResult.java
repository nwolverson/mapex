package uk.nwolverson.mapex;

import org.postgis.PGbox2d;

import java.util.Arrays;
import java.util.List;

public class AnalyseResult {
    private List<String> areas;
    private String path;
    private List<List<Double>> bounds;

    public AnalyseResult(List<String> areas, String path, PGbox2d bb) {
        this.areas = areas;
        this.path = path;
        this.bounds =
                Arrays.asList(
                        Arrays.asList(bb.getLLB().y, bb.getLLB().x),
                        Arrays.asList(bb.getURT().y, bb.getURT().x)
                );
    }

    public List<String> getAreas() {
        return areas;
    }

    public String getPath() {
        return path;
    }

    public List<List<Double>> getBounds() {
        return bounds;
    }
}
