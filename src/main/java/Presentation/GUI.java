package Presentation;

import BuisinessLogic.BillBLL;
import BuisinessLogic.ClientBLL;
import BuisinessLogic.OrderBLL;
import BuisinessLogic.ProductBLL;
import DataAccess.BillDAO;
import DataModel.Bill;
import DataModel.Client;
import DataModel.OrderTable;
import DataModel.Product;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUI extends JFrame{
    JPanel framePanel = new JPanel();

    JButton clientOperation = new JButton("Client Window");
    JButton productOperation = new JButton("Product Window");
    JButton orderOperation = new JButton("Order Window");
    JButton viewBills = new JButton("View Bills");

    JPanel buttonPanel1 = new JPanel();
    JPanel buttonPanel2 = new JPanel();
    JPanel buttonPanel3 = new JPanel();

    JTable clientTable;
    DefaultTableModel clientTableModel;
    JScrollPane clientScrollPane;


    JTable productTable;
    DefaultTableModel productTableModel;
    JScrollPane productScrollPane;

    JTable orderTable;
    DefaultTableModel orderTableModel;
    JScrollPane orderScrollPane;

    JTable billsTable;
    DefaultTableModel billsTableModel;
    JScrollPane billsScrollPane;
    


    public GUI(){
        setTitle("Aplication");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 75);
        //setLocationRelativeTo(null);
        clientOperation.addActionListener(e -> clientOpButton());
        framePanel.add(clientOperation);
        productOperation.addActionListener(e -> productOpButton());
        framePanel.add(productOperation);
        orderOperation.addActionListener(e -> orderOpButton());
        framePanel.add(orderOperation);
        viewBills.addActionListener(e -> viewBillsFrame());
        framePanel.add(viewBills);
        add(framePanel);

        setVisible(true);
    }

    /**
     * create the clients frame to view all the clients, add, delete and edit them
     */
    public void clientOpButton(){
        JFrame clientOpFrame = new JFrame("Client Operations");
        JPanel clientPanel = new JPanel();

        JButton addClientButton = new JButton("Add Client");
        JButton deleteClientButton = new JButton("Delete Client");
        JButton editClientButton = new JButton("Edit Client");

        clientOpFrame.setVisible(true);
        clientOpFrame.setLocationRelativeTo(null);
        clientOpFrame.pack();
        clientOpFrame.setSize(500, 400);

        ClientBLL clientBLL = new ClientBLL();

        clientOpFrame.setLayout(new BorderLayout());

        clientPanel.setLayout(new BorderLayout());
        clientTableModel = clientBLL.makeModel(clientBLL.findAllClients());
        clientTable = new JTable(clientTableModel);
        clientScrollPane = new JScrollPane(clientTable);
        clientPanel.add(clientScrollPane, BorderLayout.CENTER);

        buttonPanel1.add(addClientButton);
        addClientButton.addActionListener(e -> addClientButton(clientBLL));
        buttonPanel1.add(deleteClientButton);
        deleteClientButton.addActionListener(e -> deleteClientButton(clientBLL));
        buttonPanel1.add(editClientButton);
        editClientButton.addActionListener(e -> editClientButton(clientBLL));
        clientOpFrame.add(buttonPanel1, BorderLayout.NORTH);

        clientOpFrame.add(clientPanel, BorderLayout.CENTER);
    }

    /**
     * make the action listener for the add button and add clients in the database
     * @param clientBLL
     */
    public void addClientButton(ClientBLL clientBLL){
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField ageField = new JTextField();
        Object[] objects = {"Name: ", nameField, "Email: ", emailField, "Age: ", ageField};
        int option = JOptionPane.showConfirmDialog(null, objects, "Add Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try {
                String name = nameField.getText();
                String emailStr = emailField.getText();
                int age = Integer.parseInt(ageField.getText());
                if(age < 0 || age > 100){
                    JOptionPane.showMessageDialog(null, "Please enter a valid age");
                }
                Client client = new Client(name, emailStr, age);
                clientBLL.insertClient(client);
                clientBLL.refreshClientTable(clientTableModel);
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please enter a valid age.");
            }
        }
    }

    /**
     * make the action listener for the delete button and delete clients from the database
     * @param clientBLL
     */
    public void deleteClientButton(ClientBLL clientBLL){
        JTextField idField = new JTextField();
        Object[] objects = {"ID: ", idField};
        int option = JOptionPane.showConfirmDialog(null, objects, "Delete Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try {
                int id = Integer.parseInt(idField.getText());
                clientBLL.deleteClient(id);
                clientBLL.refreshClientTable(clientTableModel);
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please enter a valid id");
            }
        }
    }

    /**
     * make the action listener for the edit button and edit clients in the database
     * @param clientBLL
     */
    public void editClientButton(ClientBLL clientBLL){
        int row = clientTable.getSelectedRow();
        if (row != -1){
            int id = Integer.parseInt(clientTable.getValueAt(row, 0).toString());
            System.out.println(id);
            String name = clientTable.getValueAt(row, 1).toString();
            String email = clientTable.getValueAt(row, 2).toString();
            int age = Integer.parseInt(clientTable.getValueAt(row, 3).toString());

            JTextField nameField = new JTextField(name);
            JTextField emailField = new JTextField(email);
            JTextField ageField = new JTextField(age + "");

            Object[] objects = {"Name: ", nameField, "Email: ", emailField, "Age: ", ageField};
            int option = JOptionPane.showConfirmDialog(null, objects, "Update Client", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION){
                try {
                    String newName = nameField.getText();
                    String newEmail = emailField.getText();
                    int newAge = Integer.parseInt(ageField.getText());
                    if(newAge < 0 || newAge > 100){
                        JOptionPane.showMessageDialog(null, "Please enter a valid age");
                    }
                    Client client = new Client(id, newName, newEmail, newAge);
                    clientBLL.updateClient(client);
                    clientBLL.refreshClientTable(clientTableModel);
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Please enter a valid age");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row");
        }
    }

    /**
     * create the products frame to view all the products, add, delete and edit them
     */
    public void productOpButton(){
        JFrame productOpFrame = new JFrame("Product Operations");
        JPanel productPanel = new JPanel();

        JButton addProductButton = new JButton("Add Product");
        JButton deleteProductButton = new JButton("Delete Product");
        JButton editProductButton = new JButton("Edit Product");

        productOpFrame.setVisible(true);
        productOpFrame.setLocationRelativeTo(null);
        productOpFrame.pack();
        productOpFrame.setSize(500, 400);

        ProductBLL productBLL = new ProductBLL();

        productOpFrame.setLayout(new BorderLayout());
        productTableModel = productBLL.makeModel(productBLL.findAllProducts());
        productTable = new JTable(productTableModel);
        productScrollPane = new JScrollPane(productTable);
        productPanel.setLayout(new BorderLayout());
        productPanel.add(productScrollPane, BorderLayout.CENTER);
        productOpFrame.add(productPanel, BorderLayout.CENTER);

        buttonPanel2.add(addProductButton);
        addProductButton.addActionListener(e -> addProductButton(productBLL));
        buttonPanel2.add(deleteProductButton);
        deleteProductButton.addActionListener(e -> deleteProductButton(productBLL));
        buttonPanel2.add(editProductButton);
        editProductButton.addActionListener(e -> editProductButton(productBLL));
        productOpFrame.add(buttonPanel2, BorderLayout.NORTH);
    }

    /**
     * make the action listener for the add button and add products in the database
     * @param productBLL
     */
    public void addProductButton(ProductBLL productBLL){
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        Object[] objects = {"Name: ", nameField, "Price: ", priceField, "Stock: ", stockField};
        int option = JOptionPane.showConfirmDialog(null, objects, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try {
                String name = nameField.getText();
                int price = Integer.parseInt(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                if (stock < 0 || price < 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid stock/price, can't be negative");
                }
                Product product = new Product(name, price, stock);
                productBLL.insertProduct(product);
                productBLL.refreshProductTable(productTableModel);
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please enter a valid stock/price");
            }
        }
    }

    /**
     * make the action listener for the delete button and delete products from the database
     * @param productBLL
     */
    public void deleteProductButton(ProductBLL productBLL){
        JTextField idField = new JTextField();
        Object[] objects = {"ID: ", idField};
        int option = JOptionPane.showConfirmDialog(null, objects, "Delete Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try {
                int id = Integer.parseInt(idField.getText());
                productBLL.deleteProduct(id);
                productBLL.refreshProductTable(productTableModel);
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please enter a valid id");
            }
        }
    }

    /**
     * make the action listener for the edit button and edit products in the database
     * @param productBLL
     */
    public void editProductButton(ProductBLL productBLL){
        int row = productTable.getSelectedRow();
        if (row != -1){
            int id = Integer.parseInt(productTable.getValueAt(row, 0).toString());
            String name = productTable.getValueAt(row, 1).toString();
            int price = Integer.parseInt(productTable.getValueAt(row, 2).toString());
            int stock = Integer.parseInt(productTable.getValueAt(row, 3).toString());
            JTextField nameField = new JTextField(name);
            JTextField priceField = new JTextField(price + "");
            JTextField stockField = new JTextField(stock + "");
            Object[] objects = {"Name: ", nameField, "Price: ", priceField, "Stock: ", stockField};
            int option = JOptionPane.showConfirmDialog(null, objects, "Update Client", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION){
                try{
                    String newName = nameField.getText();
                    int newPrice = Integer.parseInt(priceField.getText());
                    int newStock = Integer.parseInt(stockField.getText());
                    Product product = new Product(id, newName, newPrice, newStock);
                    productBLL.updateProduct(product);
                    productBLL.refreshProductTable(productTableModel);
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Please enter a valid stock/price");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row");
        }
    }

    /**
     * create the order frame to view all the orders and add them
     */
    public void orderOpButton(){
        JFrame orderOpFrame = new JFrame("Order Operations");
        JPanel orderPanel = new JPanel();
        JButton addOrderButton = new JButton("Add Order");

        orderOpFrame.setVisible(true);
        orderOpFrame.setLocationRelativeTo(null);
        orderOpFrame.pack();
        orderOpFrame.setSize(500, 400);

        OrderBLL orderBLL = new OrderBLL();

        orderOpFrame.setLayout(new BorderLayout());
        buttonPanel3.add(addOrderButton);
        addOrderButton.addActionListener(e -> addOrderButton(orderBLL));
        //buttonPanel3.add(deleteOrderButton);
        //deleteOrderButton.addActionListener(e -> deleteOrderButton(orderBLL));

        orderOpFrame.add(buttonPanel3, BorderLayout.NORTH);

        orderTableModel = orderBLL.makeModel(orderBLL.findAllOrders());
        orderTable = new JTable(orderTableModel);
        orderScrollPane = new JScrollPane(orderTable);
        orderPanel.setLayout(new BorderLayout());
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);
        orderOpFrame.add(orderPanel, BorderLayout.CENTER);
    }

    /**
     * make the action listener for the add button and add orders in the database
     * @param orderBLL
     */
    public void addOrderButton(OrderBLL orderBLL){
        JTextField idClientField = new JTextField();
        JTextField idProductField = new JTextField();
        JTextField idQuantityField = new JTextField();
        Object[] objects = {"Client ID: ", idClientField, "Product ID: ", idProductField, "Quantity: ", idQuantityField};
        BillBLL billBLL = new BillBLL();
        int option = JOptionPane.showConfirmDialog(null, objects, "Add OrderTable", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                int clientId = Integer.parseInt(idClientField.getText());
                int productId = Integer.parseInt(idProductField.getText());
                int quantity = Integer.parseInt(idQuantityField.getText());
                ClientBLL clientBLL = new ClientBLL();
                ProductBLL productBLL = new ProductBLL();
                Client client = clientBLL.findClientById(clientId);
                Product product = productBLL.findProductById(productId);
                if(client == null || product == null){
                    JOptionPane.showMessageDialog(null, "Client or product not found");
                }
                if(productBLL.decreaseQuantity(product, quantity)){
                    OrderTable order = orderBLL.insertOrder(new OrderTable(clientId, productId, quantity));
                    orderBLL.refreshTableModel(orderTableModel);

                    int id = order.getId();
                    billBLL.insertBill(new Bill(id, id, clientId, productId, quantity*product.getPrice()));
                    billBLL.refreshBillTable(billsTableModel);
                }else{
                    JOptionPane.showMessageDialog(null, "Couldn't add order because there isn't enough product");
                }
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please enter a valid id/quantity");
            }
        }
    }

//    public void deleteOrderButton(OrderBLL orderBLL){
//        JTextField idField = new JTextField();
//        Object[] objects = {"ID: ", idField};
//        int option = JOptionPane.showConfirmDialog(null, objects, "Delete OrderTable", JOptionPane.OK_CANCEL_OPTION);
//        if (option == JOptionPane.OK_OPTION){
//            try{
//                int id = Integer.parseInt(idField.getText());
//                orderBLL.deleteOrder(id);
//                orderBLL.refreshTableModel(orderTableModel);
//            } catch (NumberFormatException e){
//                JOptionPane.showMessageDialog(null, "Please enter a valid id");
//            }
//        }
//    }

    /**
     * create the bills frame to view the bills generated by the orders created
     */
    public void viewBillsFrame(){
        JFrame billsFrame = new JFrame("Bills");
        JPanel billsPanel = new JPanel();

        billsFrame.setVisible(true);
        billsFrame.setLocationRelativeTo(null);
        billsFrame.pack();
        billsFrame.setSize(500, 400);
        billsFrame.setLayout(new BorderLayout());

        BillBLL billBLL = new BillBLL();

        billsTableModel = billBLL.getBillTableModel();
        billsTable = new JTable(billsTableModel);
        billsScrollPane = new JScrollPane(billsTable);
        billsPanel.setLayout(new BorderLayout());
        billsPanel.add(billsScrollPane, BorderLayout.CENTER);
        billsFrame.add(billsPanel, BorderLayout.CENTER);
    }
}
