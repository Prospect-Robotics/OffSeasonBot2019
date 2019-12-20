package edu.wpi.first.wpiutil.math.numbers;

import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.Num;

public final class N7 extends Num implements Nat<N7> {
    private N7() {
    }

    /**
     * The integer this class represents.
     *
     * @return The literal number 12
     */
    @Override
    public int getNum() {
        return 7;
    }

    /**
     * The singleton instance of this class.
     */
    public static final N7 instance = new N7();
}