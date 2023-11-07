import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Performs Gibbs Sampling on a provided network
 */
public class GibbsSampling {

    private final Network network;
    private final Random random;

    /**
     * Constructor for Gibbs Sampling
     * @param network The network to perform the test on
     */
    public GibbsSampling(Network network) {
        this.network = network;
        this.random = new Random();
    }

    /**
     * Performs Gibbs Sampling on a Network
     * @param evidence The list of evidence provided for the test
     * @param queryVariables The list of variables to query the probability of
     * @param numSamples The number of samples to perform
     * @param burnIn The number of samples to be skipped in the beginning
     * @return The results of the test
     */
    public Map<String, Float> run(Map<String, String> evidence, List<String> queryVariables, int numSamples, int burnIn) {
        Map<String, Integer> samplesCount = new HashMap<>();
        List<String> allVariables = network.getVariables().stream().map(Variable::getName).toList();

        Map<String, String> currentState = initializeRandomState(evidence);

        for (int i = 0; i < numSamples + burnIn; i++) {
            for (String variable : allVariables) {
                if (!evidence.containsKey(variable)) {
                    Map<String, Float> conditionalDistribution = calculateConditionalDistribution(variable, currentState);
                    String sampledValue = sampleFromDistribution(conditionalDistribution);
                    currentState.put(variable, sampledValue);
                }
            }
            if (i >= burnIn) {
                incrementSampleCount(samplesCount, currentState, queryVariables);
            }
        }

        return calculateProbabilitiesFromCounts(samplesCount, numSamples);
    }

    /**
     * Initializes the random states for everything besides the evidence
     * @param evidence The provided known evidence
     * @return A new map of variables and randomized values
     */
    private Map<String, String> initializeRandomState(Map<String, String> evidence) {
        Map<String, String> state = new HashMap<>(evidence);
        List<String> allVariables = network.getVariables().stream().map(Variable::getName).toList();

        for (String var : allVariables) {
            if (!evidence.containsKey(var)) {
                Variable variable = network.getVariableByString(var);
                List<String> values = variable.getValues();
                String randomValue = values.get(random.nextInt(values.size()));
                state.put(var, randomValue);

            }
        }
        return state;
    }

    /**
     *
     * @param variable The name of the variable in question
     * @param currentState The current state being evaluated
     * @return
     */
    private Map<String, Float> calculateConditionalDistribution(String variable, Map<String, String> currentState) {
        Map<String, Float> distribution = new HashMap<>();
        Variable var = network.getVariableByString(variable);
        List<String> possibleValues = var.getValues();

        Probability prob = network.getProbabilities().stream()
                .filter(p -> p.getVariable().getName().equals(variable))
                .findFirst()
                .orElse(null);

        if (prob == null) {
            throw new IllegalArgumentException("No probability distribution found for variable: " + var.getName());
        }

        for (String value : possibleValues) {
            float probabilityForValue = 1f;

            List<String> parentValues = prob.getParents().stream()
                    .map(parent -> currentState.get(parent.getName()))
                    .collect(Collectors.toList());

            List<Float> probabilitiesForParentValues = prob.getDistributions().getOrDefault(parentValues, null);

            if (probabilitiesForParentValues != null) {
                int valueIndex = var.getValues().indexOf(value);
                probabilityForValue *= probabilitiesForParentValues.get(valueIndex);
            }

            distribution.put(value, probabilityForValue);
        }

        float total = (float) distribution.values().stream().mapToDouble(f -> f).sum();
        distribution.forEach((value, probValue) -> distribution.put(value, probValue / total));

        return distribution;
    }

    private String sampleFromDistribution(Map<String, Float> conditionalDistribution) {
        float p = random.nextFloat();
        float cumulativeProbability = 0.0f;

        for (Map.Entry<String, Float> entry : conditionalDistribution.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (p <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void incrementSampleCount(Map<String, Integer> samplesCount, Map<String, String> currentState, List<String> queryVariables) {
        StringBuilder keyBuilder = new StringBuilder();
        for (String var : queryVariables) {
            keyBuilder.append(var).append("=").append(currentState.get(var)).append(";");
        }
        String key = keyBuilder.toString();

        samplesCount.put(key, samplesCount.getOrDefault(key, 0) + 1);
    }

    private Map<String, Float> calculateProbabilitiesFromCounts(Map<String, Integer> samplesCount, int numSamples) {
        Map<String, Float> probabilities = new HashMap<>();
        for (Map.Entry<String, Integer> entry : samplesCount.entrySet()) {
            probabilities.put(entry.getKey(), entry.getValue() / (float) numSamples);
        }
        return probabilities;
    }

}
