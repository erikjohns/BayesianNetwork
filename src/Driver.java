import java.util.*;

/**
 * The main class for the program
 * @author Erik Johns
 */
public class Driver {

    /**
     * The main function of the program
     * @param args Command Line arguments (not being used for this program)
     */
    public static void main(String[] args) {

        runAlarm();
        runChild();
        runHailfinder();
        runInsurance();
        runWin95Pts();

    }

    /**
     * Runs variable elimination and Gibbs sampling on the Alarm network
     */
    public static void runAlarm() {
        FileParser parser = new FileParser("./src/Networks/alarm.bif");
        Network network = parser.parseBIF();

        Agent agent = new Agent(network);
        agent.runTests(getAlarmScenarioList());
    }

    /**
     * Runs variable elimination and Gibbs sampling on the Child network
     */
    public static void runChild() {
        FileParser parser = new FileParser("./src/Networks/child.bif");
        Network network = parser.parseBIF();

        Agent agent = new Agent(network);
        agent.runTests(getChildScenarioList());
    }

    /**
     * Runs variable elimination and Gibbs sampling on the Hailfinder network
     */
    public static void runHailfinder() {
        FileParser parser = new FileParser("./src/Networks/hailfinder.bif");
        Network network = parser.parseBIF();

        Agent agent = new Agent(network);
        agent.runTests(getHailfinderScenarioList());
    }

    /**
     * Runs variable elimination and Gibbs sampling on the Insurance network
     */
    public static void runInsurance() {
        FileParser parser = new FileParser("./src/Networks/insurance.bif");
        Network network = parser.parseBIF();

        Agent agent = new Agent(network);
        agent.runTests(getInsuranceScenarioList());
    }

    /**
     * Runs variable elimination and Gibbs sampling on the Win95Pts network
     */
    public static void runWin95Pts() {
        FileParser parser = new FileParser("./src/Networks/win95pts.bif");
        Network network = parser.parseBIF();

        Agent agent = new Agent(network);
        agent.runTests(getWin95ptsScenarioList());
    }

    /**
     * Generates the various scenarios provided for the alarm network
     * @return the list of generated scenarios
     */
    public static List<Scenario> getAlarmScenarioList() {
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

        List<String> query = Arrays.asList("HYPOVOLEMIA", "LVFAILURE", "ERRLOWOUTPUT");

        Scenario[] scenarios = {new Scenario(query), new Scenario(littleEvidence, query), new Scenario(moderateEvidence, query)};

        return Arrays.asList(scenarios);
    }

    /**
     * Generates the various scenarios provided for the child network
     * @return the list of generated scenarios
     */
    public static List<Scenario> getChildScenarioList() {
        HashMap<String, String> littleEvidence = new HashMap<>();
        littleEvidence.put("LowerBodyO2", "<5");
        littleEvidence.put("RUQO2", ">=12");
        littleEvidence.put("CO2Report", ">=7.5");
        littleEvidence.put("XrayReport", "Asy/Patchy");

        HashMap<String, String> moderateEvidence = new HashMap<>(littleEvidence);
        moderateEvidence.put("GruntingReport", "Yes");
        moderateEvidence.put("LVHReport", "Yes");
        moderateEvidence.put("Age", "11-30 Days");

        List<String> query = Arrays.asList("Disease");

        Scenario[] scenarios = {new Scenario(query), new Scenario(littleEvidence, query), new Scenario(moderateEvidence, query)};
        return Arrays.asList(scenarios);
    }

    /**
     * Generates the various scenarios provided for the hailfinder network
     * @return the list of generated scenarios
     */
    public static List<Scenario> getHailfinderScenarioList() {
        HashMap<String, String> littleEvidence = new HashMap<>();
        littleEvidence.put("RSFcst", "XNIL");
        littleEvidence.put("N32StarFcst", "XNIL");
        littleEvidence.put("MountainFcst", "XNIL");
        littleEvidence.put("AreaMoDryAir", "VeryWet");

        HashMap<String, String> moderateEvidence = new HashMap<>(littleEvidence);
        moderateEvidence.put("CombVerMo", "Down");
        moderateEvidence.put("AreaMeso_ALS", "Down");
        moderateEvidence.put("CurPropConv", "Strong");

        List<String> query = Arrays.asList("SatContMoist", "LLIW");

        Scenario[] scenarios = {new Scenario(query), new Scenario(littleEvidence, query), new Scenario(moderateEvidence, query)};
        return Arrays.asList(scenarios);
    }

    /**
     * Generates the various scenarios provided for the insurance network
     * @return the list of generated scenarios
     */
    public static List<Scenario> getInsuranceScenarioList() {
        HashMap<String, String> littleEvidence = new HashMap<>();
        littleEvidence.put("Age", "Adolescent");
        littleEvidence.put("GoodStudent", "False");
        littleEvidence.put("SeniorTrain", "False");
        littleEvidence.put("DrivQuality", "Poor");

        HashMap<String, String> moderateEvidence = new HashMap<>(littleEvidence);
        moderateEvidence.put("MakeModel", "Luxury");
        moderateEvidence.put("CarValue", "FiftyThousand");
        moderateEvidence.put("DrivHistory", "Zero");

        List<String> query = Arrays.asList("MedCost", "IliCost", "PropCost");

        Scenario[] scenarios = {new Scenario(query), new Scenario(littleEvidence, query), new Scenario(moderateEvidence, query)};
        return Arrays.asList(scenarios);
    }

    /**
     * Generates the various scenarios provided for the win95pts network
     * @return the list of generated scenarios
     */
    public static List<Scenario> getWin95ptsScenarioList() {
        HashMap<String, String> evidence1 = new HashMap<>();
        evidence1.put("Problem1", "No_Output");

        HashMap<String, String> evidence2 = new HashMap<>();
        evidence1.put("Problem2", "Too_Long");

        HashMap<String, String> evidence3 = new HashMap<>();
        evidence1.put("Problem3", "No");

        HashMap<String, String> evidence4 = new HashMap<>();
        evidence1.put("Problem4", "No");

        HashMap<String, String> evidence5 = new HashMap<>();
        evidence1.put("Problem5", "No");

        HashMap<String, String> evidence6 = new HashMap<>();
        evidence1.put("Problem6", "Yes");

        List<String> query = Arrays.asList("Problem1", "Problem2", "Problem3", "Problem4", "Problem5", "Problem6");

        Scenario[] scenarios = {new Scenario(query), new Scenario(evidence1, query), new Scenario(evidence2, query), new Scenario(evidence3, query), new Scenario(evidence4, query), new Scenario(evidence5, query), new Scenario(evidence6, query)};
        return Arrays.asList(scenarios);
    }
}
