package gitee.com.ericfox.ddd_annotation.handler;

import com.google.auto.service.AutoService;
import gitee.com.ericfox.ddd_annotation.DddGetter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Getter代码生成实现类
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("gitee.com.ericfox.ddd_annotation.DddGetter")
public class DddGetterHandler extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(DddGetter.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                VariableElement field = (VariableElement) element;
                String fieldName = field.getSimpleName().toString();
                String fieldType = field.asType().toString();
                String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String getter = String.format("public %s %s() { return this.%s; }", fieldType, getterName, fieldName);
                try {
                    JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(field.getEnclosingElement().toString() + "Getter");
                    try (PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
                        out.println("package " + field.getEnclosingElement().toString() + ";");
                        out.println("public class " + field.getEnclosingElement().getSimpleName() + "Getter {");
                        out.println(getter);
                        out.println("}");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
