import java.util.List;
import java.util.Map;

/**
 *  Represents a probability within the network
 * @author Erik Johns
 */
public class Probability {
    private List<Variable> parents;
    private Variable variable;
    private Map<List<String>, List<Float>> distributions;

    public Probability(List<Variable> parents, Variable variable, Map<List<String>, List<Float>> distributions) {
        this.parents = parents;
        this.variable = variable;
        this.distributions = distributions;
    }

    public List<Variable> getParents() {
        return parents;
    }

    public Variable getVariable() {
        return variable;
    }

    public Map<List<String>, List<Float>> getDistributions() {
        return distributions;
    }
}
