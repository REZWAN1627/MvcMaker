package maker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.us_bangla.fair_comparison.maker.JipConstant.GenericImportService;


public class ServiceMaker extends JavaWriterClass {

    public <T> ServiceMaker(Class<T> targetClass, List<String> otherFileImport, String packageName,
                            String directoryPath, Map<String, String> customSearch) {

        String className = targetClass.getSimpleName();
        StringBuilder camelCaseName = new StringBuilder();

        camelCaseName.append(Character.toLowerCase(className.charAt(0)))
                .append(className.substring(1));

        StringBuilder stringBuilder = new StringBuilder(packageName).append(";").append("\n").append("\n");
        for (String nameImport : GenericImportService) {
            stringBuilder.append(nameImport).append("\n");
        }
        otherFileImport.forEach(s -> {
            stringBuilder.append("import ").append(s).append(";").append("\n");
        });
        if (!customSearch.isEmpty()) {
            stringBuilder.append(customSearch.get("importSearch")).append("\n").append("\n");
        } else {
            stringBuilder.append(JipConstant.abstractIQDto)
                    .append("\n").append("\n");
        }
        StringBuilder serviceReady = makeService(targetClass, className, camelCaseName, stringBuilder, customSearch);
        String[] split = packageName.split(" ");
        String importName = split[split.length - 1];
        JipConstant.otherImport.add(importName + "." + className + "Service");
        writeFile(directoryPath, className, "Service", serviceReady);
    }

    private <T> StringBuilder makeService(Class<T> targetClass, String className, StringBuilder camelCaseName,
                                          StringBuilder stringBuilder, Map<String, String> customSearch) {

        stringBuilder.append("@Service").append("\n");
        stringBuilder.append("public class ").append(className).append("Service").append(" extends ")
                .append("AbstractSearchService<").append(className).append(", ").append(className).append("Dto").append(", ");
        if (customSearch.isEmpty()) {
            stringBuilder.append("IdQuerySearchDto>").append(" {").append("\n");
        } else {
            stringBuilder.append(customSearch.get("searchName")).append(">").append(" {").append("\n");
        }


        stringBuilder.append("\t").append("private final ").append(className).append("Repository").append(" ")
                .append(camelCaseName).append("Repository;").append("\n");

        stringBuilder.append("\t").append("public ").append(className).append("Service").append("(")
                .append(className).append("Repository ")
                .append(camelCaseName).append("Repository)").append(" {\n").append("\t").append("\t")
                .append("super(").append(camelCaseName).append("Repository);").append("\n").append("\t").append("\t")
                .append("this.").append(camelCaseName).append("Repository").append(" = ").append(camelCaseName)
                .append("Repository;").append("\n").append("\t}")
                .append("\n");


        stringBuilder.append("\t").append("@Override").append("\n").append("\t")
                .append("protected Specification<").append(className).append(">");
        if (customSearch.isEmpty()) {
            stringBuilder.append(" buildSpecification(").append("IdQuerySearchDto ");
        } else {
            stringBuilder.append(" buildSpecification(").append(customSearch.get("searchName")).append(" ");
        }

        stringBuilder.append("searchDto) {").append("\n").append("\t")
                .append("\tCustomSpecification<").append(className).append("> customSpecification")
                .append(" = ").append("new CustomSpecification<>();").append("\n").append("\t")
                .append("\treturn").append(" null;").append("\n\t}").append("\n");

        converter(targetClass, stringBuilder, className, camelCaseName.toString(),
                "Entity", "dto");
        converter(targetClass, stringBuilder, className, camelCaseName.toString(),
                "Response", camelCaseName.toString());

        stringBuilder.append("\t").append("@Override").append("\n").append("\t")
                .append("protected ").append(className).append(" updateEntity(")
                .append(className).append("Dto").append(" dto, ").append(className).append(" entity) {\n")
                .append("\t\treturn entity;").append("\n}").append("\n");

        return stringBuilder.append("\n").append("}");

    }

    private synchronized <T> void converter(Class<T> targetClass, StringBuilder stringBuilder, String className,
                                            String camelCaseName, String convertedType, String parameterName) {
        stringBuilder.append("\t").append("@Override").append("\n");
        returnType(className, camelCaseName, stringBuilder, convertedType);
        Field[] declaredFields = targetClass.getDeclaredFields();
        constructBuilder(declaredFields, stringBuilder, parameterName);
        stringBuilder.append("\t").append("\t").append("\t")
                .append(".build();").append("\n").append("\t").append("}").append("\n");
    }

    private synchronized void constructBuilder(Field[] declaredFields, StringBuilder stringBuilder, String parameterName) {
        for (Field declaredField : declaredFields) {
            String[] split1 = declaredField.getName().split("\\.");
            String fieldName = split1[split1.length - 1];
            stringBuilder.append("\t").append("\t").append("\t").append(".").append(fieldName)
                    .append("(").append(parameterName).append(".get")
                    .append(Character.toUpperCase(fieldName.charAt(0)))
                    .append(fieldName.substring(1)).append("())").append("\n");
        }
    }

    private synchronized void returnType(String className,
                                         String camelCaseName,
                                         StringBuilder stringBuilder,
                                         String convertedType) {
        switch (convertedType) {
            case "Entity":
                stringBuilder.append("\t")
                        .append("protected ").append(className).append(" ").append("convertToEntity(").append(className)
                        .append("Dto").append(" ").append("dto").append(") {")
                        .append("\n")
                        .append("\t")
                        .append("\t")
                        .append("return ").append(className).append(".builder()")
                        .append("\n");
                break;
            case "Response":
                stringBuilder.append("\t")
                        .append("protected ").append(className).append("Response").append(" ")
                        .append("convertToResponseDto(").append(className).append(" ").append(camelCaseName).append(") {")
                        .append("\n")
                        .append("\t")
                        .append("\t")
                        .append("return ").append(className).append("Response.").append("builder()")
                        .append("\n")
                        .append("\t")
                        .append("\t")
                        .append("\t")
                        .append(".id(").append(camelCaseName).append(".getId())").append("\n");
                break;
            default:
                break;
        }
    }
}
