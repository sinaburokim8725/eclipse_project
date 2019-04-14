package org.familly.fx.adressbook.first.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.familly.fx.adressbook.first.MainApp;

import java.io.File;
import java.util.logging.Logger;

/**
 * Root 레이아웃에 대한 컨트롤러이다.
 * Root 레이아웃은 메뉴바와 JavaFx 엘리먼트가 들어갈 공간을 포함한
 * 기본적인 레이아웃을 제공한다.
 *
 * @author Kim Young Ho
 */
public class RootLayoutController {

    private static final Logger logger = Logger.getLogger(RootLayoutController.class.getSimpleName());

    //메인 애플리리케이션 참조
    private MainApp mainApp;

    /**
     * 참조를 다시 유지하기위해 메인 애플리케이션이 호출한다.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        logger.info("");
        logger.info("this.mainApp 널인가? " + (this.mainApp != null ? "ok" : null));
        this.mainApp = mainApp;
    }

    /**
     * 생일 차트를 연다.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        logger.info("start");

        mainApp.showBirthdayStatistics();

        logger.info("end");
    }

    /**
     * 비어있는 주소록을 만든다.
     */
    @FXML
    private void handleNew() {
        logger.info("");
        mainApp.getObservablePersonList().clear();
        mainApp.setPersonFilePath(null);
    }

    /**
     * FileChooser 를 열어서 사용자가 가져올 주소록을 선택하게 한다.
     */
    @FXML
    private void handleOpen() {
        logger.info("");
        //파일선택 객체 생성
        FileChooser fileChooser = new FileChooser();

        //학장자 필터 객체 생성
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");

        //파일선택 객체의 확장자 필터 메서드로 확장자 필터 객체를 추가한다.
        fileChooser.getExtensionFilters().add(extFilter);

        //Save File Dialog를 보여준다.
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    /**
     * 현재 열려있는 파일에 저장한다.
     * 만일 열려있는 파일이 없으면 "save as" 다이얼로그를 보여준다.
     */
    @FXML
    private void handleSave() {
        logger.info("");
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);

        } else {
            handleSaveAs();
        }
    }

    /**
     * FileChooser를 열어서 사용자가 저장할 파일을 산택하게 한다.
     */
    @FXML
    private void handleSaveAs() {
        logger.info("");
        FileChooser fileChooser = new FileChooser();

        //학장자 필터를 설정한다.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        //Save File Dialog를 보여준다.
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            //정확한 확장자를 가져야 한다.
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");

            }
            mainApp.savePersonDataToFile(file);
        }
    }

    /**
     * About 다이얼로그를 보여준다.
     */
    @FXML
    private void handleAbout() {
        logger.info("");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("주소록");
        alert.setHeaderText("About");
        alert.setContentText("만든이: 김영호 \n홈페이지: https://sinaburokim.tistory.com/");

        alert.showAndWait();
    }

    /**
     * 애플리케이션을 닫는다.
     */
    @FXML
    private void handleExit() {
        logger.info("");
        System.exit(0);
    }
}
