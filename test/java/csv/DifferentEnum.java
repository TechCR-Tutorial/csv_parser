package csv;

import java.util.Map;

import com.parser.util.annotate.ParserValueResolver;

public enum DifferentEnum implements ParserValueResolver<DifferentEnum> {
    DIFF_ENUM_1("DIFF_1"),
    DIFF_ENUM_2("DIFF_2"),;
    private String description;

    DifferentEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DifferentEnum getByDesc(String description) {
        for (DifferentEnum differentEnum : values()) {
            if (differentEnum.description.equals(description)) {
                return differentEnum;
            }
        }
        return null;
    }

    @Override
    public DifferentEnum resolve(String key, Map<String, Object> valueCache) {
        return getByDesc(key);
    }
}
