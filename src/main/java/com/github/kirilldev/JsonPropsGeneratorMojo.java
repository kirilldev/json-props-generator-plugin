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

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class JsonPropsGeneratorMojo extends AbstractMojo {

    //private static Log logger = new SystemStreamLog();

    @Parameter(required = true)
    private Map<String, File> jsonObjToSrcFiles;

    /**
     * Output directory for js file.
     */
    @Parameter(required = true, property = "out.directory", defaultValue = "${project.build.directory}")
    private File jsOutputProperyFile;

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
     * Js property name.
     */
    @Parameter(required = false, property = "js.property.object.name")
    private String forceSingleRootForOutProps;

    public void execute() throws MojoExecutionException {
        if (!isFlatMode) {
            throw new UnsupportedOperationException("It is not implemented yet!");
        }

        final Map<String, Properties> readProps = new HashMap<>();

        for (String variableName : jsonObjToSrcFiles.keySet()) {
            final File srcPropertyFile = jsonObjToSrcFiles.get(variableName);
            final Properties props = PlaceholderAwarePropertyReader.read(srcPropertyFile);
            readProps.put(variableName, props);
        }

        System.out.println(readProps);
    }
}
