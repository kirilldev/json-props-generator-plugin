package com.github.kirilldev;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

public class TestPlaceholderAwarePropertyReader {

    @Test
    public void itShouldReadFileWithoutPlaceholders() throws MojoExecutionException {
        //Given
        final File file = Utils.loadResourceFile("test.config0.properties");

        //When
        final Properties props = PlaceholderAwarePropertyReader.read(file);

        //Then
        assertEquals(3, props.entrySet().size());
        assertEquals("2353tg34g43b3", props.get("oauth.vk.secret"));
        assertEquals("v4334f34yh3b", props.get("oauth.vk.appid"));
        assertEquals("http://localhost:8080/test/redirect&sdfd=343", props.get("oauth.vk.redirect.url"));
    }

    @Test
    public void itShouldIgnoreLinesBetweenOpenAndClosingPlaceholders() throws MojoExecutionException {
        //Given
        final File file = Utils.loadResourceFile("test.config1.properties");

        //When
        final Properties props = PlaceholderAwarePropertyReader.read(file);

        //Then
        assertEquals(2, props.entrySet().size());
        assertEquals("565654323222", props.get("oauth.facebook.appid"));
        assertEquals("http://localhost:8080/test/link", props.get("oauth.facebook.redirect.url"));
    }

    @Test
    public void itShouldIgnoreAllLinesAfterOpenPlaceholder() throws MojoExecutionException {
        //Given
        final File file = Utils.loadResourceFile("test.config2.properties");

        //When
        final Properties props = PlaceholderAwarePropertyReader.read(file);

        //Then
        assertEquals(1, props.entrySet().size());
        assertEquals("343434344", props.get("oauth.google.appid"));
    }

    @Test(expected = MojoExecutionException.class)
    public void itShouldFail() throws MojoExecutionException {
        //Given
        final File file = new File("unexisting.properties");

        //When
        PlaceholderAwarePropertyReader.read(file);

        //Then exception
    }
}
