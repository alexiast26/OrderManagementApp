package DataAccess;

import DataModel.OrderTable;

public class OrderDAO extends AbstractDAO<OrderTable>{

    public OrderDAO() {
        super(OrderTable.class);
    }



}
