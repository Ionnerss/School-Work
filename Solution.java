import java.util.ArrayList;
import java.util.Arrays;

class Solution {
    // public int maximum(int[] a) {
    //     int max = 0;
    //     if (a.length == 1) return a[0];
    //     else {
    //         int[] temp = new int[a.length];
    //         System.arraycopy(a, 1, temp, 0, a.length);
    //         max = maximum(temp);
    //         return 
    //     }



    // }
    // int counter = 0;
    // int x = 0;
    // public int pairs(int[] a) {
    //     if (a.length < 2) return 
    //     if (counter == 2) return a[0];

    //     if (a[0] != x) counter = 0;
    //     else counter++;

    //     int[] temp = new int[a.length];
    //     System.arraycopy(a, 1, temp, 0, a.length);
    //     pairs(temp);
    // }

    public int pairs(int[] a) {
        if (a.length < 2) return -1;

        if (a[0] == a[1]) return a[0];
        
        int[] temp = new int[a.length];
        System.arraycopy(a, 1, temp, 0, a.length);
        return pairs(temp);
    }
} 
