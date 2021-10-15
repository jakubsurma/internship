package com.example.internship_app;

import lombok.Data;

import java.util.Objects;

@Data
public class NetWorthCalculation {
    private static final double DELTA = 0.001;
    private final double gross;
    private final double net;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetWorthCalculation that = (NetWorthCalculation) o;
        return Math.abs(that.gross - gross) < DELTA &&
                Math.abs(that.net - net) < DELTA;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gross, net);
    }
}
