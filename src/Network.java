import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Network {
    private List<Variable> variables = new ArrayList<>();
    private List<Probability> probabilities = new ArrayList<>();


    public void addVariable(Variable v) {
        variables.add(v);
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void addProbability(Probability p) {
        probabilities.add(p);
    }

    public List<Probability> getProbabilities() {
        return probabilities;
    }

    public Variable getVariableByString(String varName) {
        for (Variable v : variables) {
            if (v.getName().equals(varName)) {
                return v;
            }
        }
        return null;
    }

    public void printNetwork() {
        for (Variable v : getVariables()) {
            System.out.println("variable " + v.getName() + " {");
            System.out.println("  type " + v.getType() + " [ " + v.getValues().size() + " ] { " + v.getValues() + " };");
        }

        for (Probability p : getProbabilities()) {
            System.out.println("probability ( " + p.getVariable() + " | " + p.getParents() + " ) {");
            Map<List<String>, List<Float>> distributions = p.getDistributions();

            for (Map.Entry<List<String>, List<Float>> entry : distributions.entrySet()) {
                System.out.println("  (" + entry.getKey() + " " + entry.getValue() + ";");
            }
        }
    }
}
