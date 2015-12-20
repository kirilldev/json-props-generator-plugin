package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;

public class OutJsFileWriter {

    public static void writeToFile(Map<String, JSONObject> outObjects,
                                   String jsOutputProperyFile, int jsonFormatterIndent) throws MojoExecutionException {

        final File outFile = new File(jsOutputProperyFile);
        final boolean isExist;

        try {
            System.out.println(outFile.getAbsoluteFile());
            isExist = outFile.createNewFile();
        } catch (IOException e) {
            throw new MojoExecutionException("WTF", e);
        }

        try (PrintStream out = new PrintStream(new FileOutputStream(outFile, isExist))) {
            for (String varName : outObjects.keySet()) {
                out.print("var ");
                out.print(varName);
                out.print(" = ");
                out.print(outObjects.get(varName).toString(jsonFormatterIndent));
                out.print(";");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
