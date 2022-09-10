/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.screens;

import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import hr.assets.Department;
import hr.assets.Sections;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class HrScreenSectionsController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Sections> secTable; 
    @FXML
    private TableColumn<Sections, String> secTabName;
    @FXML
    private TableColumn<Sections, String> secTabId;
    @FXML
    private Label secId;
    @FXML
    private TextField secName; 
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
    @FXML
    private TableColumn<Sections, String> secTabDepart;
    @FXML
    private ComboBox<Department> department;
 
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
                                    fillCombo();

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

                Sections selected = secTable.getSelectionModel().getSelectedItem();
                secId.setText(Integer.toString(selected.getId()));
                secName.setText(selected.getName());

                ObservableList<Department> items1 = department.getItems();
                for (Department a : items1) {
                    if (a.getName().equals(selected.getDepartment())) {
                        department.getSelectionModel().select(a);
                    }
                }
            }
        });

    }

    private void intialColumn() {
        secTabDepart.setCellValueFactory(new PropertyValueFactory<>("department"));

        secTabName.setCellValueFactory(new PropertyValueFactory<>("name"));

        secTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void clear() {
        try {
            getAutoNum();
            secNew.setDisable(true);

            secDelete.setDisable(true);

            secEdite.setDisable(true);

            secAdd.setDisable(false);

            department.getSelectionModel().clearSelection();
            
            secName.setText("");
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void getAutoNum() throws Exception {
        secId.setText(Sections.getAutoNum());
    }

    private void getData() {
        try {
            secTable.setItems(Sections.getData());
            items = secTable.getItems();
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }
    ObservableList<Sections> items;

    private void fillCombo() throws Exception {
        department.setItems(Department.getData());
        department.setConverter(new StringConverter<Department>() {
            @Override
            public String toString(Department patient) {
                return patient.getName();
            }

            @Override
            public Department fromString(String string) {
                return null;
            }
        });
        department.setCellFactory(cell -> new ListCell<Department>() {
            GridPane gridPane = new GridPane();
            Label lblid = new Label();
            Label lblName = new Label();

            {
                gridPane.getColumnConstraints().addAll(
                        new ColumnConstraints(100, 100, 100),
                        new ColumnConstraints(100, 100, 100)
                );

                gridPane.add(lblid, 0, 1);
                gridPane.add(lblName, 1, 1);

            }

            @Override
            protected void updateItem(Department person, boolean empty) {
                super.updateItem(person, empty);

                if (!empty && person != null) {
                    lblid.setText("م: " + Integer.toString(person.getId()));
                    lblName.setText("الاسم: " + person.getName());
                    setGraphic(gridPane);
                } else {

                    setGraphic(null);
                }
            }
        });
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<Sections> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter));

        });

        SortedList<Sections> sortedData = new SortedList<>(filteredData);
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
                                        Sections sec = new Sections();
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
                                        Sections sec = new Sections();
                                        sec.setId(Integer.parseInt(secId.getText()));
                                        sec.setName(secName.getText());
                                        sec.setDept_id(department.getSelectionModel().getSelectedItem().getId());
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
                                    Sections sec = new Sections();
                                    sec.setId(Integer.parseInt(secId.getText()));
                                    sec.setName(secName.getText());
                                    sec.setDept_id(department.getSelectionModel().getSelectedItem().getId());
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
