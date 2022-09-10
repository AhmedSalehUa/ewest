/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.screens;

import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.controlsfx.control.CheckListView;
//import screens.accounts.assets.Accounts;
import hr.assets.Employee;
import hr.assets.EmployeeSolfa;
import hr.assets.Sections;
import hr.assets.Shifts;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class HrScreenEmployesController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Employee> empTable;
    @FXML
    private TableColumn<Employee, String> empTabSection;
    @FXML
    private TableColumn<Employee, String> empTabName;
    @FXML
    private TableColumn<Employee, String> empTabId;
    @FXML
    private Label empId;
    @FXML
    private TextField empName;
    @FXML
    private TextField empAdress;
    @FXML
    private TextField empSalary;
    @FXML
    private ComboBox<Sections> empSection;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button empNew;
    @FXML
    private Button empDelete;
    @FXML
    private Button empEdite;
    @FXML
    private Button empAdd;
    @FXML
    private CheckListView<String> shifts;
    @FXML
    private TableView<EmployeeSolfa> solfaTable;
    @FXML
    private TableColumn<EmployeeSolfa, String> solfaTabDate;
    @FXML
    private TableColumn<EmployeeSolfa, String> solfaTabAmount;
    @FXML
    private TableColumn<EmployeeSolfa, String> solfaTabId;
    @FXML
    private Label solfaId;
    @FXML
    private TextField solfaAmount;
    @FXML
    private JFXDatePicker solfaDate;
    @FXML
    private Button solfaNew;
    @FXML
    private Button solfaDelete;
    @FXML
    private Button solfaEdite;
    @FXML
    private Button solfaAdd;

    Employee selectedOne;
    @FXML
    private Button btnShow;
    @FXML
    private TableColumn<Employee, String> empTabRole;
    @FXML
    private TableColumn<Employee, String> empTabNationalId;
    @FXML
    private TableColumn<Employee, String> empTabTele2;
    @FXML
    private TableColumn<Employee, String> empTabTele1;
    @FXML
    private TextField empTele1;
    @FXML
    private TextField empEmail;
    @FXML
    private TextField empTele2;
    @FXML
    private ComboBox<String> empRole;
    @FXML
    private TextField empNationalId;
    @FXML
    private TextField empAccNum;
    @FXML
    private TextField empAccBank;
    @FXML
    private TextField filesPath;
    @FXML
    private Button btnShowSec;
    @FXML
    private TextField secPath;

    /**
     * Initializes the controller class.
     */
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
        empTable.setOnMouseClicked((e) -> {
            if (empTable.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                empNew.setDisable(false);

                empDelete.setDisable(false);

                empEdite.setDisable(false);

                empAdd.setDisable(true);

                Employee selected = empTable.getSelectionModel().getSelectedItem();
                empId.setText(Integer.toString(selected.getId()));

                empName.setText(selected.getName());
                empRole.getSelectionModel().select(selected.getRole());

                empTele1.setText(selected.getTele1());
                empTele2.setText(selected.getTele2());

                empEmail.setText(selected.getEmail());
                empAdress.setText(selected.getAddress());
                empNationalId.setText(selected.getNationalId());
                empAccNum.setText(selected.getAccNum());
                empAccBank.setText(selected.getAccBank());
                empSalary.setText(selected.getSalary());

                shifts.getCheckModel().clearChecks();

                ObservableList<Sections> items1 = empSection.getItems();
                for (Sections a : items1) {
                    if (a.getName().equals(selected.getSection())) {
                        empSection.getSelectionModel().select(a);
                    }
                }
                try {
                    shifts.getCheckModel().clearChecks();
                    if (selected.getShifts() != null) {
                        String[] split = selected.getShifts().split("-");
                        ObservableList<String> items2 = shifts.getItems();
                        for (String a : split) {
                            for (String b : items2) {
                                if (b.split("-")[0].split(":")[1].replaceAll(" ", "").contains(a)) {
                                    shifts.getCheckModel().check(b);
                                }
                            }

                        }
                    }
                } catch (Exception ex) {
                    AlertDialogs.showErrors(ex);
                }
                selectedOne = selected;
                getSolfaFor(selected.getId());
            }
        });
        solfaTable.setOnMouseClicked((e) -> {
            if (solfaTable.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                EmployeeSolfa sel = solfaTable.getSelectionModel().getSelectedItem();
                solfaId.setText(Integer.toString(sel.getId()));
                solfaAmount.setText(sel.getAmount());
//                ObservableList<Accounts> items2 = solfaAcc.getItems();
//                for (Accounts a : items2) {
//                    if (a.getName().equals(sel.getAcc())) {
//                        solfaAcc.getSelectionModel().select(a);
//                    }
//                    
//                }
                solfaDate.setValue(LocalDate.parse(sel.getDate()));
            }
        });
    }

    private void clear() {
        try {
            getAutoNum();
            empNew.setDisable(true);

            empDelete.setDisable(true);

            empEdite.setDisable(true);

            empAdd.setDisable(false);

            empSection.getSelectionModel().clearSelection();
            empRole.getSelectionModel().clearSelection();
            empName.setText("");

            empTele1.setText("");
            empTele2.setText("");

            empEmail.setText("");
            empAdress.setText("");
            empNationalId.setText("");
            filesPath.setText("");
            empAccNum.setText("");
            empAccBank.setText("");
            empSalary.setText("");

            shifts.getCheckModel().clearChecks();
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }

    }

    private void intialColumn() {
        empTabSection.setCellValueFactory(new PropertyValueFactory<>("section"));

        empTabRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        empTabNationalId.setCellValueFactory(new PropertyValueFactory<>("nationalId"));

        empTabTele1.setCellValueFactory(new PropertyValueFactory<>("tele1"));

        empTabTele2.setCellValueFactory(new PropertyValueFactory<>("tele2"));

        empTabName.setCellValueFactory(new PropertyValueFactory<>("name"));

        empTabId.setCellValueFactory(new PropertyValueFactory<>("id"));

        solfaTabDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        solfaTabAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        solfaTabId.setCellValueFactory(new PropertyValueFactory<>("id"));

    }

    private void getAutoNum() throws Exception {
        empId.setText(Employee.getAutoNum());
    }

    private void getData() {
        try {
            empTable.setItems(Employee.getData());
            items = empTable.getItems();
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }
    ObservableList<Employee> items;

    private void fillCombo() throws Exception {
        ObservableList<String> items = FXCollections.observableArrayList();
        ObservableList<Shifts> data = Shifts.getData();
        for (Shifts a : data) {
            items.add("id: " + a.getId() + " - name: " + a.getName());
        }
        shifts.setItems(items);
        empRole.getItems().add("موظف");
        empRole.getItems().add("مبيعات");
        empRole.getItems().add("صيانة");
        empSection.setItems(Sections.getData());
        empSection.setConverter(new StringConverter<Sections>() {
            @Override
            public String toString(Sections patient) {
                return patient.getName();
            }

            @Override
            public Sections fromString(String string) {
                return null;
            }
        });
        empSection.setCellFactory(cell -> new ListCell<Sections>() {

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
            protected void updateItem(Sections person, boolean empty) {
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
        FilteredList<Employee> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter)
                    || pa.getSection().contains(lowerCaseFilter) || pa.getRole().contains(lowerCaseFilter));

        });

        SortedList<Employee> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empTable.comparatorProperty());
        empTable.setItems(sortedData);
    }

    @FXML
    private void empNew(ActionEvent event) {
        clear();
    }

    @FXML
    private void empDelete(ActionEvent event) {
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
                                    alert.setTitle("Deleting Employee");
                                    alert.setHeaderText("سيتم حذف الموظف ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        Employee em = new Employee();
                                        em.setId(Integer.parseInt(empId.getText()));
                                        em.Delete();
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
    private void empEdite(ActionEvent event) {

        ObservableList<String> selectedItems = shifts.getCheckModel().getCheckedItems();
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
                                    alert.setTitle("Editing Employee");
                                    alert.setHeaderText("سيتم تعديل الموظف ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {

                                        String shifts = "";
                                        if (selectedItems.size() > 0) {

                                            for (String a : selectedItems) {
                                                shifts += a.split("-")[0].split(":")[1].replaceAll(" ", "") + "-";
                                            }
                                            shifts = shifts.substring(0, shifts.length() - 1);
                                        }

                                        Employee em = new Employee();
                                        em.setId(Integer.parseInt(empId.getText()));
                                        em.setName(empName.getText());
                                        em.setTele1(empTele1.getText());
                                        em.setTele2(empTele2.getText());
                                        em.setEmail(empEmail.getText());
                                        em.setNationalId(empNationalId.getText());
                                        em.setAccNum(empAccNum.getText());
                                        em.setAccBank(empAccBank.getText());
                                        em.setAddress(empAdress.getText());
                                        em.setSalary(empSalary.getText());
                                        em.setSectionId(empSection.getSelectionModel().getSelectedItem().getId());
                                        em.setRole(empRole.getSelectionModel().getSelectedItem());
                                        em.setShifts(shifts);
                                        if (filesPath.getText().isEmpty() && filesPath.getText().length() == 0 && secPath.getText().isEmpty() && secPath.getText().length() == 0) {
                                            em.Edite();
                                        } else {
                                            if (!filesPath.getText().isEmpty() || filesPath.getText().length() != 0) {
                                                InputStream input = new FileInputStream(new File(filesPath.getText()));
                                                em.setNationalIdPhoto(input);

                                                String[] st = filesPath.getText().split(Pattern.quote("."));
                                                em.setNationalIdPhototExt(st[st.length - 1]);
                                            }
                                            if (!secPath.getText().isEmpty() || secPath.getText().length() != 0) {
                                                InputStream secInput = new FileInputStream(new File(secPath.getText()));
                                                em.setSecurityPhoto(secInput);

                                                String[] st2 = secPath.getText().split(Pattern.quote("."));
                                                em.setSecurityPhototExt(st2[st2.length - 1]);
                                            }
                                            em.EditeWithPhoto();
                                        }
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
    private void empAdd(ActionEvent event) {
        ObservableList<String> selectedItems = shifts.getCheckModel().getCheckedItems();

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
                                    String shifts = "";
                                    if (selectedItems.size() > 0) {

                                        for (String a : selectedItems) {
                                            shifts += a.split("-")[0].split(":")[1].replaceAll(" ", "") + "-";
                                        }
                                        shifts = shifts.substring(0, shifts.length() - 1);
                                    }
                                    Employee em = new Employee();
                                    em.setId(Integer.parseInt(empId.getText()));
                                    em.setName(empName.getText());
                                    em.setTele1(empTele1.getText());
                                    em.setTele2(empTele2.getText());
                                    em.setEmail(empEmail.getText());
                                    em.setNationalId(empNationalId.getText());
                                    em.setAccNum(empAccNum.getText());
                                    em.setAccBank(empAccBank.getText());
                                    em.setAddress(empAdress.getText());
                                    em.setSalary(empSalary.getText());
                                    em.setSectionId(empSection.getSelectionModel().getSelectedItem().getId());
                                    em.setRole(empRole.getSelectionModel().getSelectedItem());
                                    em.setShifts(shifts);
                                    if (filesPath.getText().isEmpty() || filesPath.getText().length() == 0 || secPath.getText().isEmpty() || secPath.getText().length() == 0) {
                                        em.Add();
                                    } else {
                                        if (filesPath.getText().isEmpty() || filesPath.getText().length() == 0) {
                                            InputStream input = new FileInputStream(new File(filesPath.getText()));
                                            em.setNationalIdPhoto(input);

                                            String[] st = filesPath.getText().split(Pattern.quote("."));
                                            em.setNationalIdPhototExt(st[st.length - 1]);
                                        }
                                        if (secPath.getText().isEmpty() || secPath.getText().length() == 0) {
                                            InputStream secInput = new FileInputStream(new File(secPath.getText()));
                                            em.setSecurityPhoto(secInput);

                                            String[] st2 = secPath.getText().split(Pattern.quote("."));
                                            em.setSecurityPhototExt(st2[st2.length - 1]);
                                        }
                                        em.AddWithPhoto();
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
    private void solfaNew(ActionEvent event) {
        clearSolfa();
    }

    private void clearSolfa() {
        try {
            solfaId.setText(EmployeeSolfa.getAutoNum());
            solfaAmount.setText("");
            solfaDate.setValue(null);
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void solfaDelete(ActionEvent event) {
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
                                    alert.setTitle("Deleting  Solfa");
                                    alert.setHeaderText("سيتم حذف السلفة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        EmployeeSolfa emps = new EmployeeSolfa();
                                        emps.setId(Integer.parseInt(solfaId.getText()));
                                        emps.setAmount(solfaAmount.getText());
                                        emps.setEmployee_id(selectedOne.getId());
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        emps.setDate(solfaDate.getValue().format(format));
                                        emps.Delete();
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

                clearSolfa();
                try {
                    solfaTable.setItems(EmployeeSolfa.getData(selectedOne.getId()));
                } catch (Exception ex) {
                    AlertDialogs.showErrors(ex);
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void solfaEdite(ActionEvent event) {
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
                                    alert.setTitle("Editing  Solfa");
                                    alert.setHeaderText("سيتم تعديل السلفة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        EmployeeSolfa emps = new EmployeeSolfa();
                                        emps.setId(Integer.parseInt(solfaId.getText()));
//                                        emps.setAccId(solfaAcc.getSelectionModel().getSelectedItem().getId());
                                        emps.setAmount(solfaAmount.getText());
                                        emps.setEmployee_id(selectedOne.getId());
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        emps.setDate(solfaDate.getValue().format(format));
                                        emps.Edite();
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

                clearSolfa();
                try {
                    solfaTable.setItems(EmployeeSolfa.getData(selectedOne.getId()));
                } catch (Exception ex) {
                    AlertDialogs.showErrors(ex);
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void solfaAdd(ActionEvent event) {
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
                                    EmployeeSolfa emps = new EmployeeSolfa();
                                    emps.setId(Integer.parseInt(solfaId.getText()));
//                                    emps.setAccId(solfaAcc.getSelectionModel().getSelectedItem().getId());
                                    emps.setAmount(solfaAmount.getText());
                                    emps.setEmployee_id(selectedOne.getId());
                                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    emps.setDate(solfaDate.getValue().format(format));
                                    emps.Add();
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

                clearSolfa();
                try {
                    solfaTable.setItems(EmployeeSolfa.getData(selectedOne.getId()));
                } catch (Exception ex) {
                    AlertDialogs.showErrors(ex);
                }
                super.succeeded();
            }
        };
        service.start();
    }

    private void getSolfaFor(int id) {
//        try {
//            solfaAcc.setItems(Accounts.getData());
//            solfaAcc.setConverter(new StringConverter<Accounts>() {
//                @Override
//                public String toString(Accounts patient) {
//                    return patient.getName();
//                }
//                
//                @Override
//                public Accounts fromString(String string) {
//                    return null;
//                }
//            });
//            solfaAcc.setCellFactory(cell -> new ListCell<Accounts>() {
//                
//                GridPane gridPane = new GridPane();
//                Label lblid = new Label();
//                Label lblName = new Label();
//                
//                {
//                    gridPane.getColumnConstraints().addAll(
//                            new ColumnConstraints(100, 100, 100),
//                            new ColumnConstraints(100, 100, 100)
//                    );
//                    
//                    gridPane.add(lblid, 0, 1);
//                    gridPane.add(lblName, 1, 1);
//                    
//                }
//                
//                @Override
//                protected void updateItem(Accounts person, boolean empty) {
//                    super.updateItem(person, empty);
//                    
//                    if (!empty && person != null) {
//                        
//                        lblid.setText("م: " + Integer.toString(person.getId()));
//                        lblName.setText("الاسم: " + person.getName());
//                        
//                        setGraphic(gridPane);
//                    } else {
//                        setGraphic(null);
//                    }
//                }
//            });
//            solfaTable.setItems(EmployeeSolfa.getData(id));
//            solfaId.setText(EmployeeSolfa.getAutoNum());
//        } catch (Exception ex) {
//            AlertDialogs.showErrors(ex);
//        }
    }

    @FXML
    private void showFile(ActionEvent event) {
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
                                    if (empTable.getSelectionModel().getSelectedIndex() == -1) {
                                        AlertDialogs.showError("اختار العميل اولا");
                                    } else {
                                        Employee em = new Employee();
                                        em.setId(empTable.getSelectionModel().getSelectedItem().getId());
                                        em.showNationalPhoto();
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
    private void attachFile(MouseEvent event) {
        FileChooser fil_chooser = new FileChooser();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fil_chooser.showOpenDialog(st);

        if (file != null) {
            filesPath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void showSecFile(ActionEvent event) {
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
                                    if (empTable.getSelectionModel().getSelectedIndex() == -1) {
                                        AlertDialogs.showError("اختار العميل اولا");
                                    } else {
                                        Employee em = new Employee();
                                        em.setId(empTable.getSelectionModel().getSelectedItem().getId());
                                        em.showSecurityPhoto();
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
    private void attachSecFile(MouseEvent event) {
        FileChooser fil_chooser = new FileChooser();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fil_chooser.showOpenDialog(st);

        if (file != null) {
            secPath.setText(file.getAbsolutePath());
        }
    }

}
