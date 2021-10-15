package com.example.internship_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NetWorthController {

    private static final double PIT = 0.19;
    private static final double ZUS = 1380.18;
    private final List<NetWorthCalculation> netWorthCalculations = new ArrayList<>();

    @GetMapping("/calc")
    public double calculateNetValue(double gross) {
        double net = (gross * (1 - PIT) - ZUS);
        netWorthCalculations.add(new NetWorthCalculation(gross,net));
        return net;
    }
    @GetMapping("/last5entries")
    public List<NetWorthCalculation> returnLastFiveEntries() {
        int listSize = netWorthCalculations.size();
        int startingIndex = Math.max(listSize - 5, 0);
        return netWorthCalculations.subList(startingIndex, listSize);
    }

    void reset(){
        netWorthCalculations.clear();
    }
}
