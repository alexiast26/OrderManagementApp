package DataAccess;

import DataModel.Client;

import Connection.ConnectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class ClientDAO extends AbstractDAO<Client>{
    public ClientDAO() {
        super(Client.class);
    }

}
