package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N12 extends Num implements Nat<N12> {
    private N12() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 12;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N12 instance = new N12();
}