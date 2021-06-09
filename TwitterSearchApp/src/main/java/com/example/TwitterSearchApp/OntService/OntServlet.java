package com.example.TwitterSearchApp.OntService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;



/**
 * Servlet implementation class OntServlet
 */
@WebServlet("/OntServlet")
public class OntServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static OWLOntology ontology;
	private static String akalogic;
	private static OWLOntologyManager manager;
	private static IRI documentIRI;
	private static List<aClass> allClasses;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OntServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OWLOntology getOntology() {
    	return ontology;
    }
    
    public static List<aClass> getAllClasses(){
    	return allClasses;
    }
    
    @SuppressWarnings("deprecation")
	private static void findClasses(){
		Set<OWLClass> ontClasses = new HashSet<OWLClass>(); 
        ontClasses = ontology.getClassesInSignature();
        allClasses = new ArrayList<aClass>();
        
        for (Iterator<OWLClass> it = ontClasses.iterator(); it.hasNext(); ) {
        	aClass f = new aClass();
        	f.name = it.next();
        	f.id = f.name.getIRI().getFragment();
        	for(OWLAnnotationAssertionAxiom a : ontology.getAnnotationAssertionAxioms(f.name.getIRI())) {
        		if(a.getProperty().isLabel()) {
                    if(a.getValue() instanceof OWLLiteral) {
                        OWLLiteral val = (OWLLiteral) a.getValue();
                        f.label = val.getLiteral();
                    }
                }
        		else if(a.getProperty().isComment()) {
                    if(a.getValue() instanceof OWLLiteral) {
                        OWLLiteral val = (OWLLiteral) a.getValue();
                        f.comment = val.getLiteral();
                    }
                }
        		else{
        			if(a.getProperty().getIRI().getFragment().equals("aka")){
        				if(a.getValue() instanceof OWLLiteral) {
                            OWLLiteral val = (OWLLiteral) a.getValue();
                            f.aka = val.getLiteral();
                        }
        			}
        			else if(a.getProperty().getIRI().getFragment().equals("acronym")){
        				if(a.getValue() instanceof OWLLiteral) {
                            OWLLiteral val = (OWLLiteral) a.getValue();
                            f.acronym = val.getLiteral();
                        }
        			}
        		}
            }
        	allClasses.add(f);
        }
	}
	
	private static void findSubclasses(){
		for (final org.semanticweb.owlapi.model.OWLSubClassOfAxiom subClasse : ontology.getAxioms(AxiomType.SUBCLASS_OF))
        {
        	OWLClass sup = (OWLClass) subClasse.getSuperClass();
        	OWLClass sub = (OWLClass) subClasse.getSubClass();
        	
            if (sup instanceof OWLClass && sub instanceof OWLClass)
            {
            	int i;
            	for(i=0; i<allClasses.size(); i++){
            		if (sup.equals(allClasses.get(i).name)) break;
            	}
            	int j;
            	for(j=0; j<allClasses.size(); j++){
            		if (sub.equals(allClasses.get(j).name)){
            			allClasses.get(i).subClasses.add(allClasses.get(j));
            			allClasses.get(j).isSubClass = true;
            			break;
            		}
            	}
            	
            }
        }
	}
	
	private static void findSubJson(List<JSONObject> subs, aClass ac){
		for(int i=0; i<ac.subClasses.size(); i++){
			JSONObject ac1 = new JSONObject();
			List<JSONObject> subs1 = new ArrayList<JSONObject>();
			findSubJson(subs1, ac.subClasses.get(i));
			try {
				String content="";
				ac1.put("id", ac.subClasses.get(i).id);
				ac1.put("text", ac.subClasses.get(i).label);
				JSONObject cont = new JSONObject();
				content+=ac.subClasses.get(i).label;
				if(!(ac.subClasses.get(i).comment.equals(""))) content+="<br/>"+ac.subClasses.get(i).comment;
				if(!(ac.subClasses.get(i).aka.equals(""))) content+="<br/>aka: "+ac.subClasses.get(i).aka;
				if(!(ac.subClasses.get(i).acronym.equals(""))) content+="<br/>acronym: "+ac.subClasses.get(i).acronym;				
				cont.put("content", content);
				ac1.put("li_attr",cont);
				ac1.put("children", subs1);
				
				subs.add(ac1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
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
	
	private static List<JSONObject> fetchOntology(List<JSONObject> ontology)
	{
		for(int i=0; i<allClasses.size(); i++){
			if(!allClasses.get(i).isSubClass){
				try {
					JSONObject ac = new JSONObject();
					JSONObject cont = new JSONObject();
					String content="";
					List<JSONObject> subs = new ArrayList<JSONObject>();
					findSubJson(subs, allClasses.get(i));
					ac.put("id", allClasses.get(i).id);
					ac.put("text", allClasses.get(i).label);
					content+=allClasses.get(i).label;
					if(!(allClasses.get(i).comment.equals(""))) content+="<br/>"+allClasses.get(i).comment;
					if(!(allClasses.get(i).aka.equals(""))) content+="<br/>aka: "+allClasses.get(i).aka;
					if(!(allClasses.get(i).acronym.equals(""))) content+="<br/>acronym: "+allClasses.get(i).acronym;
					cont.put("content", content);					
					ac.put("li_attr",cont);
					ac.put("children", subs);
					
					ontology.add(ac);					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return ontology;
	}

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		
		manager = OWLManager.createOWLOntologyManager();
	    //documentIRI = IRI.create("file:///C:/", "social_media_search_ontology_v1_fin.owl");
		InputStream input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/infos.properties"));
    	Properties prop = new Properties();
    	// load a properties file
        prop.load(input);
		documentIRI = IRI.create(getServletContext().getResource("/WEB-INF/"+prop.getProperty("owlFile").trim()));
		//documentIRI = IRI.create(getServletContext().getResource("/WEB-INF/social_media_search_ontology_v1_fin.owl"));
		try{
	        ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
            findClasses();
            findSubclasses();
		}
		catch (OWLOntologyCreationException e) {
	        e.printStackTrace();
			
		}
		JSONObject all = new JSONObject();
		List<JSONObject> ontology = new ArrayList<JSONObject>();
		ontology = fetchOntology(ontology);
		try {
			all.put("ontology", ontology);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(all.toString());
		pw.close();
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}