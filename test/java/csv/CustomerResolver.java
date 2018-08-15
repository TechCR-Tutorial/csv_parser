package csv;

import java.util.List;
import java.util.Map;

import com.parser.util.annotate.ParserCustomValueResolver;
import com.parser.util.exception.BaseParserException;

public class CustomerResolver implements ParserCustomValueResolver<Customer> {

    private String customerNameField = "CUSTOMER NAME";
    private String customerAddressField = "CUSTOMER ADDRESS";

    @Override
    public void addRequestHeaders(List<String> propertyList) {
        propertyList.add(customerNameField);
        propertyList.add(customerAddressField);
    }

    @Override
    public Customer resolveValueByHeaders(Map<String, String> propertyMap, Map<String, Object> valueCache)
            throws BaseParserException {
        String custonerName = propertyMap.get(customerNameField);
        String custonerAddress = propertyMap.get(customerAddressField);
        return new Customer(custonerName, custonerAddress);
    }
}
