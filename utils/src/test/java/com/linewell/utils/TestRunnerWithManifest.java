package com.linewell.utils;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;
import org.robolectric.res.FsFile;
import org.robolectric.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Robolectric单元测试自带Mainfect
 * Created by aaron on 2016-07-14.
 */
public class TestRunnerWithManifest extends RobolectricTestRunner {
    public TestRunnerWithManifest(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    public static FsFile resourceFile(String... pathParts) {
        return Fs.newFile(resourcesBaseDirFile()).join(pathParts);
    }

    private static File resourcesBaseDirFile() {
        File testDir = Util.file("src", "test", "resources");
        return hasTestManifest(testDir) ? testDir : Util.file("utils", "src", "test", "resources");
    }

    private static boolean hasTestManifest(File testDir) {
        return new File(testDir, "TestAndroidManifest.xml").isFile();
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest mainfect = new AndroidManifest(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets"));
        return mainfect;
    }
}
