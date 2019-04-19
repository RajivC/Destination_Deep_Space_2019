package org.frc1923.robot.utilities;

public class MovingAverage {

    private double[] numbers;
    private int size;

    public MovingAverage(int length) {
        this.numbers = new double[length];
        this.size = 0;
    }

    public void add(double value) {
        if (this.size < this.numbers.length) {
            this.size++;
        }

        for (int i = this.size - 1; i >= 0; i--) {
            if (i >= this.numbers.length - 1) {
                continue;
            }

            this.numbers[i + 1] = this.numbers[i];
        }

        this.numbers[0] = value;
    }

    public double get() {
        double sum = 0;

        for (double num : this.numbers) {
            sum += num;
        }

        return sum / this.size;
    }

    public void clear() {
        for (int i = 0; i < this.numbers.length; i++) {
            this.numbers[i] = 0;
        }

        this.size = 0;
    }

}
