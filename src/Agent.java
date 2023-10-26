import java.util.List;

public class Agent {
    private Network network;
    private List<Variable> variables;
    private List<Probability> probabilities;

    public Agent(Network network) {
        this.network = network;
    }

}
