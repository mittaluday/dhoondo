package in.mittaluday.search_ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import in.mittaluday.query_engine.QueryProcessorMongo;
import in.mittaluday.query_engine.Result;
import in.mittaluday.search_model.Query;

@Controller
public class SearchController {
	
	public class SubStringHashSet extends HashSet<String> {
		public SubStringHashSet(List<String> stringList) {
			super(stringList);
		}

		public boolean containsSubString(String queryString){
			for(String string: this){
				if(queryString.contains(string)){
					return true;
				}
			}
			return false;
		}
	}
	
	QueryProcessorMongo qp;
	
	@PostConstruct
	public void init() throws ClassNotFoundException, IOException{
		qp = new QueryProcessorMongo();
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
    	String queryString = query.getQueryString();
    	Set<String> queryTermSet = new SubStringHashSet(Arrays.asList(queryString.toLowerCase().split(" ")));
    	
    	//Validate query string
    	if(queryString.isEmpty()){
    		return "dhoondo-home";
    	}

    	//TODO: Uncomment the following line to interface with query engine
    	/*List<Result> modelResultList = qp.queryIndex(queryString.toLowerCase());
    	
    	//Process description to make search terms strong
    	for (Result result : modelResultList) {
			String description = result.getDescription();
			String[] words = description.split(" ");
			StringBuilder descriptionBuilder = new StringBuilder();
			for (String word : words) {
				if(((SubStringHashSet) queryTermSet).containsSubString(word.toLowerCase())){
					descriptionBuilder.append("<strong>"+word+"</strong> ");
				}
				else {
					descriptionBuilder.append(word + " ");
				}
			}
			result.setDescription(descriptionBuilder.toString());
					
		}*/
    	
    	//TODO:Comment out the following when the results are fetched from query engine
    	List<Result> modelResultList = new ArrayList<Result>();
    	if(modelResultList.size() == 0){
    		return "no-results";
    	}
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
    		
    		String description = modelResult.getDescription();
			String[] words = description.split(" ");
			StringBuilder descriptionBuilder = new StringBuilder();
			for (String word : words) {
				if(((SubStringHashSet) queryTermSet).containsSubString(word.toLowerCase())){
					descriptionBuilder.append("<strong>"+word+"</strong> ");
				}
				else {
					descriptionBuilder.append(word + " ");
				}
			}
			modelResult.setDescription(descriptionBuilder.toString());
			
    		modelResultList.add(modelResult);
    	}
    	//TODO:Comment out till here
    	
    	
		model.addAttribute("query", query);
		model.addAttribute("modelresult", modelResultList);
		
		System.out.println("Query searched for:" + query.getQueryString());
		return "search-results";
	}
}
