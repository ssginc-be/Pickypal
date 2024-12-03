package common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class HeadNavController {

    @FXML
    private VBox changePane; // 메인 화면 교체 영역

    // changePane 설정 메서드
    public void setChangePane(VBox changePane) {
        this.changePane = changePane;
    }

    @FXML
    public void moveToHeadStock() {
        loadContent("/fxml/head/HeadStock.fxml");
    }

    @FXML
    public void moveToIncomingManage() {
        loadContent("/fxml/head/HeadIncomingManage.fxml");
    }

    @FXML
    public void moveToOutgoingManage() {
        loadContent("/fxml/head/HeadOutgoingManage.fxml");
    }

    @FXML
    public void moveToBranchManage() {
        loadContent("/fxml/head/BranchManage.fxml");
    }

    @FXML
    public void moveToSupplierManage() {
        loadContent("/fxml/head/SupplierManage.fxml");
    }

    // 공통 화면 로드 메서드
    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox newContent = loader.load();

            // changePane의 내용을 교체
            if (changePane != null) {
                changePane.getChildren().clear();
                changePane.getChildren().add(newContent);
            } else {
                System.out.println("changePane is null. Please check initialization.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
