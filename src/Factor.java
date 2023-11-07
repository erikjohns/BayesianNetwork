import java.util.List;
import java.util.Map;

public class Factor {
    private List<Variable> variables;
    private Map<List<String>, Float> table;

    public Factor(List<Variable> variables, Map<List<String>, Float> table) {
        this.variables = variables;
        this.table = table;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public Map<List<String>, Float> getTable() {
        return table;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Adding the list of variables
        sb.append("Variables: ").append(variables.toString()).append("\n");

        // Adding the probability table
        sb.append("Probability Table: \n");
        for (Map.Entry<List<String>, Float> entry : table.entrySet()) {
            sb.append(entry.getKey().toString()).append(" -> ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
