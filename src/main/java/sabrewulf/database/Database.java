package sabrewulf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sabrewulf.game.*;
import sabrewulf.game.Character;

public class Database {

    static final String dbURL = "jdbc:sqlite:./assets/db/levelsDB.db";
    static Connection conn = createConnection();

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            System.out.println("Could not connect to database " + e);
            System.exit(5);

            return null;
        }
    }

    public static Vector getStartPos(String levelName) {
        try {

            String sql = "SELECT x, level FROM " + levelName + " WHERE type = 'StartPos'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int x = rs.getInt("x");
            int level = rs.getInt("level");

            int TILE_WIDTH = 70;
            int FLOOR_HEIGHT = 180;

            return new Vector(x * TILE_WIDTH, TILE_WIDTH + (level * FLOOR_HEIGHT));

        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Vector(70, 0);
        }
    }

    public static Vector getCheckpoints(String levelName) {
        try {

            String sql = "SELECT x, level FROM " + levelName + " WHERE type = 'Checkpoint'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int x = rs.getInt("x");
            int level = rs.getInt("level");

            int TILE_WIDTH = 70;
            int FLOOR_HEIGHT = 180;

            return new Vector(x * TILE_WIDTH, level * FLOOR_HEIGHT);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void setPlatform(String levelName, int levelID, int object_ID, int x, int level, int width,
            String view_color) {
        try {

            String type = "Platform";
            int height = 1;
            String object_colour = "NEUTRAL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, object_ID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setInt(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setWall(String levelName, int levelID, int objectID, int x, int level, int height,
            String view_color) {
        try {

            String type = "Wall";
            String width = "NULL";
            String object_colour = "NEUTRAL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setString(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setFakePlatform(String levelName, int levelID, int objectID, int x, int level, int width,
            String view_color) {
        try {

            String type = "FakePlatform";
            int height = 1;
            String object_colour = "NEUTRAL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setInt(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setExit(String levelName, int levelID, int objectID, int x, int level) {
        try {

            String type = "Exit";
            int height = 1;
            String width = "NULL";
            String object_colour = "NEUTRAL";
            String view_color = "NEUTRAL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setString(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setSpike(String levelName, int levelID, int objectID, int x, int level, String view_color,
            String object_colour) {
        try {

            String type = "Spike";
            int height = 1;
            String width = "NULL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setString(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setCheckpoint(String levelName, int levelID, int objectID, int x, int level, String view_color,
            String object_colour) {
        try {

            String type = "Checkpoint";
            int height = 1;
            String width = "NULL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setString(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setStartPos(String levelName, int levelID, int objectID, int x, int level, String view_color,
            String object_colour) {
        try {

            String type = "StartPos";
            int height = 1;
            String width = "NULL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setString(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setMovingSpike(String levelName, int levelID, int objectID, int x, int level, int width,
            String view_color, String object_colour) {
        try {

            String type = "MovingSpike";
            int height = 1;
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setInt(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setSaw(String levelName, int levelID, int objectID, int x, int level, int width,
            String view_color, int min, int max) {
        try {

            String type = "Saw";
            int height = 1;
            String object_colour = "NEUTRAL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, objectID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setInt(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setInt(10, min);
            ps.setInt(11, max);

            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setSpikePlatform(String levelName, int levelID, int object_ID, int x, int level, int width,
            String view_color) {
        try {

            String type = "SpikePlatform";
            int height = 1;
            String object_colour = "NEUTRAL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, object_ID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setInt(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setSawPlatform(String levelName, int levelID, int object_ID, int x, int level, int width,
            String view_color) {
        try {

            String type = "SawPlatform";
            int height = 1;
            String object_colour = "NEUTRAL";
            String min = "NULL";
            String max = "NULL";

            String st = "INSERT INTO " + levelName + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(st);
            ps.setInt(1, levelID);
            ps.setInt(2, object_ID);
            ps.setString(3, type);
            ps.setInt(4, x);
            ps.setInt(5, level);
            ps.setInt(6, width);
            ps.setInt(7, height);
            ps.setString(8, view_color);
            ps.setString(9, object_colour);
            ps.setString(10, min);
            ps.setString(11, max);
            ps.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public static void setNewLevel(int levelID, String name) {
        try {
            String st = "INSERT INTO levels VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(st);

            Statement stat = conn.createStatement();

            String op1 = "CREATE TABLE IF NOT EXISTS " + name + " (\n" + "    levelID      INTEGER NOT NULL\n"
                    + "                         REFERENCES levels (levelID) \n" + "                         DEFAULT ( "
                    + levelID + "),\n" + "    object_ID    INTEGER UNIQUE\n"
                    + "                         PRIMARY KEY AUTOINCREMENT\n" + "                         NOT NULL,\n"
                    + "    type         VARCHAR NOT NULL,\n" + "    x            INTEGER NOT NULL,\n"
                    + "    level        INTEGER NOT NULL,\n" + "    width        INTEGER,\n"
                    + "    height       INTEGER DEFAULT (1),\n" + "    view_color   VARCHAR DEFAULT NEUTRAL,\n"
                    + "    object_color VARCHAR,\n" + "    min          INTEGER,\n" + "    max          INTEGER\n"
                    + ");";
            stat.execute(op1);

            ps.setInt(1, levelID);
            ps.setString(2, name);
            ps.executeUpdate();

            stat.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static List<String> getNames() {
        try {

            String sql = "SELECT " + "levelName" + " FROM " + "levels";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<String> list = new ArrayList<>();

            while (rs.next()) {
                String name = rs.getString("levelName");
                name = name.replaceAll("_", " ");
                list.add(name);
            }

            return list;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ServerLevel readLevel(String levelName) throws SQLException {
        levelName = levelName.replaceAll(" ", "_");
        Vector startPosition = getStartPos(levelName);

        ServerLevel level = new ServerLevel();
        level.add(new Checkpoint(startPosition, new Vector(70, 70)));
        level.setStartPosition(startPosition);

        System.out.println("level start pos: " + level.getStartPosition());

        String sql = "SELECT * FROM " + levelName;

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int x = rs.getInt("x");
            int floor = rs.getInt("level");
            int width = rs.getInt("width");
            int height = rs.getInt("height");
            int min = rs.getInt("min");
            int max = rs.getInt("max");
            String viewColor = rs.getString("view_color");
            String objColor = rs.getString("object_color");

            String type = rs.getString("type");

            switch (type) {
                case "Platform":
                    level.add(Platform.generate(x, floor, width, Color.valueOf(viewColor)));
                    break;
                case "Wall":
                    level.add(Wall.generate(x, floor, height, Color.valueOf(viewColor)));
                    break;
                case "Spike":
                    level.add(Spike.generate(x, floor, Color.valueOf(objColor), Color.valueOf(viewColor)));
                    break;
                case "Exit":
                    level.add(Exit.generate(x, floor));
                    break;
                case "FakePlatform":
                    level.add(FakePlatform.generate(x, floor, width, Color.valueOf(viewColor)));
                    break;

                case "SpikePlatform":
                    level.add(SpikePlatform.generate(x, floor, width));
                    break;
                case "MovingSpike":
                    MovingSpike j = MovingSpike.generate(x, floor, Color.valueOf(objColor), Color.valueOf(viewColor));
                    level.add(j);
                    level.add(Collider.generate(j));
                    break;

                case "Saw":
                    Saw k = Saw.generate(x, floor, min, max);
                    level.add(k);
                    level.add(Collider.generate(k));
                    break;

                case "SawPlatform":
                    level.add(SawPlatform.generate(x, floor, width, Color.valueOf(viewColor)));
                    break;
                case "Checkpoint":
                    level.add(Checkpoint.generate(x, floor));
            }
        }

        return level;
    }
}
