import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class FreshTask extends DefaultTask {

    @TaskAction
    def hello() {
        println "hello task"
    }
}