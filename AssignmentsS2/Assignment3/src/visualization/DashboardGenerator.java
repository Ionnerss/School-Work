package AssignmentsS2.Assignment3.src.visualization;

/*
 * Assignment 3
 * Question: Dashboard Generation
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class generates an HTML dashboard for SmartTravel A3 using the new
 * List-based service layer and the A3 output folder structure.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.exceptions.EntityNotFoundException;
import AssignmentsS2.Assignment3.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment3.src.exceptions.InvalidTransportDataException;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;
import AssignmentsS2.Assignment3.src.travel.Trip;

public class DashboardGenerator {

    public static void generateDashboard(SmartTravelService service)
            throws IOException, InvalidAccommodationDataException,
            InvalidTransportDataException, EntityNotFoundException {

        new File("output").mkdirs();
        new File("output/charts").mkdirs();
        new File("output/dashboard").mkdirs();

        TripChartGenerator.generateCostBarChart(service);
        TripChartGenerator.generateDestinationPieChart(service);
        TripChartGenerator.generateDurationLineChart(service);

        generateHtmlDashboard(service);
        openInBrowser();
        System.out.println("Dashboard generated and opened!");
    }

    private static void generateHtmlDashboard(SmartTravelService service)
            throws IOException, EntityNotFoundException {

        try (PrintWriter out = new PrintWriter("output/dashboard/dashboard.html")) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("  <meta charset='UTF-8'>");
            out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("  <title>SmartTravel A3 Dashboard</title>");
            out.println("  <style>");
            out.println("    body { font-family: Arial, sans-serif; margin: 0; background: #f5f7fb; color: #1f2937; }");
            out.println("    .container { max-width: 1200px; margin: 0 auto; padding: 24px; }");
            out.println("    header { background: white; border-radius: 14px; padding: 24px; box-shadow: 0 8px 24px rgba(0,0,0,0.08); margin-bottom: 24px; }");
            out.println("    h1, h2 { margin-top: 0; }");
            out.println("    section { background: white; border-radius: 14px; padding: 24px; box-shadow: 0 8px 24px rgba(0,0,0,0.08); margin-bottom: 24px; }");
            out.println("    table { width: 100%; border-collapse: collapse; }");
            out.println("    th, td { text-align: left; padding: 12px; border-bottom: 1px solid #e5e7eb; }");
            out.println("    th { background: #f9fafb; }");
            out.println("    .chart-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 16px; }");
            out.println("    .chart-card { background: #f9fafb; border-radius: 12px; padding: 16px; text-align: center; }");
            out.println("    .chart-card img { width: 100%; height: auto; border-radius: 8px; }");
            out.println("    .stat-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 16px; }");
            out.println("    .stat-item { background: #f9fafb; border-radius: 12px; padding: 16px; }");
            out.println("    .stat-label { display: block; font-size: 0.9rem; color: #6b7280; margin-bottom: 8px; }");
            out.println("    .stat-value { font-size: 1.3rem; font-weight: bold; }");
            out.println("  </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("  <div class='container'>");

            writeSummary(service, out);
            writeClientsTable(service, out);
            writeTripsTable(service, out);
            writeChartsSection(out);
            writeStats(service, out);

            out.println("  </div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private static void writeSummary(SmartTravelService service, PrintWriter out) {
        out.println("    <header>");
        out.println("      <h1>SmartTravel Dashboard</h1>");
        out.println("      <p>A3: Collections + Interfaces + Generics | "
                + service.getClientCount() + " Clients | "
                + service.getTripCount() + " Trips</p>");
        out.println("    </header>");
    }

    private static void writeClientsTable(SmartTravelService service, PrintWriter out) {
        out.println("    <section>");
        out.println("      <h2>Clients (" + service.getClientCount() + ")</h2>");
        out.println("      <table>");
        out.println("        <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Total Spent ($)</th></tr></thead>");
        out.println("        <tbody>");

        for (Client client : service.getClients()) {
            if (client == null) {
                continue;
            }

            out.println("          <tr>");
            out.println("            <td><strong>" + escape(client.getId()) + "</strong></td>");
            out.println("            <td>" + escape(client.getFirstName() + " " + client.getLastName()) + "</td>");
            out.println("            <td>" + escape(client.getEmailAdress()) + "</td>");
            out.println("            <td>" + String.format("%,.2f", client.getTotalSpent()) + "</td>");
            out.println("          </tr>");
        }

        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </section>");
    }

    private static void writeTripsTable(SmartTravelService service, PrintWriter out) {
        out.println("    <section>");
        out.println("      <h2>Trips (" + service.getTripCount() + ")</h2>");
        out.println("      <table>");
        out.println("        <thead><tr><th>ID</th><th>Client</th><th>Destination</th><th>Days</th><th>Price</th></tr></thead>");
        out.println("        <tbody>");

        int index = 0;
        for (Trip trip : service.getTrips()) {
            if (trip == null) {
                index++;
                continue;
            }

            String total = "unavailable";
            try {
                total = "$" + String.format("%.2f", service.calculateTripTotal(index));
            } catch (EntityNotFoundException ignored) {
                total = "unavailable";
            }

            out.println("          <tr>");
            out.println("            <td><strong>" + escape(trip.getId()) + "</strong></td>");
            out.println("            <td>" + escape(trip.getClientId()) + "</td>");
            out.println("            <td>" + escape(trip.getDestination()) + "</td>");
            out.println("            <td>" + trip.getDurationInDays() + "</td>");
            out.println("            <td>" + total + "</td>");
            out.println("          </tr>");
            index++;
        }

        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </section>");
    }

    private static void writeChartsSection(PrintWriter out) {
        out.println("    <section>");
        out.println("      <h2>Analytics (JFreeChart)</h2>");
        out.println("      <div class='chart-grid'>");
        out.println("        <figure class='chart-card'>");
        out.println("          <img src='../charts/trip_cost_bar_chart.png' alt='Trip Costs'>");
        out.println("          <figcaption>Total Cost per Trip</figcaption>");
        out.println("        </figure>");
        out.println("        <figure class='chart-card'>");
        out.println("          <img src='../charts/trips_per_destination_pie.png' alt='Destinations'>");
        out.println("          <figcaption>Trips per Destination</figcaption>");
        out.println("        </figure>");
        out.println("        <figure class='chart-card'>");
        out.println("          <img src='../charts/trip_duration_line_chart.png' alt='Durations'>");
        out.println("          <figcaption>Trip Durations</figcaption>");
        out.println("        </figure>");
        out.println("      </div>");
        out.println("    </section>");
    }

    private static void writeStats(SmartTravelService service, PrintWriter out) {
        int tripCount = service.getTripCount();

        out.println("    <section>");
        out.println("      <h2>Quick Stats (" + tripCount + " Trips)</h2>");

        if (tripCount == 0) {
            out.println("      <p>No trip data available.</p>");
            out.println("    </section>");
            return;
        }

        double totalRevenue = 0.0;
        double totalDays = 0.0;
        int validTripCount = 0;

        for (Trip trip : service.getTrips()) {
            if (trip == null) {
                continue;
            }

            try {
                totalRevenue += trip.calculateTotalCost();
                totalDays += trip.getDurationInDays();
                validTripCount++;
            } catch (EntityNotFoundException ignored) {
                // Skip broken rows.
            }
        }

        double avgCost = validTripCount > 0 ? totalRevenue / validTripCount : 0.0;
        double avgDuration = validTripCount > 0 ? totalDays / validTripCount : 0.0;
        String mostVisited = findMostVisitedDestination(service);
        int visitCount = countDestinationVisits(service, mostVisited);

        out.println("      <div class='stat-grid'>");
        out.println("        <div class='stat-item'><span class='stat-label'>Avg Trip Cost</span><span class='stat-value'>$"
                + String.format("%,.0f", avgCost) + "</span></div>");
        out.println("        <div class='stat-item'><span class='stat-label'>Avg Duration</span><span class='stat-value'>"
                + String.format("%.1f", avgDuration) + " days</span></div>");
        out.println("        <div class='stat-item'><span class='stat-label'>Total Revenue</span><span class='stat-value'>$"
                + String.format("%,.0f", totalRevenue) + "</span></div>");
        out.println("        <div class='stat-item'><span class='stat-label'>Most Visited</span><span class='stat-value'>"
                + escape(mostVisited) + "<br><small>(" + visitCount + " trips)</small></span></div>");
        out.println("      </div>");
        out.println("    </section>");
    }

    private static String findMostVisitedDestination(SmartTravelService service) {
        String mostVisited = "N/A";
        int maxCount = 0;

        for (Trip trip : service.getTrips()) {
            if (trip == null) {
                continue;
            }

            String destination = trip.getDestination();
            int count = countDestinationVisits(service, destination);
            if (count > maxCount) {
                maxCount = count;
                mostVisited = destination;
            }
        }

        return mostVisited;
    }

    private static int countDestinationVisits(SmartTravelService service, String destination) {
        if (destination == null || destination.trim().isEmpty() || "N/A".equals(destination)) {
            return 0;
        }

        int count = 0;
        for (Trip trip : service.getTrips()) {
            if (trip != null && destination.equals(trip.getDestination())) {
                count++;
            }
        }

        return count;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private static void openInBrowser() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String url = new File("output/dashboard/dashboard.html").getAbsolutePath();

            ProcessBuilder pb;
            if (os.contains("win")) {
                pb = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", url);
            } else {
                pb = new ProcessBuilder("xdg-open", url);
            }

            pb.start();
        } catch (IOException e) {
            System.out.println("Open output/dashboard/dashboard.html manually in your browser");
        }
    }
}
