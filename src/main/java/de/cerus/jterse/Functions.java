package de.cerus.jterse;

public class Functions {

    private Interpreter interpreter;

    public Functions(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void declareVar(String command, String[] args) {
        if(args.length == 0)
            interpreter.reportError(command, "Missing arguments: Name required");
        if(!args[0].startsWith("&"))
            interpreter.reportError(args[0], "Invalid argument: Missing '&'");

        interpreter.getGlobalVariables().put(args[0].substring(1), null);
    }

    public void setVar(String command, String[] args) {
        if(args.length == 0)
            interpreter.reportError(command, "Missing arguments: Value required");
        interpreter.getGlobalVariables().put(command.substring(1), Variable.of(interpreter.value(String.join(" ", args))));
    }

    public float mathAdd(String command, String[] args) {
        float[] math = math(command, args);
        return math[0] + math[1];
    }

    public float mathSub(String command, String[] args) {
        float[] math = math(command, args);
        return math[0] - math[1];
    }

    public float mathDiv(String command, String[] args) {
        float[] math = math(command, args);
        return math[0] / math[1];
    }

    public float mathMult(String command, String[] args) {
        float[] math = math(command, args);
        return math[0] * math[1];
    }

    public float mathMod(String command, String[] args) {
        float[] math = math(command, args);
        return math[0] % math[1];
    }

    private float[] math(String command, String[] args) {
        if(args.length < 2)
            interpreter.reportError(command, "Missing argument(s)");

        return new float[]{interpreter.value(args[0], Float.class), interpreter.value(args[1], Float.class)};
    }
}
