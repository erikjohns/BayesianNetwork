import java.util.List;

/**
 * Represents a variable within the network
 * @author Erik Johns
 */
public class Variable {
    private String name;
    private String type;
    private List<String> values;

    public Variable(String name, String type, List<String> values) {
        this.name = name;
        this.type = type;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return name;
    }
}
