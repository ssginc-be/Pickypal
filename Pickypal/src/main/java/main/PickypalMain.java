package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PickypalMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 폰트 적용
        Font.loadFont(getClass().getResourceAsStream("SUIT-Medium.ttf"), 14);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/HeadStock.fxml"));
        // fxml/auth/Login.fxml을 불러온다.
        Parent root = loader.load();

        // GUI에 보일 프로그램 제목을 설정한다.
        primaryStage.setTitle("Pickypal");

        // fxml 파일에 지정된 화면 크기에 따른 UI 보이게.
        primaryStage.setScene(new Scene(root));

        // 화면을 보여주는 작업 실행.
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}