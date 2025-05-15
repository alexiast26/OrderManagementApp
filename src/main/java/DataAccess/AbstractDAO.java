package DataAccess;

import Connection.ConnectionFactory;

import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AbstractDAO<T> {
    protected static final Logger log = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    public AbstractDAO(Class<T> type) {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * create select query
     * @return a string with the query
     */
    private String createSelectQuery() {
        return "SELECT * FROM " + type.getSimpleName() + " WHERE id =?";
    }

    /**
     * create findall query
     * @return a string with the query
     */
    private String createFindAllQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ");
        query.append(type.getSimpleName());
        return query.toString();
    }

    /**
     * create insert query
     * @return a string with the query
     */
    private String createInsertQuery() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(type.getSimpleName());
        List<String> fields = Arrays.stream(type.getDeclaredFields()).map(Field::getName).filter(name -> !name.equals("id")).toList();
        query.append("(");
        query.append(String.join(", ", fields));
        query.append(") VALUES (");

        String placeHolders = String.join(", ", Collections.nCopies(fields.size(), "?"));
        query.append(placeHolders);
        query.append(")");
        return query.toString();
    }

    /**
     * create delete query
     * @return a string with the query
     */
    private String createDeleteQuery() {
        return "DELETE FROM " + type.getSimpleName() + " WHERE id=?";
    }

    /**
     * create update query
     * @return a string with the query
     */
    private String createUpdateQuery() {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(type.getSimpleName()).append(" SET ");
        List<String> fields = Arrays.stream(type.getDeclaredFields()).map(Field::getName).filter(name -> !name.equals("id")).map(name -> name + " =?").toList();
        query.append(String.join(", ", fields));
        query.append(" WHERE id =? ");
        return query.toString();
    }

    /**
     * create a list of objects from the result set
     * @param rs
     * @return a List with the objects
     */
    private List<T> createObjects(ResultSet rs){
        List<T> objects = new ArrayList<>();

        Constructor<?> constructor = Arrays.stream(type.getDeclaredConstructors()).filter(c -> c.getParameterTypes().length == 0).findFirst().orElseThrow(() -> new RuntimeException("No suitable constructor"));
        try{
            while (rs.next()){
                constructor.setAccessible(true);
                T instance = (T)constructor.newInstance();
                /*
                for(Field f : type.getDeclaredFields()){
                    String fieldName = f.getName();
                    Object value = rs.getObject(fieldName);
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, type);
                    Method setter = pd.getWriteMethod();
                    setter.invoke(instance, value);
                }
                objects.add(instance);
                 */
                Arrays.stream(type.getDeclaredFields()).forEach(field -> {
                    try {
                        String fieldName = field.getName();
                        Object value = rs.getObject(fieldName);
                        PropertyDescriptor pd = new PropertyDescriptor(fieldName, type);
                        Method setter = pd.getWriteMethod();
                        setter.invoke(instance, value);
                    } catch (IntrospectionException | SQLException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
                objects.add(instance);
            }
        }catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return objects;
    }

    /**
     * query the database to find all elements in a table
     * @return list of elements
     */
    public List<T> findAll() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String findAllQuery = createFindAllQuery();

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(findAllQuery);
            rs = ps.executeQuery();
            return createObjects(rs);
        } catch (SQLException e) {
            log.log(Level.WARNING, type.getSimpleName() + " DAO:findAll(): " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(ps);
            ConnectionFactory.close(con);
        }
        return null;
    }

    /**
     * query the databese to find an object from the table by id
     * @param id
     * @return the object
     */
    public T findById(int id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery = createSelectQuery();
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(selectQuery);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            return createObjects(rs).getFirst();
        }catch (SQLException e) {
            log.log(Level.WARNING, type.getSimpleName() + " DAO:findById(): " + e.getMessage());
        }finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(ps);
            ConnectionFactory.close(con);
        }
        return null;
    }

    /**
     * query the database to insert an object in the databese
     * @param obj
     * @return the object
     */
    public T insert(T obj) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String insertQuery = createInsertQuery();
        try{
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            int param = 1;

            // ?
            List<Field> fields = Arrays.stream(type.getDeclaredFields()).filter(f -> !f.getName().equals("id")).toList();
            for (Field f : fields) {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), type);
                Method getter = pd.getReadMethod();
                Object value = getter.invoke(obj);
                ps.setObject(param++, value);
            }
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("An error occurred while inserting, no rows affected.");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                Field idField = type.getDeclaredField("id");
                PropertyDescriptor pd = new PropertyDescriptor("id", type);
                Method setter = pd.getWriteMethod();
                setter.invoke(obj, id);
            }
            log.log(Level.INFO, type.getSimpleName() + " DAO:insert(): " + obj.toString() + " successfully inserted.");
            return obj;

        } catch (SQLException | IntrospectionException | InvocationTargetException | IllegalAccessException |
                 NoSuchFieldException e) {
            log.log(Level.WARNING, type.getSimpleName() + " DAO:insert(): " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(ps);
            ConnectionFactory.close(con);
        }
        return null;
    }

    /**
     * query the database tp delete an object from the database
     * @param id
     * @return true if deletion was successful and false if it failed
     */
    public boolean delete(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        String deleteQuery = createDeleteQuery();
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(deleteQuery);
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("An error occurred when deleting, no rows affected.");
            }
            log.log(Level.INFO, type.getSimpleName() + " DAO:delete(): " + id + " successfully deleted.");
            return true;
        } catch (SQLException e) {
            log.log(Level.WARNING, type.getSimpleName() + " DAO:delete(): " + e.getMessage());
        } finally {
            ConnectionFactory.close(ps);
            ConnectionFactory.close(con);
        }
        return false;
    }

    /**
     * query the database to update an object in it
     * @param obj
     */
    public void update(T obj) {
        Connection con = null;
        PreparedStatement ps = null;
        String updateQuery = createUpdateQuery();

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(updateQuery);
            int param = 1;
            Object idValue = null;
            for (Field f : type.getDeclaredFields()) {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), type);
                Method getter = pd.getReadMethod();
                Object value = getter.invoke(obj);
                if (f.getName().equals("id")) {
                    idValue = value;
                } else {
                    ps.setObject(param++, value);
                }
            }
            System.out.println(idValue);
            ps.setObject(param, idValue);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("An error occurred while updating, no rows affected.");
            }
        } catch (IntrospectionException | SQLException | InvocationTargetException | IllegalAccessException e) {
            log.log(Level.WARNING, type.getSimpleName() + " DAO:update(): " + e.getMessage());
        } finally {
            ConnectionFactory.close(ps);
            ConnectionFactory.close(con);
        }
    }
}