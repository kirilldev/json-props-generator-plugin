package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.*;
import java.util.Properties;


public class PlaceholderAwarePropertyReader {

    private interface Placeholders {
        String START_LINE_IGNORING = "!jsonPropsGenerator:ignoreLinesStart";
        String END_LINE_IGNORING = "!jsonPropsGenerator:ignoreLinesEnd";
    }

    public static Properties read(File srcPropertyFile) throws MojoExecutionException {
        final StringBuilder filteredProps = new StringBuilder();
        boolean ignoreLines = false;

        try (BufferedReader br = new BufferedReader(new FileReader(srcPropertyFile))) {
            for (String line; (line = br.readLine()) != null; ) {
                String trimmed = line.trim();
                if (Placeholders.START_LINE_IGNORING.equals(trimmed)) {
                    ignoreLines = true;
                    continue;
                } else if (Placeholders.END_LINE_IGNORING.equals(trimmed)) {
                    ignoreLines = false;
                    continue;
                }

                if (!ignoreLines) {
                    filteredProps.append(line).append("\n");
                }
            }

            final Properties javaProps = new Properties();
            javaProps.load(new StringReader(filteredProps.toString()));
            return javaProps;
        } catch (IOException e) {
            throw new MojoExecutionException("Got an error during processing " + srcPropertyFile + " file", e);
        }
    }
}
