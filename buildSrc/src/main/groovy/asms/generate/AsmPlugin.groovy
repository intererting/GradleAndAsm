package asms.generate

import asms.generate.AsmTransform
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AsmPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {

        println 'xxxxx'

        def isApp = target.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            target.android.registerTransform(new AsmTransform())
        }
    }
}