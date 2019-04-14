package org.familly.fx.adressbook.first;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.familly.fx.adressbook.first.controller.BirthdayStatisticsController;
import org.familly.fx.adressbook.first.controller.PersonEditDialogController;
import org.familly.fx.adressbook.first.controller.PersonOverviewController;
import org.familly.fx.adressbook.first.controller.RootLayoutController;
import org.familly.fx.adressbook.first.model.Person;
import org.familly.fx.adressbook.first.model.PersonListWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class MainApp extends Application {
    private static final Logger logger = Logger.getLogger(MainApp.class.getSimpleName());

    /**
     * 연락처에 대한 observable 리스트
     */
    private ObservableList<Person> observablePersonList = FXCollections.observableArrayList();

    private Stage primaryStage;
    private BorderPane rootLayout;

    public MainApp() {

        logger.info("MainApp 생성");
        //샘플 데이터를 추가한다.
        observablePersonList.add(new Person("디질", "영호"));
        observablePersonList.add(new Person("Hans", "Mueller"));
        observablePersonList.add(new Person("최순", "문덕"));
        observablePersonList.add(new Person("갑을", "병정"));
        observablePersonList.add(new Person("자축", "인묘"));
        observablePersonList.add(new Person("진사", "오미"));
        observablePersonList.add(new Person("신유", "술해"));
        observablePersonList.add(new Person("무기", "경신"));
        observablePersonList.add(new Person("임계", "자축"));
        observablePersonList.add(new Person("성웅", "이순신"));
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("주소록");

        //애플리케이션 아이콘을 설정한다.
        String path = "images/address_book_32.png";
        URL imageURL = MainApp.class.getClassLoader().getResource(path);


        this.primaryStage.getIcons().add(new Image(String.valueOf(imageURL)));


        initRootLayout();

        showPersonOverview();


    }

    ////////////////////////
    //사용자 정의 메소드
    ///////////////////////

    /**
     * 생일 통계를 보여주기 위해 다이얼로그를 연다.
     */
    public void showBirthdayStatistics() {
        logger.info("start");
        try {
            //FXML 파일을 불러와서 팝업의 새로운  Stage를 만든다.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();

            //애플리케이션 아이콘을 설정한다.
            String path = "images/ico_statistic.png";
            URL imageURL = MainApp.class.getClassLoader().getResource(path);


            dialogStage.getIcons().add(new Image(String.valueOf(imageURL)));

            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //연락처를 컨트롤러에 설정한다.
            BirthdayStatisticsController controller = loader.getController();
            controller.setBirthdayInPersonDate(observablePersonList);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


        logger.info("end");

    }

    /**
     * 지정한 파일로 부터 연락처 데이터를 가져온다.
     * 현재 연락처 데이터로 대체합니다.
     *
     * @param file
     */
    public void loadPersonDataFromFile(File file) {
        logger.info("파일로 부터 주소록 정보를 읽어옵니다.");

        try {

            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            Unmarshaller um = context.createUnmarshaller();

            //파일로 부터 XML을 읽은 다음 역마샬링한다.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            observablePersonList.clear();
            observablePersonList.addAll(wrapper.getPersonList());

            //파일 경로를 레지스트리에 저장한다.
            setPersonFilePath(file);

        } catch (JAXBException e) {//예외잡기
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();

            //e.printStackTrace();
        }
    }

    /**
     * 현재 연락처 데이터를 지정한 파일에 저장한다.
     *
     * @param file
     */
    public void savePersonDataToFile(File file) {
        logger.info("");

        try {

            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //연락처 데이터를 감싼다.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersonList(observablePersonList);

            //마샬링후 XML을 파일에 저장한다.
            m.marshal(wrapper, file);

            //파일 경로를 레지스트리에 저장한다.
            setPersonFilePath(file);

        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
            //e.printStackTrace();
        }
    }

    /**
     * 연락처 파일 환경설정을 반환한다.
     * 즉 파일은 마지막으로 열린 것이고,환경설정은 OS 특정 레지스트리로 부터 읽는다.
     * 만일 환경설정(Preferences)을 찾지 못하면 null을 반환한다.
     *
     * @return
     */
    public File getPersonFilePath() {
        logger.info("");

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        //값이 없을경우 디폴트로 null 반환한다.
        String filePath = prefs.get("filePath", null);
        logger.info("filePath > " + filePath);

        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * 현재 불러온 파일의 경로를 설정한다. 이 경로는 OS 특정 레지스트리에 저장된다.
     *
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file) {

        logger.info("");

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {

            logger.info("file.getPath > " + file.getPath());
            prefs.put("filePath", file.getPath());
            //Stage 타이틀을 업데이트 한다.
            logger.info("file.getName > " + file.getName());
            primaryStage.setTitle("주소록 - " + file.getName());

        } else {//저장된 파일이 없다?

            prefs.remove("filePath");
            //Stage 타이틀을 업데이트한다.
            primaryStage.setTitle("주소록");

        }

    }

    /**
     * person의 자세한 정보를 변경하기 위해 다이얼로그를 연다.
     * 만일 사용자가 OK를 클릭하면 주어진 person에 내용을 저장한 후
     * true를 반환한다.
     *
     * @param person - the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        logger.info("");

        try {
            //fxml 파일을 로드하고 나서 새로운 스테이지를 만든다.
            FXMLLoader personEditDialogFXMLLoader = new FXMLLoader();
            personEditDialogFXMLLoader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) personEditDialogFXMLLoader.load();

            //다이얼로그 스테이지를 만든다.
            Stage personEditDialogStage = new Stage();

            //애플리케이션 아이콘을 설정한다.
            String path = "images/address_book_32.png";
            URL imageURL = MainApp.class.getClassLoader().getResource(path);
            personEditDialogStage.getIcons().add(new Image(String.valueOf(imageURL)));

            //
            personEditDialogStage.setTitle("Edit Person");
            personEditDialogStage.initModality(Modality.WINDOW_MODAL);
            personEditDialogStage.initOwner(primaryStage);

            //scene
            Scene scene = new Scene(page);
            personEditDialogStage.setScene(scene);

            //person을 컨트롤러에 설정한다.
            PersonEditDialogController controller = personEditDialogFXMLLoader.getController();
            controller.setDialogStage(personEditDialogStage);
            controller.setPerson(person);

            //다이얼로그를 보여주고 사용자가 닫을때까지 기다린다.
            personEditDialogStage.showAndWait();

            return controller.isOkClicked();


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 주소록에 대한 observable 리스트를 반환한다.
     */
    public ObservableList<Person> getObservablePersonList() {
        logger.info("");
        return observablePersonList;
    }

    /**
     * 1.Root 레이아웃을 초기화 합니다.
     * 2.마지막으로 열었던 연락처 파일을 가져옵니다.
     */
    public void initRootLayout() {

        try {

            logger.info("");

            //fxml 파일을 자바코드화해서 메모리에 로드한다.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));

            rootLayout = (BorderPane) loader.load();

            //상위 레이아웃을 포함하는 scene을 보여준다.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            //추가 상단 메뉴바 제어를 위한 MainApp 접근권한을 컨트롤러에게 위임한다.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);


            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO:마지막으로 열었던 연락처 파일을 가져온다.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    /**
     * 상위 레이아웃안에 주소록을 보여준다.
     */
    public void showPersonOverview() {
        try {
            logger.info("");

            //fxml을 자바코드화 해서 메모리에 올리는 작업
            FXMLLoader fxLoader = new FXMLLoader();

            fxLoader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));

            //inflater fxml 참조
            AnchorPane apPersonOverview = (AnchorPane) fxLoader.load();

            //주소록을 상위 레이아웃 가운데로 설정한다.
            rootLayout.setCenter(apPersonOverview);

            //메인 애플리케이션 컨트롤로를 이용할 수있게 한다.
            PersonOverviewController controller = fxLoader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 메인스테지를 반환한다.
     */
    public Stage getPrimaryStage() {
        logger.info("");


        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
