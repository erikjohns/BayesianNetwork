import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *  Takes a BIF file input and converts it into a Network
 *  @author Erik Johns
 */
public class FileParser {
    private final String fileName;
    private final Network network = new Network();

    // Constructor for FileParser class
    public FileParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Converts a BIF file into a Bayesian network
     * @return the Network created from the file
     */
    public Network parseBIF() {

        // Tries to open file
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.startsWith("variable")) {
                    Variable variable = parseVariable(scanner, line);
                    network.addVariable(variable);
                }

                if (line.startsWith("probability")) {
                    Probability probability = parseProbability(scanner, line);
                    network.addProbability(probability);
                }
            }
        }
        // If a file cannot be found, print the stack trace
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return network;
    }

    /**
     * Takes the entirety of a multi-line variable declaration and converts it into a Variable type
     * @param scanner The file scanner
     * @param line The last line read from the scanner
     * @return Variable created from file data
     */
    private Variable parseVariable(Scanner scanner, String line) {
        String varName = line.substring(9, line.length() - 2);

        // Process the type line
        String typeLine = scanner.nextLine().trim();
        if (!typeLine.contains("type discrete")) {
            throw new RuntimeException("Unexpected format: " + typeLine);
        }

        int startIndex = typeLine.indexOf('{') + 1;
        int endIndex = typeLine.lastIndexOf('}');
        String valuesPart = typeLine.substring(startIndex, endIndex).trim();
        String[] values = valuesPart.split(",\\s*");

        return new Variable(varName, "discrete", Arrays.asList(values));
    }

    /**
     * Takes the entirety of a multi-line probability declaration and converts it into a Probability type
     * @param scanner The file scanner
     * @param firstLine The last line read from the scanner
     * @return Probability created from file data
     */
    private Probability parseProbability(Scanner scanner, String firstLine) {
        firstLine = firstLine.substring(firstLine.indexOf("(") + 2, firstLine.indexOf(")") - 1);
        String[] firstLineSplit = firstLine.split("\\|");

        for (int i = 0; i < firstLineSplit.length; i++) {
            firstLineSplit[i] = firstLineSplit[i].replace(" ", "");
        }

        Variable variable = network.getVariableByString(firstLineSplit[0]);
        List<Variable> parentVars = new ArrayList<>();

        if (firstLineSplit.length > 1) {
            String[] parentVarNames = firstLineSplit[1].split(",");

            for (String varName : parentVarNames) {
                parentVars.add(network.getVariableByString(varName));
            }
        }

        Map<List<String>, List<Float>> distributions = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.contains("}")) {
                break;
            }

            List<String> values = new ArrayList<>();
            List<Float> valueDistributions = new ArrayList<>();
            if (line.startsWith("table")) {
                String floatString = line.substring(6, line.length() - 1);
                floatString = floatString.replace(" ", "");
                String[] floatStringSplit = floatString.split(",");
                for (String str : floatStringSplit) {
                    valueDistributions.add(Float.parseFloat(str));
                }

            } else {
                String condName = line.substring(1, line.indexOf(")"));
                condName = condName.replace(" ", "");
                String[] conds = condName.split(",");
                Collections.addAll(values, conds);

                String floatString = line.substring(line.indexOf(")") + 2, line.length() - 1);
                floatString = floatString.replace(" ", "");
                String[] floatStringSplit = floatString.split(",");
                for (String str : floatStringSplit) {
                    valueDistributions.add(Float.parseFloat(str));
                }
            }
            distributions.put(values, valueDistributions);
        }

        return new Probability(parentVars, variable, distributions);
    }


}
