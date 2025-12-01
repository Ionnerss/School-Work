import java.util.Arrays;

public class Tryouts {
    public static void main(String[] args) {
        int[] sourceArray = {10, 20, 30, 40, 50, 60, 70};
        int[] destinationArray = new int[7]; // Destination array of the same size

        System.out.println("Source Array: " + Arrays.toString(sourceArray));
        System.out.println("Destination Array (initial): " + Arrays.toString(destinationArray));

        // Example 1: Copying the entire source array to the destination array
        // System.arraycopy(sourceArray, 0, destinationArray, 0, sourceArray.length);
        // System.out.println("Destination Array (after full copy): " + Arrays.toString(destinationArray));

        System.arraycopy(sourceArray, 0, destinationArray, 0, 4);
        System.out.println("Moded" + Arrays.toString(destinationArray));
        System.arraycopy(sourceArray, 05, destinationArray, 4,2);
        System.out.println("Second Mod" + Arrays.toString(destinationArray));

        System.out.println("---------------------------------------------");
        int[] first = {4,5,6,7,8};
        int[] second = new int[first.length - 1];
        System.arraycopy(first, 1, second, 0, first.length -1);
        System.out.println(Arrays.toString(second));
        System.out.println("---------------------------------------------");


        // // Reset destination array for the next example
        // Arrays.fill(destinationArray, 0); 
        // System.out.println("Destination Array (reset): " + Arrays.toString(destinationArray));

        // // Example 2: Copying a specific portion of the source array
        // // Copy elements from index 2 (value 30) of sourceArray
        // // to index 1 of destinationArray, for a length of 3 elements (30, 40, 50)
        // System.arraycopy(sourceArray, 2, destinationArray, 1, 3);
        // System.out.println("Destination Array (after partial copy): " + Arrays.toString(destinationArray));
    }
}