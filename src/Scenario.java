import java.util.HashMap;
import java.util.List;

public class Scenario {
    HashMap<String, String> evidence;
    List<String> query;

    // Constructor for no evidence
    public Scenario(List<String> query) {
        this.evidence = new HashMap<>();
        this.query = query;
    }

    // Constructor for evidence
    public Scenario(HashMap<String, String> evidence, List<String> query) {
        this.evidence = evidence;
        this.query = query;
    }

    public HashMap<String, String> getEvidence() {
        return evidence;
    }

    public List<String> getQuery() {
        return query;
    }
}
