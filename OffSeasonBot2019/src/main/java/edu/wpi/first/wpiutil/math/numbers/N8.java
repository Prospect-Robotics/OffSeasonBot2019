package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N8 extends Num implements Nat<N8> {
    private N8() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 8;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N8 instance = new N8();
}