package Assignments.Assignment3;
// ------------------------------------------------------- 
// Assignment 3 - Question #2
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import java.util.Scanner;

public class A3_Q2 {
    static String species1;
    static String species2;
    static String species3;
    static String species4;
    static String species5;
    static String[] species = {species1, species2, species3, species4, species5};
    static int[] s1Zones;
    static int[] s2Zones;
    static int[] s3Zones;
    static int[] s4Zones;
    static int[] s5Zones;
    static int[][] sZones = {s1Zones, s2Zones, s3Zones, s4Zones, s5Zones};
    static int s1_Total;
    static int s2_Total;
    static int s3_Total;
    static int s4_Total;
    static int s5_Total;
    static int[] totals = {s1_Total, s2_Total, s3_Total, s4_Total, s5_Total};
    static String selectedSpecies;
    static int[] ssZone;



    static boolean mainMenu = true;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.println("Welcome to the Wildlife Sighting Matrix System");
        System.out.println("----------------------------------------------");
        System.out.println("Enter sightings for 5 species (format: name zone1 zone2 zone3)");

        for (int i = 1 ; i <= 5 ; i++) {
            System.out.print("Species " + i + ": ");
            String input = scanner.next();

            switch (i) {
                case 1:
                    species1 = input.substring(0, input.indexOf(""));
                    s1Zones = new int[] {input.indexOf(""), input.length()};
                    break;
                case 2:
                    species2 = input.substring(0, input.indexOf(""));
                    s2Zones = new int[] {input.indexOf(""), input.length()};
                    break;
                case 3:
                    species3 = input.substring(0, input.indexOf(""));
                    s3Zones = new int[] {input.indexOf(""), input.length()};
                    break;
                case 4:
                    species4 = input.substring(0, input.indexOf(""));
                    s4Zones = new int[] {input.indexOf(""), input.length()};
                    break;
                case 5:
                    species5 = input.substring(0, input.indexOf(""));
                    s5Zones = new int[] {input.indexOf(""), input.length()};
                    break;
            }
        }

        for (int i = 0 ; i <=3 ; i++) {
            s1_Total += s1Zones[i];
            s2_Total += s2Zones[i];
            s3_Total += s3Zones[i];
            s4_Total += s4Zones[i];
            s5_Total += s5Zones[i];
        }

        System.out.println();
        System.out.println("Data recorded successfully!");

        do {
            mainMenu = MainMenu(mainMenu);
        }
        while(mainMenu);
    }

    public static boolean MainMenu(boolean mainMenu) {
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
        System.out.println("Enter your choice: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Species     Zone1   Zone2   Zone3   Total");
                System.out.println("------------------------------------------");
                System.out.println(species1 + "     " + s1Zones[0] + "  " + s1Zones[1] + "  " + s1Zones[2] + s1_Total);
                System.out.println(species2 + "     " + s2Zones[0] + "  " + s2Zones[1] + "  " + s2Zones[2] + s2_Total);
                System.out.println(species3 + "     " + s3Zones[0] + "  " + s3Zones[1] + "  " + s3Zones[2] + s3_Total);
                System.out.println(species4 + "     " + s4Zones[0] + "  " + s4Zones[1] + "  " + s4Zones[2] + s4_Total);
                System.out.println(species5 + "     " + s5Zones[0] + "  " + s5Zones[1] + "  " + s5Zones[2] + s5_Total);
                System.out.println("------------------------------------------");
                break;
            case 2:
                System.out.println("Enter species name to update: ");
                selectedSpecies = scanner.next();

                boolean exist = true;
                
                for (int i = 0 ; i <= 4 ; i++) {
                    if (selectedSpecies.equalsIgnoreCase(species[i])){
                        selectedSpecies = species[i];
                        ssZone = sZones[i];
                        exist = true;
                    }     
                    else 
                        exist = false;
                }

                if (exist == false)
                    System.out.println("Species not found.");
                else {
                    System.out.println("Enter zone number (1-3): ");
                    int selectedZone = scanner.nextInt();

                    if ((selectedZone > 0) && (selectedZone < 4)) {
                        System.out.println("Enter new sightings count: ");
                        int newSightings = scanner.nextInt();

                        if (newSightings > 0) {
                            ssZone[selectedZone] = newSightings;
                            System.out.println("Sightings updated successfully!");
                        }
                        else {
                            System.out.println("Sightings cannot be negative. Setting to 0.");
                            System.out.println("Sightings updated successfully!");
                        }
                    }
                    else {
                        System.out.println("Invalid zone number !");
                    }
                }
                
                break;
            case 3:
                System.out.println("Enter species name to search: ");
                selectedSpecies = scanner.next();
                int ssTotal = 0;

                for (int i = 0 ; i <= 4 ; i++) {
                    if (selectedSpecies.equalsIgnoreCase(species[i])){
                        selectedSpecies = species[i];
                        ssZone = sZones[i];
                        ssTotal = totals[i];
                    }     
                    else 
                        continue;
                }
                System.out.println(selectedSpecies + " -> " + "Zone 1: " + ssZone[0] + ", Zone 2: " + ssZone[1] + ", Zone 3: " + 
                                    ssZone[2] + "(Total:" + ssTotal + ")");
                break;
            case 4:
                int largestNum = 0;
                
                for (int i = 0 ; i <= 4 ; i++) {
                    if (largestNum < totals[i]) {
                        largestNum = totals[i];
                        selectedSpecies = species[i];
                    } 
                    else
                        continue;
                }    
                System.out.println("Species with highest total sightings: " + selectedSpecies + " (" + largestNum + " sightings)");
                break;
            case 5:
                int zone1Total = 0;
                int zone2Total = 0;
                int zone3Total = 0;

                for (int i = 0 ; i <= 3 ; i++) {
                    zone1Total += sZones[i][0];
                    zone2Total += sZones[i][1];
                    zone3Total += sZones[i][2];
                }

                
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
        }
        
        return mainMenu;
    }
} 
