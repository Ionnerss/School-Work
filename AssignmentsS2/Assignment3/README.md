# SmartTravel Data Management System

**COMP 249 - Object-Oriented Programming II - Assignment 2**  
**Author:** Catalin-Ion Besleaga (40347936)

## Overview

SmartTravel is a Java console-based travel management system built for COMP 249 Assignment 2. It extends the original SmartTravel project by adding:

- robust validation in constructors and setters
- custom checked and unchecked exceptions
- CSV persistence with error logging
- fixed-size in-memory arrays
- search, filtering, and summary operations
- predefined scenario loading
- dashboard and chart generation using JFreeChart

The system manages four main data domains:

- **Clients**
- **Trips**
- **Transportation**
- **Accommodations**

## Main Features

### Core management
- Add, edit, delete, and list clients
- Create, edit, cancel, and list trips
- Add, remove, and filter transportation options
- Add, remove, and filter accommodations

### File persistence
- Load all data from CSV files
- Save all current data back to CSV files
- Preserve invalid predefined rows separately
- Log invalid file entries into `output/logs/errors.txt`

### Validation and business rules
- Email validation and duplicate-email protection
- Trip destination, duration, and price validation
- Transportation validation by subtype
- Accommodation validation by subtype
- ID resolution through service lookup methods

### Additional operations
- Display most expensive trip
- Calculate total cost of a selected trip
- Create deep copies of transportation and accommodation arrays

### Visualization
- Generate a dashboard HTML page
- Generate three charts:
  - Trip cost bar chart
  - Trips-per-destination pie chart
  - Trip duration line chart

## Assignment-Aligned Design

This project follows the Assignment 2 requirement to maintain **four fixed arrays in memory**:

- `clients[]` → max 100
- `trips[]` → max 200
- `accommodations[]` → max 50
- `transportations[]` → max 50

The service layer also tracks counts separately and works on the valid range `0..count)`.

## Package Organization

The project is organized into the following logical packages:

```text
AssignmentsS2/Assignment2/src/
├── client/
│   └── Client.java
├── driver/
│   └── SmartTravelDriver.java
├── exceptions/
│   ├── DuplicateEmailException.java
│   ├── EntityNotFoundException.java
│   ├── InvalidAccommodationDataException.java
│   ├── InvalidClientDataException.java
│   ├── InvalidTransportDataException.java
│   └── InvalidTripDataException.java
├── persistence/
│   ├── AccommodationFileManager.java
│   ├── ClientFileManager.java
│   ├── ErrorLogger.java
│   ├── TransportationFileManager.java
│   └── TripFileManager.java
├── service/
│   └── SmartTravelService.java
├── travel/
│   ├── Accommodation.java
│   ├── Bus.java
│   ├── Flight.java
│   ├── Hostel.java
│   ├── Hotel.java
│   ├── Train.java
│   ├── Transportation.java
│   └── Trip.java
└── visualization/
    ├── DashboardGenerator.java
    └── TripChartGenerator.java
```

## Class Responsibilities

### `SmartTravelDriver`
Console UI and menu navigation. Handles user interaction and delegates logic to the service layer.

### `SmartTravelService`
Central business logic layer.

Responsibilities include:
- storing the 4 fixed arrays
- tracking array counts
- creating and updating domain objects
- validating entity links by ID
- loading/saving all data through file managers
- recalculating `amountSpent` for each client
- managing invalid predefined rows

### Domain model
- `Client` stores client information and tracked spending
- `Trip` links a client with optional accommodation and/or transportation
- `Transportation` is an abstract parent for `Flight`, `Train`, and `Bus`
- `Accommodation` is an abstract parent for `Hotel` and `Hostel`

### Persistence layer
Each file manager handles one CSV type:
- parsing
- object creation
- skipping invalid rows
- logging errors
- syncing next generated ID after loading

### Visualization layer
- `TripChartGenerator` creates chart PNGs using JFreeChart
- `DashboardGenerator` creates the HTML dashboard and attempts to open it automatically

## Custom Exceptions

This project defines the following exceptions:

### Checked exceptions
- `InvalidClientDataException`
- `InvalidTripDataException`
- `InvalidTransportDataException`
- `InvalidAccommodationDataException`
- `EntityNotFoundException`

### Unchecked exception
- `DuplicateEmailException`

## Data Model Summary

### Client
Fields:
- `clientId`
- `firstName`
- `lastName`
- `emailAdress`
- `amountSpent`

### Trip
Fields:
- `tripId`
- `clientId`
- `accomodationId`
- `transportId`
- `destination`
- `duration`
- `basePrice`

### Transportation subclasses
- `Flight` → `baseFare`, `luggageAllowance`
- `Train` → `baseFare`, `seatClass`
- `Bus` → `baseFare`, `numOfStops`

### Accommodation subclasses
- `Hotel` → `starRating`
- `Hostel` → `numOfSharedBeds`

## Business Rules Implemented

### Client rules
- first and last names cannot be null/empty
- name length must be reasonable
- emails must contain `@` and `.`
- duplicate emails are rejected

### Trip rules
- destination cannot be empty
- duration must be within valid bounds
- base price must be at least 100
- client ID is mandatory
- accommodation ID and transportation ID are optional, but **at least one must exist**

### Transportation rules
- IDs must match `TR...`
- company name and cities cannot be empty
- base fare cannot be negative
- bus stops must be at least 1
- flight luggage allowance cannot be negative
- train seat class cannot be empty

### Accommodation rules
- IDs must match `A...`
- name and location cannot be empty
- nightly price must be greater than 0
- hotel star rating must be between 1 and 5
- hostel price per night cannot exceed 150
- shared beds cannot be negative

## CSV File Formats

All CSV files use **semicolon-separated values** and **no headers**.

### `clients.csv`
```text
C1001;Sophia;Rossi;sophia@example.com
```

### `transports.csv`
```text
FLIGHT;TR3001;Alitalia;JFK;FCO;850.00;23.0
TRAIN;TR3002;Shinkansen;Tokyo;Kyoto;250.00;HighSpeed
BUS;TR3003;Greyhound;NYC;Boston;75.00;3
```

### `accommodations.csv`
```text
HOTEL;A4001;Hilton Rome;Rome;280.00;4
HOSTEL;A4002;Rome Backpackers;Rome;55.00;6
```

### `trips.csv`
```text
T2001;C1001;A4001;TR3001;Rome;5;1800.00
```

Notes:
- `AccommodationID` and `TransportationID` are optional in trip rows
- however, at least one must be present for a valid trip

## Output Structure

The project uses an `output/` folder for generated files.

Expected structure:

```text
output/
├── data/
│   ├── accommodations.csv
│   ├── clients.csv
│   ├── transports.csv
│   └── trips.csv
├── dashboard/
│   ├── dashboard.html
│   └── styles.css
├── charts/
│   ├── trip_cost_bar_chart.png
│   ├── trip_duration_line_chart.png
│   └── trips_per_destination_pie.png
└── logs/
    └── errors.txt
```

## Menu Overview

The console application includes the following main menu options:

1. Client Management
2. Trip Management
3. Transportation Management
4. Accommodation Management
5. Additional Operations
6. Exit Program
7. List All Data Summary
8. Load All Data
9. Save All Data
10. Run Predefined Scenario
11. Generate Dashboard
0. Exit

## How to Use the Program

### 1. Start the application
Run `SmartTravelDriver`.

At startup, the program asks whether you want to load the predefined scenario.

- `1` → load predefined test scenario
- `2` → start mostly empty

### 2. Use menu sections
Navigate through the console menus to:
- add/edit/delete clients
- create/edit/cancel trips
- manage transportation and accommodations
- perform extra operations

### 3. Load data from CSV
Use menu option **8** to load all data from the expected data directory.

### 4. Save data to CSV
Use menu option **9** to write all current valid data back to CSV files.

### 5. Generate dashboard
Use menu option **11** to:
- generate charts
- generate the dashboard HTML
- attempt to open it automatically in your browser

## Predefined Scenario

The predefined scenario demonstrates:
- valid hardcoded clients
- valid and invalid trips
- valid and invalid accommodations
- valid and invalid transportation options
- exception handling and invalid-row preservation

This makes it useful for testing the program quickly without manually entering lots of data.

## Deep Copy Support

The driver includes operations to create deep copies of:
- transportation arrays
- accommodation arrays

These copies are made using the copy constructors of the respective subclasses.

## Client Spending Tracking

Each `Client` stores an `amountSpent` field.

This value is recalculated by `SmartTravelService` based on the total cost of all trips linked to that client. The service refreshes these totals when clients or trips are updated.

## Dependencies

This project uses:
- `jfreechart-1.5.6.jar`
- `jcommon-1.0.24.jar`

These are required for dashboard chart generation.

## Example Compile and Run

Make sure your source files are placed in directories that match their package declarations.

Example from the project root:

```bash
javac -cp ".;lib/jfreechart-1.5.6.jar;lib/jcommon-1.0.24.jar" -d . src/client/*.java src/driver/*.java src/exceptions/*.java src/persistence/*.java src/service/*.java src/travel/*.java src/visualization/*.java
```

Run:

```bash
java -cp ".;lib/jfreechart-1.5.6.jar;lib/jcommon-1.0.24.jar" AssignmentsS2.Assignment2.src.driver.SmartTravelDriver
```

### Linux/macOS variant

```bash
javac -cp ".:lib/jfreechart-1.5.6.jar:lib/jcommon-1.0.24.jar" -d . src/client/*.java src/driver/*.java src/exceptions/*.java src/persistence/*.java src/service/*.java src/travel/*.java src/visualization/*.java
java -cp ".:lib/jfreechart-1.5.6.jar:lib/jcommon-1.0.24.jar" AssignmentsS2.Assignment2.src.driver.SmartTravelDriver
```

## Notes and Limitations

- The project intentionally uses **arrays**, not `ArrayList`, `HashMap`, etc., to satisfy the assignment rules.
- Invalid predefined rows are preserved separately and can still be displayed in summaries.
- CSV loading is defensive: bad rows are skipped and logged instead of crashing the whole load.
- The visualization classes currently write dashboard and chart output using the paths coded in `DashboardGenerator` and `TripChartGenerator`, so keep your project folder structure consistent when running them.

## Suggested Repository Contents

A clean submission/repo should contain:

- source code in the correct package structure
- required library jars
- `README.md`
- `output/data/` sample CSV files
- `output/logs/` for error logging
- `output/dashboard/` and `output/charts/` for generated visualization files

## Final Summary

This project is a structured Java travel management system that demonstrates:
- inheritance and abstract classes
- exception handling
- fixed-size array management
- file I/O and CSV persistence
- dashboard/chart generation
- separation of concerns between UI, service, model, persistence, and visualization layers


### Stuff to add later
LinkedList is suitable for RecentList because this feature frequently adds new items to the front and removes old items from the end. Those operations match a recent-history list naturally and make the implementation simple and clean.

