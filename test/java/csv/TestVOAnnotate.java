/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package csv;

import com.parser.csv.operations.CSVSupportable;
import com.parser.util.annotate.ParserCustomValueProvider;
import com.parser.util.annotate.ParserDateFormat;
import com.parser.util.annotate.ParserDefaultValue;
import com.parser.util.annotate.ParserFieldA;
import com.parser.util.annotate.ParserSkipField;
import com.parser.util.annotate.ParserVOA;
import com.parser.util.annotate.ParserValueProvider;

import java.util.Date;

/**
 *
 * @author chamly
 */
@ParserVOA(groupBy = "ACTION", supperClassLevel = 1, dateFormat = "MM/dd/yy")
public class TestVOAnnotate extends BaseEntity implements CSVSupportable{

    @ParserFieldA(fieldHeader = "ACTION")
    private String action;
    @ParserFieldA(fieldHeader = "ENTITY ID")
    private Long id;
    private String entityName;
    private Double doubleField;
    @ParserFieldA(fieldHeader = "ENUM NAME")
    private EnumByName enumByName;
    private DifferentEnum differentEnum;
    @ParserCustomValueProvider(resolver = CustomerResolver.class)
    private Customer customer;
    @ParserCustomValueProvider(supplier = SupplierResolverSupplier.class)
    private Supplier supplier;
    @ParserDefaultValue(defaultValue = "M")
    @ParserValueProvider(resolver = SexValueResolver.class)
    private String sex;
    @ParserSkipField
    private String skipField;
    @ParserDateFormat(format = "MM/dd/yy")
    private Date date;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }

    public EnumByName getEnumByName() {
        return enumByName;
    }

    public void setEnumByName(EnumByName enumByName) {
        this.enumByName = enumByName;
    }

    public DifferentEnum getDifferentEnum() {
        return differentEnum;
    }

    public void setDifferentEnum(DifferentEnum differentEnum) {
        this.differentEnum = differentEnum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSkipField() {
        return skipField;
    }

    public void setSkipField(String skipField) {
        this.skipField = skipField;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TestVO getTestVO() {
        TestVO testVO = new TestVO();
        testVO.setAction(action);
        testVO.setId(id);
        testVO.setEntityName(entityName);
        testVO.setDoubleField(doubleField);
        testVO.setEnumByName(enumByName);
        testVO.setDifferentEnum(differentEnum);
        testVO.setCustomer(customer);
        testVO.setSupplier(supplier);
        testVO.setSkipField(skipField);
        testVO.setEffectiveDate(effectiveDate);
        testVO.setExpiryDate(expiryDate);
        testVO.setDate(date);
        testVO.setSex(sex);
        return testVO;
    }
}