package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N3 extends Num implements Nat<N3> {
    private N3() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 3;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N3 instance = new N3();
}