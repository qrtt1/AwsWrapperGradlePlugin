package org.qty.aws.wrapper.lambda;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;

import com.google.common.base.Stopwatch;

public class PluginInstaller {

    private static Log logger = LogFactory.getLog(PluginInstaller.class);

    private static AtomicBoolean installed = new AtomicBoolean(false);
    public static String developMaven = "";

    public synchronized static void install() {
        if (installed.get()) {
            return;
        }

        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            File dir = new File(PluginInstaller.class.getResource("/").toURI());
            File buildScript = findBuildScript(dir, 0);

            BuildResult result = GradleRunner.create().withProjectDir(buildScript.getParentFile())
                    .withArguments("publishMavenJavaPublicationToMavenRepository").build();
            if (result.task(":publishMavenJavaPublicationToMavenRepository").getOutcome() != SUCCESS) {
                throw new RuntimeException("failed to install plugin to custom-maven");
            }

            File maven = new File(buildScript.getParent(), "build/repo");
            if (!maven.exists()) {
                throw new RuntimeException("failed to locate custom-maven: " + maven.getAbsolutePath());
            }
            developMaven = maven.getAbsolutePath();
            installed.set(true);
        } catch (Exception e) {
            throw new RuntimeException("Cannot install plugin !!!", e);
        } finally {
            stopwatch.stop();
            logger.info("install plugin in " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds.");
            System.err.println("install plugin in " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds.");
        }
    }

    public static File findBuildScript(File dir, int depth) {
        File buildScript = new File(dir, "build.gradle");
        if (buildScript.exists()) {
            return buildScript;
        }

        if (depth > 3) {
            throw new RuntimeException("no build.gradle found at " + dir);
        }

        return findBuildScript(dir.getParentFile(), depth + 1);
    }

}
