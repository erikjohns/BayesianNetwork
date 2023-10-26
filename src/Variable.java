import java.util.List;

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

    public void addValue(String value) {
        values.add(value);
    }

    public void removeValue(String value) {
        values.remove(value);
    }

    public void setValue(int index, String value) {
        values.set(index, value);
    }
}
