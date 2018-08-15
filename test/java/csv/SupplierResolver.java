package csv;

import java.util.List;
import java.util.Map;

import com.parser.util.annotate.ParserCustomValueResolver;
import com.parser.util.exception.BaseParserException;

public class SupplierResolver implements ParserCustomValueResolver<Supplier> {


    private String supplierNameField = "SUPPLIER NAME";
    private String supplierAddressField = "SUPPLIER ADDRESS";

    private static SupplierResolver INSTANCE;

    private SupplierResolver() {
    }

    @Override
    public void addRequestHeaders(List<String> propertyList) {
        propertyList.add(supplierNameField);
        propertyList.add(supplierAddressField);
    }

    @Override
    public Supplier resolveValueByHeaders(Map<String, String> propertyMap, Map<String, Object> valueCache)
            throws BaseParserException {
        String custonerName = propertyMap.get(supplierNameField);
        String custonerAddress = propertyMap.get(supplierAddressField);
        return new Supplier(custonerName, custonerAddress);
    }

    public static SupplierResolver getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SupplierResolver();
        }
        return INSTANCE;
    }
}
