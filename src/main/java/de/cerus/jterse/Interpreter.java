package de.cerus.jterse;

import de.cerus.jterse.util.ConsoleReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Interpreter {

    private Functions functions = new Functions(this);

    private Map<String, Variable> globalVariables = new HashMap<>();
    private Deque<Float> stack = new LinkedList<>();

    private boolean noTab = false;
    private float previous = -1f;
    private int currentLine = 0;

    public Interpreter(boolean noTab) {
        this.noTab = noTab;
    }

    public Interpreter() {
        this(false);
    }

    private void evaluate(String command, String[] args) {
        switch (command.toLowerCase()) {
            case "push":
                stack.push(previous);
                break;
            case "decl":
                functions.declareVar(command, args);
                break;
            case "out":
                System.out.println(value(String.join(" ", args)));
                break;
            case "add":
                previous = functions.mathAdd(command, args);
                break;
            case "sub":
                previous = functions.mathSub(command, args);
                break;
            case "div":
                previous = functions.mathDiv(command, args);
                break;
            case "mult":
                previous = functions.mathMult(command, args);
                break;
            case "mod":
                previous = functions.mathMod(command, args);
                break;
            default:
                if (command.startsWith("@")) {
                    functions.setVar(command, args);
                    break;
                }

                if (value(command) == null)
                    reportError(command, "Invalid command" + ((command.contains(" ") && !noTab)
                            ? " (Hint: Did you forget --notab?)" : ""));
                break;
        }
    }

    public <T> T value(String command, Class<T> expected) {
        Object value = value(command);
        if (value == null) reportError(command, "No value available");

        if (!value.getClass().isAssignableFrom(expected))
            reportError(command, "Unexpected value type; exptected "
                    + expected.getSimpleName() + " but got " + value.getClass().getSimpleName());
        return (T) value;
    }

    public Object value(String command) {
        if (command.startsWith("@")) {
            Variable var = globalVariables.getOrDefault(command.substring(1), null);
            return var == null ? null : var.val();
        } else if (command.equals("in")) {
            try {
                return ConsoleReader.readLine();
            } catch (IOException e) {
                reportError(command, "Failed to read user input");
            }
        } else if (command.equals("pop")) {
            return stack.pop();
        } else if (command.equals("peek")) {
            return stack.peek();
        } else if (command.startsWith("\"") && command.endsWith("\"")) {
            return command.substring(1, command.length() - 1);
        } else if (command.matches("[0-9.]+")) {
            try {
                return Float.valueOf(command);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public void run(String line) {
        if (line == null) throw new IllegalStateException();
        if (line.equals("") || line.matches("\\s+")) return;
        if (line.startsWith(";")) return;

        String[] split = line.split(noTab ? "\\s+" : "\t+");
        String command = split[0];
        split = line.substring(command.length()).trim().split(" ");
        String[] args = split;

        try {
            evaluate(command, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    public void reportError(String stub, String details) {
        throw new ParseException("Error at line " + (currentLine + 1)
                + (stub == null ? "" : " (here: '" + stub + "')")
                + (details == null ? "" : " [details: " + details + "]"));
    }

    public void run(Iterable<String> iterable) {
        iterable.forEach(s -> {
            run(s);
            currentLine++;
        });
    }

    public void run(String[] lines) {
        run(Arrays.asList(lines));
    }

    public void run(File file) throws IOException {
        run(Files.readAllLines(file.toPath()));
    }

    public void reset() {
        globalVariables.clear();
        currentLine = 0;
    }

    public Map<String, Variable> getGlobalVariables() {
        return globalVariables;
    }

    public Deque<Float> getStack() {
        return stack;
    }
}
