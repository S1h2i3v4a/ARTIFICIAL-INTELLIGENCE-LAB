/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 02: Implement Depth First Search (DFS) for a tree or graph structure.
 */

import java.util.*;

public class Practical02 {

    // Recursive DFS
    public static void dfsRecursive(String node, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(node);
        order.add(node);

        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                dfsRecursive(neighbor, graph, visited, order);
            }
        }
    }

    // Iterative DFS using explicit Stack
    public static List<String> dfsIterative(String startNode, Map<String, List<String>> graph) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        stack.push(startNode);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                order.add(current);

                List<String> neighbors = graph.getOrDefault(current, Collections.emptyList());
                // Push in reverse order so left branches are processed first
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    String neighbor = neighbors.get(i);
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        return order;
    }

    // Cycle detection in Directed Graph using 3-color state
    // 0: Unvisited, 1: Visiting (in recursion stack), 2: Visited
    public static boolean hasCycleDFS(String node, Map<String, List<String>> graph, Map<String, Integer> state, List<String> cycleTrace) {
        state.put(node, 1);
        cycleTrace.add(node);

        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (state.getOrDefault(neighbor, 0) == 1) {
                cycleTrace.add(neighbor);
                return true; // Cycle detected
            }
            if (state.getOrDefault(neighbor, 0) == 0) {
                if (hasCycleDFS(neighbor, graph, state, cycleTrace)) {
                    return true;
                }
            }
        }
        state.put(node, 2);
        cycleTrace.remove(cycleTrace.size() - 1);
        return false;
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 02: DEPTH FIRST SEARCH (DFS) IN JAVA");
        System.out.println("============================================================\n");

        // Tree / Graph structure
        Map<String, List<String>> treeGraph = new LinkedHashMap<>();
        treeGraph.put("Root", Arrays.asList("Node_A", "Node_B", "Node_C"));
        treeGraph.put("Node_A", Arrays.asList("Node_D", "Node_E"));
        treeGraph.put("Node_B", Arrays.asList("Node_F"));
        treeGraph.put("Node_C", Arrays.asList("Node_G", "Node_H"));
        treeGraph.put("Node_D", Collections.emptyList());
        treeGraph.put("Node_E", Collections.emptyList());
        treeGraph.put("Node_F", Collections.emptyList());
        treeGraph.put("Node_G", Collections.emptyList());
        treeGraph.put("Node_H", Collections.emptyList());

        System.out.println("--- Tree / Graph Structure ---");
        for (Map.Entry<String, List<String>> entry : treeGraph.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }

        String start = "Root";

        // 1. Recursive DFS
        Set<String> visitedRec = new HashSet<>();
        List<String> recOrder = new ArrayList<>();
        dfsRecursive(start, treeGraph, visitedRec, recOrder);
        System.out.println("\nRecursive DFS Traversal Order:");
        System.out.println("  " + String.join(" -> ", recOrder));

        // 2. Iterative DFS
        List<String> iterOrder = dfsIterative(start, treeGraph);
        System.out.println("\nIterative DFS Traversal Order:");
        System.out.println("  " + String.join(" -> ", iterOrder));

        // 3. Cycle Detection
        System.out.println("\n--- Directed Graph Cycle Detection via DFS ---");
        Map<String, List<String>> cyclicGraph = new LinkedHashMap<>();
        cyclicGraph.put("1", Arrays.asList("2"));
        cyclicGraph.put("2", Arrays.asList("3"));
        cyclicGraph.put("3", Arrays.asList("4", "1")); // Back edge to 1
        cyclicGraph.put("4", Collections.emptyList());

        System.out.println("Graph Adjacency: " + cyclicGraph);
        Map<String, Integer> state = new HashMap<>();
        List<String> cycleTrace = new ArrayList<>();
        boolean cycleFound = false;

        for (String n : cyclicGraph.keySet()) {
            if (state.getOrDefault(n, 0) == 0) {
                if (hasCycleDFS(n, cyclicGraph, state, cycleTrace)) {
                    cycleFound = true;
                    break;
                }
            }
        }

        System.out.println("Cycle Detected: " + cycleFound);
        if (cycleFound) {
            System.out.println("Cycle Path / Back-edge: " + String.join(" -> ", cycleTrace));
        }

        System.out.println("\n[SUCCESS] Practical 02 executed successfully.");
    }
}
