package BuisinessLogic;

import DataAccess.OrderDAO;
import DataModel.OrderTable;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderBLL extends MakeTable<OrderTable>{
    private OrderDAO orderDAO = new OrderDAO();

    public OrderBLL(){
        super(OrderTable.class);
        orderDAO = new OrderDAO();
    }

    /**
     * use the DAO class to call the findAll method
     * @return list of all the orders
     */
    public List<OrderTable> findAllOrders() {
        List<OrderTable> orders = orderDAO.findAll();
        if(orders == null){
            throw new IllegalStateException("The orders list does not exist.");
        }
        return orders;
    }

//    /**
//     * use the DAO calss to call the delete method
//     * @param id
//     */
//    public void deleteOrder(int id) {
//        boolean deleted = orderDAO.delete(id);
//        if(!deleted){
//            throw new NoSuchElementException("The order with id " + id + " does not exist.");
//        }
//    }

    /**
     * use the DAO class to call the insert method
     * @param orderTable
     * @return on OrderTable object
     */
    public OrderTable insertOrder(OrderTable orderTable) {
        try {
            return orderDAO.insert(orderTable);
        }catch(Exception e){
            throw new IllegalStateException("The orderTable with id " + orderTable.getId() + " failed to insert.");
        }
    }

    /**
     * refresh the order table
     * @param model
     */
    public void refreshTableModel(DefaultTableModel model){
        model.setRowCount(0);
        List<OrderTable> orders = orderDAO.findAll();
        for(OrderTable orderTable : orders){
            model.addRow(new Object[]{
                    orderTable.getId(),
                    orderTable.getId_client(),
                    orderTable.getId_product(),
                    orderTable.getQuantity(),
            });
        }
    }
}
