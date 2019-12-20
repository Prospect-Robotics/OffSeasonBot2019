package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N9 extends Num implements Nat<N9> {
    private N9() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 9;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N9 instance = new N9();
}