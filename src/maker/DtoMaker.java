package maker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class DtoMaker extends JavaWriterClass {

    public <T> DtoMaker(Class<T> target, String packageName, String directoryPath) {
        String className = target.getSimpleName();
        StringBuilder stringBuilder = buildDtoClass(target, packageName, className);
        String[] split = packageName.split(" ");
        String importName = split[split.length - 1];
        JipConstant.otherImport.add(importName + "." + className + "Dto");
        writeFile(directoryPath, className, "Dto", stringBuilder);
    }

    private <T> StringBuilder buildDtoClass(Class<T> targetClass, String packageName, String className) {
        Field[] declaredFields = targetClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(packageName).append(";").append("\n").append("\n");
        StringBuilder importLombokAndOthers = new StringBuilder();
        StringBuilder annotation = new StringBuilder();
        StringBuilder declaredField = new StringBuilder();
        for (String tempLombok : JipConstant.importLombokAndOthers) {
            importLombokAndOthers.append(tempLombok).append("\n");
        }
        importLombokAndOthers.append(JipConstant.IDto).append("\n");
        for (String tempAnnotation : JipConstant.annotationName) {
            annotation.append(tempAnnotation);
        }
        for (Field tempDeclaredField : declaredFields) {

            if (tempDeclaredField.getType().isEnum()) {
                importLombokAndOthers.append("import ")
                        .append(tempDeclaredField.getType().getName())
                        .append(";\n");
            }

            Annotation[] declaredAnnotations = tempDeclaredField.getDeclaredAnnotations();
            Set<String> visitedField = new HashSet<>();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                Boolean booleanType = annotationsByType(declaredAnnotation.annotationType().getName(), declaredField, tempDeclaredField.getName(), importLombokAndOthers);
                if (booleanType) {
                    visitedField.add(tempDeclaredField.getName());
                }
            }

            if (!visitedField.contains(tempDeclaredField.getName())) {
                String[] split = tempDeclaredField.getGenericType().getTypeName().split("\\.");
                String typeName = split[split.length - 1];
                declaredField.append("\t")
                        .append("private ")
                        .append(typeName)
                        .append(" ")
                        .append(tempDeclaredField.getName())
                        .append(";\n");
            }
        }

        stringBuilder.append(importLombokAndOthers)
                .append(annotation)
                .append("public class ")
                .append(className)
                .append("Dto")
                .append(" implements IDto")
                .append(" {")
                .append("\n")
                .append(declaredField);

        return stringBuilder.append("}");
    }

    private Boolean annotationsByType(String name, StringBuilder declaredField, String fieldName,
                                      StringBuilder importLombokAndOthers) {
        switch (name) {
            case "javax.persistence.ManyToOne":
                declaredField.append("\t")
                        .append("private ")
                        .append("Long")
                        .append(" ")
                        .append(fieldName)
                        .append("Id")
                        .append(";\n");
                return Boolean.TRUE;
            case "javax.persistence.OneToMany":
                importLombokAndOthers.append("import java.util.Set;").append("\n");
                declaredField.append("\t")
                        .append("private ")
                        .append("Set<Long>")
                        .append(" ")
                        .append(fieldName)
                        .append("Ids")
                        .append(";\n");
                return Boolean.TRUE;

            case "javax.persistence.OneToOne":
                declaredField.append("\t")
                        .append("private ")
                        .append("Long")
                        .append(" ")
                        .append(fieldName)
                        .append("Id")
                        .append(";\n");
                return Boolean.TRUE;

            case "javax.persistence.ManyToMany":
                importLombokAndOthers.append("import java.util.Set;").append("\n");
                declaredField.append("\t")
                        .append("private ")
                        .append("Set<Long>")
                        .append(" ")
                        .append(fieldName)
                        .append("Ids")
                        .append(";\n");
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;
        }

    }

}
