package Server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.sql.Date;

public class Delete_Document {

    public Delete_Document(JsonArray documentList) {
        for(int i = 0 ; i  < documentList.size() ; i++) {
            JsonObject propertiesJson = (JsonObject) documentList.get(i);

            int id = propertiesJson.get("ID").getAsInt();

            String path = propertiesJson.get("File").getAsString();

            File file =  new File(path) ;

            if (file.delete()) {
                System.out.println("Deleted the file: " + file.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
        }
    }

}
class Document {

    private int ID;

    private String Title;


    private String File;

    private String Author;

    private Date Publish_date;


    private int Id_folder;

}