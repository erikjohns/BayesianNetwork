import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class Agent {
    private Network network;
    private Scenario scenario;
    Map<Variable, LinkedList<Variable>> adj = new HashMap<>();
    private static Random random = new Random();

    public Agent(Network network) {
        this.network = network;
    }

    public List<Variable> topoSort(){
        Stack<Variable> stack = new Stack<>(); 
        Map<Variable, Boolean> visited = new HashMap<>();       
        for (Variable key: adj.keySet()) 
            visited.put(key, false); 
        for (Variable key: adj.keySet()) { 
            if (visited.get(key) == false) 
                dfs(key, visited, stack);
        }      
        List<Variable> res = new ArrayList<>();
        while (stack.empty() == false) 
        	res.add(stack.pop());
        return res;
    }
    private void dfs(Variable v, Map<Variable, Boolean> visited, Stack<Variable> stack) { 
        visited.put(v, true); 
        for (Variable i: adj.get(v)) {
            if (!visited.get(i))
                dfs(i, visited, stack); 
        } 
        stack.push(v); 
    }
    
    /*If you're confused on what I'm going for here, refer to the 
    Gibbs Sampling slides given from class. You can also ask me 
    questions. I inserted pseudo code of the algs for reference.
    
    Basically, I'm having trouble with the whole "Sample a random var
    from a probability" thing. I imagine that I need to grab a certain
    probability amongst the probability distributions, then do something
    from there, but I don't know how to approach doing that*/

    public List<Variable> forwardSample(Network n){
        /*X[1],...X[n] = topological order of X
        for(i=1,...,n){
            u[i] <- x<Pa(X[i])>  
            Sample(x[i] from P(X[i]|u[i]))
        }
        return (x[1],...,x[n])*/
        List<Variable> X = n.getVariables();
        X = topoSort();
        for(Variable x: X){
            //stuff
        }
        return null;
    }    

    public List<Variable> gibbsSampling(){
     /* Sample(x^0 from P^0(X))
        for(t=1,...,T){
            x^t <- x^(t-1)
            for(each X[i] in X){
                Sample(x^t[i] from P[F](X[i]|x[i]))
            }
        }
        return x^0,...,x^T     
    */
        List<Variable> initSample = forwardSample(network);
        int numSamples = 1000;
        for (int i = 0; i < numSamples; i++) {

            for (Variable v : network.getVariables()) {
                //stuff
            }
        }
        return null;
    }

}
