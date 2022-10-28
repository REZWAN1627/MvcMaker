package com.automation.jipstart.maker;

public class RepositoryMaker extends JavaWriterClass {

    public <T> RepositoryMaker(Class<T> target, String packageName, String directoryPath) {
        String className = target.getSimpleName();
        StringBuilder stringBuilder = buildRepositoryClass(target, packageName, className);
        String[] split = packageName.split(" ");
        String importName = split[split.length - 1];
        JipConstant.otherImport.add(importName + "." + className + "Repository");
        writeFile(directoryPath, className, "Repository", stringBuilder);
    }

    private <T> StringBuilder buildRepositoryClass(Class<T> targetClass, String packageName, String className) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(packageName).append(";").append("\n").append("\n");
        stringBuilder.append(JipConstant.abstractRepoImport).append("\n");
        stringBuilder.append("import ").append(targetClass.getPackageName()).append(".").append(className).append(";\n");
        stringBuilder.append(JipConstant.repositoryImport).append("\n").append("\n");
        stringBuilder.append("@Repository").append("\n");

        stringBuilder.append("public interface ")
                .append(className)
                .append("Repository")
                .append(" extends ")
                .append("AbstractRepository<")
                .append(className)
                .append(">")
                .append(" {")
                .append("\n");

        stringBuilder.append("}");
        return stringBuilder;
    }

}
