package maker;

import java.util.List;
import java.util.Map;

public class ControllerMaker extends JavaWriterClass {



    public <T> ControllerMaker(Class<T> targetClass, String packageName, List<String> otherImp,
                               String directoryPath, Map<String, String> customSearch, String apiPath) {
        String className = targetClass.getSimpleName();
        StringBuilder camelCaseName = new StringBuilder();

        camelCaseName.append(Character.toLowerCase(className.charAt(0)))
                .append(className.substring(1));

        StringBuilder stringBuilder = new StringBuilder(packageName).append(";").append("\n").append("\n");
        for (String controllerImp : JipConstant.GenericImportController) {
            stringBuilder.append(controllerImp).append("\n");
        }

        otherImp.forEach(s -> {
            stringBuilder.append("import ").append(s).append(";").append("\n");
        });

        if (!customSearch.isEmpty()) {
            stringBuilder.append(customSearch.get("importSearch")).append("\n");
        } else {
            stringBuilder.append(JipConstant.abstractIQDto)
                    .append("\n");
        }

        StringBuilder controllerReady = makeController(className, camelCaseName, stringBuilder, customSearch,
                apiPath);

        writeFile(directoryPath, className, "Controller", controllerReady);

    }

    private <T> StringBuilder makeController(String className, StringBuilder camelCaseName, StringBuilder stringBuilder, Map<String, String> customSearch, String apiPath) {

        stringBuilder.append("@RestController\n").append("@RequestMapping(\"").append(apiPath).append("\")").append("\n")
                .append("public class ").append(className).append("Controller ").append("extends ")
                .append("AbstractSearchController<").append(className).append(", ")
                .append(className).append("Dto, ");
        if (customSearch.isEmpty()) {
            stringBuilder.append("IdQuerySearchDto>").append(" {").append("\n").append("\n");
        } else {
            stringBuilder.append(customSearch.get("searchName")).append(">").append(" {").append("\n").append("\n");
        }

        stringBuilder.append("\t").append("public ").append(className).append("Controller")
                .append("(").append(className).append("Service ").append(camelCaseName).append("Service")
                .append(") {").append("\n").append("\t").append("\t").append("super(").append(camelCaseName).append("Service")
                .append(");").append("\n").append("\t").append("}").append("\n").append("}");

        return stringBuilder;
    }
}
