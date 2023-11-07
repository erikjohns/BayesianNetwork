import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Factor {
    private List<Variable> variables;
    private Map<List<String>, Float> table;

    public Factor(List<Variable> variables, Map<List<String>, Float> table) {
        this.variables = variables;
        this.table = table;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public Map<List<String>, Float> getTable() {
        return table;
    }

    public void reduceFactor(Map<String, String> evidence) {
        Map<List<String>, Float> reducedTable = new HashMap<>();

        for (Map.Entry<List<String>, Float> entry : this.table.entrySet()) {
            boolean matchesEvidence = true;
            for (int i = 0; i < this.variables.size(); i++) {
                Variable var = this.variables.get(i);
                if (evidence.containsKey(var.getName()) && !evidence.get(var.getName()).equals(entry.getKey().get(i))) {
                    matchesEvidence = false;
                    break;
                }
            }
            if (matchesEvidence) {
                reducedTable.put(entry.getKey(), entry.getValue());
            }
        }

        this.table = reducedTable;
    }

    public Factor multiplyFactors(Factor other) {
        List<Variable> newVariables = new ArrayList<>(this.variables);
        for (Variable v : other.getVariables()) {
            if (!newVariables.contains(v)) {
                newVariables.add(v);
            }
        }

        Map<List<String>, Float> newTable = new HashMap<>();
        for (Map.Entry<List<String>, Float> thisEntry : this.table.entrySet()) {
            for (Map.Entry<List<String>, Float> otherEntry : other.getTable().entrySet()) {
                List<String> combinedAssignments = combineAssignments(
                        thisEntry.getKey(), otherEntry.getKey(),
                        this.variables, other.getVariables(),
                        newVariables
                );
                if (combinedAssignments != null) {
                    Float newValue = thisEntry.getValue() * otherEntry.getValue();
                    if (!newTable.containsKey(combinedAssignments)) {
                        newTable.put(combinedAssignments, newValue);
                    }
                }
            }
        }

        return new Factor(newVariables, newTable);
    }

    private List<String> combineAssignments(
            List<String> thisAssignment,
            List<String> otherAssignment,
            List<Variable> thisVariables,
            List<Variable> otherVariables,
            List<Variable> newVariables
    ) {
        Map<String, String> thisAssignmentsMap = new HashMap<>();
        Map<String, String> otherAssignmentsMap = new HashMap<>();
        for (int i = 0; i < thisVariables.size(); i++) {
            thisAssignmentsMap.put(thisVariables.get(i).getName(), thisAssignment.get(i));
        }
        for (int i = 0; i < otherVariables.size(); i++) {
            otherAssignmentsMap.put(otherVariables.get(i).getName(), otherAssignment.get(i));
        }

        List<String> combined = new ArrayList<>();

        for (Variable var : newVariables) {
            String varName = var.getName();
            // Check from 'this' assignments first
            if (thisAssignmentsMap.containsKey(varName)) {
                combined.add(thisAssignmentsMap.get(varName));
            } else if (otherAssignmentsMap.containsKey(varName)) {
                combined.add(otherAssignmentsMap.get(varName));
            } else {
                return null;
            }
        }

        return combined;
    }



    public Factor sumOutVariable(Variable var) {
        int varIndex = this.variables.indexOf(var);
        if (varIndex == -1) {
            throw new IllegalArgumentException("Variable " + var.getName() + " does not exist in the factor");
        }

        List<Variable> newVariables = new ArrayList<>(this.variables);
        newVariables.remove(varIndex);
        Map<List<String>, Float> newTable = new HashMap<>();

        for (Map.Entry<List<String>, Float> entry : this.table.entrySet()) {
            List<String> assignment = entry.getKey();
            if (assignment.size() <= varIndex) {
                throw new IllegalStateException("The assignment list does not contain the variable at the expected index. This suggests the factor table was not constructed correctly.");
            }

            List<String> assignmentWithoutVar = new ArrayList<>(assignment);
            assignmentWithoutVar.remove(varIndex);

            newTable.merge(assignmentWithoutVar, entry.getValue(), Float::sum);
        }

        return new Factor(newVariables, newTable);
    }




    public void normalize() {
        float sum = 0f;
        for (Float value : this.table.values()) {
            sum += value;
        }
        for (List<String> key : this.table.keySet()) {
            this.table.put(key, this.table.get(key) / sum);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Adding the list of variables
        sb.append("Variables: ").append(variables.toString()).append("\n");

        // Adding the probability table
        sb.append("Probability Table: \n");
        for (Map.Entry<List<String>, Float> entry : table.entrySet()) {
            sb.append(entry.getKey().toString()).append(" -> ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
