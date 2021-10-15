package com.example.internship_app;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = InternshipAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NetWorthControllerTest {
    @Value("${local.server.port}")
    int port;
    @Autowired
    private NetWorthController netWorthController;

    @BeforeEach
    public void setup() {
        netWorthController.reset();
    }

    @ParameterizedTest
    @CsvSource({
            "5000,2669.82",
            "1704,0.06",
            "7000,4289.82",
    })
    public void givenGrossValue_returnsNetValue(double gross, double net) {
        assertEquals(
                net,
                netWorthController.calculateNetValue(gross),
                0.001
        );
    }

    @ParameterizedTest
    @CsvSource({
            "5000,2669.82",
            "1900,158.82",
            "6000,3479.82",
    })
    public void calcEndpoint_givenGrossValue_returnsNetValue(double gross, double net) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:" + port + "/calc?gross=" + gross)
                .get()
                .build();
        Response response = new OkHttpClient()
                .newCall(request)
                .execute();
        assertTrue(response.isSuccessful());
        ResponseBody body = response.body();
        assertNotNull(body);
        assertEquals(
                net,
                Double.parseDouble(body.string()),
                0.001
        );
    }

    @Test
    @DisplayName("returnLastFiveEntries when no calculations were executed before returns empty list")
    public void lastFiveEntries_whenNoCalcExecutedBefore_returnsEmptyList() {
        assertEquals(
                List.of(),
                netWorthController.returnLastFiveEntries()
        );
    }

    @Test
    @DisplayName("returnLastFiveEntries when single calculation was executed before returns list containing one element")
    public void lastFiveEntries_returnsSingleElement() {
        netWorthController.calculateNetValue(5000);
        assertEquals(
                List.of(new NetWorthCalculation(5000, 2669.82)),
                netWorthController.returnLastFiveEntries()
        );
    }

    @Test
    @DisplayName("returnLastFiveEntries when five calculations were executed before returns list containing five elements")
    public void lastFiveEntries_returnsFiveElements() {
        netWorthController.calculateNetValue(5000);     //2669,82
        netWorthController.calculateNetValue(9500);     //6314,82
        netWorthController.calculateNetValue(2000);     //239,82
        netWorthController.calculateNetValue(14000);    //9959,82
        netWorthController.calculateNetValue(6500);     //3884,82
        assertEquals(
                List.of(
                        new NetWorthCalculation(5000, 2669.82),
                        new NetWorthCalculation(9500, 6314.82),
                        new NetWorthCalculation(2000, 239.82),
                        new NetWorthCalculation(14000, 9959.82),
                        new NetWorthCalculation(6500, 3884.82)
                ),
                netWorthController.returnLastFiveEntries()
        );
    }

    @Test
    @DisplayName("returnLastFiveEntries when many calculations were executed before returns list containing last five elements")
    public void lastFiveEntries_returnsLastFiveElements() {
        netWorthController.calculateNetValue(77777);    //should not be displayed
        netWorthController.calculateNetValue(5000);     //2669,82
        netWorthController.calculateNetValue(9500);     //6314,82
        netWorthController.calculateNetValue(2000);     //239,82
        netWorthController.calculateNetValue(14000);    //9959,82
        netWorthController.calculateNetValue(6500);     //3884,82
        assertEquals(
                List.of(
                        new NetWorthCalculation(5000, 2669.82),
                        new NetWorthCalculation(9500, 6314.82),
                        new NetWorthCalculation(2000, 239.82),
                        new NetWorthCalculation(14000, 9959.82),
                        new NetWorthCalculation(6500, 3884.82)
                ),
                netWorthController.returnLastFiveEntries()
        );
    }
}