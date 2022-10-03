package com.tharindutech.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.tharindutech.pos.bo.BoFactory;
import com.tharindutech.pos.bo.BoTypes;
import com.tharindutech.pos.bo.custom.ItemBo;
import com.tharindutech.pos.dao.DaoFactory;
import com.tharindutech.pos.dao.DaoTypes;
import com.tharindutech.pos.dao.DatabaseAccessCode;
import com.tharindutech.pos.dao.custom.ItemDao;
import com.tharindutech.pos.dao.custom.impl.ItemDaoImpl;
import com.tharindutech.pos.db.DBConnection;
import com.tharindutech.pos.dto.ItemDto;
import com.tharindutech.pos.entity.Item;
import com.tharindutech.pos.view.tm.ItemTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class ItemFormController {
    public AnchorPane ItemFormContext;
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtSearch;
    public JFXButton btnSaveItem;
    public TableView tblCustomer;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOptions;
    public TableView tblItem;
    private ItemBo itemBo= BoFactory.getInstance().getBo(BoTypes.ITEM);
    private String searchText = "";


    public void initialize() throws SQLException, ClassNotFoundException {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));
        searchItems(searchText);
        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) //better than (newValue!=null)
                setData((ItemTM) newValue);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    searchText = newValue;
                    try {
                        searchItems(searchText);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        );


    }

    private void setData(ItemTM tm) {
        txtCode.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));
        btnSaveItem.setText("Update Item");
    }


    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ItemFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    public void newItemOnAction(ActionEvent actionEvent) {
        btnSaveItem.setText("Save Item");
    }

    public void saveItemOnAction(ActionEvent actionEvent) {
        if (btnSaveItem.getText().equalsIgnoreCase("Save Item")) {
            try {
                boolean isItemSaved=itemBo.saveItem(
                        new ItemDto(txtCode.getText(), txtDescription.getText(),
                                Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText())));
                if (isItemSaved) {
                    searchItems(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Item  Saved Successfully").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        } else {
            try {
               boolean isItemUpdated=itemBo.updateItem(new ItemDto(txtCode.getText(), txtDescription.getText(),
                       Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText())));

                if (isItemUpdated) {
                    searchItems(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Item Updated Successfully").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void clearFields() {
        txtCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

    private void searchItems(String text) throws SQLException, ClassNotFoundException {
        String searchText = "%" + text + "%";
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();
        ArrayList<ItemDto>itemList=itemBo.searchItem(text);
        for (ItemDto i:itemList) {
            Button btn = new Button("DELETE");
            ItemTM tm = new ItemTM(i.getCode(),i.getDescription(),i.getUnitPrice(),i.getQtyOnHand(), btn);
            tmList.add(tm);
            btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want to delete?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.YES) {
                    try {
                        //ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();
                        if (itemBo.deleteItem(tm.getCode())) {
                            searchItems(searchText);
                            new Alert(Alert.AlertType.INFORMATION, "Item Deleted Successfully").show();
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        tblItem.setItems(tmList);
    }
}
