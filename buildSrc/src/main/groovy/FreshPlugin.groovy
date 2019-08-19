import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

//class FreshPlugin implements Plugin<Project> {
//
//    @Override
//    void apply(Project project) {
//        MyLogger.make(project)
//        def isApp = project.plugins.hasPlugin(AppPlugin)
//        if (isApp) {
//            project.android.registerTransform(new FreshTransform())
//        }
//    }
//}