package com.github.kirilldev;

import org.json.JSONObject;

import java.util.*;

public class FlatJsPropertyGenerator {

    public static Map<String, JSONObject> generate(Map<String, Properties> props) {
        final Map<String, JSONObject> outPropsObj = new HashMap<>();

        for (String propsKey : props.keySet()) {
            final JSONObject generatedObj = new JSONObject();
            final Properties p = props.get(propsKey);

            for (final Object propertyName : p.keySet()) {
                generatedObj.put((String) propertyName, p.getProperty((String) propertyName));
            }

            outPropsObj.put(propsKey, generatedObj);
        }

        return outPropsObj;
    }
}
