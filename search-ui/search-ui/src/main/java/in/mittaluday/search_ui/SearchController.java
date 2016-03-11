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
import in.mittaluday.search_model.Result;

@Controller
public class SearchController {
	
	QueryProcessor qp;
	
	//Load index once during the initialization of the controller
	@PostConstruct
	public void init() throws ClassNotFoundException, IOException{
		//TODO: Replace query processor by mongo query processor
		//qp = new QueryProcessor();
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

    	//TODO: Replace this by mongo query api
    	
    	/*ArrayList<String> results = qp.queryIndex(queryString.toLowerCase());
    	if(!results.isEmpty()){
    		for(String s : results){
    			stringResults.add(s);
    		}
    	}*/
    	
    	//Model results
    	for(int i = 0; i< 5; i++){
    		stringResults.add("Result: " + String.valueOf(i));
    	}
    	
    	
    	List<Result> modelResultList = new ArrayList<Result>();
    	for(int i =0; i< 5; i++){
    		Result modelResult = new Result();
    		modelResult.setTitle("Title: " +  String.valueOf(i));
    		modelResult.setDescription("Lorem ipsum dolor sit amet, "
    				+ "consectetur adipiscing elit, sed do eiusmod "
    				+ "tempor incididunt ut labore et dolore magna "
    				+ "aliqua. Ut enim ad minim veniam, quis nostrud "
    				+ "exercitation ullamco laboris nisi ut aliquip "
    				+ "ex ea commodo consequat. Duis aute irure dolor ");
    		modelResult.setUrl("http://www.url"+String.valueOf(i)+".com");
    		modelResultList.add(modelResult);
    	}

		model.addAttribute("query", query);
		model.addAttribute("results", stringResults);
		model.addAttribute("modelresult", modelResultList);
		
		System.out.println("Query searched for:" + query.getQueryString());
		return "search-results";
	}
}
