package gitee.com.ericfox.ddd_annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import gitee.com.ericfox.ddd_annotation.DddPo;
import gitee.com.ericfox.ddd_annotation.util.StrUtil;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

/**
 * Getter代码生成实现类
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("gitee.com.ericfox.ddd_annotation.DddPo")
public class DddPoProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(DddPo.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                ClassName className = ClassName.get(typeElement);
                TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);

                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    VariableElement variableElement = (VariableElement) enclosedElement;
                    final String UPPER_FIELD_NAME = StrUtil.lowerCamelToUpperSnake(variableElement.getSimpleName().toString());

                    ClassName stringClassName = ClassName.get("java.lang", "String");
                    FieldSpec fieldSpec = FieldSpec.builder(stringClassName, UPPER_FIELD_NAME)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", UPPER_FIELD_NAME)
                            .build();
                    typeSpecBuilder.addField(fieldSpec);
                }
                JavaFile javaFile = JavaFile.builder(className.packageName(), typeSpecBuilder.build()).build();
                try {
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
                }
            }
        }
        return true;
    }
}
