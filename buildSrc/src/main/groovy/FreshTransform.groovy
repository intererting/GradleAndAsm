import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import org.apache.commons.io.FileUtils
import org.objectweb.asm.*
import utils.MyLogger

import java.util.regex.Pattern

class FreshTransform extends Transform {

    def rRegex = Pattern.compile(".*R.*\\.class\$")

    @Override
    String getName() {
        return "freshTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT)
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

//        // scan all jars
//        transformInvocation.inputs.jarInputs.each { JarInput jarInput ->
//            File src = jarInput.file
//            // rename jar files
//            def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
//            if (destName.endsWith(".jar")) {
//                destName = destName.substring(0, destName.length() - 4)
//            }
//            String destName = jarInput.name
//            File dest = transformInvocation.outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
//            FileUtils.copyFile(src, dest)
//        }

        transformInvocation.inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                MyLogger.e("dest   $dest")
                directoryInput.file.eachFileRecurse { File file ->
                    if (file.isFile() && !rRegex.matcher(file.absolutePath) && file.absolutePath.endsWith(".class")) {
                        //获取所有的.class文件
                        scanClass(new FileInputStream(file))
                    }
                }
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }

    }

    static void scanClass(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        MyClassVisitor cv = new MyClassVisitor(Opcodes.ASM6, cw)
        cr.accept(cv, 0)
        inputStream.close()
    }

    static class MyClassVisitor extends ClassVisitor {

        MyClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        @Override
        void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
            if (name == "testInitData") {
                mv = new MyMethodVisitor(Opcodes.ASM6, mv)
            }
            return mv
        }
    }

    static class MyMethodVisitor extends MethodVisitor {

        MyMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv)
        }

        @Override
        void visitInsn(int opcode) {

            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL
                        , "com/yly/gradleandasm/TestJavaClass"
                        , "initData"
                        , "()V"
                        , false)
            }
            super.visitInsn(opcode)
        }

        @Override
        void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals)
        }
    }

}

