package DataAccess;

import DataModel.Bill;
import Connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillDAO {
    protected static final Logger log = Logger.getLogger(BillDAO.class.getName());

    /**
     * The method is used when creating a new order, generating a bill with the information from the log table from the database
     * @param bill
     */
    public void insertBill(Bill bill) {
        String query = "INSERT INTO log (idOrder, idClient, idProduct, total_price) VALUES(?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, bill.idOrder());
            ps.setInt(2, bill.idClient());
            ps.setInt(3, bill.idProduct());
            ps.setInt(4, bill.total_price());
            ps.executeUpdate();
            log.log(Level.INFO, "Inserted bill id " + bill.id());
        } catch (SQLException e) {
            log.log(Level.WARNING, BillDAO.class.getName() + " DAO:insertBill(): " + e.getMessage());
        }
    }

    public List<Bill> findAllBills() {
        List<Bill> bills = new ArrayList<Bill>();
        String query = "SELECT * FROM log";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill(rs.getInt("id"), rs.getInt("idOrder"), rs.getInt("idClient"), rs.getInt("idProduct"), rs.getInt("total_price"));
                bills.add(bill);
            }
            return bills;
        } catch (SQLException e) {
            log.log(Level.WARNING, BillDAO.class.getName() + " DAO:findAllBills(): " + e.getMessage());
        }
        return bills;
    }
}
