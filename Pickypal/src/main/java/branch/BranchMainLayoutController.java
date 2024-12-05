package branch;

import common.BranchNavController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class BranchMainLayoutController {

    @FXML
    private VBox changePane1; // 화면 교체 영역

    @FXML
    private BranchNavController BranchNavController; // BranchNav.fxml의 컨트롤러 직접 참조

    @FXML
    public void initialize() {
        // NavController 설정
        if (BranchNavController != null) {
            BranchNavController.setChangePane1(changePane1);
            System.out.println("BranchNavController successfully connected to changePane1.");
        } else {
            System.err.println("BranchNavController is null. Please check fx:include setup.");
        }

        // 초기 화면 설정: changePane1에 본사 재고 화면 로드
        loadInitialContent();
    }

    private void loadInitialContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/branch/BranchStock.fxml"));
            Parent initialContent = loader.load();
            changePane1.getChildren().clear(); // 초기화
            changePane1.getChildren().add(initialContent); // 본사 재고 화면 추가
        } catch (Exception e) {
            System.err.println("Failed to load initial content.");
            e.printStackTrace();
        }
    }
}
