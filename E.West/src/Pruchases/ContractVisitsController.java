/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases;

import Pruchases.assets.ContractsDetails;
import Pruchases.assets.ContractsVisits;
import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hr.assets.Employee;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.swing.ButtonGroup;

/**
 * FXML Controller class
 *
 * @author amran
 */
public class ContractVisitsController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private Button DocShow_btn;
    @FXML
    private TableView<ContractsVisits> tab;
    @FXML
    private TableColumn<ContractsVisits, String> tabReport;
    @FXML
    private TableColumn<ContractsVisits, String> tabDate;
    @FXML
    private TableColumn<ContractsVisits, String> tabMemName;
    @FXML
    private TableColumn<ContractsVisits, String> tabId;
    @FXML
    private Label visitID;
    @FXML
    private JFXDatePicker visitDate;
    @FXML
    private ImageView docdown;
    @FXML
    private TextField docpath;
    @FXML
    private JFXTextArea report_txtAr;
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
    private ContractController parentController;
    int CONTRACT_ID;
    @FXML
    private TableColumn<ContractsVisits, String> tabStatue;
    @FXML
    private TableColumn<ContractsVisits, String> tabDevice;
    @FXML
    private ComboBox<Employee> member;
    @FXML
    private ComboBox<ContractsDetails> device;
    @FXML
    private RadioButton deviceWork;
    @FXML
    private RadioButton deviceNotWork;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup btg = new ToggleGroup();
        deviceWork.setToggleGroup(btg);
        deviceNotWork.setToggleGroup(btg);
        visitDate.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
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

        tab.setOnMouseClicked((e) -> {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {

            } else {
                Add.setDisable(true);
                Edite.setDisable(false);
                Delete.setDisable(false);
                New.setDisable(false);

                ContractsVisits selected = tab.getSelectionModel().getSelectedItem();
                visitID.setText(Integer.toString(selected.getId()));
                deviceWork.setSelected(deviceWork.getText().equals(selected.getStatue()));
                deviceNotWork.setSelected(deviceNotWork.getText().equals(selected.getStatue()));
                ObservableList<Employee> items1 = member.getItems();
                for (Employee a : items1) {
                    if (a.getName().equals(selected.getMemName())) {
                        member.getSelectionModel().select(a);
                    }
                }
                ObservableList<ContractsDetails> items = device.getItems();
                for (ContractsDetails a : items) {
                    if (a.getName().equals(selected.getDevice())) {
                        device.getSelectionModel().select(a);
                    }
                }

                visitDate.setValue(LocalDate.parse(selected.getDate()));

                report_txtAr.setText(selected.getReport());

            }
        });
    }
    ObservableList<ContractsVisits> items;

    @FXML
    private void search(KeyEvent event) {

    }

    @FXML
    private void attachFile(MouseEvent event) {
        FileChooser fil_chooser = new FileChooser();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fil_chooser.showOpenDialog(st);

        if (file != null) {
            docpath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void New(ActionEvent event) {
        clear();
    }

    @FXML
    private void Delete(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
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
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Deleting  Visit");
                                    alert.setHeaderText("سيتم حذف العقد ");
                                    alert.setContentText("هل انتالعقد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        ContractsVisits pr = new ContractsVisits();
                                        pr.setId(Integer.parseInt(visitID.getText()));
                                        pr.Delete();
                                    }
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

    @FXML
    private void Edite(ActionEvent event) {
        ContractsVisits pr = new ContractsVisits();
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
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
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editting  Visit");
                                    alert.setHeaderText("سيتم تعديل العقد ");
                                    alert.setContentText("هل انتالعقد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        if (result.get() == ButtonType.OK) {

                                            pr.setId(Integer.parseInt(visitID.getText())); 
                                    pr.setContractID(CONTRACT_ID);
                                            pr.setMemID(member.getSelectionModel().getSelectedItem().getId());
                                            pr.setDeviceID(device.getSelectionModel().getSelectedItem().getId());
                                            pr.setDate(visitDate.getValue().format(format));
                                            pr.setReport(report_txtAr.getText());
                                            pr.setStatue(deviceWork.isSelected() ? deviceWork.getText() : deviceNotWork.getText());
                                            if (docpath.getText().isEmpty() || docpath.getText().length() == 0) {
                                                pr.EditeWithouPhoto();
                                            } else {
                                                InputStream in = new FileInputStream(new File(docpath.getText()));
                                                pr.setDoc(in);

                                                String[] st = docpath.getText().split(Pattern.quote("."));
                                                pr.setDoc_ext(st[st.length - 1]);

                                                pr.Edite();
                                            }
                                        }
                                    }
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

    @FXML
    private void Add(ActionEvent event) {
        ContractsVisits pr = new ContractsVisits();
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
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
                                    pr.setId(Integer.parseInt(visitID.getText()));
                                    pr.setContractID(CONTRACT_ID);
                                    pr.setMemID(member.getSelectionModel().getSelectedItem().getId());
                                    pr.setDeviceID(device.getSelectionModel().getSelectedItem().getId());
                                    pr.setDate(visitDate.getValue().format(format));
                                    pr.setReport(report_txtAr.getText());
                                    pr.setStatue(deviceWork.isSelected() ? deviceWork.getText() : deviceNotWork.getText());
                                    if (docpath.getText().isEmpty() || docpath.getText().length() == 0) {
                                        pr.AddWithouPhoto();
                                    } else {
                                        InputStream in = new FileInputStream(new File(docpath.getText()));
                                        pr.setDoc(in);

                                        String[] st = docpath.getText().split(Pattern.quote("."));
                                        pr.setDoc_ext(st[st.length - 1]);

                                        pr.Add();
                                    }
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

    public void clear() {
        getAutoNum();
        member.getSelectionModel().clearSelection();
        device.getSelectionModel().clearSelection();
        visitID.setText("");
        visitDate.setValue(null);
        report_txtAr.setText("");
        docpath.setText("");
        Add.setDisable(false);
        Edite.setDisable(true);
        Delete.setDisable(true);
        New.setDisable(true);
    }

    private void getAutoNum() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            String autoNum;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            autoNum = ContractsVisits.getAutoNum();
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
                visitID.setText(autoNum);
                super.succeeded();
            }
        };
        service.start();
    }

    private void intialColumn() {
        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabMemName.setCellValueFactory(new PropertyValueFactory<>("memName"));
        tabDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabReport.setCellValueFactory(new PropertyValueFactory<>("report"));
        tabStatue.setCellValueFactory(new PropertyValueFactory<>("statue"));
        tabDevice.setCellValueFactory(new PropertyValueFactory<>("device"));
    }

    private void getData(int id) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<ContractsVisits> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        try {
                            data = ContractsVisits.getData(id);

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

    private void fillCombo() {
        
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Employee> employees; 

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            employees = Employee.getMaintaince(); 
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
                member.setItems(employees);
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
                    protected void updateItem(Employee person, boolean empty) {
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
                 
                super.succeeded();
            }
        };
        service.start();

    }

    private void fillCombo(int contractid) {
        
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() { 
            ObservableList<ContractsDetails> details;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try { 
                            details = ContractsDetails.getData(contractid); 
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
                 
                device.setItems(details);
                device.setConverter(new StringConverter<ContractsDetails>() {
                    @Override
                    public String toString(ContractsDetails patient) {
                        return patient.getName();
                    }

                    @Override
                    public ContractsDetails fromString(String string) {
                        return null;
                    }
                });
                device.setCellFactory(cell -> new ListCell<ContractsDetails>() {

                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();
                    Label lblModel = new Label();

                    {
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);
                        gridPane.add(lblModel, 0, 2);
                    }

                    @Override
                    protected void updateItem(ContractsDetails person, boolean empty) {
                        super.updateItem(person, empty);

                        if (!empty && person != null) {

                            lblid.setText("م: " + Integer.toString(person.getId()));
                            lblName.setText("الاسم: " + person.getName());
                            lblModel.setText("الموديل: " + person.getModel());
                            setGraphic(gridPane);
                        } else {
                            setGraphic(null);
                        }
                    }
                });
                super.succeeded();
            }
        };
        service.start();

    }

    @FXML
    private void getDocument(ActionEvent event) {
        try {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {
                AlertDialogs.showmessage("اختار من الجدول اولا");
            } else {
                ContractsVisits cv = new ContractsVisits();
                cv.setId(tab.getSelectionModel().getSelectedItem().getId());
                cv.getDocdown();
            }
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    void setParentController(ContractController parentController) {
        this.parentController = parentController;
    }

    void setId(int CONTRACT_ID) {
        this.CONTRACT_ID = CONTRACT_ID;
        clear();
        fillCombo(CONTRACT_ID);
        getData(CONTRACT_ID);
    }

}
