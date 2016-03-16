package in.mittaluday.NDCGCalculator;

public class NDCG {
	
	private static double[] googleRelevance = new double[]{5,4,3,2,1};
	private static double[] googleDG = new double[]{5.0,4.0,3.0, 1.26, 0.5};
	private static double[] googleDCG = new double[]{5, 9, 12, 13.26, 13.76};
	
	public static void main(String args[]){
//		calculateGoogleDG();
//		calculateGoogleDCG();
//		show(googleDG, "googleDG");
//		show(googleDCG, "googleDCG");
		double [] mondegoold = new double[]{0,5,0,2,0};
		double [] mlold =      new double[]{0,0,0,0,0};
		double [] seold =      new double[]{0,0,0,0,0};
		double [] secold =     new double[]{0,0,1,0,0};
		double [] saold =      new double[]{4,0,0,0,0};
		double [] gradold =    new double[]{0,0,0,0,0};
		double [] lopesold =   new double[]{3,0,0,0,0};
		double [] restold =    new double[]{5,0,0,0,0};
		double [] gamesold =   new double[]{0,0,0,0,0};
		double [] irold =      new double[]{0,0,0,0,0};
		double [] mondego =    new double[]{5,0,0,0,2};
		double [] ml =         new double[]{0,0,0,0,5};
		double [] se =         new double[]{0,0,0,0,0};
		double [] sec =        new double[]{0,0,0,5,0};
		double [] sa =         new double[]{0,0,0,0,4};
		double [] grad =       new double[]{0,0,0,5,0};
		double [] lopes =      new double[]{5,0,3,0,0};
		double [] rest =       new double[]{5,0,0,0,0};
		double [] games =      new double[]{5,0,0,0,0};
		double [] ir =         new double[]{0,0,5,0,4};
		double [] actualDG = new double[5];
		double [] actualDCG = new double[5];
		
		calculateActualDG(ir, actualDG);
		show(actualDG, "actualDG");
		calculateActualDCG(actualDG, actualDCG);
		show(actualDCG, "actualDCG");
		
		double NDCG5 = actualDCG[4]/googleDCG[4];
		
		System.out.println(NDCG5);
	}

	private static void calculateActualDG(double[] actualRelevance, double[] actualDG) {
		actualDG[0] = actualRelevance[0];
		actualDG[1] = actualRelevance[1];
		for(int i=2;i<5;i++){
			actualDG[i] = actualRelevance[i]/(Math.log(i)/Math.log(2));
		}
	}

	private static void calculateActualDCG(double[] actualDG, double[] actualDCG) {
		actualDCG[0] = actualDG[0];
		for(int i=1;i<5;i++){
			actualDCG[i] = actualDG[i] + actualDCG[i-1];
		}
	}

	private static void calculateGoogleDG() {
		googleDG[0] = googleRelevance[0];
		googleDG[1] = googleRelevance[1];
		for(int i=2;i<5;i++){
			googleDG[i] = googleRelevance[i]/(Math.log(i)/Math.log(2));
		}
	}

	private static void calculateGoogleDCG() {
		googleDCG[0] = googleDG[0];
		for(int i=1;i<5;i++){
			googleDCG[i] = googleDG[i] + googleDCG[i-1];
		}		
	}
	
	private static void show(double[] array, String name) {
		StringBuffer temp = new StringBuffer(name + " ");
		for(int i=0;i<5;i++){
			temp.append(array[i] + " ");
		}
		System.out.println(temp.toString());
	}

}
