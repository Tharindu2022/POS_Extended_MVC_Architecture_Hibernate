package com.tharindutech.pos.bo.custom.impl;

import com.tharindutech.pos.bo.custom.CustomerBo;
import com.tharindutech.pos.dao.DaoFactory;
import com.tharindutech.pos.dao.DaoTypes;
import com.tharindutech.pos.dao.custom.CustomerDao;
import com.tharindutech.pos.dto.CustomerDto;
import com.tharindutech.pos.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBoImpl implements CustomerBo {

    private CustomerDao dao= DaoFactory.getInstance().getDao(DaoTypes.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
      return   dao.save(new Customer(dto.getId(),dto.getName(),dto.getAddress(),dto.getSalary()));
    }


    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return dao.update(new Customer(dto.getId(),dto.getName(),dto.getAddress(),dto.getSalary()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return dao.delete(id);
    }

    @Override
    public ArrayList<CustomerDto> searchCustomer(String searchText) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> entities= dao.searchCustomers(searchText);
        ArrayList<CustomerDto>dtoList=new ArrayList<>();
        for (Customer c:entities) {
            dtoList.add(new CustomerDto(c.getId(),c.getName(),c.getAddress(),c.getSalary()));
        }
        return dtoList ;
    }
}
