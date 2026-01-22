package Assignments.Assignment3;
// -------------------------------------------------------- 
// Assignment 3 - Question #2
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------


// Algorithm #2: Wildlife Sighting Matrix
// --------------------------------------------------------

import java.util.Scanner;

public class A3_Q2 {

    // The currently selected species name used in menu options (search/update).
    static String selectedSpecies;

    // species: holds the 5 species names entered by the user.
    // zones: labels for the three geographic zones (fixed).
    static String[] species, zones = {"Zone 1", "Zone 2", "Zone 3"};

    // selectedSZone: reference to the 3 zone counts of the selected species.
    // speciesTotals: total sightings across all zones for each species.
    // zoneTotals: total sightings across all species for each zone.
    static int[] selectedSZone, speciesTotals, zoneTotals;

    // sZones: 5x3 matrix. Row = species, Column = zone.
    // Example: sZones[2][1] is the sightings of species #3 in zone #2.
    static int[][] sZones;

    // sAvgs: per-species averages across zones (row averages of sZones).
    // zoneAvgs: per-zone averages across species (column averages of sZones).
    static double[] sAvgs, zoneAvgs;

    // Controls the main menu loop.
    static boolean mainMenuStart = true;

    // Single Scanner reused for all input.
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to the Wildlife Sighting Matrix System");
        System.out.println("----------------------------------------------");
        System.out.println("Enter sightings for 5 species (format: name zone1 zone2 zone3)");

        // Create the data containers:
        // - sZones: 5 species × 3 zones matrix of counts.
        // - species: stores 5 names.
        sZones = new int[5][3];
        species = new String[5];

        // Collect input for each species.
        // For each i (0..4) we read: name, then 3 integers for each zone.
        for (int i = 0 ; i <= 4 ; i++) {
            System.out.print("Species " + (i + 1) + ": ");
            String input = scanner.next();       // species name
            int num1 = scanner.nextInt();        // Zone 1 count
            int num2 = scanner.nextInt();        // Zone 2 count
            int num3 = scanner.nextInt();        // Zone 3 count
            scanner.nextLine(); // consume the remaining newline to keep input clean

            species[i] = input;
            sZones[i] = new int[] {num1, num2, num3}; // fill row i (the 3 zones)
        }
        
        // Pre-compute totals and averages from the matrix so that menus can use them.
        totalsCalculator();

        System.out.println();
        System.out.println("Data recorded successfully!");
        System.out.println();

        // Main menu loop rationale:
        // - do-while is used (instead of while) so the menu is shown at least once
        //   after the initial data entry phase.
        // - mainMenuStart is the single source of truth for "should the app keep running?"
        // - Each iteration, mainMenu(...) executes one user action and RETURNS the updated
        //   running state. If the user picks option 8, mainMenu returns false and the loop ends.
        do {
            mainMenuStart = mainMenu(mainMenuStart);
        }
        while(mainMenuStart);

        // Close input stream at the end.
        scanner.close();
    }

    /*
     * mainMenu(boolean)
     * Role and loop-control reasoning:
     * - Encapsulates all menu input/output and the action handling for a single choice.
     * - Does NOT run its own loop; instead it returns a boolean so main() can control
     *   the program with a simple do-while. 
     * - The parameter carries the current running state; the method may set it to false
     *   when the user selects Exit (option 8) and then returns that state to main().
     * - Combined with do-while in main(), this guarantees the menu appears at least once
     *   and stops immediately after the user chooses to exit.
     */
    public static boolean mainMenu(boolean mainMenuStart) {
        System.out.println("=============== MENU ====================================");
        System.out.println("1. Display the sightings table");
        System.out.println("2. Update the sightings of a species in a specific zone");
        System.out.println("3. Search for a species by name and display its sightings");
        System.out.println("4. Find the species with the highest total sightings");
        System.out.println("5. Find the zone with the lowest total sightings");
        System.out.println("6. Compute averages (per species or per zone)");
        System.out.println("7. Filter species by average sightings");
        System.out.println("8. Exit");
        System.out.println("=========================================================");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1: 
                // Print the table header and 5 rows (one per species).
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", "Species", "Zone1", "Zone2", "Zone3", "Total");
                System.out.println("------------------------------------------");
                for (int i = 0; i <= species.length; i++) {
                    System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", species[i], 
                                        sZones[i][0], sZones[i][1], sZones[i][2], speciesTotals[i]);
                }
                System.out.println("------------------------------------------");
                break;

            case 2: 
                // Update a single cell of the matrix: pick species, pick zone, replace count.
                System.out.print("Enter species name to update: ");
                selectedSpecies = scanner.next();

                boolean exist = false;
                
                // Find the row that matches the typed species name (case-insensitive).
                for (int i = 0 ; i <= 4 ; i++) {
                    if (!selectedSpecies.equalsIgnoreCase(species[i]))
                        continue;

                    // Save the canonical species name and a reference to its zone row.
                    selectedSpecies = species[i];
                    selectedSZone = sZones[i];
                    exist = true;
                }

                if (exist == false)
                    System.out.println("Species not found.");
                else {
                    System.out.print("Enter zone number (1-3): ");
                    int selectedZone = scanner.nextInt();

                    // Validate zone index and update value.
                    if ((selectedZone > 0) && (selectedZone < 4)) {
                        System.out.print("Enter new sightings count: ");
                        int newSightings = scanner.nextInt();

                        // Negative values are changed to 0; otherwise set as given.
                        if (newSightings > 0) {
                            selectedSZone[selectedZone - 1] = newSightings;
                            totalsCalculator(); // Recompute totals/averages after any change.

                            System.out.println("Sightings updated successfully!");
                        }
                        else {
                            selectedSZone[selectedZone - 1] = 0;
                            totalsCalculator();

                            System.out.println("Sightings cannot be negative. Setting to 0.");
                            System.out.println("Sightings updated successfully!");
                        }
                    }
                    else
                        System.out.println("Invalid zone number!");
                }
                System.out.println("------------------------------------------");
                break;

            case 3: 
                // Search by species name and show its 3 zone values and total.
                System.out.print("Enter species name to search: ");
                selectedSpecies = scanner.next();
                int ssTotal = 0;

                boolean speciesExists = false;
                for (int i = 0 ; i <= 4 ; i++) {
                    if (!selectedSpecies.equalsIgnoreCase(species[i]))
                        continue;

                    speciesExists = true;
                    selectedSpecies = species[i];
                    selectedSZone = sZones[i];
                    ssTotal = speciesTotals[i];
                }

                if (speciesExists == false)
                    System.out.println("Species not found.");
                else
                    System.out.println(selectedSpecies + " -> " + "Zone 1: " + selectedSZone[0] + ", Zone 2: " + selectedSZone[1] + ", Zone 3: " + 
                                        selectedSZone[2] + " (Total: " + ssTotal + ")");
                                    
                System.out.println("------------------------------------------");
                break;

            case 4:
                // Find the species with the largest total sightings.
                // largestNum tracks the best total seen so far; selectedSpecies tracks which species holds it.
                int largestNum = 0;
                
                for (int i = 0 ; i <= 4 ; i++) {
                    if (largestNum > speciesTotals[i])
                        continue;
                    if (largestNum == speciesTotals[i]) {
                        // Tie handling: prefer the previous species (as per existing logic).
                        largestNum = speciesTotals[i - 1];
                        selectedSpecies = species[i - 1];
                    }
                    
                    // New maximum found (or first iteration).
                    largestNum = speciesTotals[i];
                    selectedSpecies = species[i];                
                }    
                System.out.println("Species with highest total sightings: " + selectedSpecies + " (" + largestNum + " sightings)");
                System.out.println("------------------------------------------");
                break;

            case 5:
                // Find the zone with the smallest total across all species.
                // Start with a value guaranteed larger than any zone total.
                int lowestZone = (zoneTotals[0] + zoneTotals[1] + zoneTotals[2]); 
                String specificZone = "";

                for (int i = 0; i <= 2 ; i++) {
                    if (lowestZone < zoneTotals[i])
                        continue;
                    if (lowestZone == zoneTotals[i]) {
                        // Tie handling: prefer the previous zone (as per existing logic).
                        lowestZone = zoneTotals[i - 1];
                        specificZone = zones[i - 1];
                    }

                    // New minimum found.
                    lowestZone = zoneTotals[i];
                    specificZone = zones[i];   
                }
                System.out.println(specificZone + " has the lowest total sightings: " + lowestZone);
                System.out.println("------------------------------------------");
                break;

            case 6:
                // Show averages previously computed in totalsCalculator().
                System.out.println("Compute averages:");
                System.out.println("1. Per species");
                System.out.println("2. Per zone");

                System.out.print("Enter choice: ");
                choice = scanner.nextInt();

                if (choice == 1) {
                    // Print each species with its average across the 3 zones.
                    for (int i = 0; i <= 4; i++) {
                        System.out.print(species[i]);
                        System.out.printf(" -> Average: %.2f%n", sAvgs[i]);
                    }
                }
                else if (choice == 2) {
                    // Print each zone with its average across the 5 species.
                    for (int i = 0; i <= 2; i++) {
                        System.out.print(zones[i]);
                        System.out.printf(" -> Average: %.2f%n", zoneAvgs[i]);
                    }
                }
                else
                    System.out.println("Invalid choice!");

                System.out.println("------------------------------------------");
                break;

            case 7:
                // Filter species whose average sightings meet or exceed a threshold.
                System.out.print("Enter threshold value: ");
                double threshold = scanner.nextDouble();

                System.out.println("Species with average >= " + threshold);

                boolean thresholdIsSmaller = false;
                for (int i = 0; i <= 4; i++) {
                    if (!(sAvgs[i] >= threshold)) {
                        continue;
                    }

                    thresholdIsSmaller = true;
                    System.out.print("- " + species[i] + " (");
                    System.out.printf("%.2f)%n", sAvgs[i]);
                }

                if (thresholdIsSmaller == false)
                    System.out.println("No species meet the threshold.");

                System.out.println("------------------------------------------");
                break;

            case 8:
                // Exit the program by stopping the menu loop.
                System.out.println("Thank you for supporting wildlife conservation. Goodbye!");
                System.out.println("------------------------------------------");
                mainMenuStart = false;
                break;

            default:
                System.out.println("Invalid choice!");
                break;
        }
        return mainMenuStart;
    }

    /*
     * totalsCalculator()
     * Purpose:
     * - Recomputes all derived values from the sZones matrix.
     *   These include:
     *     speciesTotals: row sums (totals per species)
     *     zoneTotals: column sums (totals per zone)
     *     sAvgs: row averages (per-species averages across 3 zones)
     *     zoneAvgs: column averages (per-zone averages across 5 species)
     *
     * Why this method exists:
     * - After any update to the matrix (e.g., changing a single cell), all totals
     *   and averages can become outdated. Centralizing the recalculation here
     *   keeps the logic consistent and avoids duplicated code across menu options.
     * - It ensures the menu always shows accurate, up-to-date results.
     */
    public static void totalsCalculator() {
        // Reinitialize all derived arrays before summing to avoid carrying old values.
        speciesTotals = new int[5];
        zoneTotals = new int[3];
        zoneAvgs = new double[3];
        sAvgs = new double[5];
        
        // Compute row-based values:
        // - speciesTotals[i] = sum of the 3 zones for species i.
        // - sAvgs[i] = average of the 3 zones for species i.
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 2; j++) {
                speciesTotals[i] += sZones[i][j];
                sAvgs[i] += (sZones[i][j] / 3.0); // accumulate one-third of each zone to form the average
            }
        }

        // Compute column-based totals:
        // - zoneTotals[zone] = sum over all 5 species for that zone.
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 4; j++)
                zoneTotals[i] += sZones[j][i];      
        }

        // Compute column-based averages:
        // - Each zone average is its total divided by 5 species.
        for (int i = 0; i <= 2; i++)
            zoneAvgs[i] = zoneTotals[i] / 5.0;
    }
}