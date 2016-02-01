package in.mittaluday.data_processor;

import java.util.HashMap;
import java.util.List;

public class ThreeGramSingleton {

	HashMap<String,Integer> threeGramFrequency;
	
	private static ThreeGramSingleton threegramsingleton = new ThreeGramSingleton( );
	
	private ThreeGramSingleton()
	{
		HashMap<String,Integer> threeGramFrequency=new HashMap();
	}
	public static ThreeGramSingleton getInstance()
	{
		return threegramsingleton;
	}
	public void addThreeGramList(List<String> threeGramList)
	{
		for (int i = 0; i < threeGramList.size(); i++) {
			if (threeGramFrequency.get(threeGramList.get(i)) != null) {
				if (threeGramFrequency.containsKey(threeGramList.get(i))) {
					threeGramFrequency.put(threeGramList.get(i), threeGramFrequency.get(threeGramList.get(i))+1);
				}
			} else {
				threeGramFrequency.put(threeGramList.get(i),1);
			}
		}
	}
}

	
