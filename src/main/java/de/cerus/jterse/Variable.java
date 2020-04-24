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

    public int toInt() {
        return (int) getValue();
    }

    public float toFloat() {
        return (float) getValue();
    }

    public String toString() {
        return (String) getValue();
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
