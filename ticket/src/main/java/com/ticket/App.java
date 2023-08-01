package com.ticket;

import java.sql.*;
import java.util.Scanner;

public class App {


    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie", "root", "Hiro$76");
            Scanner scanner = new Scanner(System.in);

            User user = new User();
            Ticket ticket = new Ticket();
            Movie movie = new Movie();
            
            int userId = -1;
            String userName = "";

            System.out.print("Are you an existing user? (yes/no): ");
            String existingUserResponse = scanner.nextLine();

            if (existingUserResponse.equalsIgnoreCase("yes")) {
                System.out.print("Enter your user ID: ");
                userId = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading the integer input
                user.setUserId(userId);
                if (userId == -1 && userName.isEmpty()) {
                	System.out.println("Invalid user ID or name. Exiting.");
                	scanner.close();
                	return;
                }
                else {
                	System.out.print("Welcome " + user.fetchUserName(connection) + " ! ");
                }
            } else if (existingUserResponse.equalsIgnoreCase("no")) {
                System.out.print("Enter your name: ");
                userName = scanner.nextLine();
                System.out.print("Welcome " + userName + " ! ");
            } else {
                System.out.println("Invalid response. Please try again.");
                scanner.close();
                return;
            }


            if (userId == -1) {
                userId = User.insertUser(connection, userName);
            }
            

            if (userId > 0) {
                int choice = -1;

                while (choice != 0) {
                    displayMenu();
                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character after reading the integer input

                    switch (choice) {
                    case 1:
                    	Movie.addNewMovie(connection, scanner, movie);
                        break;
                    case 2:
                    	Movie.searchMovies(connection, scanner);
                        break;
                    case 3:
                    	movie.bookMovie(connection, scanner, userId);
                        break;
                    case 4:
                        Movie.displayAllMovieDetails(connection);
                        break;
                    case 5:
                    	Ticket.viewBookedTicketDetails(connection, userId, scanner);
                        break;
                    case 6:
                        ticket.cancelTicket(connection, scanner, userId);
                        break;
                    case 0:
                        System.out.println("Thank you for using our movie ticket booking system!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                }
            } else {
                System.out.println("Error creating user.");
            }

            scanner.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
       
    }


    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add New Movie");
        System.out.println("2. Search Movies");
        System.out.println("3. Select Movie and Book Ticket");
        System.out.println("4. Display All Movie Details");
        System.out.println("5. View Booked Tickets");
        System.out.println("6. Cancel Ticket");
        System.out.println("0. Exit");
    }
    
    

}
