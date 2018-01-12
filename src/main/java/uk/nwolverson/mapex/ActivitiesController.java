package uk.nwolverson.mapex;

import javastrava.service.Strava;
import org.postgis.PGbox2d;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ActivitiesController {
    @Autowired
    StravaContext stravaCtx;

    @Autowired
    Db db;

    @RequestMapping("/activities")
    public List<ActivitySummary> index() {
        Strava strava = stravaCtx.getStrava();
        return strava.listAuthenticatedAthleteActivities().stream()
                .map(ActivitySummary::new)
                .collect(Collectors.toList());
    }

    @RequestMapping("/activities/{id}")
    public AnalyseResult analyse(@PathVariable long id) throws SQLException {
        String polyline = stravaCtx.getStrava().getActivity(id).getMap().getPolyline();
        PreparedStatement preparedStatement = db.getConnection().prepareStatement(
                "SELECT st_asgeojson(shape), st_asgeojson(path), box2d(path)\n" +
                "    FROM voronoi_postcodes pc, " +
                "       (SELECT ST_LineFromEncodedPolyline(?) as path) as routes\n" +
                "    WHERE st_intersects(pc.shape, path)");
        preparedStatement.setString(1, polyline);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> areas = new ArrayList<>();
        String path = "";
        PGbox2d bounds = null;
        while (resultSet.next()) {
            String area = resultSet.getString(1);
            path = resultSet.getString(2);
            bounds = (PGbox2d) resultSet.getObject(3);
            areas.add(area);
        }
        return new AnalyseResult(areas, path, bounds);
    }
}
