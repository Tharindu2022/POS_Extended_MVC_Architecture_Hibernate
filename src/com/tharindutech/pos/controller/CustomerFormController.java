package com.tharindutech.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.tharindutech.pos.bo.BoFactory;
import com.tharindutech.pos.bo.BoTypes;
import com.tharindutech.pos.bo.custom.CustomerBo;
import com.tharindutech.pos.dao.DaoFactory;
import com.tharindutech.pos.dao.DaoTypes;
import com.tharindutech.pos.dao.DatabaseAccessCode;
import com.tharindutech.pos.dao.custom.CustomerDao;
import com.tharindutech.pos.dao.custom.impl.CustomerDaoImpl;
import com.tharindutech.pos.db.DBConnection;
import com.tharindutech.pos.dto.CustomerDto;
import com.tharindutech.pos.entity.Customer;
import com.tharindutech.pos.view.tm.CustomerTM;
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

public class CustomerFormController {


    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOptions;
    public JFXButton btnSaveCustomer;
    public AnchorPane customerFormContext;
    public TextField txtSearch;
    private CustomerBo customerBo= BoFactory.getInstance().getBo(BoTypes.CUSTOMER);
    private String searchText = "";

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));
        searchCustomers(searchText);

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) //better than (newValue!=null)
                setData(newValue);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            searchCustomers(searchText);
        });
    }

    private void setData(CustomerTM tm) {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }

    private void searchCustomers(String text) {
        String searchText = "%" + text + "%";
        try {
            ObservableList tmList=FXCollections.observableArrayList();
            ArrayList<CustomerDto>customerList=customerBo.searchCustomer(text);
            for (CustomerDto c:customerList) {
                Button btn = new Button("DELETE");
                CustomerTM tm = new CustomerTM(c.getId(), c.getName(),c.getAddress(),c.getSalary(), btn);
                tmList.add(tm);

                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want to delete?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {

                        try {
                            if (customerBo.deleteCustomer(tm.getId())) {
                                searchCustomers(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully").show();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                            }

                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } {


            }
            tblCustomer.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c = new Customer();
        if (btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")) {
            try {
                boolean isCustomerSaved = customerBo.saveCustomer(
                        new CustomerDto(
                                txtId.getText(), txtName.getText(), txtAddress.getText(), Double.parseDouble(txtSalary.getText())));

                if (isCustomerSaved) {
                    searchCustomers(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }


        } else {

            try {
                boolean isCustomerUpdated = customerBo.updateCustomer(
                        new CustomerDto(
                                txtId.getText(), txtName.getText(), txtAddress.getText(), Double.parseDouble(txtSalary.getText())));

                if (isCustomerUpdated) {
                    searchCustomers(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

//            for (int i = 0; i < DataBase.customerTable.size(); i++) {
//                if (txtId.getText().equalsIgnoreCase(DataBase.customerTable.get(i).getId())) {
//                    DataBase.customerTable.get(i).setName(txtName.getText());
//                    DataBase.customerTable.get(i).setAddress(txtAddress.getText());
//                    DataBase.customerTable.get(i).setSalary(Double.parseDouble(txtSalary.getText()));
//                    searchCustomers(searchText);
//                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully").show();
//                    clearFields();
//                }
//            }

        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setText("Save Customer");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) customerFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }


}
