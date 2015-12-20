package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;
import org.json.JSONObject;

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
}
