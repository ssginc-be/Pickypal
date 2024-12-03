package branch;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;

public class SupplierManageController {

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, Boolean> selectColumn;

    @FXML
    private TableColumn<Order, String> supplierIdColumn;

    @FXML
    private TableColumn<Order, String> supplierNameColumn;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private TableColumn<Order, String> addressColumn;

    @FXML
    private TableColumn<Order,String> telColumn;

    @FXML
    private TableColumn<Order, String> lastModifiedAtColumn;

    @FXML
    private ObservableList<Order> orders = FXCollections.observableArrayList();

    public void initialize() {
        // CheckBox 컬럼 설정
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);

        // 테이블 자체를 수정 가능하도록 설정
        orderTable.setEditable(true);

        // 다른 컬럼 데이터 바인딩
        supplierIdColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty());
        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        telColumn.setCellValueFactory(cellData -> cellData.getValue().telProperty());
        lastModifiedAtColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());


        // 데이터 초기화
        orders = FXCollections.observableArrayList(
                new Order(false, "ORD001", "PRD001", "Active", "강남", "2024-10-13","2024-10-13"),
                new Order(false, "ORD002", "PRD002", "Active", "구리", "2024-10-15", "2024-10-15"),
                new Order(false, "ORD003", "PRD003", "Inactive", "천안", "2024-10-19", "2024-10-19")
        );

        orderTable.setItems(orders);
    }


    public static class Order {
        @FXML
        private final SimpleBooleanProperty selected;
        private final SimpleStringProperty supplierId;
        private final SimpleStringProperty supplierName;
        private final SimpleStringProperty status;
        private final SimpleStringProperty address;
        private final SimpleStringProperty tel;
        private final SimpleStringProperty lastModifiedAt;

        public Order(boolean selected, String supplierId, String supplierName, String status, String address, String tel, String lastModifiedAt) {
            this.selected = new SimpleBooleanProperty(selected);
            this.supplierId = new SimpleStringProperty(supplierId);
            this.supplierName = new SimpleStringProperty(supplierName);
            this.status = new SimpleStringProperty(status);
            this.address = new SimpleStringProperty(address);
            this.tel = new SimpleStringProperty(tel);
            this.lastModifiedAt = new SimpleStringProperty(lastModifiedAt);
        }

        public ObservableValue<Boolean> selectedProperty() {
            return selected;
        }

        public ObservableValue<String> supplierIdProperty() {
            return supplierId;
        }

        public ObservableValue<String> supplierNameProperty() {
            return supplierName;
        }

        public ObservableValue<String> statusProperty() {
            return status;
        }

        public ObservableValue<String> addressProperty() {
            return address;
        }

        public ObservableValue<String> telProperty() {
            return tel;
        }

        public ObservableValue<String> lastModifiedAtProperty() {
            return lastModifiedAt;
        }
    }

    @FXML
    private VBox changePane;

    public void readStockButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/branch/BranchStock.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incomingRegistButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/HeadIncomingManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outgoingRegistButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/OutgoingRegist.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void branchManageButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/branchManage2.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supplierManageButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SupplierManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

