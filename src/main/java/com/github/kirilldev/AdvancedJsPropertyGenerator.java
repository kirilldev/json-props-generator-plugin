package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class AdvancedJsPropertyGenerator {

    private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

    private static final String ERR_MSG = "Problem occurred during processing property with key: '%s'. " +
            "Your properties file can't be transformed to valid JSON object. " +
            "Use flat mode or avoid ambiguous property path.";

    public static Map<String, JSONObject> generate(Map<String, Properties> props) throws MojoExecutionException {
        final Map<String, JSONObject> outPropsObj = new HashMap<>();

        for (final String propsKey : props.keySet()) {
            outPropsObj.put(propsKey, convertToJson(props.get(propsKey)));
        }

        return outPropsObj;
    }

    private static JSONObject convertToJson(final Properties p) throws MojoExecutionException {
        final JSONObject outputObject = new JSONObject();

        for (Object o : p.keySet()) {
            final String propertyName = (String) o;
            final String[] propertyPath = DOT_PATTERN.split(propertyName);
            JSONObject temp = outputObject;

            for (int i = 0; i < propertyPath.length; i++) {
                final boolean isLastProperty = i == propertyPath.length - 1;
                final String outObjProperty = propertyPath[i];

                if (temp.has(outObjProperty)) {

                    if (isLastProperty) {
                        throw new MojoExecutionException(new Formatter().format(ERR_MSG, propertyName).toString());
                    }

                    temp = temp.getJSONObject(outObjProperty);
                } else {

                    if (isLastProperty) {
                        temp.put(outObjProperty, p.getProperty(propertyName));
                    } else {
                        final JSONObject subObject = new JSONObject();
                        temp.put(outObjProperty, subObject);
                        temp = subObject;
                    }
                }
            }
        }

        return outputObject;
    }
}
