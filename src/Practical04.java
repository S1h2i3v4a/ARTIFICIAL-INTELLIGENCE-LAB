/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 04: Implement Hill Climbing Algorithm for numerical optimization or pathfinding.
 */

import java.util.*;

public class Practical04 {

    // Objective function to maximize:
    // f(x) = sin(x) + sin((10/3)*x) + ln(x) - 0.5*x + 10 over domain [1.0, 10.0]
    public static double objectiveFunction(double x) {
        return Math.sin(x) + Math.sin((10.0 / 3.0) * x) + Math.log(x) - 0.5 * x + 10.0;
    }

    static class StepRecord {
        int step;
        double x;
        double val;
        StepRecord(int step, double x, double val) {
            this.step = step;
            this.x = x;
            this.val = val;
        }
    }

    public static List<StepRecord> simpleHillClimbing(double startX, double stepSize, double minX, double maxX, int maxIter) {
        List<StepRecord> history = new ArrayList<>();
        double currentX = startX;
        double currentVal = objectiveFunction(currentX);
        history.add(new StepRecord(0, currentX, currentVal));

        for (int step = 1; step <= maxIter; step++) {
            double leftX = Math.max(minX, currentX - stepSize);
            double rightX = Math.min(maxX, currentX + stepSize);

            double leftVal = objectiveFunction(leftX);
            double rightVal = objectiveFunction(rightX);

            double bestNeighborX = leftVal > rightVal ? leftX : rightX;
            double bestNeighborVal = Math.max(leftVal, rightVal);

            if (bestNeighborVal > currentVal) {
                currentX = bestNeighborX;
                currentVal = bestNeighborVal;
                history.add(new StepRecord(step, currentX, currentVal));
            } else {
                // Peak reached (local or global maximum)
                break;
            }
        }
        return history;
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 04: HILL CLIMBING ALGORITHM IN JAVA");
        System.out.println("============================================================\n");

        System.out.println("Objective Function: f(x) = sin(x) + sin((10/3)*x) + ln(x) - 0.5*x + 10");
        System.out.println("Search Domain: [1.0, 10.0]");

        // Run 1: Simple Hill Climbing starting from x = 2.0
        double startX = 2.0;
        double stepSize = 0.02;
        List<StepRecord> trace = simpleHillClimbing(startX, stepSize, 1.0, 10.0, 300);

        System.out.println("\n--- Run 1: Simple Hill Climbing (Start x = " + startX + ") ---");
        System.out.println("Total Iterations: " + (trace.size() - 1));
        StepRecord finalStep = trace.get(trace.size() - 1);
        System.out.printf("Local Maximum Reached: x = %.4f, f(x) = %.4f\n", finalStep.x, finalStep.val);

        System.out.println("\nSample Steps:");
        for (int i = 0; i < Math.min(5, trace.size()); i++) {
            StepRecord r = trace.get(i);
            System.out.printf("  Step %2d: x = %.4f, f(x) = %.4f\n", r.step, r.x, r.val);
        }
        if (trace.size() > 5) {
            System.out.println("  ... [Search climbed towards local ridge]");
            System.out.printf("  Step %2d: x = %.4f, f(x) = %.4f\n", finalStep.step, finalStep.x, finalStep.val);
        }

        // Run 2: Random Restart Hill Climbing to find global maximum
        System.out.println("\n--- Run 2: Random-Restart Hill Climbing (Overcoming Local Optima) ---");
        int restarts = 8;
        Random rng = new Random(42);

        System.out.printf("%-8s %-12s %-16s %-12s %-8s\n", "Run #", "Start x", "Peak x", "f(x)", "Steps");
        System.out.println("--------------------------------------------------------");

        double globalBestX = -1;
        double globalBestVal = Double.NEGATIVE_INFINITY;

        for (int r = 1; r <= restarts; r++) {
            double randStart = 1.0 + rng.nextDouble() * 9.0;
            List<StepRecord> rTrace = simpleHillClimbing(randStart, stepSize, 1.0, 10.0, 300);
            StepRecord rFinal = rTrace.get(rTrace.size() - 1);

            System.out.printf("%-8d %-12.4f %-16.4f %-12.4f %-8d\n", r, randStart, rFinal.x, rFinal.val, rTrace.size() - 1);

            if (rFinal.val > globalBestVal) {
                globalBestVal = rFinal.val;
                globalBestX = rFinal.x;
            }
        }
        System.out.println("--------------------------------------------------------");
        System.out.printf("Global Optimal Solution Found: x* = %.4f, f(x*) = %.4f\n", globalBestX, globalBestVal);

        System.out.println("\n[SUCCESS] Practical 04 executed successfully.");
    }
}
