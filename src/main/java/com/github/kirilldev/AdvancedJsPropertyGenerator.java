package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class AdvancedJsPropertyGenerator {

    private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

    public static Map<String, JSONObject> generate(Map<String, Properties> props) throws MojoExecutionException {
        Map<String, JSONObject> outPropsObj = new HashMap<>();

        for (String key : props.keySet()) {
            JSONObject outputObject = new JSONObject();
            Properties p = props.get(key);

            for (Object o : p.keySet()) {
                String fullKey = (String) o;
                String[] propertyPath = DOT_PATTERN.split(fullKey);

                JSONObject temp = outputObject;

                for (int i = 0; i < propertyPath.length; i++) {
                    boolean isLastProperty = i == propertyPath.length - 1;
                    String objProperty = propertyPath[i];

                    if (temp.has(objProperty)) {

                        if (isLastProperty) {
                            throw new MojoExecutionException("Problem occurred during processing property with key: '"
                                    + fullKey + "'." + "Your properties file can't be transformed to valid JSON " +
                                    "object. Use flat mode or avoid ambiguous property path.");
                        }

                        temp = temp.getJSONObject(objProperty);
                    } else {

                        if (isLastProperty) {
                            temp.put(objProperty, p.getProperty(fullKey));
                        } else {
                            JSONObject subObject = new JSONObject();
                            temp.put(objProperty, subObject);
                            temp = subObject;
                        }
                    }
                }
            }

            outPropsObj.put(key, outputObject);
        }

        return outPropsObj;
    }

    public static void main(String[] args) throws MojoExecutionException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("app.test.config.properties");
        assert url != null;
        Properties read = PlaceholderAwarePropertyReader.read(new File(url.getPath()));
        final Map<String, Properties> readProps = new HashMap<>();
        readProps.put("java", read);
        System.out.println(generate(readProps).get("java").toString(4));
    }
}
