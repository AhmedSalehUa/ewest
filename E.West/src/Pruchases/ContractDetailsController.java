/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases;

import Pruchases.assets.ContractsDetails;
import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author MTC
 */
public class ContractDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ContractController parentController;
    @FXML
    private JFXTextField search;
    @FXML
    private TableView<ContractsDetails> tab;
    @FXML
    private TableColumn<ContractsDetails, String> tabDetails;
    @FXML
    private TableColumn<ContractsDetails, String> tabCost;
    @FXML
    private TableColumn<ContractsDetails, String> tabModel;
    @FXML
    private TableColumn<ContractsDetails, String> tabName;
    @FXML
    private TableColumn<ContractsDetails, String> tabId;
    @FXML
    private TextField name;
    @FXML
    private TextField model;
    @FXML
    private TextField cost;
    @FXML
    private TextArea details;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label deviceId;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdite;
    @FXML
    private Button btnAdd;

    int CONTRACT_ID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clear();
                                    intialColumn();

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);

                super.succeeded();
            }
        };
        service.start();
        tab.setOnMouseClicked((e) -> {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                btnNew.setDisable(false);

                btnDelete.setDisable(false);

                btnEdite.setDisable(false);

                btnAdd.setDisable(true);

                ContractsDetails selected = tab.getSelectionModel().getSelectedItem();
                deviceId.setText(Integer.toString(selected.getId()));
                name.setText(selected.getName());
                model.setText(selected.getModel());
                cost.setText(selected.getCost());
                details.setText(selected.getDetails());

            }
        });
    }

    private void intialColumn() {
        tabDetails.setCellValueFactory(new PropertyValueFactory<>("details"));

        tabCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tabModel.setCellValueFactory(new PropertyValueFactory<>("model"));

        tabName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void clear() {
        getAutoNum();
        btnNew.setDisable(true);

        btnDelete.setDisable(true);

        btnEdite.setDisable(true);

        btnAdd.setDisable(false);

        name.setText("");
        model.setText("");
        cost.setText("");
        details.setText("");
    }

    private void getAutoNum() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    deviceId.setText(ContractsDetails.getAutoNum());
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                super.succeeded();
            }
        };
        service.start();
    }
    ObservableList<ContractsDetails> items;

    private void getData(int id) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    items = ContractsDetails.getData(id);
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                tab.setItems(items);
                super.succeeded();
            }
        };
        service.start();
    }

    void setParentController(ContractController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<ContractsDetails> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter)
                    || pa.getModel().contains(lowerCaseFilter)
                    || pa.getCost().contains(lowerCaseFilter)
                    || pa.getDetails().contains(lowerCaseFilter));

        });

        SortedList<ContractsDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }

    @FXML
    private void New(ActionEvent event) {
        clear();
    }

    @FXML
    private void Delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Deleting Device");
                                    alert.setHeaderText("سيتم حذف الجهاز ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        ContractsDetails opd = new ContractsDetails();
                                        opd.setId(Integer.parseInt(deviceId.getText())); 
                                        opd.Delete();
                                        parentController.reduceAccount(CONTRACT_ID, tab.getSelectionModel().getSelectedItem().getCost());
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                clear();
                getData(CONTRACT_ID);
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Edite(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Editing Device");
        alert.setHeaderText("سيتم تعديل الجهاز ");
        alert.setContentText("هل انت متاكد؟");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
        }

        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            ContractsDetails opd = new ContractsDetails();

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editing Device");
                                    alert.setHeaderText("سيتم تعديل الجهاز ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        opd.setId(Integer.parseInt(deviceId.getText()));
                                        opd.setName(name.getText());
                                        opd.setDetails(details.getText());
                                        opd.setCost(cost.getText());
                                        opd.setModel(model.getText());
                                        opd.setContract_id(CONTRACT_ID);
                                        parentController.reduceAccount(CONTRACT_ID, tab.getSelectionModel().getSelectedItem().getCost());
                                        opd.Edite();
                                        parentController.setAccount(CONTRACT_ID, cost.getText());
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                    parentController.setAccount(CONTRACT_ID, tab.getSelectionModel().getSelectedItem().getCost());
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                if (ok) {
                    clear();
                    getData(CONTRACT_ID);
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Add(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ContractsDetails opd = new ContractsDetails();
            boolean ok = true;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    opd.setId(Integer.parseInt(deviceId.getText()));
                                    opd.setName(name.getText());
                                    opd.setDetails(details.getText());
                                    opd.setCost(cost.getText());
                                    opd.setModel(model.getText());
                                    opd.setContract_id(CONTRACT_ID);
                                    opd.Add();
                                    parentController.setAccount(CONTRACT_ID, cost.getText());
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                if (ok) {
                    clear();
                    getData(CONTRACT_ID);
                }
                super.succeeded();
            }
        };
        service.start();
    }

    void setId(int CONTRACT_ID) {
        this.CONTRACT_ID = CONTRACT_ID;
        clear();
        getData(CONTRACT_ID);
    }
}
