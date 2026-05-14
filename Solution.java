import java.util.ArrayList;

class Solution {
    public int romanToInt(String s) {
        ArrayList<Integer> a = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && s.charAt(i) == 'I' && (s.charAt(i + 1) == 'V' || s.charAt(i + 1) == 'X')) {
                a.add(convert(s.charAt(i + 1)) - convert(s.charAt(i)));
                i++;
                continue;
            }

            if (i + 1 < s.length() && s.charAt(i) == 'X' && (s.charAt(i + 1) == 'L' || s.charAt(i + 1) == 'C')) {
                a.add(convert(s.charAt(i + 1)) - convert(s.charAt(i)));
                i++;
                continue;
            }

            if (i + 1 < s.length() && s.charAt(i) == 'C' && (s.charAt(i + 1) == 'D' || s.charAt(i + 1) == 'M')) {
                a.add(convert(s.charAt(i + 1)) - convert(s.charAt(i)));
                i++;
                continue;
            }

            a.add(convert(s.charAt(i)));
        }

        int sum = 0;

        for (int i = 0; i < a.size(); i++) {
            sum += a.get(i);
        }

        return sum;
    }

    public int convert(char c) {
        if (c == 'I') return 1;
        else if (c == 'V') return 5;
        else if (c == 'X') return 10;
        else if (c == 'L') return 50;
        else if (c == 'C') return 100;
        else if (c == 'D') return 500;
        else if (c == 'M') return 1000;
        else return 0;
    }
}
