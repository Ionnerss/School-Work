package AssignmentsS2.Assignment2.src.visualization;

/**
 * DashboardGenerator - Generates a professional HTML Dashboard for SmartTravel Pro (COMP249 A2)
 * 
 * Features:
 * - Responsive tables for Clients and Trips
 * - JFreeChart integration (bar/pie/line charts)
 * - Bookman Old Style typography with Google Fonts fallback
 * - Auto-opens in default browser
 * - Professional CSS animations and hover effects
 * 
 * Usage: DashboardGenerator.generateDashboard(service);
 * 
 * Output: output/dashboard.html + 3 chart PNGs
 * 
 * @author Kaustubha Mendhurwar
 * @version Winter 2026 - A2
 */

import AssignmentsS2.Assignment2.src.service.SmartTravelService;
import AssignmentsS2.Assignment2.src.client.Client;
import AssignmentsS2.Assignment2.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment2.src.exceptions.InvalidTransportDataException;
import AssignmentsS2.Assignment2.src.travel.Trip;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

public class DashboardGenerator {
    
	/**
	 * Main entry point for dashboard generation.
	 * 
	 * <p>Workflow:</p>
	 * <ol>
	 *   <li>Generates JFreeChart PNGs (cost bar, destination pie, duration line)</li>
	 *   <li>Writes HTML with embedded CSS and tables</li>
	 *   <li>Auto-opens output/dashboard.html in browser</li>
	 * </ol>
	 * 
	 * @param service SmartTravelService with populated Client/Trip arrays
	 * @throws IOException if file I/O fails (output dir/charts)
	 * @throws InvalidTransportDataException 
	 * @throws InvalidAccommodationDataException 
	 */
    public static void generateDashboard(SmartTravelService service) throws IOException, InvalidAccommodationDataException, InvalidTransportDataException {
        // Ensure output dir exists
        new File("output").mkdirs();
        
        // 1. Generate charts FIRST (your existing code)
        TripChartGenerator.generateCostBarChart(service);
        TripChartGenerator.generateDestinationPieChart(service);
        TripChartGenerator.generateDurationLineChart(service);
        
        // 2. Generate HTML dashboard
        generateHTMLDashboard(service);
        
        // 3. Auto-open in browser
        openInBrowser();
        
        System.out.println("Dashboard generated and opened!");
    }
    
    /**
     * Generates complete HTML dashboard with responsive design.
     * 
     * <p>Writes to:</p>
     * <ul>
     *   <li>Header with app stats</li>
     *   <li>Clients table (ID, Name, Email)</li>
     *   <li>Trips table (ID, Client, Destination, Days, Price)</li>
     *   <li>Charts section (3 JFreeChart PNGs)</li>
     *   <li>Quick stats summary</li>
     * </ul>
     * 
     * @param service Service containing all data
     * @throws IOException if HTML write fails
     * @throws InvalidTransportDataException 
     * @throws InvalidAccommodationDataException 
     */
    private static void generateHTMLDashboard(SmartTravelService service) throws IOException, InvalidAccommodationDataException, InvalidTransportDataException {
        PrintWriter out = new PrintWriter("output/dashboard/dashboard.html");
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>SmartTravel A2 Dashboard</title>");
        // 
        out.println("    <link rel='stylesheet' href='styles.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        writeSummary(service, out);
        writeClientsTable(service, out);
        writeTripsTable(service, out);
        writeChartsSection(out);
        writeStats(service, out);
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    
    /**
     * Writes dashboard header with dynamic stats.
     * 
     * @param service Service for client/trip counts
     * @param out HTML PrintWriter
     */
    private static void writeSummary(SmartTravelService service, PrintWriter out) {
        out.println("        <header>");
        out.println("            <h1>SmartTravel Dashboard</h1>");
        out.println("            <p>A2: File I/O + Exceptions | " + 
                   service.getClientCount() + " Clients | " + service.getTripCount() + " Trips</p>");
        out.println("        </header>");
    }
    
    /**
     * Generates responsive Clients table from service data.
     * 
     * <p>Columns: ID (bold), Full Name, Email</p>
     * <p>Hover effects + alternating row colors</p>
     * 
     * @param service Service containing Client array
     * @param out HTML PrintWriter
     */
    private static void writeClientsTable(SmartTravelService service, PrintWriter out)
            throws InvalidAccommodationDataException, InvalidTransportDataException {
        out.println("        <section class='data-section'>");
        out.println("            <h2> Clients (" + service.getClientCount() + ")</h2>");
        out.println("            <table>");
        out.println("                <thead>");
        out.println("                    <tr><th>ID</th><th>Name</th><th>Email</th><th>Total Spent ($)</th></tr>");
        out.println("                </thead>");
        out.println("                <tbody>");
        
        for (int i = 0; i < service.getClientCount(); i++) {
            Client client = service.getClient(i);
            double spent = client.getTotalSpent();
            out.println("                    <tr>");
            out.println("                        <td><strong>" + client.getClientID() + "</strong></td>");
            out.println("                        <td>" + client.getFirstName() + " " + client.getLastName() + "</td>");
            out.println("                        <td>" + client.getEmailAdress() + "</td>");
            out.println("                        <td style='font-weight: bold; color: " + 
                    								(spent > 3000 ? "#d32f2f" : "#388e3c") + ";'>" + 
                    										String.format("%,.2f", spent) + "</td>");
            out.println("                    </tr>");
        }
        out.println("                </tbody>");
        out.println("            </table>");
        out.println("        </section>");
    }
    
    /**
     * Generates responsive Trips table sorted by ID.
     * 
     * <p>Columns: ID (bold), Client ID, Destination, Days, Price ($ formatted)</p>
     * 
     * @param service Service containing Trip array
     * @param out HTML PrintWriter
     */
    private static void writeTripsTable(SmartTravelService service, PrintWriter out)
            throws InvalidAccommodationDataException, InvalidTransportDataException {
        out.println("        <section class='data-section'>");
        out.println("            <h2> Trips (" + service.getTripCount() + ")</h2>");
        out.println("            <table>");
        out.println("                <thead>");
        out.println("                    <tr><th>ID</th><th>Client</th><th>Destination</th><th>Days</th><th>Price</th></tr>");
        out.println("                </thead>");
        out.println("                <tbody>");
        
        for (int i = 0; i < service.getTripCount(); i++) {
            Trip trip = service.getTrip(i);
            out.println("                    <tr>");
            out.println("                        <td><strong>" + trip.getTripId() + "</strong></td>");
            out.println("                        <td>" + trip.getClient() + "</td>");
            out.println("                        <td>" + trip.getDestination() + "</td>");
            out.println("                        <td>" + trip.getDurationInDays() + "</td>");
            out.println("                        <td>$" + String.format("%.2f", service.calculateTripTotal(i)) + "</td>");
            out.println("                    </tr>");
        }
        out.println("                </tbody>");
        out.println("            </table>");
        out.println("        </section>");
    }
    
    /**
     * Embeds 3 JFreeChart PNGs in responsive grid.
     * 
     * <p>Expects PNGs: trip_cost_bar_chart.png, trips_per_destination_pie.png, trip_duration_line_chart.png</p>
     * 
     * @param out HTML PrintWriter
     */
    private static void writeChartsSection(PrintWriter out) {
        out.println("        <section class='charts-section'>");
        out.println("            <h2>Analytics (JFreeChart)</h2>");
        out.println("            <div class='chart-grid'>");
        out.println("                <figure class='chart-card'>");
        out.println("                    <img src='../charts/trip_cost_bar_chart.png' alt='Trip Costs' loading='lazy'>");
        out.println("                    <figcaption>Total Cost per Trip</figcaption>");
        out.println("                </figure>");
        out.println("                <figure class='chart-card'>");
        out.println("                    <img src='../charts/trips_per_destination_pie.png' alt='Destinations' loading='lazy'>");
        out.println("                    <figcaption>Trips per Destination</figcaption>");
        out.println("                </figure>");
        out.println("                <figure class='chart-card'>");
        out.println("                    <img src='../charts/trip_duration_line_chart.png' alt='Durations' loading='lazy'>");
        out.println("                    <figcaption>Trip Durations</figcaption>");
        out.println("                </figure>");
        out.println("            </div>");
        out.println("        </section>");
    }
    
    /**
     * Calculates and displays key business metrics.
     * 
     * <p>Stats: Average Trip Cost, Total Revenue</p>
     * 
     * @param service Service containing Trip array
     * @param out HTML PrintWriter
     */
    private static void writeStats(SmartTravelService service, PrintWriter out)
            throws InvalidAccommodationDataException, InvalidTransportDataException {
        
    	int tripCount = service.getTripCount();
        if (tripCount == 0) {
            out.println("        <section class='stats-section'>");
            out.println("            <h2>No Trip Data</h2>");
            out.println("        </section>");
            return;
        }
    	
        // 1. Total Revenue & Avg Cost
        double totalRevenue = 0.0;
        Trip[] trips = service.getTrips();
        int safeTripCount = Math.min(tripCount, trips.length);

        for (int i = 0; i < safeTripCount; i++) {
            if (trips[i] == null) {
                continue;
            }
            totalRevenue += service.calculateTripTotal(i);
        }
        double avgCost = safeTripCount == 0 ? 0.0 : totalRevenue / safeTripCount;
        
        // 2. Average Duration (days)
        double totalDays = 0.0, avgDuration =0.0;

        for (int i = 0; i < safeTripCount; i++) {
            if (trips[i] == null) {
                continue;
            }
            totalDays += trips[i].getDurationInDays();
        }
        avgDuration = safeTripCount == 0 ? 0.0 : totalDays / safeTripCount;

        String mostVisited = "N/A";
        int visitCount = 0;
        for (int i = 0; i < safeTripCount; i++) {
            Trip trip = trips[i];
            if (trip == null) {
                continue;
            }

            String candidate = trip.getDestination();
            int currentCount = 0;
            for (int j = 0; j < safeTripCount; j++) {
                if (trips[j] != null && candidate.equals(trips[j].getDestination())) {
                    currentCount++;
                }
            }

            if (currentCount > visitCount) {
                visitCount = currentCount;
                mostVisited = candidate;
            }
        }
    	
        
        out.println("        <section class='stats-section'>");
        out.println("            <h2>Quick Stats (" + tripCount + " Trips)</h2>");
        out.println("            <div class='stat-grid'>");
                
        // Stat 1: Average Cost
        out.println("                <div class='stat-item'>");
        out.println("                    <span class='stat-label'>Avg Trip Cost</span>");
        out.println("                    <span class='stat-value'>$" + String.format("%,.0f", avgCost) + "</span>");
        out.println("                </div>");
        
        // Stat 2: Average Duration 
        out.println("                <div class='stat-item'>");
        out.println("                    <span class='stat-label'>Avg Duration</span>");
        out.println("                    <span class='stat-value'>" + String.format("%.1f", avgDuration) + " days</span>");
        out.println("                </div>");
        
        // Stat 3: Total Revenue
        out.println("                <div class='stat-item'>");
        out.println("                    <span class='stat-label'>Total Revenue</span>");
        out.println("                    <span class='stat-value'>$" + String.format("%,.0f", totalRevenue) + "</span>");
        out.println("                </div>");
        
        // Stat 4: Most Visited
        out.println("                <div class='stat-item'>");
        out.println("                    <span class='stat-label'>Most Visited</span>");
        out.println("                    <span class='stat-value'>" + mostVisited + "<br><small>(" + visitCount + " trips)</small></span>");
        out.println("                </div>");
        
        out.println("            </div>");
        out.println("        </section>");
    }
    
    /**
     * Cross-platform browser launcher for dashboard.html.
     * 
     * <p>Supports Windows (rundll32), macOS (open), Linux (xdg-open)</p>
     */
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
    
    /* HELPER METHODS */
    /**
     * Finds destination with most trips
     */
    private static String findMostVisitedDestination(SmartTravelService service) {
        Trip[] trips = service.getTrips();
        if (trips == null || trips.length == 0 || service.getTripCount() == 0)
            return "N/A";

        int limit = Math.min(service.getTripCount(), trips.length);
        String mostVisited = "N/A";
        int maxCount = 0;

        for (int i = 0; i < limit; i++) {
            Trip trip = trips[i];
            if (trip == null)
                continue;

            String destination = trip.getDestination();
            int count = countDestinationVisits(service, destination);
            if (count > maxCount) {
                maxCount = count;
                mostVisited = destination;
            }
        }

        return mostVisited;
    }

    /**
     * Counts trips to specific destination
     */
    private static int countDestinationVisits(SmartTravelService service, String destination) {
        if (destination == null || destination.trim().isEmpty())
            return 0;

        Trip[] trips = service.getTrips();
        if (trips == null || trips.length == 0 || service.getTripCount() == 0)
            return 0;

        int limit = Math.min(service.getTripCount(), trips.length);
        int count = 0;
        for (int i = 0; i < limit; i++) {
            Trip trip = trips[i];
            if (trip != null && destination.equals(trip.getDestination()))
                count++;
        }

        return count;
    }
}
