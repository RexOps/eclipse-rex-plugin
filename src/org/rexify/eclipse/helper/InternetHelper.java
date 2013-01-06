package org.rexify.eclipse.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rexify.eclipse.model.TemplateModel;

public class InternetHelper {

	static public HashMap<String,TemplateModel> getTemplates() {
		HashMap<String, TemplateModel> ret = new HashMap<String, TemplateModel>();
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(GET("http://localhost:3000/"));
			JSONArray arr = (JSONArray)obj;
			Iterator iter = arr.iterator();
			
			while(iter.hasNext()) {
				JSONObject entry = (JSONObject)iter.next();
				
				System.out.println(entry.get("name"));
				
				ret.put(entry.get("name").toString(), new TemplateModel(entry.get("name").toString(), entry.get("template").toString()));
			}
		} catch(ParseException ex) {
			// display error message
			System.out.println(ex);
		}
		
		return ret;
	}
	
	static public String GET(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget   = new HttpGet(url);
		
		try {
			HttpResponse resp  = client.execute(httpget);
			HttpEntity entity  = resp.getEntity();
			
			if(entity != null) {
				InputStream content_stream = entity.getContent();
				return InternetHelper.convertStreamToString(content_stream);
			}
			else {
				// Use a sane fallback
				return "[{\"name\": \"Empty\", \"template\": \"# Rexfile\nuse Rex -base;\n\"}]";
			}
		} catch(ClientProtocolException pro_ex) {
			// display error message
			System.out.println(pro_ex);
		} catch(IOException io_ex) {
			// display error message
			System.out.println(io_ex);
		}
		
		// Use a sane fallback
		return "[{\"name\": \"Empty\", \"template\": \"# Rexfile\nuse Rex -base;\n\"}]";
	}
	
	static public String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	
}
