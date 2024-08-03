package de.dion.argumentparser;

import java.io.File;

/**
 * ValueArgument is a subclass of EmptyArgument that supports arguments with values.
 */
public class ValueArgument extends EmptyArgument {

    private String value;
    private boolean required = false;

    public ValueArgument(String name, Object defaultValue, String description) {
        super(name, description);
        setValue(defaultValue);
        hasBeenSet = false;
    }

    public ValueArgument(String name, Object defaultValue) {
        this(name, defaultValue, "");
    }

    /**
     * Marks this argument as required.
     *
     * @return This ValueArgument instance for method chaining
     */
    public ValueArgument required() {
        setRequired(true);
        return this;
    }

    /**
     * Sets the description for this argument.
     *
     * @param description The description of the argument
     * @return This ValueArgument instance for method chaining
     */
    public ValueArgument description(String description) {
        setDescription(description);
        return this;
    }

    /**
     * Sets an alias for this argument.
     *
     * @param alias The alias of the argument
     * @return This ValueArgument instance for method chaining
     */
    public ValueArgument alias(String alias) {
        setAlias(alias);
        return this;
    }

    /**
     * Gets the value of this argument.
     * If the argument is required but not set, it will print the help message and exit the program.
     *
     * @return The value of the argument as a String
     */
    public String getValue() {
        if (isRequired() && !hasBeenSet) {
            printHelpMessage();
            System.exit(1);
        }
        return this.value;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isOptional() {
        return !isRequired();
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setValue(Object o) {
        if (o instanceof File) {
            this.value = ((File) o).getAbsolutePath();
        } else {
            this.value = o.toString();
        }
        super.set();
    }

    @Override
    public void printHelpMessage() {
        if (this.isRequired()) {
            System.out.println("This Argument is required.");
        } else {
            System.out.println("This Argument is optional.");
        }
        System.out.println("-" + this.name + " <Value>");
        System.out.println(this.description);
        System.out.println("Example: -" + this.name + " " + this.value);
    }
}
