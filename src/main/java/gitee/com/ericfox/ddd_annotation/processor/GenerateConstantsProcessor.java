package gitee.com.ericfox.ddd_annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import gitee.com.ericfox.ddd_annotation.DddPo;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GenerateConstantsProcessor extends AbstractProcessor {
    private static final String CLASS_SUFFIX = "Constants";
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "初始化");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(DddPo.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    continue;
                }

                TypeElement typeElement = (TypeElement) annotatedElement;
                String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).toString();
                String className = typeElement.getSimpleName().toString();
                String constantClassName = className + CLASS_SUFFIX;

                TypeSpec.Builder builder = TypeSpec.classBuilder(constantClassName)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

                for (Element member : typeElement.getEnclosedElements()) {
                    if (member.getKind() != ElementKind.FIELD) {
                        continue;
                    }
                    String memberName = member.getSimpleName().toString();
                    String constantName = toConstantName(memberName);
                    builder.addField(FieldSpec.builder(String.class, constantName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", memberName)
                            .build());
                }

                JavaFile javaFile = JavaFile.builder(packageName, builder.build())
                        .build();

                try {
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private String toConstantName(String memberName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < memberName.length(); i++) {
            char c = memberName.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }
}
