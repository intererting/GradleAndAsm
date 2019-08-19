package asms.generate

import asms.generate.AsmTransform
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.MyLogger

class AsmPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        MyLogger.make(target)
        def isApp = target.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            target.android.registerTransform(new AsmTransform())
        }
    }
}