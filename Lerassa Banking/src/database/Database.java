package database;

import java.sql.*;

public class Database {

    // Absolute path recommended
    private static final String URL = "jdbc:sqlite:C:/Users/Admin/Documents/Lerassa Banking/lerassa.db";

    private static boolean initialized = false;

    // Connect to database
    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);

            // Enable foreign key constraints
            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            // Run setup once only
            if (!initialized) {
                setupTables(conn);
                initialized = true;
            }

            return conn;

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    // Create tables ONLY once
    private static void setupTables(Connection conn) throws SQLException {

        try (Statement st = conn.createStatement()) {

            // USERS TABLE
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                )
            """);

            // CUSTOMERS TABLE (NO UNIQUE on username)
            st.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    fullName TEXT,
                    surname TEXT,
                    idNumber TEXT,
                    dob TEXT,
                    phone TEXT,
                    country TEXT,
                    email TEXT,
                    address TEXT,
                    username TEXT,
                    password TEXT
                )
            """);

            // ACCOUNTS TABLE
            st.execute("""
                CREATE TABLE IF NOT EXISTS accounts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER,
                    account_number TEXT,
                    account_type TEXT,
                    balance REAL DEFAULT 0,
                    FOREIGN KEY(customer_id) REFERENCES customers(id)
                )
            """);

            // TRANSACTIONS TABLE
            st.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_id INTEGER,
                    amount REAL,
                    type TEXT,
                    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(account_id) REFERENCES accounts(id)
                )
            """);

            // DEFAULT ADMIN USER
            st.execute("""
                INSERT OR IGNORE INTO users(username, password, role)
                VALUES('admin', 'admin123', 'admin')
            """);
        }
    }
}
