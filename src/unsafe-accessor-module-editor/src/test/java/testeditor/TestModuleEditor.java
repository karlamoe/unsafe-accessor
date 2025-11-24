package testeditor;

import moe.karla.usf.module.ModuleEditor;
import org.junit.jupiter.api.Test;

public class TestModuleEditor {
    @Test
    void test() {
        ModuleEditor editor = ModuleEditor.getInstance();

        editor.addExports(
                editor.getModule(TestModuleEditor.class),
                "testeditor",
                editor.getModule(TestModuleEditor.class)
        );
        editor.addOpens(
                editor.getModule(TestModuleEditor.class),
                "testeditor",
                editor.getModule(TestModuleEditor.class)
        );
        editor.addReads(
                editor.getModule(TestModuleEditor.class),
                editor.getModule(TestModuleEditor.class)
        );
    }

    @Test
    void testNamed() {
        ModuleEditor editor = ModuleEditor.getInstance();
        Object moduleThis = editor.getModule(TestModuleEditor.class);
        Object moduleLang = editor.getModule(Object.class);

        editor.addExports(moduleLang, "java.lang", moduleThis);
        editor.addOpens(moduleLang, "java.lang", moduleThis);
        editor.addReads(moduleLang, moduleThis);
    }
}
