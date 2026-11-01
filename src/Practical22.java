/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 22: Perform simple Bayesian reasoning for decision making in Java.
 */

import java.util.*;

public class Practical22 {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 22: BAYESIAN REASONING IN MEDICAL DIAGNOSIS (JAVA)");
        System.out.println("============================================================\n");

        // Medical Decision Problem Formulation:
        // Disease: Rare Medical Condition (D)
        // Test: Diagnostic Lab Test (+ or -)
        // Prior Probability P(D): Prevalence in population = 1% (0.01)
        // Sensitivity P(+ | D): Probability test is positive given disease = 95% (0.95)
        // False Positive Rate P(+ | ~D): Probability test is positive given no disease = 5% (0.05)
        // Specificity P(- | ~D) = 1 - False Positive = 95% (0.95)

        double pDisease = 0.01;            // Prior P(D)
        double pNoDisease = 1.0 - pDisease; // P(~D) = 0.99

        double pPosGivenDisease = 0.95;    // Sensitivity P(+ | D)
        double pNegGivenDisease = 1.0 - pPosGivenDisease; // False negative P(- | D) = 0.05

        double pPosGivenNoDisease = 0.05;  // False positive P(+ | ~D)
        double pNegGivenNoDisease = 1.0 - pPosGivenNoDisease; // Specificity P(- | ~D) = 0.95

        System.out.println("--- Problem Baseline Probabilities ---");
        System.out.printf("  Prior Probability of Disease P(D):         %.4f (1.0%%)\n", pDisease);
        System.out.printf("  Prior Probability of No Disease P(~D):     %.4f (99.0%%)\n", pNoDisease);
        System.out.printf("  Test Sensitivity P(+ | D):                 %.4f (95.0%%)\n", pPosGivenDisease);
        System.out.printf("  Test False Positive Rate P(+ | ~D):        %.4f (5.0%%)\n", pPosGivenNoDisease);
        System.out.printf("  Test Specificity P(- | ~D):                %.4f (95.0%%)\n", pNegGivenNoDisease);

        // Case 1: Patient Tests POSITIVE (+)
        // Total Probability of Testing Positive: P(+) = P(+|D)*P(D) + P(+|~D)*P(~D)
        double pPositive = (pPosGivenDisease * pDisease) + (pPosGivenNoDisease * pNoDisease);

        // Bayes' Theorem: P(D | +) = [P(+ | D) * P(D)] / P(+)
        double pDiseaseGivenPositive = (pPosGivenDisease * pDisease) / pPositive;

        System.out.println("\n--- Case 1: Patient Receives a POSITIVE (+) Diagnostic Result ---");
        System.out.printf("  Evidence P(+) = P(+|D)P(D) + P(+|~D)P(~D)\n");
        System.out.printf("                = (%.2f * %.2f) + (%.2f * %.2f) = %.5f\n",
            pPosGivenDisease, pDisease, pPosGivenNoDisease, pNoDisease, pPositive);
        System.out.printf("  Posterior Probability P(D | +) = [%.4f * %.4f] / %.5f\n",
            pPosGivenDisease, pDisease, pPositive);
        System.out.printf("  Result: Posterior P(D | +) = %.4f (%.2f%%)\n",
            pDiseaseGivenPositive, pDiseaseGivenPositive * 100.0);
        System.out.println("  Clinical Decision Insight: Even with a 95% sensitive test, the rare prior");
        System.out.printf("  prevalence (1%%) means only %.2f%% of positive-testing patients actually have the disease!\n",
            pDiseaseGivenPositive * 100.0);

        // Case 2: Patient Tests POSITIVE a SECOND TIME in an independent follow-up test
        // New Prior = Posterior from Test 1: P_new(D) = pDiseaseGivenPositive
        double newPriorD = pDiseaseGivenPositive;
        double newPriorNoD = 1.0 - newPriorD;

        double pSecondPositive = (pPosGivenDisease * newPriorD) + (pPosGivenNoDisease * newPriorNoD);
        double pDiseaseGivenTwoPositives = (pPosGivenDisease * newPriorD) / pSecondPositive;

        System.out.println("\n--- Case 2: Sequential Bayesian Update (Second Independent Positive Test) ---");
        System.out.printf("  Updated Prior for Round 2: P(D) = %.4f (%.2f%%)\n", newPriorD, newPriorD * 100.0);
        System.out.printf("  Posterior Probability P(D | +, +) = %.4f (%.2f%%)\n",
            pDiseaseGivenTwoPositives, pDiseaseGivenTwoPositives * 100.0);
        System.out.println("  Clinical Decision Insight: Two consecutive positive tests confirm the diagnosis with overwhelming certainty.");

        // Case 3: Patient Tests NEGATIVE (-)
        double pNegative = (pNegGivenDisease * pDisease) + (pNegGivenNoDisease * pNoDisease);
        double pDiseaseGivenNegative = (pNegGivenDisease * pDisease) / pNegative;

        System.out.println("\n--- Case 3: Patient Receives a NEGATIVE (-) Test Result ---");
        System.out.printf("  Evidence P(-) = %.5f\n", pNegative);
        System.out.printf("  Posterior Probability P(D | -) = %.6f (%.4f%%)\n",
            pDiseaseGivenNegative, pDiseaseGivenNegative * 100.0);
        System.out.println("  Clinical Decision Insight: A negative result provides 99.9% assurance of health.");

        System.out.println("\n[SUCCESS] Practical 22 executed successfully.");
    }
}
