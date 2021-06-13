package com.example.TwitterSearchApp.OntService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Service;

@Service("resultService")
public class ResultService {
	
	private static String akalogic;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	private static String getSubKeywords(String keywords, aClass checked){
		if(keywords.equals("")) keywords += checked.label;			
		else keywords += "|"+checked.label;
		if(akalogic.equals("yes")){
			if(!(checked.aka.equals(""))){
				List<String> akaList = Arrays.asList(checked.aka.split(",[ ]*"));
				for(int i=0; i<akaList.size(); i++){
					keywords += "|"+akaList.get(i)+" (aka of "+checked.label+")";
				}
			}
			if(!(checked.acronym.equals(""))){
				List<String> acronymList = Arrays.asList(checked.acronym.split(",[ ]*"));
				for(int i=0; i<acronymList.size(); i++){
					keywords += "|"+acronymList.get(i)+" (acronym of "+checked.label+")";
				}
			}

		}
		for(int i=0; i<checked.subClasses.size(); i++){
			keywords = getSubKeywords(keywords, checked.subClasses.get(i));
		}
		return keywords;
	}
	
	ResponseEntity<?> search(InputClass input) throws Exception{
		JSONObject all = new JSONObject();
		if(input.items.isEmpty() && input.mykeywords.replace(",", "").trim().isEmpty()) {
				try {
					all.put("errormessage", "No keywords found. Please select or type some keywords and try again.");
					/*response.setContentType("text/html; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					PrintWriter pw = response.getWriter();
					pw.print(all.toString());
					pw.close();*/
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}
		else {
			akalogic = input.akalogic;
			Resource resource = resourceLoader.getResource("/WEB-INF/infos.properties");
			InputStream inputStream = resource.getInputStream();
			Properties prop = new Properties();
			prop.load(inputStream);
			
			String querykeywords="";
			//URI uri = new URI("http://"+prop.getProperty("domain")+":"+prop.getProperty("port")+"/retrieve");
			List<String> keywordsList = new ArrayList<String>();
			if(!input.items.isEmpty()){
				List<String> checkedList = Arrays.asList(input.items.split(","));
				for(int j=0; j<checkedList.size(); j++){
					for(int i=0; i<OntServlet.getAllClasses().size(); i++){
						if(checkedList.get(j).equals(OntServlet.getAllClasses().get(i).id)){
							try{
								String keywords = getSubKeywords("", OntServlet.getAllClasses().get(i));
								keywordsList.add(keywords);
								if(querykeywords.equals("")) querykeywords = OntServlet.getAllClasses().get(i).label;
								else querykeywords += ", "+OntServlet.getAllClasses().get(i).label;
							}
							catch (Exception e) {
					   			System.out.println(e);
					   		}
							
							break;	
						}
					}	
				}
			}
			if(!input.mykeywords.trim().isEmpty()){
				List<String> mykeywords = Arrays.asList(input.mykeywords.trim().split(","));
				for(int j=0; j<mykeywords.size(); j++){
					keywordsList.add(mykeywords.get(j).trim());
					if(querykeywords.equals("")) querykeywords = mykeywords.get(j).trim();
					else querykeywords += ", "+mykeywords.get(j).trim();
				}
			}
			RestTemplate restTemplate = new RestTemplate();
			//Search req = new Search(keywordsList,"",akalogic);
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
			JSONObject params = new JSONObject();
			params.put("keywords", keywordsList);
		    params.put("place", "");
		    params.put("logic", input.logic);
		    HttpEntity<String> request = new HttpEntity<String>(params.toString(), headers);
			ResponseEntity<String> response = restTemplate.postForEntity("http://"+prop.getProperty("domain")+":"+prop.getProperty("port")+"/TwitterSearchApp/retrieve", request, String.class);
			List<JSONObject> tweets = new ArrayList<JSONObject>();
			//System.out.println(response.getBody());
			//InputClass inp = new Gson().fromJson(request.getReader(), InputClass.class);
			JSONArray jsonarray = new JSONArray(response.getBody());
			for (int k = 0; k < jsonarray.length(); k++) {
	            tweets.add(jsonarray.getJSONObject(k));
		    }
			all.put("tweets", tweets);
			all.put("query", querykeywords);
			all.put("total", tweets.size());
			all.put("logic", input.logic);
			all.put("akalogic", akalogic);
			//System.out.println(all);
		}
		//System.out.println(ResponseEntity.ok(all));
		return ResponseEntity.ok(all.toString());
	}
}
