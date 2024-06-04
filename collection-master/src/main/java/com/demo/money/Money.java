package com.demo.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Money {
    /**
     * 金额
     */
    private final BigDecimal amount;
    /**
     * 币种
     */
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException("Scale of amount is greater than the currency's default fraction digits");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param other
     * @return
     */
    public Money add(Money other) {
        checkCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }
    /**
     * @param other
     */
    public  void addTo(Money other) {
    	
        checkCurrency(other);
       this.amount.add(other.amount);
       
    }
    /**
     * @param other
     * @return
     */
    public Money subtract(Money other) {
        checkCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
    
    /**
     * @param other
     */
    public void subtractTo (Money other) {
        checkCurrency(other);
       this.amount.subtract(other.amount);
    }
    

    /**
     * @param multiplier
     * @return
     */
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier), this.currency);
    }

    /**
     * @param divisor
     * @return
     */
    public Money divide(BigDecimal divisor) {
        return new Money(this.amount.divide(divisor, currency.getDefaultFractionDigits(), BigDecimal.ROUND_HALF_EVEN), this.currency);
    }

    /**
     * @param other
     */
    private void checkCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    /**
     *
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }

    public static void main(String[] args) {
        // Example usage:
        Currency usd = Currency.getInstance("USD");
        Money money1 = new Money(new BigDecimal("10.00"), usd);
        Money money2 = new Money(new BigDecimal("5.50"), usd);

        Money sum = money1.add(money2);
        System.out.println("Sum: " + sum);  // Sum: Money{amount=15.50, currency=USD}

        Money difference = money1.subtract(money2);
        System.out.println("Difference: " + difference);  // Difference: Money{amount=4.50, currency=USD}
    }
}
