package BuisinessLogic;

import DataAccess.ClientDAO;
import DataModel.Client;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientBLL extends MakeTable<Client>{
    private ClientDAO clientDAO;
    public ClientBLL() {
        super(Client.class);
        clientDAO = new ClientDAO();
    }

    /**
     * use the DAO class to call the findById method
     * @param id
     * @return the client with the given id
     */
    public Client findClientById(int id) {
        Client client = clientDAO.findById(id);
        if(client == null){
            throw new NoSuchElementException("The client with id " + id + " does not exist.");
        }
        return client;
    }

    /**
     * use the DAO class to call the findAll method
     * @return list of all the clients
     */
    public List<Client> findAllClients() {
        List<Client> clients = clientDAO.findAll();
        if(clients == null){
            throw new IllegalStateException("The client list is empty.");
        }
        return clients;
    }

    /**
     * use the DAO class to call the delete method
     * @param id
     */
    public void deleteClient(int id) {
        boolean deleted = clientDAO.delete(id);
        if(!deleted){
            throw new NoSuchElementException("The client with id " + id + " wasn't deleted.");
        }
    }

    /**
     * use the DAO class to call the insert method
     * @param client
     */
    public void insertClient(Client client) {
        if(client.getName() == null || client.getName().isEmpty()){
            throw new IllegalArgumentException("The client name is empty.");
        }
        try {
            clientDAO.insert(client);
        }catch (Exception e){
            throw new IllegalStateException("The client with id " + client.getId() + " failed to be inserted.");
        }
    }

    /**
     * use the DAO class to call the update method
     * @param client
     */
    public void updateClient(Client client) {
        if(client.getName() == null || client.getName().isEmpty()){
            throw new IllegalArgumentException("The client name is empty.");
        }
        try {
            clientDAO.update(client);
        }catch (Exception e){
            throw new IllegalStateException("The client with id " + client.getId() + " failed to be updated.");
        }
    }

    /**
     * refresh the client table
     * @param model
     */
    public void refreshClientTable(DefaultTableModel model){
        model.setRowCount(0);
        List<Client> clients = findAllClients();
        for(Client client : clients){
            model.addRow(new Object[]{
                    client.getId(),
                    client.getName(),
                    client.getEmail(),
                    client.getAge()
            });
        }
    }
}
