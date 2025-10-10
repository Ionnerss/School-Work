package ToDoList_Project;

import java.io.File;

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
        String jsonString = file.toString();







        // JSONObject jsonObject = new JSONObject(jsonString);
        
        // JSONArray dataArray = jsonObject.getJSONArray("data");

        // JSONObject newItem = new JSONObject();
        // newItem.put("id", (dataArray.length() + 1));
        // newItem.put("Title", name);
        // newItem.put("Date", time);
        // newItem.put("Description", description);

        // dataArray.put(newItem);
        // jsonObject.put("data", dataArray);

        // String updatedjsonString = jsonObject.toString(2);
        // System.out.println(updatedjsonString);





    }
}
