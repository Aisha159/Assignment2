
package com.uet.legacy;

public class TaxCalculator {

    public double calculateTax(double netAmount, String country) {

        double taxRate = 0.15;

        if (country.equals("PK")) {
            taxRate = 0.17;
        } else if (country.equals("US")) {
            taxRate = 0.08;
        } else if (country.equals("UK")) {
            taxRate = 0.20;
        }

        return netAmount * taxRate;
    }
}
