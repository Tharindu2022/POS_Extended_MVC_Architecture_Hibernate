package com.tharindutech.pos.bo.custom;

import com.tharindutech.pos.dto.ItemDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBo {

    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<ItemDto> searchItem(String searchText) throws SQLException, ClassNotFoundException;



}
