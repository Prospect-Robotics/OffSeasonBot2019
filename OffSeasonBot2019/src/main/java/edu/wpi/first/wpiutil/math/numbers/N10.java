package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N10 extends Num implements Nat<N10> {
    private N10() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 10;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N10 instance = new N10();
}