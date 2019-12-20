package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N11 extends Num implements Nat<N11> {
    private N11() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 11;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N11 instance = new N11();
}