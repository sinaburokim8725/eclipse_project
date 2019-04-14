package org.familly.fx.adressbook.first.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.familly.fx.adressbook.first.MainApp;
import org.familly.fx.adressbook.first.model.Person;
import org.familly.fx.adressbook.first.util.DateUtil;

import java.util.logging.Logger;

public class PersonOverviewController {
    private static final Logger logger = Logger.getLogger(PersonOverviewController.class.getSimpleName());

    //FXML 의 각 컴포넌트의 fx:id 명과 일치해야 참조할수 있다.
    @FXML
    private TableView<Person> mPersonTableView;

    @FXML
    private TableColumn<Person, String> mFirstNameTablecolumn;

    @FXML
    private TableColumn<Person, String> mLastNameTablecolumn;

    @FXML
    private Label mFirstNameLabel;

    @FXML
    private Label mLastNameLabel;

    @FXML
    private Label mStreetLabel;

    @FXML
    private Label mCityLabel;

    @FXML
    private Label mPostalCodeLabel;

    @FXML
    private Label mBirthdayLabel;

    //메인app 참조
    private MainApp mainApp;

    /**
     * 생성자
     * initialize() 메서드 이전에 호출된다.
     */
    public PersonOverviewController() {
        logger.info("");
    }

    /**
     * 연락처의 자세한 정보를 보여주기 위해 모든 텍스트 필드를 채운다.
     * 만일 Person 객체가 null 이면 모든 텍스트 필드가 지워진다.
     */
    private void showPersonDetails(Person person) {

        logger.fine("person > " + ((person != null)? true : false));

        if (person != null) {
            //성
            mFirstNameLabel.setText(person.getFirstName());
            //이름
            mLastNameLabel.setText(person.getLastName());
            //주소
            mStreetLabel.setText(person.getStreet());
            //우편코드
            mPostalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            //도시명
            mCityLabel.setText(person.getCity());

            //Todo: 생일은 String으로 변환해야한다.
            mBirthdayLabel.setText(DateUtil.format(person.getBirthday()));

        } else {

            //person이 null 일 경우
            //성
            mFirstNameLabel.setText("");
            //이름
            mLastNameLabel.setText("");
            //주소
            mStreetLabel.setText("");
            //우편코드
            mPostalCodeLabel.setText("");
            //도시명
            mCityLabel.setText("");
            //생일
            mBirthdayLabel.setText("");
        }
    }


    /**
     * 컨트롤러 클래스를 초기화 한다.
     * fxml 파일이 로드되고 나서 자동으로 호출된다.
     */
    @FXML
    private void initialize() {
        logger.info("");
        //주소록 테이블의 두열을 초기화 한다.
        mFirstNameTablecolumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        mLastNameTablecolumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        //연락처 정보를 지운다.
        showPersonDetails(null);

        //선택을 감지하고 그 때마다 연락처의 자세한 정보를 보여 준다.
        mPersonTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * 참조를 다시 유지하기 위해 메인 애플리케이션을 호출한다.
     */
    public void setMainApp(MainApp mainApp) {
        logger.info("");
        this.mainApp = mainApp;
        //테이블에 obserable 리스트 데이터를 추가한다.
        mPersonTableView.setItems(mainApp.getObservablePersonList());

    }

    /**
     * 사용자가 new 버튼을 클릭할때 호출된다.
     * 새로운 연락처 정보를 넣기위해 다이얼로그를 연다.
     */
    @FXML
    private void handleNewPerson() {
        logger.info("");
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getObservablePersonList().add(tempPerson);
        }
    }

    /**
     * 사용자가 edit 버튼을 클릭할때 호출된다.
     * 선택한 연락처 정보를 변경하기 위해 다이얼로그를 연다.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = mPersonTableView.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }
        } else {
            //아무것도 선택하지 않았다.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selected");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table");

            alert.showAndWait();
        }
    }

    /**
     * 사용자가 삭제 버튼을 클릭하면 호출된다.
     * 메소드 접급범위가 public 이거나  @FXML를 사용하는 방법이 있다.
     */
    @FXML
    private void handleDeletePerson() {

        int selectedIndex = mPersonTableView.getSelectionModel().getSelectedIndex();
        logger.info("selectedIndex > " + selectedIndex);

        if (selectedIndex < 0) {
            //아무것도 선택하지 않았을 경우
            Alert alertBox = new Alert(Alert.AlertType.WARNING);
            alertBox.initOwner(mainApp.getPrimaryStage());
            alertBox.setTitle("No Selection");
            alertBox.setHeaderText("No Person Selected");
            alertBox.setContentText("Please Select a Person in the table");

            alertBox.showAndWait();
        } else {

            mPersonTableView.getItems().remove(selectedIndex);
        }

    }
}
