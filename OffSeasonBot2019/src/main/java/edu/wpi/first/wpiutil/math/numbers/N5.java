package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N5 extends Num implements Nat<N5> {
    private N5() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 5;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N5 instance = new N5();
}