package org.familly.fx.adressbook.first.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import org.familly.fx.adressbook.first.model.Person;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;


/**
 * 통계뷰에 대한 컨트롤러
 * 기능설명:
 * 1.컨트롤로는 FXML 파일에서 엘리먼트 2개에 접근해야 합니다.
 *   1.1 barChart : String 과 Integer 타입을 가집니다.String은 xAxis에서 월로 사용되고
 *   Integer 는 그 달의 연락처 개수에 사용됩니다.
 *   1.2 xAxis : 이걸로 월 String을 추가한다.
 * 2.initialize() : 모든 월 리스트를 x축에 값으로 할당합니다.
 * 3.setBirthdayInPersonDate() : MainApp 클래스에서 주소록 데이터를 설정하기 위해사용.
 * 주소록을 검색해서 월별로 생일 개수를 카운트 합니다.
 * series 데이터에 모든 달의 XYChart.Data 를 추가합니다.
 * 각 XYChart.Data 객체는 차트에서 바 하나를 나타냅니다.
 *
 * @author 김 영호
 */
public class BirthdayStatisticsController {
    private static final Logger logger = Logger.getLogger(BirthdayStatisticsController.class.getSimpleName());

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();


    /**
     * 컨트롤러 클래스를 초기화한다. 이 메서드는
     * FXML 팡일이 로드된 후 자동으로 호출된다.
     */
    @FXML
    private void initialize() {
        logger.info("start");
        //영어 월 이름을 배열로 가져온다.
        String[] months = DateFormatSymbols.getInstance(Locale.KOREA).getMonths();

        logger.info("months ? " + months);

        //배열을 리스트로 변환하고 나서 ObservableList에 추가한다.
        monthNames.addAll(Arrays.asList(months));

        //수평축에 월 이름을 카테코리로 할당한다.
        xAxis.setCategories(monthNames);

        logger.info("end");

    }

    /**
     * 통계에 보여줄 연락처를 설정한다.
     *
     * @param persons
     */
    public void setBirthdayInPersonDate(List<Person> persons) {
        logger.info("start");

        //주소록 생일의 월 개수를 누적한다.
        int[] monthCounter = new int[12];

        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        //월별로 XYChart.Data 객체를 만든다. series 에 추가한다.
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }

        barChart.getData().add(series);


        logger.info("end");

    }





}
