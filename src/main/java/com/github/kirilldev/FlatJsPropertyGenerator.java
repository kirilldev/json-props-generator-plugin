package com.github.kirilldev;

import net.minidev.json.JSONObject;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.net.URL;
import java.util.*;

public class FlatJsPropertyGenerator {

    public static Map<String, JSONObject> generate(Map<String, Properties> props) {
        Map<String, JSONObject> outPropsObj = new HashMap<>();

        for (String key : props.keySet()) {
            JSONObject obj = new JSONObject();
            Properties p = props.get(key);

            for (Object name : p.keySet()) {
                obj.put((String) name, p.getProperty((String) name));
            }

            outPropsObj.put(key, obj);
        }

        return outPropsObj;
    }

    public static void main(String[] args) throws MojoExecutionException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("app.test.config.properties");
        assert url != null;
        Properties read = PlaceholderAwarePropertyReader.read(new File(url.getPath()));
        final Map<String, Properties> readProps = new HashMap<>();
        readProps.put("java", read);
        System.out.println(generate(readProps));
    }
}
