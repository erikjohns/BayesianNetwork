import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableElimination {

    private Network network;

    public VariableElimination(Network network) {
        this.network = network;
    }

    public Map<List<String>, Float> run(Scenario scenario, List<Variable> eliminationOrder) {
        // Initialize
        List<Factor> factors = initializeFactors();

        // Observe
        factors = observeEvidence(factors, scenario.getEvidence());

        // Eliminate
        for (Variable variable : eliminationOrder) {
            factors = eliminateVariable(factors, variable);
        }

        // Final Multiplication
        Factor result = factors.get(0);
        for (int i = 1; i < factors.size(); i++) {
            result = multiplyFactors(result, factors.get(i));
        }

        List<Factor> finalFactors = new ArrayList<>();
        finalFactors.add(result);
        return evaluateQuery(finalFactors, scenario.getQuery());
    }

    private Map<List<String>, Float> evaluateQuery(List<Factor> factors, List<String> queryVariables) {
        Map<List<String>, Float> result = new HashMap<>();

        for (Factor factor : factors) {
            for (String queryVariable : queryVariables) {
                if (factor.getVariables().stream().anyMatch(v -> v.getName().equals(queryVariable))) {
                    result.putAll(factor.getTable());
                }
            }
        }

        return result;
    }

    private List<Factor> initializeFactors() {
        List<Factor> factors = new ArrayList<>();
        for (Probability prob : network.getProbabilities()) {
            factors.add(createFactor(prob));
        }
        return factors;
    }

    private List<Factor> observeEvidence(List<Factor> factors, HashMap<String, String> evidence) {
        for (int i = 0; i < factors.size(); i++) {
            Factor factor = factors.get(i);
            for (Variable variable : factor.getVariables()) {
                if (evidence.containsKey(variable.getName())) {
                    String observedValue = evidence.get(variable.getName());

                    Map<List<String>, Float> newTable = new HashMap<>();

                    for (Map.Entry<List<String>, Float> entry : factor.getTable().entrySet()) {
                        List<String> assignment = entry.getKey();
                        int variableIndex = factor.getVariables().indexOf(variable);

                        if (assignment.get(variableIndex).equals(observedValue)) {
                            newTable.put(assignment, entry.getValue());
                        }
                    }

                    factor = new Factor(factor.getVariables(), newTable);
                    factors.set(factors.indexOf(factor), new Factor(factor.getVariables(), newTable));
                }
            }
        }
        return factors;
    }

    /*
    private List<Factor> observeEvidence(List<Factor> factors, HashMap<String, String> evidence) {
        List<Factor> updatedFactors = new ArrayList<>();

        for (Factor factor : factors) {
            Factor updatedFactor = factor;

            for (Variable variable : factor.getVariables()) {
                if (evidence.containsKey(variable.getName())) {
                    String observedValue = evidence.get(variable.getName());

                    Map<List<String>, Float> newTable = new HashMap<>();

                    for (Map.Entry<List<String>, Float> entry : updatedFactor.getTable().entrySet()) {
                        List<String> assignment = entry.getKey();
                        int variableIndex = updatedFactor.getVariables().indexOf(variable);

                        if (assignment.get(variableIndex).equals(observedValue)) {
                            newTable.put(assignment, entry.getValue());
                        }
                    }

                    updatedFactor = new Factor(updatedFactor.getVariables(), newTable);
                }
            }

            updatedFactors.add(updatedFactor);
        }

        return updatedFactors;
    }

     */


    private List<Factor> eliminateVariable(List<Factor> factors, Variable variable) {
        List<Factor> involvedFactors = new ArrayList<>();
        System.out.println(factors);
        for (Factor factor : factors) {
            if (factor.getVariables().contains(variable)) {
                involvedFactors.add(factor);
            }
        }

        System.out.println("Factors before:");
        System.out.println(involvedFactors);

        if (involvedFactors.size() == 0) return factors;

        Factor multipliedFactor = involvedFactors.get(0);
        for (int i = 1; i < involvedFactors.size(); i++) {
            multipliedFactor = multiplyFactors(multipliedFactor, involvedFactors.get(i));
        }
        System.out.println("Factor after multiplication:");
        System.out.println(multipliedFactor);

        Factor summedFactor = sumOutVariable(multipliedFactor, variable);
        System.out.println("Factor after addition:");
        System.out.println(summedFactor);

        factors.removeAll(involvedFactors);
        factors.add(summedFactor);

        return factors;
    }

    public Factor createFactor(Probability prob) {
        List<Variable> variables = new ArrayList<>();
        variables.add(prob.getVariable());
        variables.addAll(prob.getParents());

        Map<List<String>, Float> table = new HashMap<>();

        for (Map.Entry<List<String>, List<Float>> entry : prob.getDistributions().entrySet()) {
            List<String> key = entry.getKey();
            List<Float> values = entry.getValue();

            for (int i = 0; i < values.size(); i++) {
                List<String> newKey = new ArrayList<>(key);
                newKey.add(prob.getVariable().getValues().get(i));
                table.put(newKey, values.get(i));
            }
        }

        return new Factor(variables, table);
    }

    public Factor multiplyFactors(Factor factor1, Factor factor2) {
        List<Variable> combinedVariables = new ArrayList<>(factor1.getVariables());
        for (Variable v : factor2.getVariables()) {
            if (!combinedVariables.contains(v)) {
                combinedVariables.add(v);
            }
        }

        Map<List<String>, Float> combinedTable = new HashMap<>();

        for (Map.Entry<List<String>, Float> entry1 : factor1.getTable().entrySet()) {
            for (Map.Entry<List<String>, Float> entry2 : factor2.getTable().entrySet()) {
                List<String> combinedKey = combineKeys(entry1.getKey(), entry2.getKey(), factor1, factor2);

                if (combinedKey != null) {
                    Float combinedProb = entry1.getValue() * entry2.getValue();
                    combinedTable.merge(combinedKey, combinedProb, Float::sum);
                }
            }
        }

        return new Factor(combinedVariables, combinedTable);

    }

    private List<String> combineKeys(List<String> key1, List<String> key2, Factor factor1, Factor factor2) {
        List<String> combinedKey = new ArrayList<>(key1);

        for (int i = 0; i < key2.size(); i++) {
            Variable var = factor2.getVariables().get(i);
            int indexInFactor1 = factor1.getVariables().indexOf(var);
            if (indexInFactor1 != -1) {
                if (!key1.get(indexInFactor1).equals(key2.get(i))) {
                    return null;
                }
            } else {
                combinedKey.add(key2.get(i));
            }
        }

        return combinedKey;
    }

    public Factor sumOutVariable(Factor factor, Variable variableToEliminate) {
        List<Variable> newVariables = new ArrayList<>(factor.getVariables());
        int indexToRemove = newVariables.indexOf(variableToEliminate);
        if (indexToRemove == -1) {
            throw new IllegalArgumentException("Variable to eliminate not found in the factor.");
        }
        newVariables.remove(variableToEliminate);

        Map<List<String>, Float> newTable = new HashMap<>();

        for (Map.Entry<List<String>, Float> entry : factor.getTable().entrySet()) {
            List<String> keyWithoutVariable = new ArrayList<>(entry.getKey());
            keyWithoutVariable.remove(indexToRemove);

            newTable.merge(keyWithoutVariable, entry.getValue(), Float::sum);
        }

        return new Factor(newVariables, newTable);
    }


    /*
    public Factor multiplyFactors(Factor factor1, Factor factor2) {
        List<Variable> combinedVariables = new ArrayList<>(factor1.getVariables());
        for (Variable v : factor2.getVariables()) {
            if (!combinedVariables.contains(v)) {
                combinedVariables.add(v);
            }
        }

        Map<List<String>, Float> combinedTable = new HashMap<>();

        for (Map.Entry<List<String>, Float> entry1 : factor1.getTable().entrySet()) {
            List<String> key1 = entry1.getKey();
            Float prob1 = entry1.getValue();

            for (Map.Entry<List<String>, Float> entry2 : factor2.getTable().entrySet()) {
                List<String> key2 = entry2.getKey();
                Float prob2 = entry2.getValue();

                boolean keysMatch = true;
                for (Variable v : factor1.getVariables()) {
                    if (factor2.getVariables().contains(v)) {
                        if (!key1.get(factor1.getVariables().indexOf(v)).equals(key2.get(factor2.getVariables().indexOf(v)))) {
                            keysMatch = false;
                            break;
                        }
                    }
                }

                if (keysMatch) {
                    List<String> combinedKey = new ArrayList<>(key1);
                    for (String val : key2) {
                        if (!combinedKey.contains(val)) {
                            combinedKey.add(val);
                        }
                    }

                    Float combinedProb = prob1 * prob2;
                    combinedTable.put(combinedKey, combinedProb);
                }
            }
        }

        return new Factor(combinedVariables, combinedTable);
    }

    public Factor sumOutVariable(Factor factor, Variable variableToEliminate) {
        List<Variable> newVariables = new ArrayList<>(factor.getVariables());
        newVariables.remove(variableToEliminate);

        Map<List<String>, Float> newTable = new HashMap<>();

        for (Map.Entry<List<String>, Float> entry : factor.getTable().entrySet()) {
            List<String> key = entry.getKey();
            Float prob = entry.getValue();

            List<String> newKey = new ArrayList<>(key);

            int indexToRemove = factor.getVariables().indexOf(variableToEliminate);
            if (indexToRemove != -1 && indexToRemove < newKey.size()) {
                newKey.remove(indexToRemove);
            } else {
                throw new IllegalArgumentException("Variable not found in factor's variables or newKey list is shorter than expected.");
            }

            newTable.put(newKey, newTable.getOrDefault(newKey, 0f) + prob);
        }

        return new Factor(newVariables, newTable);
    }

     */

}
