package ToDoList_Project;

import java.io.File;
// import org.json.JSONObject;
// import org.json.JSONArray;
// import java.io.FileWriter;

public class TaskCreator {

    public void TaskProcessor(String[] info){
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

        File file = new File("TaskLibrary.json");
        String path = file.getPath();



        
        // File file = new File("TaskLibrary.json");
        // String jsonString = file.toString();
        // JSONObject jsonObject = new JSONObject(jsonString);
        
        JSONArray dataArray = jsonObject.getJSONArray("data");

        // Assuming info[0]=name, info[1]=time, info[2]=description
        String name = info[0];
        String time = info[1];
        String description = info[2];

        // JSONObject newItem = new JSONObject();
        // newItem.put("id", (dataArray.length() + 1));
        // newItem.put("Title", name);
        // newItem.put("Date", time);
        // newItem.put("Description", description);

        dataArray.put(newItem);
        jsonObject.put("data", dataArray);

        // FileWriter fileWriter = new Filewriter("TaskLibrary.json");


        // String updatedjsonString = jsonObject.toString(2);
        // System.out.println(updatedjsonString);





    }

    public static void main(String[] args) {
        // test code here
    }
}
