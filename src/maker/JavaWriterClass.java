package com.automation.jipstart.maker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JavaWriterClass {

    protected void writeFile(String directoryPAth, String className, String classType, StringBuilder stringBuilder) {
        try {
            FileWriter myWriter = new FileWriter(directoryPAth + className + classType + ".java");
            myWriter.write(stringBuilder.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    protected void makeDir(String directoryPath, String fileName) {
        File theDir = new File(directoryPath + fileName);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }
}
