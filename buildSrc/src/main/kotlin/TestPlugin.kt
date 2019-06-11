import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("=========================")
        println("testPlugin")
        println("=========================")
    }
}