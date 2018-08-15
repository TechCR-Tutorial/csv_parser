ENTITY PARSER READ ME.


Can modify value which need to execute process from outside using com.parser.util.vo.ParserVO



CSV ENTITY PARSER.
Entity should be type of om.parser.csv.operations.CSVSupportable

Its need to create com.parser.csv.operations.CsvOperator for start process.
Can create CsvOperator by annotate entity class by com.parser.util.annotate.ParserVOA or creating new CSVVo instance.
default properties can override through annotation or csv vo instance.

Ex :
csv.TestVO using annotation.
csv.CsvParserTest#testByParserAnnotated
csv.CsvParserTest#testByParserInstance


Parser will identity property name by field name if it camel case matching.

Ex : ID -> id
     ENTITY NAME -> entityName

If not you have provide header map like header as key and property as value.
Ex :
csv.CsvParserTest#config

NOTE :
ENUM:
    if column value contain exact enum name then process will pick enum by name.
    if not need to implement com.parser.util.annotate.ParserValueResolver
    Ex :
    csv.DifferentEnum


Also API support to annotate field header.
Ex :
    csv.TestVOAnnotate
    csv.CsvParserTest#testByAnnotationFields