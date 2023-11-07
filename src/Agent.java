import java.util.List;
import java.util.Map;

/**
 * Resembles the agent performing the tests on the network
 * @author Erik Johns
 */
public class Agent {
    private Network network;

    public Agent(Network network) {
        this.network = network;
    }

    /**
     * Runs variable elimination on the network
     * @param scenario The evidence and query for the test
     * @return The results of the variable elimination
     */
    public Factor runVariableElimination(Scenario scenario) {
        VariableElimination variableElimination = new VariableElimination(network);
        List<Variable> eliminationOrder = network.getEliminationOrder(scenario);

        return variableElimination.run(scenario);
    }

    /**
     * Runs Gibbs Sampling on the network
     * @param scenario The evidence and query for the test
     * @param sampleCount The number of samples
     * @param burnIn The number of samples to skip during the start
     * @return The results of the Gibbs Sampling
     */
    public Map<String, Float> runGibbsSampling(Scenario scenario, int sampleCount, int burnIn) {
        GibbsSampling gibbsSampling = new GibbsSampling(network);

        return gibbsSampling.run(scenario.getEvidence(), scenario.getQuery(), sampleCount, burnIn);
    }

    /**
     * Runs both Variable Elimination and Gibbs Sampling on the network and outputs the results
     * @param scenarios The list of scenarios for the network
     */
    public void runTests(List<Scenario> scenarios) {

        for (Scenario scenario : scenarios) {

            Factor varElimResult = runVariableElimination(scenario);
            System.out.println(varElimResult);

            /*for (Map.Entry<List<String>, Float> entry : varElimResult.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

             */

            Map<String, Float> gibbsSamplingResult = runGibbsSampling(scenario, 100000, 1000);

            for (Map.Entry<String, Float> entry : gibbsSamplingResult.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

            System.out.println();
            System.out.println("----------------");
            System.out.println();

        }
    }

}
