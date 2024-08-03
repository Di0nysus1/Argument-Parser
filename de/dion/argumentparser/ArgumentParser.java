package de.dion.argumentparser;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * ArgumentParser is a utility class to parse and handle command-line arguments.
 * It supports both named arguments with values and flag-like arguments without values.
 */
public class ArgumentParser {

    private LinkedList<EmptyArgument> values = new LinkedList<>();
    private boolean shutDownIfHelpArgument = true;

    
    /**
     * Creates a new instance of the ArgumentParser.
     * 
     * Example usage:
     * <pre>
     * {@code
     * ArgumentParser ap = new ArgumentParser();
     * ap.add(new ValueArgument("headless", true, "Determines if the browser runs in headless mode. If TRUE, the windows will be invisible."));
     * ap.add(new ValueArgument("lang", "en:de", "Specifies the language pair for translation.").alias("language"));
     * ap.add(new ValueArgument("translator", "deepl", "Specifies the translation service to use. Possible values: \"google\" or \"deepl\""));
     * ap.add(new ValueArgument("driverpath", "C:\\path\\to\\geckodriver.exe", "Path to the Geckodriver binary.").alias("path").required());
     * ap.add(new EmptyArgument("penis2").description("Ein Test Argument"));
     * ap.parseArguments(args);
     * 
     * boolean headless = ap.getBoolean("headless");
     * File driverPath = ap.getFile("driverpath");
     * String lang = ap.getString("lang");
     * String translator = ap.getString("translator");
     * 
     * System.out.println(ap.getArgument("penis2").hasBeenSet());
     * System.out.println(headless);
     * }
     * </pre>
     */
    public ArgumentParser() {
    }

    /**
     * Parses the command-line arguments and populates the internal argument list.
     * If the -help argument is provided, it displays the help message and optionally exits the program.
     *
     * @param args Command-line arguments
     */
    public void parseArguments(String[] args) {

        // Check -help Argument
        if (args.length > 0 && args[0].toLowerCase().contains("-help")) {
            printHelp();
            if (this.shutDownIfHelpArgument) {
                System.exit(0);
            }
            return;
        }

        Map<String, String> argMap = new HashMap<>();

        // Argumente parsen
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                String name = args[i].substring(1).trim();
                if (i + 1 < args.length) {
                    String nextArg = args[i + 1];

                    if (nextArg.startsWith("-")) {
                        setEmptyArgument(name);
                    } else {
                        argMap.put(name, nextArg);
                        i++; // Überspringe den Wert des aktuellen Arguments
                    }

                } else {
                    setEmptyArgument(name);
                }
            }
        }

        if (argMap.isEmpty()) {
            return;
        }

        // Argumente in Liste einpflegen
        Iterator<String> names = argMap.keySet().iterator();
        Iterator<String> values = argMap.values().iterator();
        while (names.hasNext()) {
            setArgument(names.next(), values.next());
        }
    }

    /**
     * Adds a new argument definition to the parser.
     *
     * @param value The argument definition to add
     */
    public void add(EmptyArgument value) {
        if (!hasArgument(value.getName())) {
            values.add(value);
        }
    }

    /**
     * Retrieves the argument by name or alias.
     *
     * @param name The name or alias of the argument
     * @return The corresponding EmptyArgument object, or null if not found
     */
    public EmptyArgument getArgument(String name) {
        for (EmptyArgument val : this.values) {
            if (val.getName().equalsIgnoreCase(name)) {
                return val;
            }
        }
        for (EmptyArgument val : this.values) {
            if (val.getAlias() != null && val.getAlias().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

    /**
     * Checks if a specific argument has been set.
     *
     * @param name The name of the argument
     * @return True if the argument has been set, false otherwise
     */
    public boolean isSet(String name) {
        EmptyArgument ea = getArgument(name);
        return ea != null && ea.hasBeenSet();
    }

    /**
     * Retrieves a ValueArgument by its name.
     *
     * @param name The name of the ValueArgument
     * @return The corresponding ValueArgument object, or null if not found
     */
    public ValueArgument getValueArgument(String name) {
        EmptyArgument arg = getArgument(name);
        if (arg instanceof ValueArgument) {
            return (ValueArgument) arg;
        }
        return null;
    }

    /**
     * Marks an argument as set without a specific value.
     *
     * @param name The name of the argument
     */
    private void setEmptyArgument(String name) {
        EmptyArgument val = getArgument(name);
        if (val != null) {
            val.set();
        } else {
            val = new EmptyArgument(name);
            val.set();
            values.add(val);
        }
    }

    /**
     * Sets the value of a specific argument.
     *
     * @param name The name of the argument
     * @param o    The value to set
     */
    public void setArgument(String name, Object o) {
        ValueArgument val = getValueArgument(name);
        if (val != null) {
            val.setValue(o);
        } else {
            this.values.add(new ValueArgument(name, o));
        }
    }

    /**
     * Checks if a specific argument exists.
     *
     * @param name The name of the argument
     * @return True if the argument exists, false otherwise
     */
    public boolean hasArgument(String name) {
        return getArgument(name) != null;
    }

    /**
     * Retrieves the value of a specific argument as a String.
     *
     * @param name The name of the argument
     * @return The value of the argument as a String
     */
    public String getString(String name) {
        return getValueArgument(name).getValue();
    }

    /**
     * Retrieves the value of a specific argument as a boolean.
     *
     * @param name The name of the argument
     * @return The value of the argument as a boolean
     * @throws IllegalArgumentException If the argument value cannot be converted to a boolean
     */
    public boolean getBoolean(String name) {
        String val = getValueArgument(name).getValue();
        if (val.equalsIgnoreCase("true")) {
            return true;
        } else if (val.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException("\"" + val + "\" is not a Boolean!");
    }

    /**
     * Retrieves the value of a specific argument as an integer.
     *
     * @param name The name of the argument
     * @return The value of the argument as an integer
     * @throws NumberFormatException If the argument value cannot be converted to an integer
     */
    public int getInt(String name) {
        return Integer.parseInt(getValueArgument(name).getValue());
    }

    /**
     * Retrieves the value of a specific argument as a File object.
     *
     * @param name The name of the argument
     * @return The value of the argument as a File object
     */
    public File getFile(String name) {
        File out = new File(getValueArgument(name).getValue());
        return out;
    }

    /**
     * Checks if the program should shut down after displaying the help message.
     *
     * @return True if the program should shut down after showing help, false otherwise
     */
    public boolean isShutDownIfHelpArgument() {
        return shutDownIfHelpArgument;
    }

    /**
     * Sets whether the program should shut down after displaying the help message.
     *
     * @param shutDownIfHelpArgument True to shut down after showing help, false otherwise
     */
    public void setShutDownIfHelpArgument(boolean shutDownIfHelpArgument) {
        this.shutDownIfHelpArgument = shutDownIfHelpArgument;
    }

    /**
     * Prints the help message for the command-line arguments.
     */
    public void printHelp() {
        System.out.println("Usage: java -jar Programmname.jar [OPTIONS]");
        System.out.println();
        System.out.println("Options:");
        System.out.println();

        for (EmptyArgument vv : values) {
            vv.printHelpMessage();
            System.out.println("");
        }
        System.out.println("-help");
        System.out.println("Displays this help message.");
    }
}