SmartTravel A3 - COMP 249 Assignment 3
Written by: Catalin-Ion Besleaga (40347936)

Overview:
This project is the Assignment 3 version of SmartTravel. It extends the previous SmartTravel system by replacing fixed-array storage with ArrayList collections, adding interfaces, generics, repository-based filtering and sorting, a generic file manager, and an advanced analytics menu, while keeping the original A2 menus and functionality operational.

A2 Compatibility: Y
All original A2 menus and core features remain available, including client, trip, transportation, and accommodation management, file loading/saving, predefined scenario support, and dashboard generation.

LinkedList Justification:
LinkedList was used inside RecentList<T> because this feature constantly adds the newest item to the front and may remove the oldest item from the end. Those operations are simple and efficient with LinkedList, which makes it a better fit than ArrayList for recent-history tracking.

Project Structure:
- src/      -> all Java source files
- data/     -> CSV input/output files used by GenericFileManager
- output/   -> generated dashboard, charts, and error logs

Implemented A3 Requirements:
1. Collections Foundation
   - Replaced fixed-size arrays with ArrayList collections in SmartTravelService
   - Implemented RecentList<T> using LinkedList with max size 10

2. Interfaces
   - Identifiable
   - Billable
   - CsvPersistable
   - Comparable<T> natural ordering in model classes

3. Generics
   - Repository<T>
   - GenericFileManager<T>
   - RecentList<T>

4. Filtering and Sorting
   - Predicate-based filtering through Repository<T>
   - Natural business ordering for Client, Trip, Accommodation, and Transportation

5. Advanced Analytics Menu
   - Trips by destination
   - Trips by cost range
   - Top clients by spending
   - Recent trips
   - Smart sorted collections

Notes:
- CSV files are stored in the top-level data/ folder.
- Dashboard files and logs are generated in output/.
- Invalid predefined rows are preserved when saving and are still visible to the user.