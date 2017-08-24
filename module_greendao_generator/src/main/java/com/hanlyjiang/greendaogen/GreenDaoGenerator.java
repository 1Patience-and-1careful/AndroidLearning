package com.hanlyjiang.greendaogen;

import com.hanlyjiang.greendaogen.base.IGenerator;
import com.hanlyjiang.greendaogen.document.DocumentGen;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * 用于生成Greendao的类
 */
public class GreenDaoGenerator {

    private static final Class[] classz = new Class[]{
            DocumentGen.class,
    };


    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "de.greenrobot.daoexample");
        DaoGenerator daoGenerator = new DaoGenerator();
        IGenerator iGenerator;
        for (Class cla : classz) {
            iGenerator = (IGenerator) cla.newInstance();
            schema = new Schema(iGenerator._version(), iGenerator._packageName());
            iGenerator.addEntity(schema);
            File file = new File(iGenerator._genPath());
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            daoGenerator.generateAll(schema, file.getAbsolutePath());
        }
    }

//    private static void addNote(Schema schema) {
//        Entity note = schema.addEntity("Note");
//        note.addIdProperty();
//        note.addStringProperty("text").notNull();
//        note.addStringProperty("comment");
//        note.addDateProperty("date");
//    }
//
//    private static void addCustomerOrder(Schema schema) {
//        Entity customer = schema.addEntity("Customer");
//        customer.addIdProperty();
//        customer.addStringProperty("name").notNull();
//
//        Entity order = schema.addEntity("Order");
//        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
//        order.addIdProperty();
//        Property orderDate = order.addDateProperty("date").getProperty();
//        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
//        order.addToOne(customer, customerId);
//
//        ToMany customerToOrders = customer.addToMany(order, customerId);
//        customerToOrders.setName("orders");
//        customerToOrders.orderAsc(orderDate);
//    }
}
