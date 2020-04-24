package de.cerus.jterse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JTerse {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();

        if (args.length > 0) {
            // Parse file arguments n stuff
            try {
                interpreter.run(new File(String.join(" ", args)));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to run interpreter");
            }
            return;
        }

        // Start interpreter in REPL mode
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Interpreter started in REPL mode");
        System.out.println();

        while (true) {
            try {
                String input = bufferedReader.readLine();
                if (input == null) throw new IllegalStateException();

                if (input.toLowerCase().matches("q|quit|exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                interpreter.run(input);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        interpreter.getGlobalVariables().forEach((s, variable) ->
                System.out.println(s + "\t\t: " + (variable == null ? "N/A" : variable.toString())));

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
