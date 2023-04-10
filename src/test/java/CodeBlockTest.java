import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.junit.jupiter.api.Test;

public class CodeBlockTest {
    @Test
    public void test() {
        System.out.println(CodeBlock.of("输出1：$S", "str1"));
        System.out.println(CodeBlock.of("输出2：$L", "2"));
        try {
//            System.out.println(CodeBlock.of("输出3：$M", String.class.getMethod("toString", (Class<?>[]) null)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(CodeBlock.of("输出4：$T", ClassName.BOOLEAN));
        System.out.println(CodeBlock.of("输出5：$N", "name5"));
//        System.out.println(CodeBlock.of("$F", "field6"));
        System.out.println(CodeBlock.of("输出7：$E", "enum7"));
    }
}
