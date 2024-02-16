import dto.MeasurementDTO;
import dto.MeasurementResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientGet {
    public static void main(String[] args) {
        List<Double> temps = getTemps();
        drawChart(temps);
    }

    private static List<Double> getTemps(){
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";

        MeasurementResponse measurementResponse = restTemplate.getForObject(url, MeasurementResponse.class);

        if (measurementResponse == null || measurementResponse.getMeasurements() == null){
            return Collections.emptyList();
        }

        return measurementResponse.getMeasurements().stream().map(MeasurementDTO::getValue)
                .collect(Collectors.toList());
    }

    private static void drawChart(List<Double> temps){
        double[] xData = IntStream.range(0, temps.size()).asDoubleStream().toArray();
        double[] yData = temps.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temp", "X", "Y", "temperature",
                xData, yData);

        new SwingWrapper(chart).displayChart();
    }
}
