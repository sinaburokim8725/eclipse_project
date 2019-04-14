package org.familly.fx.adressbook.first.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.familly.fx.adressbook.first.model.Person;
import org.familly.fx.adressbook.first.util.DateUtil;

import java.util.logging.Logger;

/**
 * 연락처 정보를 변경하는 다이얼로그
 *
 * @author Kim Young Ho
 */
public class PersonEditDialogController {
    private static final Logger logger = Logger.getLogger(PersonEditDialogController.class.getSimpleName());

    @FXML
    private TextField mFirstNameTextField;
    @FXML
    private TextField mLastNameTextField;
    @FXML
    private TextField mStreetTextField;
    @FXML
    private TextField mPostalCodeTextField;
    @FXML
    private TextField mCityTextField;
    @FXML
    private TextField mBirthdayTextField;

    private Stage mDialogStage;
    private Person person;
    private boolean okClicked = false;

    /**
     * 컨트롤러 클래스를 초기화 한다.
     * 이 메서드는 fxml 파일이 로드된 후 자동으로 호출된다.
     */
    @FXML
    private void initialize() {
        logger.info("");

    }

    /**
     * 이 다이얼로그의 스테이지를 설정한다.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        logger.info("다이얼로그Stage 참조 > " + (dialogStage != null ? "OK" : "NO"));

        mDialogStage = dialogStage;
    }


    /**
     * 다이얼로그에서 변경될 연락처를 설정한다.
     *
     * @param person
     */
    public void setPerson(Person person) {
        logger.info("연락처 person 참조 > " + (person != null ? "OK" : "NO"));

        this.person = person;

        mFirstNameTextField.setText(person.getFirstName());
        mLastNameTextField.setText(person.getLastName());
        mStreetTextField.setText(person.getStreet());
        mPostalCodeTextField.setText(String.valueOf(person.getPostalCode()));
        mCityTextField.setText(person.getCity());
        mBirthdayTextField.setText(DateUtil.format(person.getBirthday()));
        mBirthdayTextField.setPromptText("yyyy.mm.dd");

    }

    /**
     * 사용자가 OK를 클릭하면 true를 그 외에는 false를 반환한다.
     *
     * @return
     */
    public boolean isOkClicked() {
        logger.info("ok버튼 클릭여부 > " + okClicked);

        return okClicked;
    }

    /**
     * 사용자가 OK를 클릭하면 호출된다.
     */
    @FXML
    private void handleOk() {
        logger.info("");
        //ok버튼 이벤트 실행전 입력필드에 대한 검사를 한다.
        //TODO: 수정사항: 각필드 입력할때마다 즉시 검사를 하며
        // 전필드의 검사가 끝났을 경우 OK 버튼을 활성화 시킨다.
        if (isInputVaild()) {
            person.setFirstName(mFirstNameTextField.getText());
            person.setLastName(mLastNameTextField.getText());
            person.setCity(mCityTextField.getText());
            person.setPostalCode(Integer.parseInt(mPostalCodeTextField.getText()));
            person.setStreet(mStreetTextField.getText());
            person.setBirthday(DateUtil.parse(mBirthdayTextField.getText()));

            okClicked = true;
            mDialogStage.close();
        }
    }

    /**
     * 사용자가 cancel을 클릭하면 호출된다.
     */
    @FXML
    private void handCancel() {
        logger.info("");

        mDialogStage.close();
    }

    /**
     * 텍스트 필드로 사용자 입력을 검사한다.
     *
     * @return
     */
    private boolean isInputVaild() {
        logger.info("");

        String errorMessage = "";

        //성 필드 검사
        if (mFirstNameTextField.getText() == null || mFirstNameTextField.getText().length() == 0) {
            errorMessage += "No Valid first name!\n";
        }
        //이름필드검사
        if (mLastNameTextField.getText() == null || mLastNameTextField.getText().length() == 0) {
            errorMessage += "No Valid last name!\n";
        }
        //거주지 필드검사
        if (mStreetTextField.getText() == null || mStreetTextField.getText().length() == 0) {
            errorMessage += "No Vaild street!\n";
        }
        //우편번호 필드검사
        if (mPostalCodeTextField.getText() == null || mPostalCodeTextField.getText().length() == 0) {
            errorMessage += "No Valid postal code!\n";
        }else{
            //우편 번호를 int 타입으로 반환한다.
            try {

                Integer.parseInt(mPostalCodeTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n";

            }
        }
        //도시 필드검사
        if (mCityTextField.getText() == null || mCityTextField.getText().length() == 0) {
            errorMessage += "No Valid city!\n";
        }
        //생일 날짜 필드검사
        if (mBirthdayTextField.getText() == null || mBirthdayTextField.getText().length() == 0) {

            errorMessage += "No valid birthdat!\n";
        } else {
            if (!DateUtil.validDate(mBirthdayTextField.getText())) {
                errorMessage += "No valid birthday. Use the format yyyy.mm.dd";
            }
        }

        if (errorMessage.length() == 0 || errorMessage.equals("")) {

            logger.info("No Error!!!");

            return true;
        } else {

            //오류 메시지를 보여 준다.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mDialogStage);
            alert.setTitle("Invalid Field");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;

        }
    }

}
