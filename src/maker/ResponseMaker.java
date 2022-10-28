package com.automation.jipstart.maker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

public class ResponseMaker extends JavaWriterClass {

    public <T> ResponseMaker(Class<T> target, String packageName, String directoryPath) {
        String className = target.getSimpleName();
        StringBuilder stringBuilder = buildResponseClass(target, packageName, className);
        String[] split = packageName.split(" ");
        String importName = split[split.length - 1];
        JipConstant.otherImport.add(importName + "." + className + "Response");
        writeFile(directoryPath, className, "Response", stringBuilder);
    }

    private void creatingForRelationShipField(StringBuilder declaredField, Field tempDeclaredField, StringBuilder importLombokAndOthers) {
        importLombokAndOthers.append("import java.util.Set;").append("\n");
        ParameterizedType genericType = (ParameterizedType) tempDeclaredField.getGenericType();
        Class<?> type = (Class<?>) genericType.getActualTypeArguments()[0];
        String childName = type.getSimpleName();
        declaredField.append("\t")
                .append("private ")
                .append("Set")
                .append("<")
                .append(childName)
                .append(">")
                .append(" ")
                .append(Character.toLowerCase(childName.charAt(0)))
                .append(childName.substring(1))
                .append(";\n");
    }

    private <T> StringBuilder buildResponseClass(Class<T> targetClass, String packageName, String className) {
        Field[] declaredFields = targetClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(packageName).append(";").append("\n").append("\n");
        StringBuilder importLombokAndOthers = new StringBuilder();
        StringBuilder annotation = new StringBuilder();
        StringBuilder declaredField = new StringBuilder();
        for (String tempLombok : JipConstant.importLombokAndOthers) {
            importLombokAndOthers.append(tempLombok).append("\n");
        }
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
                Boolean booleanType = annotationsByType(declaredAnnotation.annotationType().getName(),
                        declaredField, tempDeclaredField, importLombokAndOthers);
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
                .append("Response")
                .append(" {")
                .append("\n")
                .append("\tprivate Long id;")
                .append("\n")
                .append(declaredField);

        return stringBuilder.append("}");
    }

    private Boolean annotationsByType(String name, StringBuilder declaredField,
                                      Field tempDeclaredField,
                                      StringBuilder importLombokAndOthers) {
        switch (name) {
            case "javax.persistence.ManyToOne":
                return makeResponseWithRelation((Class<?>) tempDeclaredField.getGenericType(), declaredField);
            case "javax.persistence.OneToMany":
                creatingForRelationShipField(declaredField, tempDeclaredField, importLombokAndOthers);
                return Boolean.TRUE;

            case "javax.persistence.OneToOne":
                return makeResponseWithRelation(tempDeclaredField.getDeclaringClass(), declaredField);

            case "javax.persistence.ManyToMany":
                creatingForRelationShipField(declaredField, tempDeclaredField, importLombokAndOthers);
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;
        }

    }

    private Boolean makeResponseWithRelation(Class<?> declaringClass, StringBuilder declaredField) {
        String parentName = declaringClass.getSimpleName();
        Field[] declaredFields = declaringClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                boolean contains = annotation.toString().contains("@javax.persistence.Column");
                if (contains) {
                    boolean unique = annotation.toString().contains("unique = true");
                    if (unique) {
                        String[] split1 = field.getName().split("\\.");
                        String[] split2 = field.getType().getName().split("\\.");
                        String fieldType = split2[split2.length - 1];
                        String fieldName1 = split1[split1.length - 1];
                        declaredField.append("\t")
                                .append("private ")
                                .append("Long ")
                                .append(Character.toLowerCase(parentName.charAt(0)))
                                .append(parentName.substring(1))
                                .append("Id;")
                                .append("\n")
                                .append("\t")
                                .append("private ")
                                .append(fieldType)
                                .append(" ")
                                .append(fieldName1)
                                .append(";")
                                .append("\n");
                        return Boolean.TRUE;
                    }

                }
            }
        }
        String[] split1 = declaredFields[0].getName().split("\\.");
        String fieldName = split1[split1.length - 1];
        String[] split2 = declaredFields[0].getType().getName().split("\\.");
        String fieldName2 = split2[split2.length - 1];
        declaredField.append("\t")
                .append("private ")
                .append("Long ")
                .append(Character.toLowerCase(parentName.charAt(0)))
                .append(parentName.substring(1))
                .append("Id;")
                .append("\n")
                .append("\t")
                .append("private ")
                .append(fieldName2)
                .append(" ")
                .append(fieldName)
                .append(";")
                .append("\n");
        return Boolean.TRUE;

    }
}
