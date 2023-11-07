import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableElimination {

    private Network network;

    public VariableElimination(Network network) {
        this.network = network;
    }

    public Factor run(Scenario scenario) {
        List<Factor> factors = new ArrayList<>();
        // Convert probabilities in the network to factors and reduce them by evidence if necessary.
        for (Probability prob : network.getProbabilities()) {
            Factor factor = convertProbabilityToFactor(prob);
            factor.reduceFactor(scenario.getEvidence());
            factors.add(factor);
        }

        // Get elimination order
        List<Variable> eliminationOrder = network.getEliminationOrder(scenario);

        // Eliminate variables according to the order
        for (Variable var : eliminationOrder) {
            Factor combinedFactor = null;
            List<Factor> toRemove = new ArrayList<>();
            for (Factor factor : factors) {
                if (factor.getVariables().contains(var)) {
                    if (combinedFactor == null) {
                        combinedFactor = factor;
                    } else {
                        combinedFactor = combinedFactor.multiplyFactors(factor);
                    }
                    toRemove.add(factor);
                }
            }

            factors.removeAll(toRemove);

            if (combinedFactor != null) {
                Factor summedOutFactor = combinedFactor.sumOutVariable(var);
                factors.add(summedOutFactor);
            }
        }

        // Multiple remaining factors
        Factor result = null;
        for (Factor factor : factors) {
            if (result == null) {
                result = factor;
            } else {
                result = result.multiplyFactors(factor);
            }
        }

        if (result != null) {
            // Normalize the result
            result.normalize();
        }

        return result; // This factor now represents the distribution over the query variable(s)
    }

    private Factor convertProbabilityToFactor(Probability prob) {
        List<Variable> variables = new ArrayList<>();
        variables.addAll(prob.getParents());
        variables.add(prob.getVariable());

        Map<List<String>, Float> table = new HashMap<>();
        for (Map.Entry<List<String>, List<Float>> entry : prob.getDistributions().entrySet()) {
            List<String> variableAssignments = entry.getKey();
            List<Float> distributionValues = entry.getValue();

            for (int i = 0; i < distributionValues.size(); i++) {
                List<String> fullAssignment = new ArrayList<>(variableAssignments);
                fullAssignment.add(prob.getVariable().getValues().get(i));
                table.put(fullAssignment, distributionValues.get(i));
            }
        }

        return new Factor(variables, table);
    }


}
