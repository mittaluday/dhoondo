package in.mittaluday.search_ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import in.mittaluday.search_model.Query;

@Controller
public class SearchController {

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
	public String search(@ModelAttribute(value="query") Query query, Model model){
		
		//TODO: Perform search and populate results to add into the model. 
		
		List<String> stringResults = new ArrayList<String>();
		stringResults.add("Result 1: HC result");
		stringResults.add("Results Helloworld");
		stringResults.add("Whatever");
		stringResults.add("Yet another result");
		stringResults.add("That's it for testing");
		
		model.addAttribute("query", query);
		model.addAttribute("results", stringResults);
		
		System.out.println("Query searched for:" + query.getQueryString());
		return "search-results";
	}
}
