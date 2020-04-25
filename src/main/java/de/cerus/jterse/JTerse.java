package de.cerus.jterse;

import de.cerus.jterse.util.ConsoleReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JTerse {

    public static void main(String[] args) {
        boolean noTab = false;
        if (args.length >= 1 && args[0].equalsIgnoreCase("--notab")) {
            noTab = true;
            args = Arrays.copyOfRange(args, 1, args.length);
        }

        ConsoleReader.setup();
        Interpreter interpreter = new Interpreter(noTab);

        if (args.length > 0) {
            // Parse file arguments n stuff
            try {
                interpreter.run(new File(String.join(" ", args)));

                ConsoleReader.dispose();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to run interpreter");
            }
            return;
        }

        // Start interpreter in REPL mode
        System.out.println("Interpreter started in REPL mode");
        System.out.println();

        while (true) {
            try {
                String input = ConsoleReader.readLine();
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
            ConsoleReader.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
