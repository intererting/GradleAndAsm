package asms.generate

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.*
import utils.MyLogger

import java.util.regex.Pattern

class AsmTransform extends Transform {

    def rRegex = Pattern.compile(".*R.*\\.class\$")

    @Override
    String getName() {
        return 'asmTranform'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT)
//        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        // output file
        transformInvocation.inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                String destName = jarInput.name
                // rename jar files
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4)
                }
                // input file
                File src = jarInput.file
                // output file
                File dest = transformInvocation.outputProvider.getContentLocation(destName + "_" + hexName,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(src, dest)
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                directoryInput.file.eachFileRecurse { File file ->
                    if (file.isFile() && !rRegex.matcher(file.absolutePath) && file.absolutePath.endsWith(".class")) {
                        //获取所有的.class文件
//                        if (file.absolutePath.endsWith("Template.class")) {
                        scanMethod(file)
//                        }
                    }
                }
                // copy to dest
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
    }

    static void scanMethod(File file) {
        def inputStream = new FileInputStream(file)
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        MyClassVisitor cv = new MyClassVisitor(Opcodes.ASM6, cw)
        cr.accept(cv, 0)
        def outputStream = new FileOutputStream(file)
        outputStream.write(cw.toByteArray())
        inputStream.close()
        outputStream.close()
    }

    static class MyClassVisitor extends ClassVisitor {

        MyClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
//            if (name == "temp") {
            mv = new MyMethodVisitor(Opcodes.ASM6, mv)
//            }
            return mv
        }
    }

    void testGenAsm() {
        //测试asm
//        public class TestAsmClass {
//            public TestAsmClass();
//            Code:
//            0: aload_0
//            1: invokespecial #1                  // Method java/lang/Object."<init>":()V
//            4: return
//
//            public void run();
//            Code:
//            0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
//            3: ldc           #3                  // String This is my first ASM test
//            5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
//            8: return
//        }
        //生成以上字节码
        FileOutputStream outputStream = new FileOutputStream(new File('C:\\Users\\user\\Desktop\\TestAsmClass.class'))
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        //声明一个类，使用JDK1.8版本，public的类，父类是java.lang.Object，没有实现任何接口
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC,
                "com/yly/generate/TestAsmClass", null,
                "java/lang/Object", null)
        //初始化一个无参的构造函数
        MethodVisitor constructor = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        //参照上面字节码
        constructor.visitVarInsn(Opcodes.ALOAD, 0)
        //执行父类的init初始化
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        //从当前方法返回void
        constructor.visitInsn(Opcodes.RETURN)
        constructor.visitEnd()

        //创建get方法
        //()Ljava/lang/Integer;表示函数，无参数，返回值为：java.lang.Integer，注意最后面的分号，没有就会报错
        MethodVisitor getMethod = writer.visitMethod(Opcodes.ACC_PUBLIC, "getIntVal", "()Ljava/lang/Integer;", null, null);
        //将单字节的常量值(-128~127)推送至栈顶(如果不是-128~127之间的数字，则不能用bipush指令)
        getMethod.visitIntInsn(Opcodes.BIPUSH, 10);
        //调用Integer的静态方法valueOf把10转换成Integer对象
        String methodDesc = Type.getMethodDescriptor(Integer.class.getMethod("valueOf", int.class));
        getMethod.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", methodDesc, false);
        //从当前方法返回对象引用
        getMethod.visitInsn(Opcodes.ARETURN);
        getMethod.visitEnd();
        outputStream.write(writer.toByteArray())
        outputStream.close()

        //创建run方法
        //()V表示函数，无参数，无返回值
//        MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
//        //先获取一个java.io.PrintStream对象
//        runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])
//        runMethod.visitLdcInsn(message);
//        //执行println方法（执行的是参数为字符串，无返回值的println函数）
//        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//        runMethod.visitInsn(Opcodes.RETURN);
//        runMethod.visitMaxs(1, 1);
//        runMethod.visitEnd();
    }
}

class MyMethodVisitor extends MethodVisitor {

    boolean needLoginCheck = false

    MyMethodVisitor(int api, MethodVisitor mv) {
        super(api, mv)
    }

    @Override
    void visitCode() {
        super.visitCode()
        if (needLoginCheck) {
            //先获取一个java.io.PrintStream对象
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])
            mv.visitLdcInsn("before i got it");
            //执行println方法（执行的是参数为字符串，无返回值的println函数）
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }

    @Override
    void visitInsn(int opcode) {
        if (needLoginCheck && (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            //先获取一个java.io.PrintStream对象
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])
            mv.visitLdcInsn("after i got it");
            //执行println方法（执行的是参数为字符串，无返回值的println函数）
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode)
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc == 'Lcom/yly/manno/NeedLoginCheck;') {
            needLoginCheck = true
        }
        return super.visitAnnotation(desc, visible)
    }
}