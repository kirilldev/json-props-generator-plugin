# json-props-generator-plugin
Plugin can easly generete js file with json object properties from java property file

Simple configuration example:

        <plugins>
            ...
            <plugin>
                <groupId>com.github.kirilldev</groupId>
                <artifactId>json.props.generator</artifactId>
                <executions>
                    <execution>
                        <configuration>
                            <jsonObjNameToSrcFile>
                                <CONFIG>${basedir}/src/main/resources/java.config.properties</CONFIG>
                            </jsonObjNameToSrcFile>
                            <jsonOutputProperyFilePath>
                                ${project.basedir}/src/main/webapp/assets/js/properties.js
                            </jsonOutputProperyFilePath>
                        </configuration>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            ...
        </plugins>
        
It will generate "properties.js" file with content of "java.config.properties" inside assigned to variable named "CONFIG".


Plugin supports two marker keywords:

"!jsonPropsGenerator:ignoreLinesStart" - it means that all props following after this line will not get to the out file.
"!jsonPropsGenerator:ignoreLinesEnd" - use it in case if you want to disable line ignoring turned on by ignoreLinesStart.