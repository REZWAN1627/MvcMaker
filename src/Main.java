import maker.*;
import maker.JipConstant;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        String directoryPath = "src/main/java/com/automation/jipstart/example";
        Class<JipConstant> exampleClass = JipConstant.class;
        JipConstant.otherImport.add("com.automation.jipstart.example.entity." + exampleClass.getSimpleName() + ";");
        new DirectoryMaker(directoryPath, "package com.automation.jipstart.example");
        new DtoMaker(exampleClass, JipConstant.packageImport.get(".dto.request"), JipConstant.mvcDirectoryPath.get("/dto/request"));
        new ResponseMaker(exampleClass, JipConstant.packageImport.get(".dto.response"), JipConstant.mvcDirectoryPath.get("/dto/response"));
        new RepositoryMaker(exampleClass, JipConstant.packageImport.get(".repository"), JipConstant.mvcDirectoryPath.get("/repository"));
        new ServiceMaker(exampleClass, JipConstant.otherImport, JipConstant.packageImport.get(".service"), JipConstant.mvcDirectoryPath.get("/service"), new HashMap<>());
        new ControllerMaker(exampleClass, JipConstant.packageImport.get(".controller"), JipConstant.otherImport, JipConstant.mvcDirectoryPath.get("/controller"), new HashMap<>(), "/api/example");
    }
}