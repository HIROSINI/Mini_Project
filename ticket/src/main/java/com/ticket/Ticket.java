package com.ticket;

import java.sql.*;
import java.util.Scanner;

interface TicketInterface{
	public void cancelTicket(Connection connection, Scanner scanner, int userId) throws SQLException ;
}

public class Ticket implements TicketInterface{
    private Connection connection;

    
    private int ticketId;
    private int userId;
    private int movieId;
    private int numberOfSeats;
    private int totalAmount;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    
     static int insertTicket(Connection connection, int userId, int movieId, int numberOfSeats, int totalAmount, String seatNumbers, String bookingDate) throws SQLException {
    	    String insertQuery = "INSERT INTO ticket (movie_id, user_id, number_of_seats, totalamount, seat_numbers, date) VALUES (?, ?, ?, ?, ?, ?)";
    	    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
    	    preparedStatement.setInt(1, movieId);
    	    preparedStatement.setInt(2, userId);
    	    preparedStatement.setInt(3, numberOfSeats);
    	    preparedStatement.setInt(4, totalAmount);
    	    preparedStatement.setString(5, String.join(" ", seatNumbers));
    	    preparedStatement.setString(6,bookingDate);
    	    int rowsAffected = preparedStatement.executeUpdate();

    	    int ticketId = -1;
    	    if (rowsAffected > 0) {
    	        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
    	        if (generatedKeys.next()) {
    	            ticketId = generatedKeys.getInt(1);
    	            System.out.println("Ticket booked successfully! Ticket ID: " + ticketId);
    	        }
    	        generatedKeys.close();
    	    }

    	    preparedStatement.close();
    	    return ticketId;
    	}


	
     static void viewBookedTicketDetails(Connection connection, int userId, Scanner scanner) throws SQLException {
    	    System.out.println("Your Booked Tickets:");
    	    String selectQuery = "SELECT t.ticket_id, t.movie_id, m.movie_name, t.number_of_seats, t.totalamount, t.seat_numbers " +
    	                         "FROM ticket t " +
    	                         "INNER JOIN movie m ON t.movie_id = m.movie_id " +
    	                         "WHERE t.user_id = ?";
    	    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
    	    preparedStatement.setInt(1, userId);
    	    ResultSet resultSet = preparedStatement.executeQuery();

    	    while (resultSet.next()) {
    	        int ticketId = resultSet.getInt("ticket_id");
    	        int movieId = resultSet.getInt("movie_id");
    	        String movieName = resultSet.getString("movie_name");
    	        int numberOfSeats = resultSet.getInt("number_of_seats");
    	        int totalAmount = resultSet.getInt("totalamount");
    	        String seatNumbers = resultSet.getString("seat_numbers");

    	        System.out.println("Ticket ID: " + ticketId);
    	        System.out.println("Movie Name: " + movieName);
    	        System.out.println("Number of Seats: " + numberOfSeats);
    	        System.out.println("Seat Numbers: " + seatNumbers);
    	        System.out.println("Total Amount: $" + totalAmount);
    	        System.out.println("-----------------------------");
    	    }

    	    resultSet.close();
    	    preparedStatement.close();
    	}

	@Override
	 public void cancelTicket(Connection connection, Scanner scanner, int userId) throws SQLException {
        System.out.print("Enter the ticket ID to cancel: ");
        setTicketId(scanner.nextInt());
        scanner.nextLine(); // Consume the newline character after reading the integer input

        String deleteQuery = "DELETE FROM ticket WHERE ticket_id = ? and user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, getTicketId());
        preparedStatement.setInt(2, userId);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Ticket with ID " + ticketId + " canceled successfully!");
        } else {
            System.out.println("Unable to cancel the ticket. Make sure the ticket ID is correct or it might be beyond the cancellation period.");
        }

        preparedStatement.close();
    }
	 
	 
}
