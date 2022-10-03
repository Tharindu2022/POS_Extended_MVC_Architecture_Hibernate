package com.tharindutech.pos.dao.custom.impl;

import com.tharindutech.pos.dao.CrudUtil;
import com.tharindutech.pos.dao.custom.CustomerDao;
import com.tharindutech.pos.db.DBConnection;
import com.tharindutech.pos.entity.Customer;
import com.tharindutech.pos.view.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(Customer c) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Customer VALUES(?,?,?,?)",c.getId(),c.getName(),c.getAddress(),c.getSalary());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Customer WHERE id=?",s);
    }

    @Override
    public boolean update(Customer c) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute("UPDATE Customer SET name=?,address=?,salary=? WHERE id= ?",c.getName(),c.getAddress(),c.getSalary(),c.getId());
    }

    @Override
    public ArrayList<Customer> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();
        ResultSet set =CrudUtil.execute("SELECT * FROM Customer WHERE name LIKE ? || address LIKE ?",searchText,searchText);

        ArrayList<Customer>list=new ArrayList<>();
        for (Customer c: list) {
            list.add(new Customer(set.getString(1), set.getString(2), set.getString(3), set.getDouble(4)));
        }
        return list;
    }
}
