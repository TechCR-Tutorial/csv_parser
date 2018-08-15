package csv;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.parser.csv.operations.CsvAnnotationOperator;
import com.parser.csv.operations.CsvOperator;
import com.parser.csv.vo.CsvVO;

public class CsvParserTest {

    private Map<String, String> headerMap;

    @Before
    public void config() {
        headerMap = new HashMap<>();
        headerMap.put("ENTITY ID", "id");
        headerMap.put("EFF DATE", "effectiveDate");
        headerMap.put("EXP DATE", "expiryDate");
        headerMap.put("ENUM NAME", "enumByName");
    }

    @Test
    public void testByParserAnnotated() throws Exception {
        CsvOperator operator = new CsvOperator(TestVO.class);
        InputStream stream = CsvParserTest.class.getResourceAsStream("TEST.csv");
        Map<String, List<TestVO>> map = operator.csvToEntityMap(TestVO.class, stream, headerMap);
        assertResult(map);
    }

    @Test
    public void testByParserInstance() throws Exception {
        CsvVO csvVO = new CsvVO();
        csvVO.setGroupBy("ACTION");
        csvVO.setSupperClassLevel(1);
        csvVO.setDateFormat("MM/dd/yy");
        csvVO.setDateTimeFormat("MM/dd/yy");
        CsvOperator operator = new CsvOperator(csvVO);
        InputStream stream = CsvParserTest.class.getResourceAsStream("TEST.csv");
        Map<String, List<TestVO>> map = operator.csvToEntityMap(TestVO.class, stream, headerMap);
        assertResult(map);
    }

    @Test
    public void testByAnnotationFields() throws Exception {
        CsvAnnotationOperator operator = new CsvAnnotationOperator(TestVOAnnotate.class);
        InputStream stream = CsvParserTest.class.getResourceAsStream("TEST.csv");
        Map<String, String> additionalHeaderMap = new HashMap<>();
        additionalHeaderMap.put("EFF DATE", "effectiveDate");
        additionalHeaderMap.put("EXP DATE", "expiryDate");
        Map<String, List<TestVOAnnotate>> map = operator.csvToEntityMap(TestVOAnnotate.class, stream, additionalHeaderMap);
        Map<String, List<TestVO>> result = new HashMap<>();
        result.put("N", map.get("N").stream().map(voAnnotate -> voAnnotate.getTestVO()).collect(Collectors.toList()));
        assertResult(result);
    }



    private void assertResult(Map<String, List<TestVO>> map ) {
        Assert.assertEquals(1, map.size());
        List<TestVO> resultList = map.get("N");
        Assert.assertEquals(2, resultList.size());
        Optional<TestVO> voEntity = resultList.stream().filter(testVO -> testVO.getId() == 4047).findFirst();
        Assert.assertTrue(voEntity.isPresent());
        TestVO vo = voEntity.get();
        Assert.assertEquals("FIRST", vo.getEntityName());
        Assert.assertEquals(new Double(0.5), vo.getDoubleField());
        testDate(2010, 02, 10, vo.getEffectiveDate());
        testDate(2010, 02, 10, vo.getExpiryDate());
        Assert.assertEquals(EnumByName.ENUM_1, vo.getEnumByName());
        Assert.assertEquals(DifferentEnum.DIFF_ENUM_1, vo.getDifferentEnum());
        Assert.assertEquals("CUSTOMER ONE", vo.getCustomer().getCustomerName());
        Assert.assertEquals("CU ONE ADD", vo.getCustomer().getCustomerAddress());
        Assert.assertEquals("SUPP ONE", vo.getSupplier().getSupplierName());
        Assert.assertEquals("SUP ONE ADD", vo.getSupplier().getSupplierAddress());
        Assert.assertTrue(vo.getSkipField() == null);
        Assert.assertEquals("male", vo.getSex());
        testDate(2018, 11, 25, vo.getDate());

    }

    private void testDate(int year, int month, int date, Date resultDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        Assert.assertEquals(DateUtils.truncate(calendar.getTime(), Calendar.YEAR), DateUtils.truncate(resultDate, Calendar.YEAR));
        Assert.assertEquals(DateUtils.truncate(calendar.getTime(), Calendar.MONTH), DateUtils.truncate(resultDate, Calendar.MONTH));
        Assert.assertEquals(DateUtils.truncate(calendar.getTime(), Calendar.DATE), DateUtils.truncate(resultDate, Calendar.DATE));
    }
}
