package io.mosip.kernel.masterdata.utils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
public class Test {

    public static String solution(int w, int h, int s) {
        final List<Permutation> colPermutations = genPermutations(w);
        final List<Permutation> rowPermutations = genPermutations(h);

        final BigInteger S = BigInteger.valueOf(s);

        BigInteger total = colPermutations
                .stream()
                .parallel()
                .map(cp -> {
                    BigInteger sum = ZERO;
                    for (Permutation rp : rowPermutations) {
                        BigInteger fixedMatrices = countFixedMatrices(rp, cp, S);
                        sum = sum.add(fixedMatrices);
                    }
                    return sum;
                })
                .reduce(BigInteger::add)
                .orElse(ZERO);

        BigInteger G = factorial(w).multiply(factorial(h));
        BigInteger result = total.divide(G);
        return result.toString();
    }

    private static BigInteger countFixedMatrices(Permutation rp, Permutation cp, BigInteger S) {
        // calculate for M(F) sub-matrix
        BigInteger fixed = S.pow(rp.fixed * cp.fixed);
        fixed = fixed.multiply(rp.count);
        fixed = fixed.multiply(cp.count);

        // calculate for those sub-matrices that only permute on rows
        BigInteger m = S.pow(cp.fixed * rp.cycles.length);
        fixed = fixed.multiply(m);

        // calculate for those sub-matrices that only permute on columns
        m = S.pow(rp.fixed * cp.cycles.length);
        fixed = fixed.multiply(m);
        int t = 0;
        for (int lr : rp.cycles) {
            for (int lc : cp.cycles) {
                int v = lr * lc / lcm(lr, lc);
                t += v;
            }
        }

        m = S.pow(t);
        fixed = fixed.multiply(m);
        return fixed;
    }

    static List<Permutation> genPermutations(int n) {
        List<Permutation> result = IntStream.rangeClosed(0, n - 2)
                .parallel()
                .mapToObj(fixed -> {
                    List<Permutation> ls = new ArrayList<>();
                    List<int[]> cyclesList = numbersSumToN(n - fixed);

                    for (int[] cycles : cyclesList) {
                        Permutation p = makePermutation(n, fixed, cycles);
                        ls.add(p);
                    }
                    return ls;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // Our result would be incomplete without the identity permutation.
        result.add(new Permutation(n, new int[0], ONE));
        return result;
    }

    static List<int[]> numbersSumToN(int n) {
        List<int[]> res = new ArrayList<>();
        // our cycles should have length greater than 1 (otherwise, it's a fixed
        // permutation).
        int[] ls = new int[n / 2 + 1];
        int len = 0;
        for (int i = 2; i <= n; i++) {
            ls[len] = i;
            numbersSumToNRecursive(res, n - i, ls, len + 1);
            ls[len] = 0;
        }
        return res;
    }

    static void numbersSumToNRecursive(
            List<int[]> all, int rem, int[] ls, int len
    ) {
        if (rem == 0) {
            int[] copy = new int[len];
            System.arraycopy(ls, 0, copy, 0, len);
            all.add(copy);
            return;
        }

        if (rem < 0) {
            return;
        }

        int last = ls[len - 1];
        for (int i = last; i <= rem; i++) {
            ls[len] = i;
            numbersSumToNRecursive(all, rem - i, ls, len + 1);
            ls[len] = 0;
        }
    }

    static Permutation makePermutation(int n, int fixed, int[] cycles) {
        BigInteger N = BigInteger.valueOf(n);
        BigInteger f = BigInteger.valueOf(fixed);
        BigInteger count = combination(N, f);

        BigInteger remains = BigInteger.valueOf(n - fixed);
        int repeatedLen = 1;
        int previousLen = 0;
        for (int ln : cycles) {
            BigInteger len = BigInteger.valueOf(ln);
            BigInteger selections = combination(remains, len);
            remains = remains.subtract(len);
            BigInteger disjointCycles = factorial(ln - 1);
            count = count.multiply(selections).multiply(disjointCycles);
            if (previousLen == ln) {
                repeatedLen++;
            } else {
                count = count.divide(factorial(repeatedLen));
                repeatedLen = 1;
                previousLen = ln;
            }
        }

        count = count.divide(factorial(repeatedLen));
        return new Permutation(fixed, cycles, count);
    }

    static class Permutation {
        int fixed;
        int[] cycles;
        BigInteger count;

        public Permutation(int fixed, int[] cycles, BigInteger count) {
            this.fixed = fixed;
            this.cycles = cycles;
            this.count = count;
        }

        @Override
        public String toString() {
            return "Permutation{" +
                    "fixed=" + fixed +
                    ", cycles=" + Arrays.toString(cycles) +
                    ", count=" + count +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Permutation that = (Permutation) o;
            return fixed == that.fixed && Arrays.equals(cycles, that.cycles) && Objects.equals(count, that.count);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(fixed, count);
            result = 31 * result + Arrays.hashCode(cycles);
            return result;
        }
    }

    static BigInteger factorial(int n) {
        BigInteger r = ONE;
        for (int i = 1; i <= n; i++) {
            r = r.multiply(BigInteger.valueOf(i));
        }
        return r;
    }

    static BigInteger combination(BigInteger n, BigInteger k) {
        if (n.compareTo(k) < 0) {
            return ZERO;
        }

        BigInteger v = ONE;
        BigInteger i = ONE;
        while (i.compareTo(k) <= 0) {
            v = v.multiply(n).divide(i);
            i = i.add(ONE);
            n = n.subtract(ONE);
        }

        return v;
    }

    private static int lcm(int a, int b) {
        return b * a / gcd(a, b);
    }

    static int gcd(int x, int y) {
        if (x == 0) {
            return y;
        }

        if (y == 0) {
            return x;
        }

        //noinspection SuspiciousNameCombination
        return gcd(y % x, x);
    }

    public static void main(String[] args)
    {
        String str=null;
        System.out.println( str.isEmpty() );      //false

        System.out.println( " ABC ".isBlank() );    //false

        System.out.println( "  ".isBlank() );     //true

        System.out.println( "".isBlank() );       //true
    }
}
