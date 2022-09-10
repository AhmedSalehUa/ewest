/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases;

import EWest.EWest;
import Pruchases.assets.OperationsCosts;
import assets.classes.AlertDialogs;
import static assets.classes.statics.USERNAME_DEFAULT;
import static assets.classes.statics.USER_ID;
import static assets.classes.statics.USER_NAME;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class OperationsCostsController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<OperationsCosts> tab;
    @FXML
    private TableColumn<OperationsCosts, String> tabDate;
    @FXML
    private TableColumn<OperationsCosts, String> tabPayFor;
    @FXML
    private TableColumn<OperationsCosts, String> tabAmount;
    @FXML
    private TableColumn<OperationsCosts, String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField amount;
    @FXML
    private TextField payFor;
    @FXML
    private JFXDatePicker date;
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
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    OperationsController parentController;
    int OPERATION_ID = 0;
    @FXML
    private TableColumn<OperationsCosts, String> tabDateTime;
    @FXML
    private TableColumn<OperationsCosts, String> tabUser;
    @FXML
    private ImageView costDocAttach;
    @FXML
    private TextField costPath;

    Preferences prefs;
    @FXML
    private Button showFile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(EWest.class);
        date.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return format.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, format);
            }
        });
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

                                    intialColumn(); 
                                    showFile.setDisable(true);
                                    
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
                showFile.setDisable(true);
            } else {
                Add.setDisable(true);
                Edite.setDisable(false);
                Delete.setDisable(false);
                New.setDisable(false);
                showFile.setDisable(false);
                OperationsCosts selected = tab.getSelectionModel().getSelectedItem();

                id.setText(Integer.toString(selected.getId()));

                amount.setText(selected.getAmount());
                date.setValue(LocalDate.parse(selected.getDate()));
                payFor.setText(selected.getPayFor());
               
            }
        });
    }

    private void intialColumn() {
        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabPayFor.setCellValueFactory(new PropertyValueFactory<>("payFor"));
        tabAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tabDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabDateTime.setCellValueFactory(new PropertyValueFactory<>("SysTime"));
        tabUser.setCellValueFactory(new PropertyValueFactory<>("user"));
    }

    private void clear() {
        getAutoNum();
        date.setValue(null);
        payFor.setText("");
        amount.setText("");
        costPath.setText("");
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
                            idNum = OperationsCosts.getAutoNum();

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

    void getData(int id) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<OperationsCosts> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            data = OperationsCosts.getData(id);
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
    ObservableList<OperationsCosts> items;

    @FXML
    private void search(KeyEvent event) {
        FilteredList<OperationsCosts> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            if (pa.getPayFor().contains(lowerCaseFilter) || pa.getAmount().contains(lowerCaseFilter)
                    || pa.getDate().contains(lowerCaseFilter)) {
                return true;
            } else {
                return false;
            }

        });

        SortedList< OperationsCosts> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }

    @FXML
    private void setTotal(KeyEvent event) {

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
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Deleting  OperationCosts");
                                    alert.setHeaderText("سيتم حذف البند ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        OperationsCosts opd = new OperationsCosts();
                                        opd.setId(Integer.parseInt(id.getText()));
                                        opd.Delete();
                                        parentController.reduceSpended(OPERATION_ID, tab.getSelectionModel().getSelectedItem().getAmount());

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
                getData(OPERATION_ID);
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Edite(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            OperationsCosts opd = new OperationsCosts();

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
                                    alert.setTitle("Editting  Cost");
                                    alert.setHeaderText("سيتم تعديل البند ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        opd.setId(Integer.parseInt(id.getText()));
                                        opd.setDate(date.getValue().format(format));
                                        opd.setAmount(amount.getText());
                                        opd.setPayFor(payFor.getText());
                                        opd.setOperation_id(OPERATION_ID);
                                        opd.setUser_id(Integer.parseInt(prefs.get(USER_ID, "0")));
                                        parentController.reduceSpended(OPERATION_ID, tab.getSelectionModel().getSelectedItem().getAmount());
                                        if (costPath.getText().isEmpty() || costPath.getText().length() == 0) {
                                            opd.Edite();
                                        } else {
                                            InputStream input = new FileInputStream(new File(costPath.getText()));
                                            opd.setPhoto(input);

                                            String[] st = costPath.getText().split(Pattern.quote("."));
                                            opd.setPhoto_ext(st[st.length - 1]);
                                            opd.EditeWithPhoto();
                                        }
                                        parentController.setSpended(OPERATION_ID, amount.getText());
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                    parentController.setSpended(OPERATION_ID, tab.getSelectionModel().getSelectedItem().getAmount());
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
                    getData(OPERATION_ID);
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
            OperationsCosts opd = new OperationsCosts();
            boolean ok = true;

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
                                    opd.setId(Integer.parseInt(id.getText()));
                                    opd.setOperation_id(OPERATION_ID);
                                    opd.setAmount(amount.getText());
                                    opd.setPayFor(payFor.getText());
                                    opd.setDate(date.getValue().format(format));
                                    opd.setUser_id(Integer.parseInt(prefs.get(USER_ID, "0")));
                                    if (costPath.getText().isEmpty() || costPath.getText().length() == 0) {
                                        opd.Add();
                                    } else {
                                        InputStream input = new FileInputStream(new File(costPath.getText()));
                                        opd.setPhoto(input);

                                        String[] st = costPath.getText().split(Pattern.quote("."));
                                        opd.setPhoto_ext(st[st.length - 1]);
                                        opd.AddWithPhoto();
                                    }
                                    parentController.setSpended(OPERATION_ID, amount.getText());
                                } catch (Exception ex) {
                                    ok = false;
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
                if (ok) {
                    clear();
                    getData(OPERATION_ID);
                }
                super.succeeded();
            }
        };
        service.start();
    }

    public void setParentController(OperationsController parentController) {
        this.parentController = parentController;

    }

    public void setId(int id) {
        this.OPERATION_ID = id;

        clear();
        getData(OPERATION_ID);
    }

    @FXML
    private void attachCostFile(MouseEvent event) {
        FileChooser fil_chooser = new FileChooser();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fil_chooser.showOpenDialog(st);

        if (file != null) {
            costPath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void shoeFile(ActionEvent event) {
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
                                    if (tab.getSelectionModel().getSelectedIndex() == -1) {
                                        AlertDialogs.showError("اختار من الجدول اولا");
                                    } else {
                                        OperationsCosts cl = new OperationsCosts();
                                        cl.setId(tab.getSelectionModel().getSelectedItem().getId());
                                        cl.getCostPhoto();
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
                getData(OPERATION_ID);
                super.succeeded();
            }
        };
        service.start();
    }

}
