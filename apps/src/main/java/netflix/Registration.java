package netflix;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.*;

// Servlet mapped to /Registration
@WebServlet("/Registration")
public class Registration extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3306/clone_apps";
    private static final String USER = "root";
    private static final String PASSWORD = "SARTHAK";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data from the request
        String fullName = request.getParameter("full_name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone_number");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String gender = request.getParameter("gender");

        // Validate password match
        if (!password.equals(confirmPassword)) {
            response.getWriter().println("Passwords do not match.");
            return;
        }

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database and insert the data
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "INSERT INTO registration (full_name, username, email, phone_number, password, gender) VALUES (?, ?, ?, ?, ?, ?)";

                // Prepared statement to avoid SQL injection
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, fullName);
                    stmt.setString(2, username);
                    stmt.setString(3, email);
                    stmt.setString(4, phoneNumber);
                    stmt.setString(5, password); // Consider hashing the password
                    stmt.setString(6, gender);

                    // Execute the query
                    int rowsInserted = stmt.executeUpdate();
                    
                    // Execute the query
               

                    // Redirect on successful registration
                    if (rowsInserted > 0) {
                        response.sendRedirect("home.html"); // Redirect to a success page
                    } else {
                        response.getWriter().println("Error during registration.");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }
}