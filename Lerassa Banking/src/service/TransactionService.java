package service;

import database.Database;
import java.sql.*;

public class TransactionService {

    public static void record(int customerId, String type, double amount){
        try {
            Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions(customerId,type,amount,datetime) VALUES(?,?,?,datetime('now'))");
            ps.setInt(1, customerId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
            conn.close();
        } catch(Exception e){}
    }
}
