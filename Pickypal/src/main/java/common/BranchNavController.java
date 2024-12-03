package common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class BranchNavController {

    @FXML
    private VBox changePane1; // 메인 화면 교체 영역

    // changePane1 설정 메서드
    public void setChangePane1(VBox changePane1) {
        this.changePane1 = changePane1;
    }

    @FXML
    public void moveToBranchStock() {
        loadContent("/fxml/branch/BranchStock.fxml");
    }

    @FXML
    public void moveToBranchIncomingMange() {
        loadContent("/fxml/branch/BranchIncomingManage.fxml");
    }

    @FXML
    public void moveToBranchOrdersManage() {
        loadContent("/fxml/branch/BranchOrdersManage.fxml");
    }

    // 공통 화면 로드 메서드
    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox newContent = loader.load();

            // changePane1의 내용을 교체
            if (changePane1 != null) {
                changePane1.getChildren().clear();
                changePane1.getChildren().add(newContent);
            } else {
                System.out.println("changePane1 is null. Please check initialization.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
