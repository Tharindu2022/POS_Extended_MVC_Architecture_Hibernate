package com.tharindutech.pos.bo;

import com.tharindutech.pos.bo.custom.impl.CustomerBoImpl;
import com.tharindutech.pos.bo.custom.impl.ItemBoImpl;
import com.tharindutech.pos.dao.custom.impl.CustomerDaoImpl;
import com.tharindutech.pos.dao.custom.impl.ItemDaoImpl;

public class BoFactory {
private static BoFactory boFactory;
private BoFactory(){
}
    public static BoFactory getInstance(){
    return boFactory==null?boFactory=new BoFactory():boFactory;
    }


    public <T> T getBo(BoTypes types){
    switch (types){
        case CUSTOMER:
            return (T) new CustomerBoImpl();
        case ITEM:
            return (T) new ItemBoImpl();
        default:return null;
    }
    }



}
