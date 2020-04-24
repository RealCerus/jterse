package de.cerus.jterse;

public class Variable {
    public enum Type {
        INT, FLOAT, STRING;
    }

    private Type type;
    private Object value;

    public Variable(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static Variable of(Object o) {
        if (o instanceof Float)
            return new Variable(Type.FLOAT, o);
        if (o instanceof Integer)
            return new Variable(Type.INT, o);
        if (o instanceof String)
            return new Variable(Type.STRING, o);
        return null;
    }

    public int toInt() {
        return (int) getValue();
    }

    public float toFloat() {
        return (float) getValue();
    }

    public String toStr() {
        return (String) getValue();
    }

    public Object val() {
        switch (type) {
            case INT:
                return toInt();
            case FLOAT:
                return toFloat();
            case STRING:
                return toStr();
        }
        return null;
    }

    @Override
    public String toString() {
        switch (type) {
            case INT:
                return String.valueOf((int) value);
            case FLOAT:
                return String.valueOf((float) value);
            case STRING:
                return (String) value;
            default:
                return "Variable{" + type.name() + ", " + value.toString() + "}";
        }
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
