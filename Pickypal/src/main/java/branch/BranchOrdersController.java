package branch;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BranchOrdersController {

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, Boolean> selectColumn;

    @FXML
    private TableColumn<Order, String> ordersIDColumn;

    @FXML
    private TableColumn<Order, String> productIDColumn;

    @FXML
    private TableColumn<Order, String> productNameColumn;

    @FXML
    private TableColumn<Order, Integer> ordersAmountColumn;

    @FXML
    private TableColumn<Order, Integer> priceColumn;

    @FXML
    private TableColumn<Order, String> ordersStatusColumn;

    @FXML
    private TableColumn<Order, String> ordersDateColumn;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    public void initialize() {
        // CheckBox 컬럼 설정
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);

        // 테이블 자체를 수정 가능하도록 설정
        ordersTable.setEditable(true);

        // 다른 컬럼 데이터 바인딩
        ordersIDColumn.setCellValueFactory(cellData -> cellData.getValue().ordersIDProperty());
        productIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        ordersAmountColumn.setCellValueFactory(cellData -> cellData.getValue().ordersAmountProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        ordersStatusColumn.setCellValueFactory(cellData -> cellData.getValue().ordersStatusProperty());
        ordersDateColumn.setCellValueFactory(cellData -> cellData.getValue().ordersDateProperty());
        // 데이터 초기화
        orders = FXCollections.observableArrayList(
                new Order(false, "ORD001", "PRD001", "쿠앤크", 10, 1000, "출고 대기", LocalDateTime.now()),
                new Order(false, "ORD002", "PRD002", "딸기바", 20, 2000,"출고 대기", LocalDateTime.now()),
                new Order(false, "ORD003", "PRD003", "초코바", 10, 1500,"출고 대기", LocalDateTime.now())
        );

        ordersTable.setItems(orders);
    }

    // 다른 클래스에서 호출 가능한 데이터 추가 메서드
    public void addOrder(String ordersID, String productID, String productName, Integer ordersAmount, Integer price, String ordersStatus, LocalDateTime ordersDate ) {
        // 새 Order 객체를 리스트에 추가
        orders.add(new Order(false, ordersID, productID, productName, ordersAmount, price, ordersStatus, ordersDate));
    }

    public static class Order {
        @FXML
        private final SimpleBooleanProperty selected;
        private final SimpleStringProperty ordersID;
        private final SimpleStringProperty productID;
        private final SimpleStringProperty productName;
        private final SimpleIntegerProperty ordersAmount;
        private final SimpleIntegerProperty price;
        private final SimpleStringProperty ordersStatus;
        private final SimpleStringProperty ordersDate;

        public String getOrdersID() {
            return ordersID.get();
        }

        // DateTimeFormatter 정의 (사용자 정의 형식)
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public Order(boolean selected, String ordersID, String productID, String productName, Integer ordersAmount, Integer price, String ordersStatus, LocalDateTime ordersDate ) {
            this.selected = new SimpleBooleanProperty(selected);
            this.ordersID = new SimpleStringProperty(ordersID);
            this.productID = new SimpleStringProperty(productID);
            this.productName = new SimpleStringProperty(productName);
            this.ordersAmount = new SimpleIntegerProperty(ordersAmount);
            this.price = new SimpleIntegerProperty(price);
            this.ordersStatus = new SimpleStringProperty(ordersStatus);
            this.ordersDate = new SimpleStringProperty(ordersDate.format(FORMATTER));
        }

        public ObservableValue<Boolean> selectedProperty() {
            return selected;
        }

        public ObservableValue<String> ordersIDProperty() {
            return ordersID;
        }

        public ObservableValue<String> productIDProperty() {
            return productID;
        }

        public ObservableValue<String> productNameProperty() {
            return productName;
        }

        public SimpleIntegerProperty ordersAmountProperty() {
            return ordersAmount;
        }

        public SimpleIntegerProperty priceProperty() {
            return price;
        }

        public SimpleStringProperty ordersStatusProperty() {return ordersStatus; }

        public SimpleStringProperty ordersDateProperty() {return ordersDate; }
    }

    @FXML
    private VBox changePane;

    @FXML
    public void deleteSelectedOrder() {
        // 선택된 주문을 가져옵니다.
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            // 주문이 선택되었다면, 해당 주문의 ordersID를 이용해 삭제
            deleteOrder(selectedOrder.getOrdersID());
        } else {
            // 선택된 항목이 없을 때 처리
            System.out.println("삭제할 주문을 선택하세요.");
        }
    }

    public void deleteOrder(String ordersID) {
        try {
            // 선택된 주문을 찾아서 삭제
            Order orderToDelete = null;

            // orders 리스트에서 ordersID에 해당하는 주문을 찾음
            for (Order order : orders) {
                if (order.getOrdersID().equals(ordersID)) {
                    orderToDelete = order;
                    break;
                }
            }

            // 해당 주문이 있으면 삭제
            if (orderToDelete != null) {
                orders.remove(orderToDelete); // ObservableList에서 제거
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void search(String ordersID ) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void enrollOrder() {
        try {
            // FXML 파일을 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/branch/BranchOrdersRegist.fxml"));
            VBox newContent = loader.load();

            // 새로운 Stage 생성 (팝업용 창)
            Stage stage = new Stage();

            // 로드된 FXML을 새로운 Scene에 설정
            Scene scene = new Scene(newContent);
            stage.setScene(scene);

            // 팝업 창에 타이틀 설정 (필요에 따라 설정)
            stage.setTitle("주문 등록");

            // 팝업 창이 다른 창을 차단하도록 설정 (모달 설정)
            stage.initModality(Modality.APPLICATION_MODAL);

            // 팝업 창을 보여주기
            stage.showAndWait();  // showAndWait()를 사용하면 팝업이 닫힐 때까지 기다림
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
