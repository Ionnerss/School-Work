package Assignments.Assignment3;
// ------------------------------------------------------- 
// Assignment 3 - Question #2
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import java.util.Scanner;

public class A3_Q2 {
    static String selectedSpecies;

    static String[] species, zones = {"Zone 1", "Zone 2", "Zone 3"};

    static int[] selectedSZone, speciesTotals, zoneTotals;

    static int[][] sZones;

    static double[] sAvgs, zoneAvgs;

    static boolean mainMenuStart = true;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to the Wildlife Sighting Matrix System");
        System.out.println("----------------------------------------------");
        System.out.println("Enter sightings for 5 species (format: name zone1 zone2 zone3)");

        sZones = new int[5][3];
        species = new String[5];
        for (int i = 0 ; i <= 4 ; i++) {
            System.out.print("Species " + (i + 1) + ": ");
            String input = scanner.next();
            int num1 = scanner.nextInt();
            int num2 = scanner.nextInt();
            int num3 = scanner.nextInt();
            scanner.nextLine(); // consume the remaining newline

            species[i] = input;
            sZones[i] = new int[] {num1, num2, num3};
        }
        
        totalsCalculator();

        System.out.println();
        System.out.println("Data recorded successfully!");
        System.out.println();

        do {
            mainMenuStart = mainMenu(mainMenuStart);
        }
        while(mainMenuStart);
        scanner.close();
    }

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
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", "Species", "Zone1", "Zone2", "Zone3", "Total");
                System.out.println("------------------------------------------");
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", species[0], sZones[0][0], sZones[0][1], sZones[0][2], speciesTotals[0]);
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", species[1], sZones[1][0], sZones[1][1], sZones[1][2], speciesTotals[1]);
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", species[2], sZones[2][0], sZones[2][1], sZones[2][2], speciesTotals[2]);
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", species[3], sZones[3][0], sZones[3][1], sZones[3][2], speciesTotals[3]);
                System.out.printf("%-10s  %-5s   %-5s   %-5s   %-5s%n", species[4], sZones[4][0], sZones[4][1], sZones[4][2], speciesTotals[4]);
                System.out.println("------------------------------------------");
                break;
            case 2: 
                System.out.print("Enter species name to update: ");
                selectedSpecies = scanner.next();

                boolean exist = false;
                
                for (int i = 0 ; i <= 4 ; i++) {
                    if (!selectedSpecies.equalsIgnoreCase(species[i]))
                        continue;

                    selectedSpecies = species[i];
                    selectedSZone = sZones[i];
                    exist = true;
                }

                if (exist == false)
                    System.out.println("Species not found.");
                else {
                    System.out.print("Enter zone number (1-3): ");
                    int selectedZone = scanner.nextInt();

                    if ((selectedZone > 0) && (selectedZone < 4)) {
                        System.out.print("Enter new sightings count: ");
                        int newSightings = scanner.nextInt();

                        if (newSightings > 0) {
                            selectedSZone[selectedZone - 1] = newSightings;
                            totalsCalculator();

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
                int largestNum = 0;
                
                for (int i = 0 ; i <= 4 ; i++) {
                    if (largestNum > speciesTotals[i])
                        continue;
                    if (largestNum == speciesTotals[i]) {
                        largestNum = speciesTotals[i - 1];
                        selectedSpecies = species[i - 1];
                    }
                    
                    largestNum = speciesTotals[i];
                    selectedSpecies = species[i];                
                }    
                System.out.println("Species with highest total sightings: " + selectedSpecies + " (" + largestNum + " sightings)");
                System.out.println("------------------------------------------");
                break;
            case 5:
                int lowestZone = (zoneTotals[0] + zoneTotals[1] + zoneTotals[2]); //random int which will strictly be bigger than any of the zone totals.
                String specificZone = "";

                for (int i = 0; i <= 2 ; i++) {
                    if (lowestZone < zoneTotals[i])
                        continue;
                    if (lowestZone == zoneTotals[i]) {
                        lowestZone = zoneTotals[i - 1];
                        specificZone = zones[i - 1];
                    }

                    lowestZone = zoneTotals[i];
                    specificZone = zones[i];   
                }
                System.out.println(specificZone + " has the lowest total sightings: " + lowestZone);
                System.out.println("------------------------------------------");
                break;
            case 6: //Works fine
                System.out.println("Compute averages:");
                System.out.println("1. Per species");
                System.out.println("2. Per zone");

                System.out.print("Enter choice: ");
                choice = scanner.nextInt();

                if (choice == 1) {
                    for (int i = 0; i <= 4; i++) {
                        System.out.print(species[i]);
                        System.out.printf(" -> Average: %.2f%n", sAvgs[i]);
                    }
                }
                else if (choice == 2) {
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

    public static void totalsCalculator() {
        speciesTotals = new int[5];
        zoneTotals = new int[3];
        zoneAvgs = new double[3];
        sAvgs = new double[5];
        
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 2; j++) {
                speciesTotals[i] += sZones[i][j];
                sAvgs[i] += (sZones[i][j] / 3.0);
            }
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 4; j++)
                zoneTotals[i] += sZones[j][i];      
        }

        for (int i = 0; i <= 2; i++)
            zoneAvgs[i] = zoneTotals[i] / 5.0;
    }
} 