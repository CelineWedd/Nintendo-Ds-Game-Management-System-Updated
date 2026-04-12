package ndsgms;
/**
 * Celine Wedderburn
 * CEN-3024C - Software Development 1
 * Mar 27, 2026
 * databaseManager.java
 *
 * The purpose of this class is to manage the sqlite database. This class communicates with the controller by running
 * SQL and allowing ds games to be stored, saved, updated and removed from a real database.
 */

import java.io.File;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ndsgms.NDSgmsController.DsGame;
// database class and connection to the database
public class databaseManager {
    private static String URL = "jdbc:sqlite:games.db";
    public static void setDatabase(String dbPath) {
        File file = new File(dbPath);
        // Validating if file exist in location
        if (!file.exists()) {
            throw new RuntimeException("Database file not found: " + dbPath);
        }
        URL = "jdbc:sqlite:" + dbPath;
    }
    public static void init() {
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            // Launches database and creates a table if not already existing
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS games (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    developer TEXT,
                    genre TEXT,
                    year INTEGER,
                    multiplayer INTEGER,
                    rating TEXT
                )
            """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Inserts new ds game into the database
    // Creates an ID for new ds game
    public static int insertGame(DsGame game) {
        String sql = """
            INSERT INTO games (title, developer, genre, year, multiplayer, rating)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getDeveloper());
            pstmt.setString(3, game.getGenre());
            pstmt.setInt(4, game.getReleaseYear());
            pstmt.setInt(5, game.getMultiplayerString().equals("Yes") ? 1 : 0);
            pstmt.setString(6, game.getRating());

            pstmt.executeUpdate();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    // Retrieves all games from the database and displays them into a list
    public static ObservableList<DsGame> getAllGames() {
        ObservableList<DsGame> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM games";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DsGame game = new DsGame(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("developer"),
                        rs.getString("genre"),
                        rs.getInt("year"),
                        rs.getInt("multiplayer") == 1,
                        rs.getString("rating")
                );
                list.add(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // Updates an already existing ds game
    public static void updateGame(DsGame game) {
        String sql = """
            UPDATE games
            SET title=?, developer=?, genre=?, year=?, multiplayer=?, rating=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getDeveloper());
            pstmt.setString(3, game.getGenre());
            pstmt.setInt(4, game.getReleaseYear());
            pstmt.setInt(5, game.getMultiplayerString().equals("Yes") ? 1 : 0);
            pstmt.setString(6, game.getRating());
            pstmt.setInt(7, game.getId());

          pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Removes an already existing ds game
    public static void deleteGame(int id) {
        String sql = "DELETE FROM games WHERE id=?";

        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}