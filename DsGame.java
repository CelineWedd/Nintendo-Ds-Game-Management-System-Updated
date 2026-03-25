/**
* Celine Wedderburn
* CEN-3024C - Software Development 1
* Mar 4, 2026
* DSGame.java

* The purpose of this class is to store information of each DS Game entered into the system. This information includes storing data
* such as the DS Game title, developer, genre,release year,rating and whether the DS game has multiplayer support.This class is
* important to the overall application because it allows for easy management and organization of Ds Game information.
*/

//Ds Game Information
public class DsGame {
    //Gives all ds games a personal id depending on however many are in the system
    //Counts up
    private static int idcounter = 1 ;
    private int id;
    String title;
    String developer;
    String genre;
    int releaseYear;
    boolean multiplayerSupport;
    String rating;

    // This allows the Ds game to be created within the system with its new information
    public DsGame(String title, String developer, String genre, int releaseYear,
                  boolean multiplayerSupport, String rating) {
        this.id = idcounter++;
        this.title = title;
        this.developer = developer;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.multiplayerSupport = multiplayerSupport;
        this.rating = rating;
    }
    //Retrieves Ds game information
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public boolean isMultiplayerSupport() {
        return multiplayerSupport;
    }

    public void setMultiplayerSupport(boolean multiplayerSupport) {
        this.multiplayerSupport = multiplayerSupport;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return  "Title: " + title + "\n" +
                "Developer: " + developer + "\n" +
                "Genre: " + genre + "\n" +
                "Release Year: " + releaseYear + "\n" +
                "Multiplayer: " + (multiplayerSupport ? "Yes" : "No")  + "\n" +
                "Rating: " + rating + "\n" +
                "--------------------";
    }
}
