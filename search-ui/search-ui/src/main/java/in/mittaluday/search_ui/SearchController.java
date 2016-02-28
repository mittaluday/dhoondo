package in.mittaluday.search_ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import in.mittaluday.query_engine.QueryProcessor;
import in.mittaluday.search_model.Query;

@Controller
public class SearchController {
	
	QueryProcessor qp;
	
	//Load index once during the initialization of the controller
	@PostConstruct
	public void init() throws ClassNotFoundException, IOException{
		qp = new QueryProcessor();
	}

	@RequestMapping("/test")
	public String greeting(
			@RequestParam(value="name", required=false, defaultValue="World")
			String name, Model model){
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@RequestMapping(value="/dhoondo", method=RequestMethod.GET)
	public String homePage(Model model){
		model.addAttribute("query", new Query());
		return "dhoondo-home";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String search(@ModelAttribute(value="query") Query query, Model model) throws ClassNotFoundException, IOException{
		
		//TODO: Perform search and populate results to add into the model.
		//TODO: Handle exception to re-route to error page
		
    	String queryString = query.getQueryString();
    	
    	//Validate query string
    	if(queryString.isEmpty()){
    		return "dhoondo-home";
    	}

    	List<String> stringResults = new ArrayList<String>();

    	ArrayList<String> results = qp.queryIndex(queryString.toLowerCase());
    	if(!results.isEmpty()){
    		for(String s : results){
    			stringResults.add(s);
    		}
    	}
    	
		model.addAttribute("query", query);
		model.addAttribute("results", stringResults);
		
		System.out.println("Query searched for:" + query.getQueryString());
		return "search-results";
	}
}
