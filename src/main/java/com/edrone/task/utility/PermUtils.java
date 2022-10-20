package com.edrone.task.utility;

import java.util.TreeSet;

public class PermUtils {
    static public boolean shouldSwap(String str, int start, int curr) {
        for (int i = start; i < curr; i++) {
            if (str.charAt(i) == str.charAt(curr)) {
                return false;
            }
        }
        return true;
    }

    static public void findPermutations(String str, int index, int n, TreeSet<String> set) {
        if (index >= n) {
            set.add(str);
            return;
        }

        for (int i = index; i < n; i++) {
            boolean check = shouldSwap(str, index, i);
            if (check) {
                str = swap(str, index, i);
                findPermutations(str, index + 1, n, set);
                str = swap(str, index, i);
            }
        }
    }

    static public TreeSet<String> findSubPermutations(int min, int max, TreeSet<String> set, long number) {
        TreeSet<String> result = new TreeSet<>();
        for (int i = min; i <= max; i++) {
            for (String perms : set) {
                result.add(perms.substring(0, i));
                if (result.size() == number) {
                    return result;
                }
            }
        }
        return result;
    }

    static public String swap(String str, int i, int j) {
        char[] ch = str.toCharArray();
        char c = ch[i];
        ch[i] = ch[j];
        ch[j] = c;
        return new String(ch);
    }

}
