package com.ticket;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

abstract class MovieAbstract
{
	abstract public void insertMovie(Connection connection,String movieName, String language, String genre, String fromDate, String toDate,
            String fromTime, String toTime, int price) throws SQLException ;
}
public class Movie extends MovieAbstract {
    private static Connection connection;

    private int movieId;
    private String movieName;
    private String language;
    private String genre;
    private String fromDate;
    private String toDate;
    private String fromTime;
    private String toTime;
    private int price;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	
	static void addNewMovie(Connection connection, Scanner scanner, Movie movie) throws SQLException {
        System.out.print("Enter the movie name: ");
        String movieName = scanner.nextLine();

        System.out.print("Enter the language: ");
        String language = scanner.nextLine();

        System.out.print("Enter the genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter the from date: ");
        String fromDate = scanner.nextLine();

        System.out.print("Enter the to date: ");
        String toDate = scanner.nextLine();

        System.out.print("Enter the from time: ");
        String fromTime = scanner.nextLine();

        System.out.print("Enter the to time: ");
        String toTime = scanner.nextLine();

        System.out.print("Enter the price: ");
        int price = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after reading the integer input

        movie.insertMovie(connection,movieName, language, genre, fromDate, toDate, fromTime, toTime, price);
    }

	public void displayMovies(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM movie");
        System.out.println("Available Movies:");
        while (resultSet.next()) {
            int movieId = resultSet.getInt("movie_id");
            String movieName = resultSet.getString("movie_name");
            System.out.println(movieId + ". " + movieName);
        }
        resultSet.close();
        statement.close();
    }

     static int calculateTotalAmount(Connection connection, int movieId, int numberOfSeats) throws SQLException {
        String selectQuery = "SELECT price FROM movie WHERE movie_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setInt(1, movieId);
        ResultSet resultSet = preparedStatement.executeQuery();

        int price = -1;
        if (resultSet.next()) {
            price = resultSet.getInt("price");
        }

        resultSet.close();
        preparedStatement.close();

        if (price > 0) {
            return price * numberOfSeats;
        } else {
            return -1;
        }
    }
     
     public void insertMovie(Connection connection,String movieName, String language, String genre, String fromDate, String toDate,
             String fromTime, String toTime, int price) throws SQLException {
		String insertQuery = "INSERT INTO movie (movie_name, language, genre, fromdate, todate, fromtime, totime, price) " +
		              "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, movieName);
		preparedStatement.setString(2, language);
		preparedStatement.setString(3, genre);
		preparedStatement.setString(4, fromDate);
		preparedStatement.setString(5, toDate);
		preparedStatement.setString(6, fromTime);
		preparedStatement.setString(7, toTime);
		preparedStatement.setInt(8, price);
		
		int rowsAffected = preparedStatement.executeUpdate();
		
		if (rowsAffected > 0) {
		ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
		 int movieId = generatedKeys.getInt(1);
		 System.out.println("Movie added successfully with ID: " + movieId);
		}
		generatedKeys.close();
		}
		
		preparedStatement.close();
		}
     
     
     static void searchMovies(Connection connection, Scanner scanner) throws SQLException {
    	    System.out.println("Enter the search criteria:");
    	    System.out.println("1. Movie Name");
    	    System.out.println("2. Movie Language");
    	    System.out.println("3. Movie Genre");
    	    System.out.print("Enter your choice: ");
    	    int searchOption = scanner.nextInt();
    	    scanner.nextLine(); // Consume the newline character after reading the integer input

    	    System.out.print("Enter the search keyword: ");
    	    String searchKeyword = scanner.nextLine();

    	    String searchQuery = "";
    	    switch (searchOption) {
    	        case 1:
    	            searchQuery = "SELECT * FROM movie WHERE movie_name LIKE ?";
    	            break;
    	        case 2:
    	            searchQuery = "SELECT * FROM movie WHERE language LIKE ?";
    	            break;
    	        case 3:
    	            searchQuery = "SELECT * FROM movie WHERE genre LIKE ?";
    	            break;
    	        default:
    	            System.out.println("Invalid search option. Please try again.");
    	            return;
    	    }

    	    PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
    	    preparedStatement.setString(1, "%" + searchKeyword + "%"); // Add wildcard to search for partial matches

    	    ResultSet resultSet = preparedStatement.executeQuery();

    	    boolean foundMovies = false; // Flag to check if any movies were found
    	    System.out.println("Search Results:");
    	    while (resultSet.next()) {
    	        int movieId = resultSet.getInt("movie_id");
    	        String movieName = resultSet.getString("movie_name");
    	        String language = resultSet.getString("language");
    	        String genre = resultSet.getString("genre");
    	        String fromDate = resultSet.getString("fromdate");
    	        String toDate = resultSet.getString("todate");
    	        String fromTime = resultSet.getString("fromtime");
    	        String toTime = resultSet.getString("totime");
    	        int price = resultSet.getInt("price");

    	        System.out.println("Movie ID: " + movieId);
    	        System.out.println("Movie Name: " + movieName);
    	        System.out.println("Language: " + language);
    	        System.out.println("Genre: " + genre);
    	        System.out.println("From Date: " + fromDate);
    	        System.out.println("To Date: " + toDate);
    	        System.out.println("From Time: " + fromTime);
    	        System.out.println("To Time: " + toTime);
    	        System.out.println("Price: $" + price);
    	        System.out.println("-----------------------------");

    	        foundMovies = true;
    	    }

    	    if (!foundMovies) {
    	        System.out.println("No movies found based on the search criteria.");
    	    }

    	    resultSet.close();
    	    preparedStatement.close();
    	}
   



         static boolean checkSeatAvailability(Connection connection, int movieId, String[] seatNumbers, String selectedDate) throws SQLException {
             String selectQuery = "SELECT GROUP_CONCAT(seat_numbers) AS booked_seats, GROUP_CONCAT(date) AS booked_dates FROM ticket WHERE movie_id = ?";
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             preparedStatement.setInt(1, movieId);
             ResultSet resultSet = preparedStatement.executeQuery();

             if (resultSet.next()) {
                 String bookedSeatsStr = resultSet.getString("booked_seats");
                 String bookedDatesStr = resultSet.getString("booked_dates");
                 
                 if (bookedSeatsStr == null || bookedDatesStr == null) {
                     return true; // All seats are available if no seats are booked yet
                 } else {
                     String[] bookedSeats = bookedSeatsStr.split(",");
                     String[] bookedDates = bookedDatesStr.split(",");
                     Set<String> bookedSeatSet = new HashSet<>(Arrays.asList(bookedSeats));
                     Set<String> bookedDatesSet = new HashSet<>(Arrays.asList(bookedDates));

                     for (String seatNumber : seatNumbers) {
                         if (bookedSeatSet.contains(seatNumber)) {
                             // Check if the seat is booked on the selected date
                             if (bookedDatesSet.contains(selectedDate)) {
                                 return false; // Seat is already booked for the selected date
                             }
                         }
                     }
                     return true; // All selected seats are available for the selected date
                 }
             } else {
                 return false; // Movie with the given movieId not found
             }
         }


     
     public static boolean isValidBookingDate(String dateString, String fromDate, String toDate) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         try {
             java.util.Date date= sdf.parse(dateString);
             java.util.Date fromDateObj = sdf.parse(fromDate);
             java.util.Date toDateObj = sdf.parse(toDate);

             // Check if the date is within the specified range
             if (date.compareTo(fromDateObj) >= 0 && date.compareTo(toDateObj) <= 0) {
                 return true;
             }
         } catch (ParseException e) {
             // Invalid date format
         }
         return false;
     }

     public void bookMovie(Connection connection, Scanner scanner, int userId) throws SQLException {
    	    displayMovies(connection);

    	    System.out.print("Enter the movie ID you want to book (0 to cancel): ");
    	    int movieId = scanner.nextInt();
    	    scanner.nextLine(); // Consume the newline character after reading the integer input

    	    if (movieId == 0) {
    	        System.out.println("Booking canceled.");
    	        return;
    	    }

    	    String selectQuery = "SELECT * FROM movie WHERE movie_id = ?";
    	    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
    	    preparedStatement.setInt(1, movieId);
    	    ResultSet resultSet = preparedStatement.executeQuery();

    	    if (resultSet.next()) {
    	        String movieName = resultSet.getString("movie_name");
    	        String language = resultSet.getString("language");
    	        String genre = resultSet.getString("genre");
    	        String fromDate = resultSet.getString("fromdate");
    	        String toDate = resultSet.getString("todate");
    	        String fromTime = resultSet.getString("fromtime");
    	        String toTime = resultSet.getString("totime");
    	        int price = resultSet.getInt("price");
    	        int availableSeats = resultSet.getInt("available_seats");

    	        System.out.println("Movie ID: " + movieId);
    	        System.out.println("Movie Name: " + movieName);
    	        System.out.println("Language: " + language);
    	        System.out.println("Genre: " + genre);
    	        System.out.println("From Date: " + fromDate);
    	        System.out.println("To Date: " + toDate);
    	        System.out.println("From Time: " + fromTime);
    	        System.out.println("To Time: " + toTime);
    	        System.out.println("Price: " + price);
    	        System.out.println("Available Seats: " + availableSeats);

    	        System.out.print("Enter the date to book tickets (yyyy-mm-dd): ");
    	        String bookingDate = scanner.nextLine();
    	        
    	        if (!isValidBookingDate(bookingDate, fromDate, toDate)) {
    	        	System.out.println("Invalid booking date. It should be between " + fromDate + " and " + toDate + ". Booking canceled.");
    	        	return;
    	        }
    	        
    	        System.out.print("Enter the number of seats: ");
    	        int numberOfSeats = scanner.nextInt();
    	        scanner.nextLine(); // Consume the newline character after reading the integer input

    	        
    	        if (numberOfSeats > 0 && numberOfSeats <= availableSeats) {
    	            System.out.println("Please select " + numberOfSeats + " seat(s) (separated by spaces):");
    	            String[] seatNumbersArray = scanner.nextLine().split(" ");

    	            if (seatNumbersArray.length != numberOfSeats) {
    	                System.out.println("Invalid number of seat selections. Booking canceled.");
    	                return;
    	            }

    	            String seatNumbers = String.join(",", seatNumbersArray);

    	            if (!checkSeatAvailability(connection, movieId, seatNumbersArray, bookingDate)) {
    	                System.out.println("One or more selected seats are already booked. Booking canceled.");
    	                return;
    	            }


    	            int totalPrice = calculateTotalAmount(connection, movieId, numberOfSeats);
    	            if (totalPrice > 0) {
    	                System.out.println("Total Amount: $" + totalPrice);

    	                // Update available seats in 'movie' table
    	                int updatedAvailableSeats = availableSeats - numberOfSeats;
    	                String updateQuery = "UPDATE movie SET available_seats = ? WHERE movie_id = ?";
    	                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
    	                updateStatement.setInt(1, updatedAvailableSeats);
    	                updateStatement.setInt(2, movieId);
    	                updateStatement.executeUpdate();
    	                updateStatement.close();

    	                // Insert the ticket into 'ticket' table
    	                int ticketId = Ticket.insertTicket(connection, userId, movieId, numberOfSeats, totalPrice, seatNumbers, bookingDate);
    	            } else {
    	                System.out.println("Invalid number of seats.");
    	            }
    	        } else {
    	            System.out.println("Invalid number of seats or no available seats for this movie.");
    	        }
    	    } else {
    	        System.out.println("Invalid movie ID.");
    	    }

    	    resultSet.close();
    	    preparedStatement.close();
    	}


         

     
	static void displayAllMovieDetails(Connection connection) throws SQLException {
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM movie");
         System.out.println("All Movie Details:");
         while (resultSet.next()) {
             int movieId = resultSet.getInt("movie_id");
             String movieName = resultSet.getString("movie_name");
             String language = resultSet.getString("language");
             String genre = resultSet.getString("genre");
             String fromDate = resultSet.getString("fromdate");
             String toDate = resultSet.getString("todate");
             String fromTime = resultSet.getString("fromtime");
             String toTime = resultSet.getString("totime");
             int price = resultSet.getInt("price");

             System.out.println("Movie ID: " + movieId);
             System.out.println("Movie Name: " + movieName);
             System.out.println("Language: " + language);
             System.out.println("Genre: " + genre);
             System.out.println("From Date: " + fromDate);
             System.out.println("To Date: " + toDate);
             System.out.println("From Time: " + fromTime);
             System.out.println("To Time: " + toTime);
             System.out.println("Price: " + price);
             System.out.println("-----------------------------");
         }
         resultSet.close();
         statement.close();
     }

}