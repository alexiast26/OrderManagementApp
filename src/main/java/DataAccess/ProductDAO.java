package DataAccess;

import DataModel.Product;

import Connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class ProductDAO extends AbstractDAO<Product>{
    public ProductDAO() {
        super(Product.class);
    }

    /**
     * Decrease the stock from the product when making a new order, the method will be called when inserting an order in the GUI class
     * @param product
     * @param amount
     * @return true if successful and false if it failed
     */
    public boolean decreaseStock(Product product, int amount) {
        String query = "UPDATE product SET stock = stock - ? WHERE id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(query);
            if(product.getStock() - amount < 0){
                log.log(Level.WARNING, "Not enough products for the amount required.");
                return false;
            }
            ps.setInt(1, amount);
            ps.setInt(2, product.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.log(Level.WARNING, "An error occurred while decreasing stock.");
        }
        return false;
    }

}
