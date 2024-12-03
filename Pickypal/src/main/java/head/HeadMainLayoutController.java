package head;

import common.HeadNavController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class HeadMainLayoutController {

    @FXML
    private VBox changePane; // 화면 교체 영역

    @FXML
    private HeadNavController headNavController; // HeadNav.fxml의 컨트롤러 직접 참조

    @FXML
    public void initialize() {
        // NavController 설정
        if (headNavController != null) {
            headNavController.setChangePane(changePane);
            System.out.println("NavController successfully connected to changePane.");
        } else {
            System.err.println("NavController is null. Please check fx:include setup.");
        }

        // 초기 화면 설정: changePane에 본사 재고 화면 로드
        loadInitialContent();
    }

    private void loadInitialContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/HeadStock.fxml"));
            Parent initialContent = loader.load();
            changePane.getChildren().clear(); // 초기화
            changePane.getChildren().add(initialContent); // 본사 재고 화면 추가
        } catch (Exception e) {
            System.err.println("Failed to load initial content.");
            e.printStackTrace();
        }
    }
}
