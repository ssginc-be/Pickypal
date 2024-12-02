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

public class BranchManageController {

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, Boolean> selectColumn;

    @FXML
    private TableColumn<Order, String> branchIdColumn;

    @FXML
    private TableColumn<Order, String> branchNameColumn;

    @FXML
    private TableColumn<Order, String> addressColumn;

    @FXML
    private TableColumn<Order, String> createAtColumn;

    @FXML
    private TableColumn<Order, String> updatedAtColumn;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    public void initialize() {
        // CheckBox 컬럼 설정
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);

        // 테이블 자체를 수정 가능하도록 설정
        orderTable.setEditable(true);

        // 다른 컬럼 데이터 바인딩
        branchIdColumn.setCellValueFactory(cellData -> cellData.getValue().branchIdProperty());
        branchNameColumn.setCellValueFactory(cellData -> cellData.getValue().branchNameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        createAtColumn.setCellValueFactory(cellData -> cellData.getValue().createAtProperty());
        updatedAtColumn.setCellValueFactory(cellData -> cellData.getValue().updatedAtProperty());

        // 데이터 초기화
        orders = FXCollections.observableArrayList(
                new Order(false, "ORD001", "PRD001", "강남", "2024-10-11", "2024-10-13"),
                new Order(false, "ORD002", "PRD002", "구리", "2024-10-12", "2024-10-15"),
                new Order(false, "ORD003", "PRD003", "천안", "2024-10-13", "2024-10-19")
        );

        orderTable.setItems(orders);
    }

    // 다른 클래스에서 호출 가능한 데이터 추가 메서드
    public void addOrder(String branchId, String branchName, String address, String createAt, String updatedAt) {
        // 새 Order 객체를 리스트에 추가
        orders.add(new Order(false, branchId, branchName, address, createAt, updatedAt));
    }

    public static class Order {
        @FXML
        private final SimpleBooleanProperty selected;
        private final SimpleStringProperty branchId;
        private final SimpleStringProperty branchName;
        private final SimpleStringProperty address;
        private final SimpleStringProperty createAt;
        private final SimpleStringProperty updatedAt;

        public Order(boolean selected, String branchId, String branchName, String address, String createAt, String updateAt) {
            this.selected = new SimpleBooleanProperty(selected);
            this.branchId = new SimpleStringProperty(branchId);
            this.branchName = new SimpleStringProperty(branchName);
            this.address = new SimpleStringProperty(address);
            this.createAt = new SimpleStringProperty(createAt);
            this.updatedAt = new SimpleStringProperty(updateAt);
        }

        public ObservableValue<Boolean> selectedProperty() {
            return selected;
        }

        public ObservableValue<String> branchIdProperty() {
            return branchId;
        }

        public ObservableValue<String> branchNameProperty() {
            return branchName;
        }

        public ObservableValue<String> addressProperty() {
            return address;
        }

        public ObservableValue<String> createAtProperty() {
            return createAt;
        }

        public ObservableValue<String> updatedAtProperty() {
            return updatedAt;
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
