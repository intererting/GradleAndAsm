package com.yly.asmprocessor

import com.yly.asmannotation.LoginCallback
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AsmProcessor : AbstractProcessor() {

    private lateinit var mFiler: Filer
    private lateinit var mMessager: Messager
    private lateinit var mElementUtils: Elements

    override fun init(processingEnv: ProcessingEnvironment) {
        mFiler = processingEnv.filer
        mMessager = processingEnv.messager
        mElementUtils = processingEnv.elementUtils
        super.init(processingEnv)
    }

    override fun process(
            annotations: MutableSet<out TypeElement>,
            roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(LoginCallback::class.java)
        for (element in elements) {
            log(element.simpleName.toString())
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(LoginCallback::class.java.canonicalName)
    }

    /**
     * 创建文件
     */
    private fun createFile() {
//        val pkgName = mElementUtils.getPackageOf()
    }

    /**
     * 日志打印
     */
    private fun log(log: String) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, log)
    }

}
