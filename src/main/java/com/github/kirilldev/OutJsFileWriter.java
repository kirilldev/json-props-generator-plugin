package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OutJsFileWriter {

    public static void writeToFile(Map<String, JSONObject> outObjects, String jsOutputPropertyFile,
                                   int jsonFormatterIndent, String jsNameForAggregatedPropsRoot,
                                   boolean skipEnclosureLevelAfterAggregatedRoot) throws MojoExecutionException {

        final File outFile = new File(jsOutputPropertyFile);
        final boolean isExist;

        try {
            isExist = outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Can't create out file!", e);
        }

        try (final PrintStream out = new PrintStream(new FileOutputStream(outFile, isExist))) {

            if (jsNameForAggregatedPropsRoot != null) {
                final Map<String, JSONObject> aggregated = new HashMap<>();
                aggregated.put(jsNameForAggregatedPropsRoot,
                        aggregateData(outObjects, skipEnclosureLevelAfterAggregatedRoot));
                writeVarWithAssignedObject(out, aggregated, jsonFormatterIndent);
            } else {
                writeVarWithAssignedObject(out, outObjects, jsonFormatterIndent);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Error happened while writing to the out file!", e);
        }
    }

    private static JSONObject aggregateData(Map<String, JSONObject> outObjects,
                                            boolean skipEnclosureLevelAfterAggregatedRoot) {
        final JSONObject aggregatedRoot = new JSONObject();

        if (skipEnclosureLevelAfterAggregatedRoot) {
            for (String varName : outObjects.keySet()) {
                for (String key : outObjects.get(varName).keySet()) {
                    aggregatedRoot.put(key, outObjects.get(key));
                }
            }
        } else {
            for (String varName : outObjects.keySet()) {
                aggregatedRoot.put(varName, outObjects.get(varName));
            }
        }

        return aggregatedRoot;
    }

    private static void writeVarWithAssignedObject(PrintStream out,
                                              Map<String, JSONObject> outObjects, int jsonFormatterIndent) {
        for (final String varName : outObjects.keySet()) {
            out.print("var ");
            out.print(varName);
            out.print(" = ");
            out.print(outObjects.get(varName).toString(jsonFormatterIndent));
            out.print(";");
        }
    }
}
