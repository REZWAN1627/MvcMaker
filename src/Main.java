import com.us_bangla.fair_comparison.maker.*;
import com.us_bangla.fair_comparison.rolemanagement.entity.ActionGroup;
import com.us_bangla.fair_comparison.rolemanagement.entity.Role;
import com.us_bangla.fair_comparison.rolemanagement.entity.SystemAction;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        String directoryPath = "src/main/java/com/us_bangla/fair_comparison/rolemanagement";
        Class<SystemAction> exampleClass = SystemAction.class;
        JipConstant.otherImport.add("com.us_bangla.fair_comparison.rolemanagement.entity." + exampleClass.getSimpleName() + ";");
        new DirectoryMaker(directoryPath, "package com.us_bangla.fair_comparison.rolemanagement");
        new DtoMaker(exampleClass, JipConstant.packageImport.get(".dto.request"), JipConstant.mvcDirectoryPath.get("/dto/request"));
        new ResponseMaker(exampleClass, JipConstant.packageImport.get(".dto.response"), JipConstant.mvcDirectoryPath.get("/dto/response"));
        new RepositoryMaker(exampleClass, JipConstant.packageImport.get(".repository"), JipConstant.mvcDirectoryPath.get("/repository"));
        new ServiceMaker(exampleClass, JipConstant.otherImport, JipConstant.packageImport.get(".service"), JipConstant.mvcDirectoryPath.get("/service"), new HashMap<>());
        new ControllerMaker(exampleClass, JipConstant.packageImport.get(".controller"), JipConstant.otherImport, JipConstant.mvcDirectoryPath.get("/controller"), new HashMap<>(), "/api/example");
    }
}