package branch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BranchMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/branchManage.fxml"));
        // fxml/testFX.fxml을 불러온다.
        Parent root = loader.load();

        BranchManageController controller = loader.getController();

        // 데이터 추가
        controller.addOrder("ORD004", "PRD004", "서울", "2024-11-01", "2024-11-05");
        controller.addOrder("ORD005", "PRD005", "부산", "2024-11-02", "2024-11-06");

        // 기본 타이틀바 제거
//        primaryStage.initStyle(StageStyle.UNDECORATED);

        // GUI에 보일 프로그램 제목을 설정한다.
        primaryStage.setTitle("Pickypal");

        // 화면을 설정한다. 두번째 파라미터는 프로그램의 Width, 세번째는 Height를 설정한다
        primaryStage.setScene(new Scene(root, 1024, 768));

        // 화면을 보여주는 작업을 실행한다
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}