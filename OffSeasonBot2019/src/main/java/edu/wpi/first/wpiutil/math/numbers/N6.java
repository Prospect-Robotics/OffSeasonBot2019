package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N6 extends Num implements Nat<N6> {
    private N6() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 6;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N6 instance = new N6();
}