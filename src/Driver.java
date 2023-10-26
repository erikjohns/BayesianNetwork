import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Driver {

    public static void main(String[] args) {
        String[] fileNames = {"alarm.bif", "child.bif", "hailfinder.bif", "insurance.bif", "win95pts.bif"};

        FileParser parser = new FileParser("./src/Networks/alarm.bif");
        Network network = parser.parseBIF();

        HashMap<String, String> littleEvidence = new HashMap<>();
        littleEvidence.put("HRBP", "HIGH");
        littleEvidence.put("CO", "LOW");
        littleEvidence.put("BP", "HIGH");

        HashMap<String, String> moderateEvidence = new HashMap<>();
        moderateEvidence.put("HRBP", "HIGH");
        moderateEvidence.put("CO", "LOW");
        moderateEvidence.put("BP", "HIGH");
        moderateEvidence.put("HRSAT", "LOW");
        moderateEvidence.put("HREKG", "LOW");
        moderateEvidence.put("HISTORY", "TRUE");

        List<String> query = Arrays.asList("Disease");

        Scenario lowEvidenceScenario = new Scenario(littleEvidence, query);
        Scenario moderateEvidenceScenario = new Scenario(moderateEvidence, query);

        network.printNetwork();



    }
}
