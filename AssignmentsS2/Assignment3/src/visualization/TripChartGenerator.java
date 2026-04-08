package AssignmentsS2.Assignment3.src.visualization;

/*
 * Assignment 3
 * Question: Chart Generation
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class generates JFreeChart PNG charts using the A3 List-based
 * SmartTravel service layer and writes them to output/charts.
 */

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import AssignmentsS2.Assignment3.src.exceptions.EntityNotFoundException;
import AssignmentsS2.Assignment3.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment3.src.exceptions.InvalidTransportDataException;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;
import AssignmentsS2.Assignment3.src.travel.Trip;

public class TripChartGenerator {

    private static void applyChartStyling(JFreeChart chart) {
        Font titleFont = new Font("Bookman Old Style", Font.BOLD, 28);
        Font axisLabelFont = new Font("Bookman Old Style", Font.BOLD, 18);
        Font tickLabelFont = new Font("Bookman Old Style", Font.PLAIN, 14);
        Font legendFont = new Font("Bookman Old Style", Font.BOLD, 14);
        Font pieLabelFont = new Font("Bookman Old Style", Font.PLAIN, 13);

        if (chart.getTitle() != null) {
            chart.getTitle().setFont(titleFont);
        }

        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(legendFont);
        }

        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            CategoryAxis domainAxis = plot.getDomainAxis();
            ValueAxis rangeAxis = plot.getRangeAxis();

            domainAxis.setLabelFont(axisLabelFont);
            domainAxis.setTickLabelFont(tickLabelFont);
            rangeAxis.setLabelFont(axisLabelFont);
            rangeAxis.setTickLabelFont(tickLabelFont);
        }

        if (chart.getPlot() instanceof PiePlot) {
            @SuppressWarnings("unchecked")
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(pieLabelFont);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            plot.setSectionOutlinesVisible(false);
            plot.setShadowPaint(null);
            plot.setLabelBackgroundPaint(new Color(245, 245, 245, 180));
        }
    }

    public static void generateCostBarChart(SmartTravelService service)
            throws IOException, InvalidAccommodationDataException,
            InvalidTransportDataException, EntityNotFoundException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int index = 0;
        for (Trip trip : service.getTrips()) {
            if (trip == null) {
                index++;
                continue;
            }

            try {
                dataset.addValue(service.calculateTripTotal(index), "Total Cost", trip.getId());
            } catch (EntityNotFoundException ignored) {
                // Skip broken trip references.
            }

            index++;
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Trip Costs",
                "Trip ID",
                "Cost ($)",
                dataset
        );

        applyChartStyling(chart);
        ensureChartDirectory();
        ChartUtils.saveChartAsPNG(new File("output/charts/trip_cost_bar_chart.png"), chart, 900, 600);
    }

    public static void generateDestinationPieChart(SmartTravelService service) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        ListCounter counter = new ListCounter();

        for (Trip trip : service.getTrips()) {
            if (trip == null || trip.getDestination() == null || trip.getDestination().trim().isEmpty()) {
                continue;
            }

            counter.increment(trip.getDestination());
        }

        for (int i = 0; i < counter.size(); i++) {
            dataset.setValue(counter.getLabel(i), counter.getCount(i));
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Trips per Destination",
                dataset,
                true,
                true,
                false
        );

        applyChartStyling(chart);
        ensureChartDirectory();
        ChartUtils.saveChartAsPNG(new File("output/charts/trips_per_destination_pie.png"), chart, 900, 600);
    }

    public static void generateDurationLineChart(SmartTravelService service) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Trip trip : service.getTrips()) {
            if (trip != null) {
                dataset.addValue(trip.getDurationInDays(), "Duration", trip.getId());
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Trip Durations",
                "Trip ID",
                "Days",
                dataset
        );

        applyChartStyling(chart);
        ensureChartDirectory();
        ChartUtils.saveChartAsPNG(new File("output/charts/trip_duration_line_chart.png"), chart, 900, 600);
    }

    private static void ensureChartDirectory() {
        new File("output").mkdirs();
        new File("output/charts").mkdirs();
    }

    private static class ListCounter {
        private final java.util.ArrayList<String> labels = new java.util.ArrayList<>();
        private final java.util.ArrayList<Integer> counts = new java.util.ArrayList<>();

        void increment(String label) {
            if (label == null) {
                return;
            }

            for (int i = 0; i < labels.size(); i++) {
                if (label.equals(labels.get(i))) {
                    counts.set(i, counts.get(i) + 1);
                    return;
                }
            }

            labels.add(label);
            counts.add(1);
        }

        int size() {
            return labels.size();
        }

        String getLabel(int index) {
            return labels.get(index);
        }

        int getCount(int index) {
            return counts.get(index);
        }
    }
}
