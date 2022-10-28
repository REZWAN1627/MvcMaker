package com.automation.jipstart.maker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class JavaDocMaker {

    public <T> String makeJavaDoc(Class<T> targetClass) {
        StringBuilder stringBuilder = new StringBuilder();
        Constructor<?> declaredConstructors = targetClass.getDeclaredConstructors()[0];
        String docForConstructor = createDocForConstructor(declaredConstructors);
        stringBuilder.append(docForConstructor);
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            stringBuilder.append(makeMethodDoc(declaredMethod));
        }

        return stringBuilder.toString();
    }

    private String findSuitableDoc(String declaredMethod) {
        switch (declaredMethod) {
            case "buildSpecification":
                return "creating Specification for search criteria";
            case "convertToEntity":
                return "converting Dto to ChildCLass";
            case "convertToResponseDto":
                return "converting ChildCLass to Response";
            case "search":
                return "getting paginate data based on search query";
            case "getAll":
                return "getting all data and data can be handle by StatusId value";
            default:
                return declaredMethod;
        }
    }

    private String createDocForConstructor(Constructor<?> cons) {
        String[] split = cons.getName().split("\\.");
        String consName = split[split.length - 1];
        StringBuilder stringBuilder = new StringBuilder("/**")
                .append("\n*")
                .append(" This is ")
                .append(consName)
                .append(" Class Constructor.")
                .append("\n")
                .append("*")
                .append("\n");

        int parameterCount = cons.getParameterCount();
        Parameter[] parameters = cons.getParameters();
        for (int i = 0; i < parameterCount; i++) {
            stringBuilder.append("* ")
                    .append("@param ")
                    .append(parameters[i].getName())
                    .append(" {")
                    .append("@link ");
            String[] typeSplit = parameters[i].getParameterizedType().getTypeName().split("\\.");
            String listOrSet = "";
            boolean parameterList = false;
            for (String s : typeSplit) {
                if (s.contains("List")) {
                    parameterList = true;
                    listOrSet = "List<";
                } else if (s.contains("Set")) {
                    parameterList = true;
                    listOrSet = "Set<";
                }
            }
            String parameterTypeName = typeSplit[typeSplit.length - 1];
            if (parameterList) {
                stringBuilder.append(listOrSet).append(parameterTypeName)
                        .append(" }")
                        .append("\n");
            } else {
                stringBuilder.append(parameterTypeName)
                        .append(" }")
                        .append("\n");
            }
        }
        stringBuilder.append("*/");
        return stringBuilder.append("\n").append("---------------------------------------------\n").toString();

    }

    private <T> String makeMethodDoc(Method method) {
        String methodName = method.getName();
        int parameterCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        Class<?> returnType = method.getReturnType();
        String[] split = returnType.getTypeName().split("\\.");
        StringBuilder stringBuilder = new StringBuilder("/**")
                .append("\n*")
                .append(" This ")
                .append(methodName)
                .append(" method")
                .append(" is responsible for ")
                .append(findSuitableDoc(methodName))
                .append("\n")
                .append("*")
                .append("\n");

        for (int i = 0; i < parameterCount; i++) {
            stringBuilder.append("* ")
                    .append(" @param ")
                    .append(parameters[i].getName())
                    .append(" {")
                    .append("@link ");
            String[] typeSplit = parameters[i].getParameterizedType().getTypeName().split("\\.");
            boolean parameterList = false;
            String listOrSet = "";
            for (String s : typeSplit) {
                if (s.contains("List")) {
                    parameterList = true;
                    listOrSet = "List<";
                } else if (s.contains("Set")) {
                    parameterList = true;
                    listOrSet = "Set<";
                }
            }
            String parameterTypeName = typeSplit[typeSplit.length - 1];
            if (parameterList) {
                stringBuilder.append(listOrSet).append(parameterTypeName)
                        .append(" }")
                        .append("\n");
            } else {
                stringBuilder.append(parameterTypeName)
                        .append(" }")
                        .append("\n");
            }

        }

        if (split.length > 0 && split[split.length - 1].equals("List")) {
            collectionTypeDoc("List", stringBuilder, method);
        } else if (split.length > 0 && split[split.length - 1].equals("Set")) {
            collectionTypeDoc("Set", stringBuilder, method);
        } else if (split.length > 0 && split[split.length - 1].equals("Specification")) {
            collectionTypeDoc("Specification", stringBuilder, method);
        } else if (split.length > 0) {
            stringBuilder.append("* ").append(" @return ")
                    .append(" {")
                    .append("@link ")
                    .append(split[split.length - 1])
                    .append(" }\n");
        }

        stringBuilder.append("*/");
        return stringBuilder.append("\n").append("---------------------------------------------\n").toString();
    }

    private void collectionTypeDoc(String type, StringBuilder stringBuilder, Method method) {
        String[] genericName = method.getGenericReturnType().getTypeName().split("\\.");
        String typeName = genericName[genericName.length - 1];
        stringBuilder.append("* ")
                .append(" @return ")
                .append(" {")
                .append("@link ")
                .append(type)
                .append("<")
                .append(typeName)
                .append(" }\n");
    }
}
