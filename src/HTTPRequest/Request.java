package HTTPRequest;

import Utils.jsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Request {
	
	public Request () {
		
	}

	/**
	 * Example of connection to the API via /message
	 * 
	 * @param message
	 * @return
	 */
	public HttpResponse authenticate (String message) {
		
		String urlPath = "/message";
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s&q=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET), 
					                           URLEncoder.encode(message, Constants.CHARSET));

		    HttpGet request = new HttpGet(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
			
	}
	
	/**
	 * Example of connection to the API via /message with the version of the API i want to use
	 * 
	 * @param message
	 * @return
	 */
	public HttpResponse authenticateWithVersion (String message) {
		
		String urlPath = "/message";
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s&q=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET), 
											   URLEncoder.encode(message, Constants.CHARSET));

		    HttpGet request = new HttpGet(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    return response;
	}
	
	
	/**
	 * Create a value for an entity
	 * If the value already exists it just had the new expressions
	 * It can't remove expressions
	 * 
	 * @param entityId
	 * @param value
	 * @param expressions
	 * @return
	 */
	public HttpResponse setValueToEntity (String entityId, String value, ArrayList<String> expressions) {
		
		String urlPath = "/entities/" + entityId + "/values";

	    String json = "{\"value\" : \"" + value + "\","
	    		+ "\"expressions\" : " + jsonUtils.listToJsonString(expressions) + "}";
	    StringEntity jsonEntity = null;
		try {
			jsonEntity = new StringEntity(json);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	    System.out.println("json : " + json);
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpPost request = new HttpPost(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    request.setHeader("Content-Type", "application/json");
		    
		    request.setEntity(jsonEntity);
		    
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}
	
	/**
	 * Simply remove an entity value
	 * 
	 * @param entityId
	 * @param value
	 * @return
	 */
	public HttpResponse removeValueFromEntity (String entityId, String value) {

	    HttpResponse response = null;
	    
		try {
			
			String urlPath = "/entities/" + entityId + "/values/" + URLEncoder.encode(value, Constants.CHARSET).replace("+", "%20");
		
			System.out.println("urlPath : " + urlPath);
			
		    String query;

		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpDelete request = new HttpDelete(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}
	
	
	/**
	 * list all the entity_id
	 * 
	 * @return
	 */
	public HttpResponse getEntities () {
		
		HttpResponse response = null;
	    
		try {
			
			String urlPath = "/entities";
		
			System.out.println("urlPath : " + urlPath);
			
		    String query;

		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpGet request = new HttpGet(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
		
	
	/**
	 * get all the value of an entity
	 * 
	 * @param entityId
	 * @return
	 */
	public HttpResponse getEntityValues (String entityId) {
		
		HttpResponse response = null;
	    
		try {
			
			String urlPath = "/entities/" + URLEncoder.encode(entityId, Constants.CHARSET).replace("+", "%20");
		
			System.out.println("urlPath : " + urlPath);
			
		    String query;

		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpGet request = new HttpGet(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	 * get the entities and according values of a sentence
	 * 
	 * @param message
	 * @return
	 */
	public String getSentenceMeaning (String message) {
	
		String urlPath = "/message";
		
	    String query;
	
	    HttpResponse response = null;
	    
	    String result = "";
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s&q=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET), 
											   URLEncoder.encode(message, Constants.CHARSET).replace("+", "%20"));
	
		    HttpGet request = new HttpGet(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
	
		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	result += line + "\n";
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    return result;
	}


	/**
	 * Create a new entity with values (or not) and with expressions (or not)
	 * 
	 * @param entityId
	 * @param values
	 * @param doc
	 * @return
	 */
	public HttpResponse createEntity (String entityId, HashMap<String, ArrayList<String>> values, String doc) {
		
		String urlPath = "/entities";

	    String json = "{\"doc\" : \"" + doc + "\", "
	    		+ "\"id\" : \"" + entityId + "\", "
	    		+ "\"values\" : [";
	    for (Entry<String, ArrayList<String>> e : values.entrySet()) {
	    	json += "{\"value\" : \"" + e.getKey() + "\", "
		    + "\"expressions\" : " + jsonUtils.listToJsonString(e.getValue()) + "},";
	    }
	    json = json.substring(0, json.length()-1);
	    json += "]}";
	    
	    StringEntity jsonEntity = null;
		try {
			jsonEntity = new StringEntity(json);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	    System.out.println("json : " + json);
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpPost request = new HttpPost(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    request.setHeader("Content-Type", "application/json");
		    
		    request.setEntity(jsonEntity);
		    
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}


	/**
	 * can add or remove or change either values or doc or value's expressions of an entity
	 * 
	 * @param entityId
	 * @param values
	 * @param doc
	 * @return
	 */
	public HttpResponse UpdateValuesOfEntity (String entityId, HashMap<String, ArrayList<String>> values, String doc) {
		
		String urlPath = "/entities/" + entityId;
		
		

	    String json = "{\"doc\" : \"" + doc + "\", "
	    		+ "\"values\" : [";
	    for (Entry<String, ArrayList<String>> e : values.entrySet()) {
	    	json += "{\"value\" : \"" + e.getKey() + "\", "
		    + "\"expressions\" : " + jsonUtils.listToJsonString(e.getValue()) + "},";
	    }
	    json = json.substring(0, json.length()-1);
	    json += "]}";
	    
	    StringEntity jsonEntity = null;
		jsonEntity = new StringEntity(json, "UTF-8");
	    System.out.println("json : " + json);
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpPut request = new HttpPut(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    request.setHeader("Content-Type", "application/json; charset=utf-8");
		    
		    
		    System.out.println(jsonUtils.convertStreamToString(jsonEntity.getContent()));
		    
		    request.setEntity(jsonEntity);
		    
		    
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}

	
	/**
	 * Delete the entity if it exists
	 * 
	 * @param entityId
	 * @return
	 */
	public HttpResponse removeEntity (String entityId) {
		
		String urlPath = "/entities/" + entityId;
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpDelete request = new HttpDelete(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		      
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}

	
	/**
	 * add a new expressions to a value of an entity
	 * 
	 * @param entityId
	 * @param value
	 * @param expression
	 * @return
	 */
	public HttpResponse setExpressionToEntity (String entityId, String value, String expression) {
		
		String urlPath = "/entities/" + entityId + "/values/" + value + "/expressions";

	    String json = "{\"expression\" : \"" + expression + "\"}";
	    
	    StringEntity jsonEntity = null;
		try {
			jsonEntity = new StringEntity(json);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	    System.out.println("json : " + json);
	
	    String query;

	    HttpResponse response = null;
	    
		try {
		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpPost request = new HttpPost(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);
		    request.setHeader("Content-Type", "application/json");
		    
		    request.setEntity(jsonEntity);
		    
		 
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}

	
	/**
	 * Remove an expression from a value of an entity
	 * 
	 * @param entityId
	 * @param value
	 * @param expression
	 * @return
	 */
	public HttpResponse removeExpressionFromEntity (String entityId, String value, String expression) {

	    HttpResponse response = null;
	    
		try {
			
			String urlPath = "/entities/" + URLEncoder.encode(entityId, Constants.CHARSET).replace("+", "%20") + "/values/" + URLEncoder.encode(value, Constants.CHARSET).replace("+", "%20") + "/expressions/" + URLEncoder.encode(expression, Constants.CHARSET).replace("+", "%20");
		
		    String query;
	    

		    HttpClient client = HttpClientBuilder.create().build();
		    
			query = String.format("v=%s", URLEncoder.encode(Constants.VERSION, Constants.CHARSET));

		    HttpDelete request = new HttpDelete(Constants.URL_DOMAIN + urlPath + "?" + query);
		    
		    request.setHeader("Authorization", "Bearer " + Constants.TOKEN_FRENCH_BOT);		    
		 
			response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine());

		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	System.out.println(line);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
			
	}

}
