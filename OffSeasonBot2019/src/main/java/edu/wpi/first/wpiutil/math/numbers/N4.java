package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N4 extends Num implements Nat<N4> {
    private N4() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 4;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N4 instance = new N4();
}