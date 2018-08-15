package csv;

import com.parser.util.annotate.ParserCustomValueProvider;
import com.parser.util.annotate.ParserCustomValueResolver;
import com.parser.util.annotate.ParserCustomValueResolverSupplier;

public class SupplierResolverSupplier implements ParserCustomValueResolverSupplier<Supplier> {

    @Override
    public ParserCustomValueResolver<Supplier> supply() {
        return SupplierResolver.getINSTANCE();
    }
}
