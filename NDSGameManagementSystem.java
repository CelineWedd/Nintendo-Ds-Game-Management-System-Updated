/**
* Celine Wedderburn
* CEN-3024C - Software Development 1
* Mar 4, 2026
* NDSGameManagementSystem.java
*
* The purpose of this application is to store and manage data of Nintendo Ds Games. This data management application
* allows the user to add, view, update, delete and search Ds games by genre within the system. This system is called
* the Nintendo Ds Game Management System and the expected output should be a menu where the user can choose on how they
* interact with Ds Games.
*/

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;

// Main Method
public class NDSGameManagementSystem {
    public static void main(String[] args) {

        // The scanner is so the user can input an option, and
        // Used Map to temporarily store data within the program.
        Scanner scanner = new Scanner(System.in);
        Map<Integer, DsGame> dsGames = new HashMap<>();
        boolean running = true;

        // This loops the menu while the User is done interacting with it.
        // Displays numbered menu options the user can pick from and lets the user enter a choice.
        while (running) {
            System.out.println("\n          Nintendo Ds     ");
            System.out.println("    Game Management System");
            System.out.println("---------------------------------");
            System.out.println("1. Add DS Game");
            System.out.println("2. View Ds Games");
            System.out.println("3. Update DS Game");
            System.out.println("4. Delete DS Game");
            System.out.println("5. Search By Genre");
            System.out.println("6. Exit");

            // Grabs the users option they chose and activates the corresponding actions
            int choice;
            while (true) {
                System.out.println("Enter Your Choice: ");
                String input = scanner.nextLine();
                try {
                    choice = Integer.parseInt(input);
                    if (choice >= 1 && choice <= 6) {
                        break;
                    } else {
                        System.out.println("Invalid Option. Please Enter A Number Between 1 And 6.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Input. Please Enter A Number.");
                }
            }
        switch (choice) {
            // Allows the user to add a ds game manually or via text file
            case 1:
                System.out.println("Enter DS Game Manually or Load Ds Game Text File: ");
                System.out.println("1. Add DS Game Manually");
                System.out.println("2. Load Ds Game Text File");

                    // Validates that the user can only enter numbers 1 and 2 and only allows numbers to be entered.
                    int userChoice;
                    while (true) {
                        System.out.println("Enter Your Choice: ");
                        String input = scanner.nextLine();
                        try {
                            userChoice = Integer.parseInt(input);
                            if (userChoice == 1 || userChoice == 2) {
                                break;
                            } else {
                                System.out.println("Invalid Option. Please Enter Numbers 1 or 2 Only.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Input. Please Enter A Number Only.");
                        }
                    }
                    // Directs the user to enter manually if option 1 is selected
                    // Directs the user to enter a file path to a text file containing Ds game data if option 2 is selected
                    if (userChoice == 1) {
                        manuallyAdd(scanner, dsGames);
                    } else if (userChoice == 2) {
                        System.out.println("Enter DS Game Text File Path: ");
                        String filePath = scanner.nextLine();
                        loadDsGames(filePath, dsGames);
                    }
                    break;
            // Displays all Ds Games currently in the system
            case 2:
                System.out.println("    All Ds Games  ");
                System.out.println("--------------------");

                    if (dsGames.size() > 0) {
                        for (DsGame currentDsGame : dsGames.values()) {
                            System.out.println("ID:  " + currentDsGame.getId());
                            System.out.println(currentDsGame);
                        }
                    } else {
                        System.out.println("No Ds Games Found.");
                    }
                    break;
            // Allows the user to update a ds game by entering an ID
            // Allows Ds game data to be updated separately
            // Sub-menu also loops until user is finished updating desired Ds Game data

            case 3:
                int updateId = -1;
                while (true) {
                    System.out.println("Enter ID Of Ds Game To Update: ");
                    String input = scanner.nextLine();
                    try {
                        updateId = Integer.parseInt(input);

                        if (dsGames.containsKey(updateId)) {
                            break;
                        } else {
                            System.out.println("No DS Game Found With That ID.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                // Sub-menu for updating Ds game data
                DsGame game = dsGames.get(updateId);
                        boolean dsUpdate = true;
                        while (dsUpdate) {
                            System.out.println("\n Currently Updating: " + game.getTitle());
                            System.out.println("---------------------------------");
                            System.out.println("1. Update Ds Game Title");
                            System.out.println("2. Update Ds Game Developer");
                            System.out.println("3. Update DS Game Genre");
                            System.out.println("4. Update DS Game Release Year");
                            System.out.println("5. Update DS Game Multiplayer Support");
                            System.out.println("6. Update DS Game Rating");
                            System.out.println("7. Finish Updating DS Game");

                            // Validates that the user enters only numbers in the sub-menu that are 1 through 7
                            // User can not enter anything other than a number
                            int updateChoice;
                            while (true) {
                                System.out.println("Enter Your Choice: ");
                                String input = scanner.nextLine();
                                try {
                                    updateChoice = Integer.parseInt(input);
                                    if (updateChoice >= 1 && updateChoice <= 7) {
                                        break;
                                    } else {
                                        System.out.println("Invalid Option. Please Enter A Number Between 1 And 7.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid Input. Please Enter A Number.");
                                }
                            }
                            // Allows user to update title of a Ds Game separately
                            switch (updateChoice) {
                                case 1:
                                    String updatedTitle;
                                    while (true) {
                                        System.out.println("Enter Updated Title: ");
                                        updatedTitle = scanner.nextLine().trim();
                                        // Validates that user cant just press enter
                                        // Checks if new updated title already exsist in the system.
                                        if (updatedTitle.isEmpty()) {
                                            System.out.println("Missing Updated Title, Please Enter A Title.");
                                            continue;
                                        }
                                        boolean exsistingTitle = false;
                                        for (DsGame existingGame : dsGames.values()) {
                                            if (existingGame.getId() != game.getId() &&
                                                    existingGame.getTitle().equalsIgnoreCase(updatedTitle)) {

                                                System.out.println("This Title Already Exists.");
                                                exsistingTitle = true;
                                                break;
                                            }
                                        }
                                        if (exsistingTitle) {
                                            continue;
                                        }
                                        break;
                                    }
                                    game.setTitle(updatedTitle);
                                    System.out.println("Title Updated.");
                                    break;
                                // Allows user to update the developer of a Ds Game separately
                                case 2:
                                String updatedDeveloper;
                                while (true) {
                                    System.out.println("Enter Updated Developer Name: ");
                                    updatedDeveloper = scanner.nextLine().trim();
                                    if (updatedDeveloper.isEmpty()) {
                                        System.out.println("Missing Developer Name.");
                                    } else {
                                        break;
                                    }
                                }
                                game.setDeveloper(updatedDeveloper);
                                System.out.println("Developer Updated.");
                                break;

                                // Allows user to update genre of a Ds Game separately
                                case 3:
                                String updatedGenre;
                                while (true) {
                                    System.out.println("Enter Updated Genre: ");
                                    updatedGenre = scanner.nextLine().trim();
                                    if (updatedGenre.isEmpty()) {
                                        System.out.println("Missing Genre.");
                                    } else if (!updatedGenre.matches("[a-zA-Z\\- ]+")) {
                                        System.out.println("Genre Can Only Consist Of Letters,Spaces or Hyphens.");
                                    } else {
                                        break;
                                    }
                                }
                                game.setGenre(updatedGenre);
                                System.out.println("Genre Updated.");
                                break;
                                // Allows user to update the release year of a Ds Game separately
                                case 4:
                                int updatedReleaseYear;
                                while (true) {
                                    System.out.println("Enter Updated Release Year: ");
                                    String input = scanner.nextLine().trim();
                                    try {
                                        updatedReleaseYear = Integer.parseInt(input);
                                        if (updatedReleaseYear > 2003 && updatedReleaseYear <= 2016) {
                                            break;
                                        } else {
                                            System.out.println("Enter a valid year between 2004 and 2015.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input. Please enter a 4-Digit number for the year.");
                                    }
                                }
                                game.setReleaseYear(updatedReleaseYear);
                                System.out.println("Release Year Updated.");
                                break;
                                // Allows user to update the multiplayer support status of a Ds Game separately
                                case 5:
                                boolean updatedMultiplayerSupport;
                                while (true) {
                                    System.out.println("Enter Updated Multiplayer Support Status (Yes/No): ");
                                    String input = scanner.nextLine().trim();
                                    if (input.equalsIgnoreCase("Yes")) {
                                        updatedMultiplayerSupport = true;
                                        break;
                                    } else if (input.equalsIgnoreCase("No")) {
                                        updatedMultiplayerSupport = false;
                                        break;
                                    } else {
                                        System.out.println("Please Enter Yes Or No Only.");
                                    }
                                }
                                game.setMultiplayerSupport(updatedMultiplayerSupport);
                                System.out.println("Multiplayer Support Status Updated.");
                                break;
                                // Allows user to update the rating of a Ds Game separately
                                case 6:
                                    String updatedRating;
                                    while (true) {
                                    System.out.println("Enter Updated Rating (E,T,M): ");
                                    updatedRating = scanner.nextLine().trim();
                                    if (updatedRating.equals("E") || updatedRating.equals("T") || updatedRating.equals("M")) {
                                    break;
                                    } else {
                                    System.out.println("Invalid rating. Please Enter E, T, or M Only.");
                                      }
                                    }
                                    game.setRating(updatedRating);
                                    System.out.println("Rating Updated.");
                                    break;
                                // When user is finished updating desired information
                                // This allows them to exit the sub-menu and go back to the main menu
                                case 7:
                                    dsUpdate = false;
                                    System.out.println("Returning Back To Main Menu");
                                    break;
                            }
                        }
                            break;
            // Removes Ds game and its data by ID if it exists, notifies user if ds game doesn't exist
            case 4:
                int removeId;

                while (true) {
                    System.out.println("Enter ID Of Ds Game To Remove:  ");
                    String input = scanner.nextLine();

                    try {
                        removeId = Integer.parseInt(input);
                        if (dsGames.containsKey(removeId)) {
                            break;
                        } else {
                            System.out.println("There Is No DS Game With That ID.");
                        }
                    } catch  (NumberFormatException e) {
                    System.out.println("Invalid Input. Please Enter A Number");}
                }
                DsGame removed = dsGames.remove(removeId);
                System.out.println("Ds Game Removed:  " + "\n" + removed);
                break;
            // Custom action that allows the user to search Ds games by genre
            // List games with the matching desired genre if found
            // Makes sure user enters the correct data
            case 5:
                String searchGenre;
                while (true) {
                    System.out.println("Enter A Game Genre To Search: ");
                    searchGenre = scanner.nextLine().trim();

                    if (searchGenre.isEmpty()) {
                        System.out.println("Missing Genre.");
                    } else if (!searchGenre.matches("[a-zA-Z\\- ]+")) {
                        System.out.println("Genre Can Only Consist Of Letters,Spaces or Hyphens.");
                    } else {
                        break;
                    }
                }
                boolean genreFound = false;
                for (DsGame dsgame : dsGames.values()) {
                    if (dsgame.getGenre().equalsIgnoreCase(searchGenre)) {
                            System.out.println(dsgame);
                            genreFound = true;
                        }
                    }
                    if (!genreFound) {
                        System.out.println("No DS Games Found In That Genre.");
                    }
                    break;
            // This allows the user to exit the NDS-GMS when they are done using it
            case 6:
                running = false;
                System.out.println("You Have Successfully Exited The Program.");
                break;
            }
        }
    }

//--------------------------------------------------------------------------------------------
    // Part 1 of case 1, allows user to enter Ds Game data manually
    // Validates that the user only enters the correct information/inputs for each piece of data
    public static void manuallyAdd(Scanner scanner, Map<Integer, DsGame> dsGames) {
        // Allows user to enter a valid Title for a Ds Game
        String title;
        while (true) {
            System.out.println("Enter DS Game Title: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Missing Title.");
            } else {
                break;
            }
        }
        // Checks if title already exist within the system
        for (DsGame existingGame : dsGames.values()) {
            if (existingGame.getTitle().equalsIgnoreCase(title)) {
                System.out.println("This Title Already Exists. Please Try Again.");
                return;
            }
        }
        // Allows user to enter a valid developer name
        String developer;
        while (true) {
            System.out.println("Enter DS Game Developer Name: ");
            developer = scanner.nextLine().trim();
            if (developer.isEmpty()) {
                System.out.println("Missing Developer Name.");
            } else {
                break;
            }
        }
        // Allows user to enter a valid genre for a Ds Game
        // Allows a user to use a hyphen and spaces for some genres
        String genre;
        while (true) {
            System.out.println("Enter DS Game Genre: ");
            genre = scanner.nextLine().trim();
            if (genre.isEmpty()) {
                System.out.println("Missing Genre.");
            } else if (!genre.matches("[a-zA-Z\\- ]+")) {
                System.out.println("Genre Can Only Consist Of Letters,Spaces or Hyphens.");
            } else {
                break;
            }
        }
        // Allows user to enter a valid release year for a Ds Game
        int releaseYear;
        while (true) {
            System.out.println("Enter DS Game Release Year: ");
            String input = scanner.nextLine().trim();
            try {
                releaseYear = Integer.parseInt(input);
                if (releaseYear > 2003 && releaseYear <= 2016) {
                break;
                } else {
                     System.out.println("Enter a valid year between 2004 and 2015.");
                }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number for the year.");
                }
            }
            // Allows user to enter valid input for the multiplayer status
            // User can only enter Yes or No
            boolean multiplayerSupport;
            while (true) {
            System.out.println("Does this DS Game have Multiplayer Support? (Yes/No) : ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("Yes")) {
                    multiplayerSupport = true;
                    break;
                } else if (input.equalsIgnoreCase("No")) {
                    multiplayerSupport = false;
                    break;
                } else {
                    System.out.println("Please Enter Yes Or No Only.");
                }
            }
            // Allows the user to enter Rating
            // Validates that the rating input is only either E,T, or M
            String rating;
            while (true) {
                System.out.println("Enter Rating Of Ds Game (E,T,M): ");
                rating = scanner.nextLine().trim();
                if (rating.equals("E") || rating.equals("T") || rating.equals("M")) {
                    break;
                } else {
                    System.out.println("Invalid rating. Please Enter E, T, or M Only.");
                }
            }
            // Confirms that the Ds game, along with its data is created and stored to the NDS-GMS
            DsGame game = new DsGame(title, developer, genre, releaseYear, multiplayerSupport, rating);
            dsGames.put(game.getId(), game);

            System.out.println("Ds Game Added Successfully!");
        }
    // Part 2 of Case 1, allows the user to enter Ds Game data via file path
    // Validates that the data in text file has 6 data attributes only
    // Makes sure each Ds Game is also in the correct format
    public static void loadDsGames (String filePath, Map <Integer, DsGame> dsGames){
         try {
             Scanner fileScanner = new Scanner(new File(filePath));
             while (fileScanner.hasNextLine()) {

                 String line = fileScanner.nextLine();
                 if (line.trim().isEmpty()) {
                     continue;
                 }
                 // User can only enter a set amount of data which is 6
                 // 5 commas only
                 String[] parts = line.split(",");
                 if (parts.length != 6) {
                     System.out.println("Invalid Data Amount" + line);
                     continue;
                 }
                 String title = parts[0];
                 String developer = parts[1];
                 String genre = parts[2];
                 int releaseYear = Integer.parseInt(parts[3]);
                 boolean multiplayerSupport = parts[4].equalsIgnoreCase("Yes");
                 String rating = parts[5];

                 // Confirms that the Ds game, along with its data is created and stored to the NDS-GMS
                 DsGame game = new DsGame(title, developer, genre, releaseYear, multiplayerSupport, rating);
                 dsGames.put(game.getId(), game);
             }
             // Validates whether the games were loaded correctly
             // Will notify user if there was an error loading the text file
             fileScanner.close();
             System.out.println("Ds Games Loaded Successfully!");
         }   catch (FileNotFoundException e) {
             System.out.println("Text File Not Found. Please Try Again.");
         }   catch (Exception e)  {
             System.out.println("Error Loading Ds Games From File.");
         }
    }}

