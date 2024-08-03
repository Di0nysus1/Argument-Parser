package de.dion.argumentparser;

/**
 * EmptyArgument represents a command-line argument that does not require a value.
 * It can also serve as a base class for arguments with values.
 */
public class EmptyArgument {

    protected final String name;
    protected String alias = null;
    protected String description;
    protected boolean hasBeenSet;

    public EmptyArgument(String name, String description) {
        super();
        this.name = name;
        this.description = description;
        hasBeenSet = false;
    }

    public EmptyArgument(String name) {
        this(name, "");
    }

    /**
     * Sets the description for this argument.
     *
     * @param description The description of the argument
     * @return This EmptyArgument instance for method chaining
     */
    public EmptyArgument description(String description) {
        setDescription(description);
        return this;
    }

    /**
     * Sets an alias for this argument.
     *
     * @param alias The alias of the argument
     * @return This EmptyArgument instance for method chaining
     */
    public EmptyArgument alias(String alias) {
        setAlias(alias);
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean hasBeenSet() {
        return this.hasBeenSet;
    }

    public void set() {
        this.hasBeenSet = true;
    }

    /**
     * Prints the help message for this argument.
     */
    public void printHelpMessage() {
        System.out.println("This Argument is optional.");
        System.out.println("-" + this.name);
        System.out.println(this.description);
    }
}
