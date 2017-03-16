package Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class jsonUtils {

	public static String listToJsonString (ArrayList<String> list) {
		
		String json = new Gson().toJson(list);
		
		return json;
	}
	
	public static String mapToJsonString (HashMap<String,ArrayList<String>> map) {
		
		String json = new Gson().toJson(map);
		
		return json;
	}

	public static String getValue(String json) {
		
		String result = null;
		
		JsonObject  jobject = (JsonObject) new JsonParser().parse(json);
		//System.out.println("jobject : " + jobject);
		
		jobject = jobject.getAsJsonObject("entities");
		//System.out.println("jobject : " + jobject);

		Set<Entry<String, JsonElement>> set = jobject.entrySet();
	    Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();

	    if (set.size() != 0) {
	    	
	    //while (iterator.hasNext()) {
	    	
	    	Map.Entry<String, JsonElement> entry = iterator.next();
	        String key = entry.getKey();
	        
	        JsonArray jarray = (JsonArray) entry.getValue();
			//System.out.println("jarray : " + jarray);
			
			jobject = (JsonObject) jarray.get(0);
			//System.out.println("jobject : " + jobject);
			
			JsonPrimitive jprimitive = jobject.getAsJsonPrimitive("value");
			//System.out.println("jprimitive : " + jprimitive);
			
			result = jprimitive.getAsString();
	    }
		 
		
		return result;
	}
	
	public static String convertStreamToString(InputStream is) {
	    Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
}
