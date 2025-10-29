package ToDoList_Project;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class TaskCreator {

    public void TaskProcessor(String[] info){

        File file = new File("TaskLibrary.json");

        /*
         * This represents the key/value pair they talked about in the vid. Basically the key (data)
         * allows us to go fetch specific information about an object within this array. We have multiple ids
         * which contain specific information. Thats what we'll use to store, modify, and view data.
         *  {
         *      "data": [
         *           {"id": 1, "name": "Item A"},
         *           {"id": 2, "name": "Item B"}
         *      ]
         *   }
         */
            StringBuilder jsonStringBuilder = new StringBuilder();
            try (java.util.Scanner scanner = new java.util.Scanner(file)) {
                while (scanner.hasNextLine()) {
                    jsonStringBuilder.append(scanner.nextLine());
                }
            } catch (java.io.FileNotFoundException e) {
                // If file doesn't exist, initialize with empty JSON object
                jsonStringBuilder.append("{\"data\":[]}");
            }
            String jsonString = jsonStringBuilder.toString();
            if (jsonString.isEmpty()) {
                jsonString = "{\"data\":[]}";
            }
        
        JSONObject jsonObject = new JSONObject(jsonString);
        
        JSONArray dataArray = jsonObject.getJSONArray("data");

        // Assuming info[0]=name, info[1]=time, info[2]=description
        String name = info[0];
        String time = info[1];
        String description = info[2];

        JSONObject newItem = new JSONObject();
        newItem.put("id", (dataArray.length()));
        newItem.put("Title", name);
        newItem.put("Date", time);
        newItem.put("Description", description);

        dataArray.put(newItem);
        jsonObject.put("data", dataArray);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, jsonObject);
        } catch (java.io.IOException e) {
            // Handle the exception (log or print). IOException covers StreamWriteException and DatabindException.
            e.printStackTrace();
        }

        String updatedjsonString = jsonObject.toString(2);
        System.out.println(updatedjsonString);

    }

    public static void main(String[] args) {
        // test code here
    }
}
