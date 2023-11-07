import java.util.HashMap;
import java.util.List;

/**
 * Represent a Scenario (Evidence and Query)
 * @author Erik Johns
 */
public class Scenario {
    HashMap<String, String> evidence;
    List<String> query;

    /**
     * Constructor for Scenario with no evidence
     * @param query The variable(s) to be queried
     */
    public Scenario(List<String> query) {
        this.evidence = new HashMap<>();
        this.query = query;
    }

    /**
     * Constructor for Scenario with evidence
     * @param evidence The provided evidence
     * @param query The variable(s) to be queried
     */
    public Scenario(HashMap<String, String> evidence, List<String> query) {
        this.evidence = evidence;
        this.query = query;
    }

    /**
     * Fetches the provided evidence
     * @return the evidence Map as Strings
     */
    public HashMap<String, String> getEvidence() {
        return evidence;
    }

    /**
     * Fetches the query variables
     * @return the List of query variables as Strings
     */
    public List<String> getQuery() {
        return query;
    }
}
