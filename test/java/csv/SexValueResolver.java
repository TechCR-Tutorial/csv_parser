package csv;

import java.util.Map;

import com.parser.util.annotate.ParserValueResolver;

public class SexValueResolver implements ParserValueResolver<String> {
    @Override
    public String resolve(String key, Map<String, Object> valueCache) {
        if ("M".equals(key)) {
            return "male";
        }
        return "female";
    }
}
