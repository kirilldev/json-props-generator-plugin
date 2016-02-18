package com.github.kirilldev;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JavaScriptPropertiesGeneratorMojo extends AbstractMojo {

    /**
     * Source files to process.
     */
    @Parameter(required = true)
    private Map<String, File> jsonObjNameToSrcFile;

    /**
     * Path to output js property file.
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}")
    private String jsonOutputProperyFilePath;

    /**
     * In flat mode '.' character in property key will not create a new object in js.
     * i.e file with single java property:
     * <p>
     * java.property.key=value
     * <p>
     * in flat mode will be converted to:
     * <p>
     * <pre>
     * var PROPERTIES = {
     *      'java.property.key' : value
     * }
     * </pre>
     * <p>
     * if flat mode == false than resulting json will be:
     * <p>
     * <pre>
     * var PROPERTIES = {
     *  'java': {
     *      'property' : {
     *          'key' : value
     *      }
     *  }
     * }
     * </pre>
     */
    @Parameter(required = true, defaultValue = "true")
    private Boolean isFlatMode;

    /**
     * Affects on json formatting.
     */
    @Parameter(required = true, defaultValue = "4")
    private int jsonFormatterIndent;

    /**
     * If this value was passed all property values will have single root with this name.
     */
    @Parameter(required = false)
    private String jsonNameForAggregatedPropsRoot;

    /**
     * Has effect only if nameForAggregatedPropsRoot was passed
     */
    @Parameter(required = false, defaultValue = "false")
    private Boolean skipEnclosureLevelAfterAggregatedRoot;

    /**
     * @throws MojoExecutionException if execution failed.
     */
    public void execute() throws MojoExecutionException {
        final Map<String, Properties> jsonObjNameToPropertyFile = new HashMap<>();

        for (final String jsVarNameToCreate : jsonObjNameToSrcFile.keySet()) {
            final File srcPropertyFile = jsonObjNameToSrcFile.get(jsVarNameToCreate);
            final Properties props = PlaceholderAwarePropertyReader.read(srcPropertyFile);
            jsonObjNameToPropertyFile.put(jsVarNameToCreate, props);
        }

        final Map<String, JSONObject> jsonObjNameToOutObject = isFlatMode
                ? FlatJsPropertyGenerator.generate(jsonObjNameToPropertyFile)
                : AdvancedJsPropertyGenerator.generate(jsonObjNameToPropertyFile);

        OutJsFileWriter.writeToFile(jsonObjNameToOutObject, jsonOutputProperyFilePath, jsonFormatterIndent,
                jsonNameForAggregatedPropsRoot, skipEnclosureLevelAfterAggregatedRoot);
    }
}
