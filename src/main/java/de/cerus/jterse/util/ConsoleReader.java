package de.cerus.jterse.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {

    private static BufferedReader reader;

    private ConsoleReader() {
    }

    public static void setup() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static String readLine() throws IOException {
        return reader.readLine();
    }

    public static void dispose() throws IOException {
        reader.close();
    }

}
