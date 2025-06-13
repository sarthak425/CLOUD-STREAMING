package netflix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login_amazon")
public class LoginAmazon extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/clone_apps"; // Database URL
    private static final String USER = "root"; // Database username
    private static final String PASSWORD = "SARTHAK"; // Database password

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            response.getWriter().println("Please enter both email and password.");
            return;
        }

        try {
            // Load the JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish a connection to the database
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                // Query to check if the email and password match
                String query = "SELECT * FROM amazon_login WHERE email = ? AND password = ?";
                
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, password);
                    
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    if (resultSet.next()) {
                        // If credentials are correct, redirect to the main page or user's dashboard
                        response.sendRedirect("main_amazon-prime-video.html");
                    } else {
                        // If credentials are incorrect, send an error message
                        response.getWriter().println("Invalid email or password. Please try again.");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database connection error: " + e.getMessage());
        }
    }
}
