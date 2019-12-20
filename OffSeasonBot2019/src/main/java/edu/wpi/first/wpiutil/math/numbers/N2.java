package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N2 extends Num implements Nat<N2> {
    private N2() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 2;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N2 instance = new N2();
}