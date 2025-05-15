package BuisinessLogic;

import DataAccess.ProductDAO;
import DataModel.Client;
import DataModel.Product;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL extends MakeTable<Product>{
    private ProductDAO productDAO;

    public ProductBLL() {
        super(Product.class);
        productDAO = new ProductDAO();
    }

    /**
     * use the DAO class to call the findById method
     * @param id
     * @return the product with the selected id
     */
    public Product findProductById(int id){
        Product product = productDAO.findById(id);
        if (product == null){
            throw new NoSuchElementException("The product with id " + id + " does not exist.");
        }
        return product;
    }

    /**
     * use the DAO class to call the findAll method
     * @return a list of all the products
     */
    public List<Product> findAllProducts(){
        List<Product> products = productDAO.findAll();
        if (products == null){
            throw new IllegalStateException("The product list is empty.");
        }
        return products;
    }

    /**
     * use the DAO class to call the delete method
     * @param id
     */
    public void deleteProduct(int id){
        boolean deleted = productDAO.delete(id);
        if (!deleted){
            throw new NoSuchElementException("The product with id " + id + " failed to delete.");
        }
    }

    /**
     * use the DAO class to call the insert method
     * @param product
     */
    public void insertProduct(Product product){
        if(product.getName() == null || product.getName().isEmpty()){
            throw new IllegalArgumentException("The product name is empty.");
        }
        try {
            productDAO.insert(product);
        }catch (Exception e){
            throw new IllegalStateException("The product with id " + product.getId() + " failed to insert.");
        }
    }

    /**
     * use the DAO class to call the update method
     * @param product
     */
    public void updateProduct(Product product){
        if (product.getName() == null || product.getName().isEmpty()){
            throw new IllegalArgumentException("The product name is empty.");
        }
        try {
            productDAO.update(product);
        } catch (Exception e){
            throw new IllegalStateException("The product with id " + product.getId() + " failed to update.");
        }
    }

    /**
     * use the DAO class to call the decreaseStock method
     * @param product
     * @param quantity
     * @return true if it was successful or false if it failed
     */
    public boolean decreaseQuantity(Product product, int quantity){
        try {
            return productDAO.decreaseStock(product, quantity);
        } catch (Exception e){
            throw new IllegalStateException("The product with id " + product.getId() + " failed to decrease.");
        }
    }

    /**
     * refresh the product table
     * @param model
     */
    public void refreshProductTable(DefaultTableModel model){
        model.setRowCount(0);
        List<Product> products = productDAO.findAll();
        for(Product product : products){
            model.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock()
            });
        }
    }
}
