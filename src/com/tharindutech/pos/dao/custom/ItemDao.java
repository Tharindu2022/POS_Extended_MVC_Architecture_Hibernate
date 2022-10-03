package com.tharindutech.pos.dao.custom;

import com.tharindutech.pos.dao.CrudDao;
import com.tharindutech.pos.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDao extends CrudDao<Item, String> {
    public ArrayList<Item> searchItems(String searchText) throws SQLException, ClassNotFoundException;

}
