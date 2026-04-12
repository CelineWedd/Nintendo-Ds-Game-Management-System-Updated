package ndsgms;
/**
 * Celine Wedderburn
 * CEN-3024C - Software Development 1
 * Mar 27, 2026
 * NDSGameManagementSystem.java
 *
 * The purpose of this application is to store and manage data of Nintendo Ds Games. This data management application
 * allows the user to add, view, update, delete and search Ds games by genre within the system. This system is called
 * the Nintendo Ds Game Management System and the expected output should be a ndsgms.ndsgms.GUI menu where the user can
 * choose on how they interact with Ds Games.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.util.Optional;


// Map is no longer needed to temporarily store data within the program.
// Observable list for user that is connected to the TableView
public class NDSgmsController {
    private ObservableList<DsGame> dsGameList = FXCollections.observableArrayList();

    @FXML
    private TableView<DsGame> tableView;
//----------------------------------------------------------
    @FXML
    private TableColumn<DsGame, Integer> colId;
    @FXML
    private TableColumn<DsGame, String> colTitle;
    @FXML
    private TableColumn<DsGame, String> colDeveloper;
    @FXML
    private TableColumn<DsGame, String> colGenre;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private TableColumn<DsGame, Integer> colYear;
    @FXML
    private TableColumn<DsGame, String> colMultiplayer;
    @FXML
    private TableColumn<DsGame, String> colRating;
//----------------------------------------------------------
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnView;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnExit;

    // Connects the controller to fxml
    // Assigns buttons, sets up tableView and adds genres to the comboBox
    @FXML
    public void initialize() {
        // TableView columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDeveloper.setCellValueFactory(new PropertyValueFactory<>("developer"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        colMultiplayer.setCellValueFactory(new PropertyValueFactory<>("multiplayerString"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableView.setItems(dsGameList);
        databaseManager.init();
        loadGamesFromDatabase();

        btnAdd.setOnAction(e -> addDsGame());
        btnView.setOnAction(e -> refreshTable());
        btnUpdate.setOnAction(e -> updateSelectedGame());
        btnRemove.setOnAction(e -> removeSelectedGame());
        btnExit.setOnAction(e -> System.exit(0));
        genreComboBox.setItems(FXCollections.observableArrayList(
                 "ARPG", "RPG", "Action-Adventure", "Racing",
                "Platformer", "FPS", "Puzzle", "Sports", "Adventure", "Survival Horror"
        ));
        genreComboBox.setValue("ARPG");
        genreComboBox.setOnAction(e -> filterByGenre());
    }
    private void loadGamesFromDatabase() {
        dsGameList.setAll(databaseManager.getAllGames());
    }

    // TableView will filter matching selected genre from ComboBox
    private void filterByGenre() {
      String selectedGenre = genreComboBox.getValue();

      ObservableList<DsGame> filtered = FXCollections.observableArrayList();
        for (DsGame game : dsGameList) {
            if (game.getGenre().equalsIgnoreCase(selectedGenre)) {
                filtered.add(game);
            }
      }
        tableView.setItems(filtered);
}
//----------------------------------------------------------------------------------------------------------------------
    // Lets user choose if they want to add games manually or via file path
    private void addDsGame() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Manual Entry","Manual Entry", "Load From Database");
        dialog.setTitle("Add DS Game");
        dialog.setHeaderText("Choose how to add a DS Game");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get().equals("Manual Entry")) {
                manuallyAddGui();
            } else {
                loadFromDatabaseGui();
            }
        }
    }
    // Allows user to enter Ds Game data manually
    // Validates that the user only enters the correct information/inputs for each piece of data
    private void manuallyAddGui() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add DS Game");
        // Input Fields
        TextField titleField = new TextField();
        TextField developerField = new TextField();
        TextField genreField = new TextField();
        TextField yearField = new TextField();
        // Dropdown menu for Multiplayer support
        ComboBox<String> multiplayerBox = new ComboBox<>();
        multiplayerBox.getItems().addAll("Yes", "No");
        multiplayerBox.setValue("Yes");
        // Dropdown menu for Rating
        ComboBox<String> ratingBox = new ComboBox<>();
        ratingBox.getItems().addAll("E", "T", "M");
        ratingBox.setValue("E");
        // Adds input boxes and labels to each one
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Title"), titleField,
                new Label("Developer"), developerField,
                new Label("Genre"), genreField,
                new Label("Release Year"), yearField,
                new Label("Multiplayer"), multiplayerBox,
                new Label("Rating"), ratingBox
        );

        dialog.getDialogPane().setContent(layout);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // loops until valid input or user cancels
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                return;
            }
            String title = titleField.getText().trim();
            String developer = developerField.getText().trim();
            String genre = genreField.getText().trim();
            String yearText = yearField.getText().trim();

            // Validation checks for if user left a field empty
            // Validation for if user input is invalid
            // Checks is user enters a valid year for a ds game
            // Checks for duplicates
            if (title.isEmpty() || developer.isEmpty() || genre.isEmpty() || yearText.isEmpty()) {
                showAlert("All fields are required.");
                continue;
            }
            int year;
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException e) {
                showAlert("Year must be a valid number.");
                continue;
            }
            if (year < 2004 || year > 2015) {
                showAlert("Year must be between 2004 and 2015.");
                continue;
            }
            boolean exists = false;
            for (DsGame game : dsGameList) {
                if (game.getTitle().equalsIgnoreCase(title)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                showAlert("A game with this title already exists.");
                continue;
            }
            boolean multiplayer = multiplayerBox.getValue().equals("Yes");
            String rating = ratingBox.getValue();
            // creates the new ds game object then adds it to the database
            DsGame newGame = new DsGame(title, developer, genre, year, multiplayer, rating);
            int id = databaseManager.insertGame(newGame);
            newGame.setId(id);
            // Confirmation message when Ds game is added successfully
            dsGameList.add(newGame);
            showAlert("DS Game added successfully!");
            return;
        }
    }
    // Allows the user Add Ds Game data by loading database from filepath
    // Validates is filepath is correct
    // Makes sure input is not left empty
    // Displays an alert message with information depending on user circumstance
    private void loadFromDatabaseGui() {
        TextInputDialog dbDialog = new TextInputDialog();
        dbDialog.setTitle("Load Database");
        dbDialog.setHeaderText("Enter database file path:");

        Optional<String> result = dbDialog.showAndWait();

        if (result.isEmpty() || result.get().trim().isEmpty()) {
            showAlert("No database path entered.");
            return;
        }
        String dbPath = result.get().trim();
        try {
            databaseManager.setDatabase(dbPath);
            databaseManager.init();
            refreshTable();
            showAlert("Database loaded successfully!");
        } catch (Exception e) {
            showAlert("Failed to load database: " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
//----------------------------------------------------------------------------------------------------------------------
    // View Button Method
    // Refreshes the tableView
    private void refreshTable() {
        dsGameList.setAll(databaseManager.getAllGames());
        tableView.setItems(dsGameList);
    }
//----------------------------------------------------------------------------------------------------------------------
    // Allows the user to update a ds game
    // Allows Ds game data to be updated separately via drop-down menu
    // Validates user inputs
    private void updateSelectedGame() {
        DsGame selectedGame = tableView.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            showAlert("Please select a game to update.");
            return;
        }
        ChoiceDialog<String> fieldDialog = new ChoiceDialog<>("Title",
                "Title", "Developer", "Genre", "Year", "Multiplayer", "Rating");

        fieldDialog.setHeaderText("Select field to update");
        Optional<String> fieldResult = fieldDialog.showAndWait();

        if (fieldResult.isEmpty()) {
            return;
        }
        String field = fieldResult.get();

    switch (field) {
        // Allows user to update title of a Ds Game separately
        case "Title":
            while (true) {
                TextInputDialog titleDialog = new TextInputDialog(selectedGame.getTitle());
                titleDialog.setHeaderText("Enter Updated title");
                Optional<String> titleResult = titleDialog.showAndWait();
                // Validates that user cant just press enter
                // Checks if new updated title already exists in the system.
                // Allows user to cancel
                if (titleResult.isEmpty())
                    return;
                if (titleResult.get().trim().isEmpty()) {
                    showAlert("Missing Updated Title.");
                    continue;
                }
                boolean exists = false;
                for (DsGame game : dsGameList) {
                    if (game.getTitle().equalsIgnoreCase(titleResult.get().trim()) && game != selectedGame) {
                        showAlert("This Title Already Exists.");
                        exists = true;
                        break;
                    }
                }
                if (exists) continue;
                selectedGame.setTitle(titleResult.get().trim());
                break;
            }
            break;
        // Allows user to update the developer of a Ds Game separately
        case "Developer":
            while (true) {
                TextInputDialog devDialog = new TextInputDialog(selectedGame.getDeveloper());
                devDialog.setHeaderText("Enter Updated Developer Name");
                Optional<String> devResult = devDialog.showAndWait();

                // Allows user to cancel
                if (devResult.isEmpty())
                    return;
                if (devResult.get().trim().isEmpty()) {
                    showAlert("Missing Updated Developer Name.");
                    continue;
                }
                selectedGame.setDeveloper(devResult.get().trim());
                break;
            }
            break;
        // Allows user to update genre of a Ds Game separately
        case "Genre":
            while (true) {
                TextInputDialog genreDialog = new TextInputDialog(selectedGame.getGenre());
                genreDialog.setHeaderText("Enter Updated Genre");
                Optional<String> genreResult = genreDialog.showAndWait();

                // Allows user to cancel
                if (genreResult.isEmpty())
                    return;
                if (genreResult.get().trim().isEmpty()) {
                    showAlert("Missing Updated Genre.");
                    continue;
                }
                String genre = genreResult.get().trim();
                if (!genre.matches("[a-zA-Z\\- ]+")) {
                    showAlert("Genre can only contain letters, spaces, or hyphens.");
                    continue;
            }
                selectedGame.setGenre(genre);
                break;
            }
            break;
        // Allows user to update the release year of a Ds Game separately
        case "Year":
            while (true) {
                TextInputDialog yearDialog = new TextInputDialog(String.valueOf(selectedGame.getReleaseYear()));
                yearDialog.setHeaderText("Enter Updated year");
                Optional<String> yearResult = yearDialog.showAndWait();

                // Allows user to cancel
                if (yearResult.isEmpty())
                    return;
                if (yearResult.get().trim().isEmpty()) {
                    showAlert("Missing Updated Year.");
                    continue;
                }
                try {
                    int year = Integer.parseInt(yearResult.get().trim());
                    if (year < 2004 || year > 2015) {
                        showAlert("Year Must Be Between 2004 And 2015.");
                        continue;
                    }
                    selectedGame.setReleaseYear(year);
                    break;
                } catch (NumberFormatException e) {
                    showAlert("Invalid input. Please enter a 4-Digit number for the year.");
                }
            }
            break;
        // Allows user to update the multiplayer support status of a Ds Game separately
        case "Multiplayer":
            while (true) {
                ChoiceDialog<String> multiDialog = new ChoiceDialog<>(selectedGame.getMultiplayerString(), "Yes", "No");
                multiDialog.setHeaderText("Select Updated Multiplayer Support Status");
                Optional<String> multiResult = multiDialog.showAndWait();

                // Allows user to cancel
                if (multiResult.isEmpty()) return;
                selectedGame.setMultiplayerSupport(multiResult.get().equalsIgnoreCase("Yes"));
                break;
            }
            break;
        // Allows user to update the rating of a Ds Game separately
        case "Rating":
            while (true) {
                ChoiceDialog<String> ratingDialog = new ChoiceDialog<>(selectedGame.getRating(), "E", "T", "M");
                ratingDialog.setHeaderText("Select Updated Rating");
                Optional<String> ratingResult = ratingDialog.showAndWait();

                // Allows user to cancel
                if (ratingResult.isEmpty()) return;
                selectedGame.setRating(ratingResult.get());
                break;
            }
            break;
    }
        databaseManager.updateGame(selectedGame);
        refreshTable();
        showAlert("Game updated successfully!");
    }
//----------------------------------------------------------------------------------------------------------------------
    // Removes Ds game and its data  if it exists, User must select Ds game first before removing
    private void removeSelectedGame() {
        DsGame selectedGame = tableView.getSelectionModel().getSelectedItem();

        if (selectedGame == null) {
            showAlert("Please Select A Game First, Then Click 'Remove DS Game' Again.");
            return;
        }
        // Confirms if user does actually want to remove the Ds game
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove Game");
        confirm.setContentText("Are You Sure You Want To Remove This Game?");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            databaseManager.deleteGame(selectedGame.getId());
            refreshTable();
            showAlert("DS Game Removed Successfully!");

            tableView.getSelectionModel().clearSelection();
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    /**
     * Celine Wedderburn
     * CEN-3024C - Software Development 1
     * Mar 27, 2026
     * DSGame.java

     * The purpose of this class is to store information of each DS Game entered into the system. This information includes storing data
     * such as the DS Game title, developer, genre,release year,rating and whether the DS game has multiplayer support.This class is
     * important to the overall application because it allows for easy management and organization of Ds Game information.
     */

    // DS game class
    // Ds game information
    public static class DsGame {

        private int id;
        private String title;
        private String developer;
        private String genre;
        private int releaseYear;
        private boolean multiplayerSupport;
        private String rating;
        // This allows the Ds game to be created within the system with its new information
        public DsGame(String title, String developer, String genre, int releaseYear,
                      boolean multiplayerSupport, String rating) {
            this.title = title;
            this.developer = developer;
            this.genre = genre;
            this.releaseYear = releaseYear;
            this.multiplayerSupport = multiplayerSupport;
            this.rating = rating;
        }
        // Assigns each ds game with an id
        public DsGame(int id, String title, String developer, String genre, int releaseYear,
                      boolean multiplayerSupport, String rating) {
            this.id = id;
            this.title = title;
            this.developer = developer;
            this.genre = genre;
            this.releaseYear = releaseYear;
            this.multiplayerSupport = multiplayerSupport;
            this.rating = rating;
        }
        public void setId(int id) {
            this.id = id;
        }
        //Retrieves Ds game information
        public void setTitle(String title) {
            this.title = title;
        }
        public void setDeveloper(String developer) {
            this.developer = developer;
        }
        public void setGenre(String genre) {
            this.genre = genre;
        }
        public void setReleaseYear(int releaseYear) {
            this.releaseYear = releaseYear;
        }
        public void setMultiplayerSupport(boolean multiplayerSupport) {
            this.multiplayerSupport = multiplayerSupport;
        }
        public void setRating(String rating) {
            this.rating = rating;
        }
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDeveloper() { return developer; }
        public String getGenre() { return genre; }
        public int getReleaseYear() { return releaseYear; }
        public String getMultiplayerString() { return multiplayerSupport ? "Yes" : "No"; }
        public String getRating() { return rating; }
    }
}