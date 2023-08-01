package com.ticket;

import java.sql.*;
import java.util.Scanner;

public class User {
    private Connection connection;

    private int userId;
    private String userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

     String fetchUserName(Connection connection) throws SQLException {
         String query = "SELECT username FROM user WHERE userid = ?";
         Scanner scanner = new Scanner(System.in);
         try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setInt(1, userId);
             try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 if (resultSet.next()) {
                     return resultSet.getString("username");
                 }
             }
         }
         return null;
     }

     private int fetchUserId() throws SQLException {
         String query = "SELECT userid FROM user WHERE username = ?";
         try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, userName);
             try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 if (resultSet.next()) {
                     return resultSet.getInt("userid");
                 }
             }
         }
         return -1;
     }

	static int insertUser(Connection connection, String userName) throws SQLException {
        String insertQuery = "INSERT INTO user (username) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, userName);
        int rowsAffected = preparedStatement.executeUpdate();

        int userId = -1;
        if (rowsAffected > 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
                System.out.println("User created with ID: " + userId);
            }
            generatedKeys.close();
        }

        preparedStatement.close();
        return userId;
    }

}
