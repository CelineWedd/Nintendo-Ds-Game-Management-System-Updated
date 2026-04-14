package ndsgms;
/**
 * @author Celine Wedderburn
 * CEN-3024C - Software Development 1
 * @since Mar 27, 2026
 * databaseManager.java
 *
 * The purpose of this class is to manage the sqlite database for the Nintendo Ds Game Management System.
 * This class communicates with the controller by running SQL and allowing ds games to be stored, saved,
 * updated and removed from a real database.
 */

import java.io.File;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ndsgms.NDSgmsController.DsGame;

public class databaseManager {
    private static String URL = "jdbc:sqlite:games.db";

    /**
     * setup for the file path that will be used by the application
     *
     * @param dbPath the file path that the application uses.
     * @throws RuntimeException if the database file does not exist
     */
    public static void setDatabase(String dbPath) {
        File file = new File(dbPath);
        if (!file.exists()) {
            throw new RuntimeException("Database file not found: " + dbPath);
        }
        URL = "jdbc:sqlite:" + dbPath;
    }
    /**
     * Creates the table for the ds games if one does not already exist.
     */
    public static void init() {
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
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
    /**
     * Inserts new ds game into the database
     *
     * @param game the Ds game object along with its data to be added
     * @return an ID of the newly added Ds game, or -1 if new Ds game added fails
     */
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
    /**
     * Retrieves all games from the database and displays them into a list
     *
     * @return a list of all ds games that are currently stored into the database
     */
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
    /**
     * Updates an already existing ds game in the database
     *
     * @param game the ds game object that has the newly updated game data
     */
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
    /**
     * Removes an already existing ds game from the database based on its id
     *
     * @param id the unique identifier of the specific ds game that will be removed
     */
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