package uk.nwolverson.mapex;

import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private String dbconn;

    public Db(String conn) {
        dbconn = conn;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(dbconn, "postgres", "pwd");
        ((PGConnection)connection).addDataType("geometry",PGgeometry.class);
        ((PGConnection)connection).addDataType("box3d", PGbox3d.class);
        return connection;
    }
}
