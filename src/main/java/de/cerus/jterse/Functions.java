package de.cerus.jterse;

public class Functions {

    private Interpreter interpreter;

    public Functions(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void declareVar(String command, String[] args) {
        if (args.length == 0)
            interpreter.reportError(command, "Missing arguments: Name required");
        if (!args[0].startsWith("&"))
            interpreter.reportError(args[0], "Invalid argument: Missing '&'");

        interpreter.getGlobalVariables().put(args[0].substring(1), null);
    }

    public void setVar(String command, String[] args) {
        if (args.length == 0)
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
        if (args.length < 2)
            interpreter.reportError(command, "Missing argument(s)");

        return new float[]{interpreter.value(args[0], Float.class), interpreter.value(args[1], Float.class)};
    }

    public boolean condEq(String command, String[] args) {
        Object[] objArr = cond(command, args);
        return objArr[0].equals(objArr[1]);
    }

    public boolean condLt(String command, String[] args) {
        Object[] objArr = cond(command, args);

        if (!objArr[0].getClass().isAssignableFrom(Float.class))
            interpreter.reportError(String.join(" ", args), "Unexpected value type; " +
                    "expected Float but got " + objArr[0].getClass().getSimpleName());

        return ((float) objArr[0]) < ((float) objArr[1]);
    }

    public boolean condGt(String command, String[] args) {
        Object[] objArr = cond(command, args);

        if (!objArr[0].getClass().isAssignableFrom(Float.class))
            interpreter.reportError(String.join(" ", args), "Unexpected value type; " +
                    "expected Float but got " + objArr[0].getClass().getSimpleName());

        return ((float) objArr[0]) > ((float) objArr[1]);
    }

    public boolean condLe(String command, String[] args) {
        Object[] objArr = cond(command, args);

        if (!objArr[0].getClass().isAssignableFrom(Float.class))
            interpreter.reportError(String.join(" ", args), "Unexpected value type; " +
                    "expected Float but got " + objArr[0].getClass().getSimpleName());

        return ((float) objArr[0]) <= ((float) objArr[1]);
    }

    public boolean condGe(String command, String[] args) {
        Object[] objArr = cond(command, args);

        if (!objArr[0].getClass().isAssignableFrom(Float.class))
            interpreter.reportError(String.join(" ", args), "Unexpected value type; " +
                    "expected Float but got " + objArr[0].getClass().getSimpleName());

        return ((float) objArr[0]) >= ((float) objArr[1]);
    }

    private Object[] cond(String command, String[] args) {
        if (args.length < 2)
            interpreter.reportError(command, "Missing argument(s)");

        String rawArg1 = null;
        String rawArg2 = null;

        boolean str = false;
        StringBuilder strBuilder = new StringBuilder();

        int i = 0;
        for (String arg : args) {
            i++;

            if ((arg.equals("INT") || arg.equals("FLOAT") || arg.equals("String"))
                    && args[args.length - 1].equals(arg) && i == args.length)
                continue;

            if (arg.startsWith("\"") && arg.endsWith("\"")) {
                if (rawArg1 == null) {
                    rawArg1 = arg;
                } else {
                    rawArg2 = arg;
                }
                continue;
            }

            if (arg.startsWith("\"") || arg.endsWith("\"")) {
                if (str) {
                    strBuilder.append(arg).append(" ");
                    str = false;

                    if (rawArg1 == null) {
                        rawArg1 = strBuilder.toString();
                        rawArg1 = rawArg1.substring(0, rawArg1.length() - 1);
                    } else {
                        rawArg2 = strBuilder.toString();
                        rawArg2 = rawArg2.substring(0, rawArg2.length() - 1);
                    }

                    strBuilder = new StringBuilder();
                    continue;
                } else if (arg.startsWith("\"")) {
                    str = true;
                    strBuilder.append(arg).append(" ");
                    continue;
                }
            }

            if (str) {
                strBuilder.append(arg);
                continue;
            }

            if (rawArg1 == null) {
                rawArg1 = arg;
            } else if (rawArg2 == null) {
                rawArg2 = arg;
            }
        }

        if (rawArg1 == null || rawArg2 == null) {
            interpreter.reportError(String.join(" ", args), "Failed to parse arguments");
        }

        Object o1 = interpreter.value(rawArg1);
        Object o2 = interpreter.value(rawArg2);

        if(o1 == null ||o2 == null) {
            interpreter.reportError(String.join(" ", args), "Failed to parse arguments");
        }

        String lastArg = args[args.length - 1];
        if (lastArg.equals("FLOAT") || lastArg.equals("INT")) {
            if (!(o1 instanceof Float)) {
                try {
                    o1 = Float.parseFloat(o1.toString());
                } catch (NumberFormatException ignored) {
                    interpreter.reportError(String.join(" ", args), "Failed to cast " + o1 + " to " + lastArg);
                }
            }
            if (!(o2 instanceof Float)) {
                try {
                    o2 = Float.parseFloat(o2.toString());
                } catch (NumberFormatException ignored) {
                    interpreter.reportError(String.join(" ", args), "Failed to cast " + o2 + " to " + lastArg);
                }
            }
        }

        if (lastArg.equals("STRING")) {
            if (!(o1 instanceof String)) {
                o1 = o1.toString();
            }
            if (!(o2 instanceof String)) {
                o2 = o2.toString();
            }
        }

        if (o1.getClass() != o2.getClass()) {
            interpreter.reportError(rawArg1 + "; " + rawArg2, "Different types: Got "
                    + o1.getClass().getSimpleName() + " and " + o2.getClass().getSimpleName());
        }

        return new Object[]{o1, o2};
    }
}
