package com.github.kirilldev;

import java.io.File;
import java.net.URL;

public class Utils {

    public static File loadResourceFile(String path) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        assert url != null;
        return new File(url.getPath());
    }
}

