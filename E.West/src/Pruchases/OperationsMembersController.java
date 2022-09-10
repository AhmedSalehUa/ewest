/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases;

import Pruchases.assets.AcapyMembers;
import Pruchases.assets.Operations;
import Pruchases.assets.OperationsMembers;
import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXTextField;
import hr.assets.Employee;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter; 

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class OperationsMembersController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<OperationsMembers> tab;
    @FXML
    private TableColumn<OperationsMembers, String> tabmember;
    @FXML
    private TableColumn<OperationsMembers, String> tabId;
    @FXML
    private Label id;
    @FXML
    private ComboBox<Employee> member;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button New;
    @FXML
    private Button Delete;
    @FXML
    private Button Edite;
    @FXML
    private Button Add;
    int OPERATION_ID = 0;
    OperationsController parentConrol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
                                    intialColumn();
                                    fillCombo1();

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
                Add.setDisable(true);
                Edite.setDisable(false);
                Delete.setDisable(false);
                New.setDisable(false);

                OperationsMembers selected = tab.getSelectionModel().getSelectedItem();

                id.setText(Integer.toString(selected.getId()));
                ObservableList<Employee> items3 = member.getItems();
                for (Employee a : items3) {
                    if (a.getName().equals(selected.getMember())) {
                        member.getSelectionModel().select(a);
                    }
                }
//                ObservableList<AcapyMembers> items2 = productstore.getItems();
//                for (AcapyMembers a : items2) {
//                    if (a.getName().equals(selected.getMember_name())) {
//                        member.getSelectionModel().select(a);
//                    }
//                }

            }
        });

    }
    // TODO

    private void intialColumn() {
        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabmember.setCellValueFactory(new PropertyValueFactory<>("member"));

    }

    private void fillCombo1() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Employee> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            data = Employee.getMaintaince();

                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);

                member.setItems(data);
                member.setConverter(new StringConverter<Employee>() {
                    @Override
                    public String toString(Employee patient) {
                        return patient.getName();
                    }

                    @Override
                    public Employee fromString(String string) {
                        return null;
                    }
                });
                member.setCellFactory(cell -> new ListCell<Employee>() {

                    // Create our layout here to be reused for each ListCell
                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();

                    // Static block to configure our layout
                    {
                        // Ensure all our column widths are constant
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);

                    }

                    // We override the updateItem() method in order to provide our own layout for this Cell's graphicProperty
                    @Override
                    protected void updateItem(Employee person, boolean empty) {
                        super.updateItem(person, empty);

                        if (!empty && person != null) {

                            // Update our Labels
                            lblid.setText("م: " + Integer.toString(person.getId()));
                            lblName.setText("الاسم: " + person.getName());

                            setGraphic(gridPane);
                        } else {
                            // Nothing to display here
                            setGraphic(null);
                        }
                    }
                });
                super.succeeded();
            }
        };
        service.start();

    }

    private void clear() {
        getAutoNum();
        member.getSelectionModel().clearSelection();

        Add.setDisable(false);
        Edite.setDisable(true);
        Delete.setDisable(true);
        New.setDisable(true);
    }

    private void getAutoNum() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            String idNum;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            idNum = OperationsMembers.getAutoNum();

                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                id.setText(idNum);
                super.succeeded();
            }
        };
        service.start();

    }
    ObservableList<OperationsMembers> items;

    public void getData(int id) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<OperationsMembers> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            data = OperationsMembers.getData(id);
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };
            }

            @Override
            protected void succeeded() {
                tab.setItems(data);
                items = data;
                progress.setVisible(false);
                super.succeeded();
            }
        };
        service.start();
    }

    void setParentController(OperationsController parentConrol) {
        this.parentConrol = parentConrol;
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<OperationsMembers> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            if (pa.getOperation().contains(lowerCaseFilter)
                    || pa.getMember().contains(lowerCaseFilter)) {
                return true;
            } else {
                return false;
            }

        });

        SortedList< OperationsMembers> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);

    }

    @FXML
    private void New(ActionEvent event) {
        clear();
    }

    @FXML
    private void Delete(ActionEvent event) {
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
                                    alert.setTitle("Deleting  opration datails");
                                    alert.setHeaderText("سيتم حذف الصيانة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        OperationsMembers opm = new OperationsMembers();
                                        opm.setId(Integer.parseInt(id.getText()));
                                        opm.Delete();
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
                getData(opm.getOperation_id());
                super.succeeded();
            }
        };
        service.start();
    }

    OperationsMembers opm = new OperationsMembers();

    @FXML
    private void Edite(ActionEvent event) {
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
                                OperationsMembers opm = new OperationsMembers();
                                Operations op = new Operations();
                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editting  Clients");
                                    alert.setHeaderText("سيتم تعديل الصيانة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        opm.setId(Integer.parseInt(id.getText()));
//                                     opd.setOperation_id(operation.getSelectionModel().getSelectedItem().getId());
                                        opm.setMember_id(member.getSelectionModel().getSelectedItem().getId());

                                        opm.setOperation_id(OPERATION_ID);
                                        opm.Edit();

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
                getData(opm.getOperation_id());
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Add(ActionEvent event) {
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
                                OperationsMembers opm = new OperationsMembers();
                                Operations op = new Operations();
                                try {
                                    opm.setId(Integer.parseInt(id.getText()));
//                                     opd.setOperation_id(operation.getSelectionModel().getSelectedItem().getId());
                                    opm.setMember_id(member.getSelectionModel().getSelectedItem().getId());

                                    opm.setOperation_id(OPERATION_ID);

                                    opm.Add();

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
                getData(opm.getOperation_id());
                super.succeeded();
            }
        };
        service.start();
    }

    public void setId(int id) {
        this.OPERATION_ID = id;
        clear();
        getData(OPERATION_ID);

    }
}
