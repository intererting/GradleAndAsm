import org.gradle.api.Project
import org.gradle.api.logging.Logger

class MyLogger {
    static Logger logger

    static void make(Project project) {
        logger = project.getLogger()
    }

    static void i(String info) {
        if (null != info && null != logger) {
            logger.info("Plugin >>> " + info)
        }
    }

    static void e(String error) {
        if (null != error && null != logger) {
            logger.error("Plugin >>> " + error)
        }
    }

    static void w(String warning) {
        if (null != warning && null != logger) {
            logger.warn("Plugin >>> " + warning)
        }
    }
}
