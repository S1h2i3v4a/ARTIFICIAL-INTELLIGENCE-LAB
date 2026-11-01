/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 01: Implement Breadth First Search (BFS) for a given graph or maze.
 */

import java.util.*;

public class Practical01 {

    // --- Part A: Graph BFS ---
    public static void runGraphBFS() {
        System.out.println("--- Part A: Graph Traversal & Shortest Path via BFS ---");
        
        // Define graph using Adjacency List
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("A", "D", "E"));
        graph.put("C", Arrays.asList("A", "F", "G"));
        graph.put("D", Arrays.asList("B"));
        graph.put("E", Arrays.asList("B", "F"));
        graph.put("F", Arrays.asList("C", "E"));
        graph.put("G", Arrays.asList("C"));

        String start = "A";
        String goal = "F";

        System.out.println("Graph Adjacency List: " + graph);
        System.out.println("Start Node: " + start + ", Goal Node: " + goal);

        // BFS Queue and tracking structures
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new LinkedHashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        queue.add(start);
        visited.add(start);
        parentMap.put(start, null);

        List<String> traversalOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            traversalOrder.add(current);

            if (current.equals(goal)) {
                break;
            }

            for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        System.out.println("BFS Exploration Order: " + String.join(" -> ", traversalOrder));

        // Path reconstruction
        List<String> path = new ArrayList<>();
        String curr = goal;
        while (curr != null) {
            path.add(curr);
            curr = parentMap.get(curr);
        }
        Collections.reverse(path);
        System.out.println("Shortest Path from " + start + " to " + goal + ": " + String.join(" -> ", path));
    }

    // --- Part B: Maze Solving with BFS ---
    static class Point {
        int r, c;
        Point(int r, int c) { this.r = r; this.c = c; }
        @Override
        public String toString() { return "(" + r + "," + c + ")"; }
    }

    public static void runMazeBFS() {
        System.out.println("\n--- Part B: 2D Maze Solving with BFS ---");
        int[][] maze = {
            {0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0}
        };

        Point start = new Point(0, 0);
        Point goal = new Point(4, 4);

        System.out.println("Maze Layout (0 = Walkable, 1 = Wall):");
        for (int[] row : maze) {
            System.out.println("  " + Arrays.toString(row));
        }

        int rows = maze.length, cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Point[][] parent = new Point[rows][cols];
        Queue<Point> queue = new LinkedList<>();

        queue.add(start);
        visited[start.r][start.c] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right
        boolean reached = false;

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p.r == goal.r && p.c == goal.c) {
                reached = true;
                break;
            }

            for (int[] d : directions) {
                int nr = p.r + d[0];
                int nc = p.c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && maze[nr][nc] == 0 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    parent[nr][nc] = p;
                    queue.add(new Point(nr, nc));
                }
            }
        }

        if (reached) {
            List<Point> path = new ArrayList<>();
            Point curr = goal;
            while (curr != null) {
                path.add(curr);
                curr = parent[curr.r][curr.c];
            }
            Collections.reverse(path);
            System.out.println("Start: " + start + ", Goal: " + goal);
            System.out.println("Shortest Path Length: " + (path.size() - 1) + " steps");
            System.out.println("Path Coordinates: " + path);
        } else {
            System.out.println("No path found.");
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 01: BREADTH FIRST SEARCH (BFS) IN JAVA");
        System.out.println("============================================================\n");
        runGraphBFS();
        runMazeBFS();
        System.out.println("\n[SUCCESS] Practical 01 executed successfully.");
    }
}
