package de.cerus.jterse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private Map<String, Variable> globalVariables = new HashMap<>();

    public Interpreter() {
    }

    public void run(String line) {

    }

    public void run(Iterable<String> iterable) {
        iterable.forEach(this::run);
    }

    public void run(String[] lines) {
        run(Arrays.asList(lines));
    }

    public void run(File file) throws IOException {
        run(Files.readAllLines(file.toPath()));
    }

}
