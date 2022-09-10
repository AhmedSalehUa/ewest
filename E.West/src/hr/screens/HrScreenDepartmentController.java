/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.screens;

import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXTextField; 
import hr.assets.Department;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author MTC
 */
public class HrScreenDepartmentController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Department> secTable;
    @FXML
    private TableColumn<Department,String> depTabName;
    @FXML
    private TableColumn<Department,String> secTabId;
    @FXML
    private Label secId;
    @FXML
    private TextField depName;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button secNew;
    @FXML
    private Button secDelete;
    @FXML
    private Button secEdite;
    @FXML
    private Button secAdd;

    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                                    clear();
                                    intialColumn();
                                    getData();
                                  

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
        secTable.setOnMouseClicked((e) -> {
            if (secTable.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                secNew.setDisable(false);

                secDelete.setDisable(false);

                secEdite.setDisable(false);

                secAdd.setDisable(true);

                Department selected = secTable.getSelectionModel().getSelectedItem();
                secId.setText(Integer.toString(selected.getId()));
                depName.setText(selected.getName());

               
            }
        });

    }

    private void intialColumn() {
        
        depTabName.setCellValueFactory(new PropertyValueFactory<>("name"));

        secTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void clear() {
        try {
            getAutoNum();
            secNew.setDisable(true);

            secDelete.setDisable(true);

            secEdite.setDisable(true);

            secAdd.setDisable(false);

             
            depName.setText("");
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void getAutoNum() throws Exception {
        secId.setText(Department.getAutoNum());
    }

    private void getData() {
        try {
            secTable.setItems(Department.getData());
            items = secTable.getItems();
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }
    ObservableList<Department> items;
 

    @FXML
    private void search(KeyEvent event) {
        FilteredList<Department> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter));

        });

        SortedList<Department> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(secTable.comparatorProperty());
        secTable.setItems(sortedData);
    }

    @FXML
    private void secNew(ActionEvent event) {
        clear();
    }

    @FXML
    private void secDelete(ActionEvent event) {
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
                                    alert.setTitle("Deleting Section");
                                    alert.setHeaderText("سيتم حذف القسم ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        Department sec = new Department();
                                        sec.setId(Integer.parseInt(secId.getText()));
                                        sec.Delete();
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
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void secEdite(ActionEvent event) {
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
                                    alert.setTitle("Editing Section");
                                    alert.setHeaderText("سيتم تعديل القسم ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        Department sec = new Department();
                                        sec.setId(Integer.parseInt(secId.getText()));
                                        sec.setName(depName.getText());
                                         sec.Edite();
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
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void secAdd(ActionEvent event) {
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
                                    Department sec = new Department();
                                    sec.setId(Integer.parseInt(secId.getText()));
                                    sec.setName(depName.getText());
                                     sec.Add();
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
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

}
