package Connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String Driver = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/management_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(Driver);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a connection to the database
     * @return the connection made
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
            logger.log(Level.INFO, "Connection established successfully.");
        }catch (SQLException e){
            logger.log(Level.WARNING, "An error occurred while connecting to the database.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * get the connection so other methods can connect and query the database
     * @return the connection
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * close the connection
     * @param connection
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            }catch (SQLException e) {
                logger.log(Level.WARNING, "An error occurred while closing the connection.");
            }
        }
    }

    /**
     * close the statement
     * @param statement
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }catch (SQLException e) {
                logger.log(Level.WARNING, "An error occurred while closing the statement.");
            }
        }
    }

    /**
     * close the resultset
     * @param resultSet
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            }catch (SQLException e) {
                logger.log(Level.WARNING, "An error occurred while closing the resultset.");
            }
        }
    }
}
