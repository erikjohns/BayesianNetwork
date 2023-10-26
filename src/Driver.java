import java.util.List;
import java.util.Map;

public class Driver {

    public static void main(String[] args) {
        String[] fileNames = {"alarm.bif", "child.bif", "hailfinder.bif", "insurance.bif", "win95pts.bif"};

        FileParser parser = new FileParser("./src/Networks/child.bif");
        Network network = parser.parseBIF();

        network.printNetwork();

    }
}
