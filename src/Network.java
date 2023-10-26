import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Network {
    private final List<Variable> variables = new ArrayList<>();
    private final List<Probability> probabilities = new ArrayList<>();


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

    /**
     *  Prints the Network in the style of the BIF files provided
     */
    public void printNetwork() {
        // Prints all the variables
        for (Variable v : getVariables()) {
            // Prints the variable name
            System.out.println("variable " + v.getName() + " {");

            // Prints the variable type and values
            System.out.print("  type " + v.getType() + " [ " + v.getValues().size() + " ] { ");
            List<String> varVals = v.getValues();

            for (int i = 0; i < varVals.size(); i++) {
                if (i == varVals.size() - 1) {
                    System.out.println(varVals.get(i) + " };");
                } else {
                    System.out.print(varVals.get(i) + ", ");
                }
            }
        }

        // Prints all the probabilities
        for (Probability p : getProbabilities()) {
            // Prints the probability variable
            System.out.print("probability ( " + p.getVariable().getName() + " | ");

            // Prints the probability parent variables
            List<Variable> parents = p.getParents();
            for (int i = 0; i < parents.size(); i++) {
                if (i == parents.size() - 1) {
                    System.out.println(parents.get(i).getName() + " ) {");
                } else {
                    System.out.print(parents.get(i).getName() + ", ");
                }
            }
            Map<List<String>, List<Float>> distributions = p.getDistributions();

            // Prints the probability distributions
            for (Map.Entry<List<String>, List<Float>> entry : distributions.entrySet()) {
                List<String> values = entry.getKey();
                List<Float> valueDistributions = entry.getValue();

                System.out.print("  (");
                for (int i = 0; i < values.size(); i++) {
                    if (i == values.size() - 1) {
                        System.out.print(values.get(i) + ") ");
                    } else {
                        System.out.print(values.get(i) + ", ");
                    }
                }

                for (int i = 0; i < valueDistributions.size(); i++) {
                    if (i == valueDistributions.size() - 1) {
                        System.out.println(valueDistributions.get(i) + ";");
                    } else {
                        System.out.print(valueDistributions.get(i) + ", ");
                    }
                }
            }
        }
    }
}
