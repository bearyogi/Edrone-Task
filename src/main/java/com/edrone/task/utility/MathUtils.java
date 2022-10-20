package com.edrone.task.utility;

public class MathUtils {
    public static int fact(int a)
    {
        int i, f = 1;

        // Loop to find the factorial
        // of the given number
        for(i = 2; i <= a; i++)
            f = f * i;

        return f;
    }
}
