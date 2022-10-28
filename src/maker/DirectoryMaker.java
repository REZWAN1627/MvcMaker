package com.automation.jipstart.maker;

public class DirectoryMaker extends JavaWriterClass {

    public DirectoryMaker(String directoryPath, String commonPackage) {
        for (String fileName : JipConstant.fileNames) {
            makeDir(directoryPath, fileName);
            JipConstant.mvcDirectoryPath.put(fileName, directoryPath + fileName + "/");
        }

        for (String name : JipConstant.packageName) {
            JipConstant.packageImport.put(name, commonPackage + name);
        }
    }
}
