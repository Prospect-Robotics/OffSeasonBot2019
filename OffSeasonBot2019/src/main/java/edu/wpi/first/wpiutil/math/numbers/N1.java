package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N1 extends Num implements Nat<N1> {
    private N1() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 1.
     */
    @Override
    public int getNum() {
        return 1;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N1 instance = new N1();
}