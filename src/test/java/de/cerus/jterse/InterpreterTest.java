package de.cerus.jterse;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class InterpreterTest {

    private final Interpreter interpreter = new Interpreter(true);

    @Test
    public void testBasicScript() {
        interpreter.reset();

        try {
            interpreter.run(new File("./examples/basics.terse"));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Map<String, Variable> vars = interpreter.getGlobalVariables();
        Assert.assertTrue(vars.containsKey("null"));
        Assert.assertNotNull(vars.getOrDefault("val", null));
        Assert.assertNotNull(vars.getOrDefault("math", null));
        Assert.assertNull(vars.get("null"));
        Assert.assertEquals("value", vars.get("val").toStr());
        Assert.assertEquals(6.0f, vars.get("math").toFloat(), 0);
    }

    @Test
    public void testCondScript() {
        interpreter.reset();

        try {
            interpreter.run(new File("./examples/cond.terse"));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Map<String, Variable> vars = interpreter.getGlobalVariables();
        for (int i = 1; i < 7; i++) {
            Assert.assertTrue(vars.containsKey("works" + i));
        }
    }

}
