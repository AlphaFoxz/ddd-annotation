import com.squareup.javapoet.CodeBlock;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CodeBlock of = CodeBlock.of("$L", "123");
        System.out.println(of);
        System.out.println(Arrays.toString(new String[]{""}));
    }
}
