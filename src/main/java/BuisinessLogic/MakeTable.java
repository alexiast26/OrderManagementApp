package BuisinessLogic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MakeTable<T>{
    private final Class<T> type;

    public MakeTable(Class<T> type) {
        this.type = type;
    }

    /**
     * The method uses reflection techniques to make a table model from one of the 3 tables in the database: Client, Product, OrderTable
     * @param list
     * @return the table model
     */

    public DefaultTableModel makeModel(List<T> list) {
        Field[] fields = type.getDeclaredFields();
        String[] columNames = Arrays.stream(fields).map(field -> {
            String fieldName = field.getName();
            return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).replaceAll("([A-Z])", "$1");
        }).toArray(String[]::new);

        DefaultTableModel tableModel = new DefaultTableModel(columNames, 0);
        list.stream().map(obj -> Arrays.stream(fields).map(field -> {
                try{
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), type);
                    Method getter = pd.getReadMethod();
                    return getter.invoke(obj);
                } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                    return "n/a";
                }
        }).toArray()).forEach(tableModel::addRow);
        return tableModel;
    }
}
