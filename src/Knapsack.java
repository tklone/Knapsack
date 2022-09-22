import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;

public class Knapsack {

	public static int WeightCapacity;
	public static int n;
	public static ArrayList<Integer> Weights = new ArrayList<Integer>();
	public static ArrayList<Integer> Values = new ArrayList<Integer>();
	public static int highestValue = 0;
	public static ArrayList<Integer> winningValues = new ArrayList<Integer>();

	public static ArrayList<Integer> arrayOfIndexes() {
		ArrayList<Integer> arrayOfIndexes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			arrayOfIndexes.add(i);
		}
		return arrayOfIndexes;
	}

	// CREATES POWERSET
	public static ArrayList<ArrayList<Integer>> recursivePowerSet(ArrayList<Integer> array) {
		if (array.isEmpty()) {
			ArrayList<ArrayList<Integer>> returnEmptySet = new ArrayList<>();
			returnEmptySet.add(array);
			return returnEmptySet;
		}

		ArrayList<ArrayList<Integer>> powerSet = new ArrayList<>();

		Integer element = array.iterator().next();
		ArrayList<Integer> subsetWithoutElement = getSubSetWithoutElement(array, element);

		ArrayList<ArrayList<Integer>> powerSetSubSetWithoutElement = recursivePowerSet(subsetWithoutElement);
		ArrayList<ArrayList<Integer>> powerSetSubsetWithElement = addElementToAll(powerSetSubSetWithoutElement,
				element);

		powerSet.addAll(powerSetSubSetWithoutElement);
		powerSet.addAll(powerSetSubsetWithElement);

		return powerSet;
	}

	public static ArrayList<ArrayList<Integer>> SetsUnderCap(ArrayList<ArrayList<Integer>> powerSet) {
		ArrayList<ArrayList<Integer>> possibleSets = new ArrayList<>();
		for (int i = 0; i < powerSet.size(); i++) {
			Integer totalWeight = 0;
			ArrayList<Integer> currentArray = new ArrayList<>(powerSet.get(i));
			for (int j = 0; j < currentArray.size(); j++) {
				totalWeight = totalWeight + Weights.get(currentArray.get(j));
			}
			if (totalWeight <= WeightCapacity && !possibleSets.contains(currentArray)) {
				possibleSets.add(currentArray);
			}
		}
		return possibleSets;
	}

	public static ArrayList<Integer> HighestValue(ArrayList<ArrayList<Integer>> possibleSets) {
		for (int i = 0; i < possibleSets.size(); i++) {
			ArrayList<Integer> currentArray = new ArrayList<>(possibleSets.get(i));
			int currentValue = 0;
			for (int j = 0; j < currentArray.size(); j++) {
				currentValue = currentValue + Values.get(currentArray.get(j));
			}
			if (currentValue > highestValue) {
				highestValue = currentValue;
				winningValues.clear();
				winningValues.addAll(currentArray);
			}
		}

		for (int i = 0; i < winningValues.size(); i++) {
			int currentNum = winningValues.get(i);
			currentNum = currentNum + 1;
			winningValues.remove(i);
			winningValues.add(currentNum);
		}
		Set<Integer> orderArray = new HashSet<>(winningValues);
		winningValues.clear();
		winningValues.addAll(orderArray);

		System.out.println(highestValue);
		System.out.println(winningValues);
		return winningValues;
	}

	private static ArrayList<ArrayList<Integer>> addElementToAll(
			ArrayList<ArrayList<Integer>> powerSetSubSetWithoutElement, Integer element) {
		ArrayList<ArrayList<Integer>> powerSetSubSetWithElement = new ArrayList<>();
		for (ArrayList<Integer> subsetWithoutElement : powerSetSubSetWithoutElement) {
			ArrayList<Integer> subsetWithElement = new ArrayList<>(subsetWithoutElement);
			subsetWithElement.add(element);
			powerSetSubSetWithElement.add(subsetWithElement);
		}
		return powerSetSubSetWithElement;
	}

	private static ArrayList<Integer> getSubSetWithoutElement(ArrayList<Integer> set, Integer element) {
		ArrayList<Integer> subset = new ArrayList<>();
		subset.addAll(set);
		subset.remove(element);
		return subset;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// METHODS FOR GREEDY
	// Creates an array of ratios of value to weight
	public static ArrayList<Double> Ratios(ArrayList<Integer> weights, ArrayList<Integer> values) {
		ArrayList<Double> ratios = new ArrayList<Double>();
		for (int i = 0; i < weights.size(); i++) {
			ratios.add((double) values.get(i) / (double) weights.get(i));
		}
		return ratios;
	}

	//I DIDN'T ACCOUNT FOR TIES!!!  MAYBE HE WILL LET ME GO BACK IN LATER THIS WEEK AND TAKE THE GREEDY QUIZ AGAIN?
	// Sort ratio array from greatest to least
	public static ArrayList<Integer> RatiosSortedIndex(ArrayList<Double> ratiosUnsorted) {

		ArrayList<Integer> indexValsSorted = new ArrayList<>();
		ArrayList<Double> duplicateSortedList = new ArrayList<>(ratiosUnsorted);
		ratiosUnsorted.sort(Comparator.reverseOrder());
		for (int i = 0; i < n; i++) {
			indexValsSorted.add(duplicateSortedList.indexOf(ratiosUnsorted.get(i)));
		}
		return indexValsSorted;

	}

	public static ArrayList<Integer> PackBag(ArrayList<Integer> sortedIndexVals) {
		int totalWeight = 0;
		ArrayList<Integer> thingsInBag = new ArrayList<>();
		label: for (int i = 0; i < n; i++) {
			totalWeight = totalWeight + Weights.get(sortedIndexVals.get(i));
			if (totalWeight <= WeightCapacity) {
				thingsInBag.add(sortedIndexVals.get(i));
			} else if (totalWeight > WeightCapacity) {
				break label;
			}
		}
		return thingsInBag;
	}

	public static void GreedyAnswers(ArrayList<Integer> thingsInBag) {
		int totalVal = 0;
		for (int i = 0; i < thingsInBag.size(); i++) {
			totalVal = totalVal + Values.get(thingsInBag.get(i));
		}
		System.out.println(totalVal);
		System.out.println(thingsInBag);
	}

	public static void main(String[] args) {

		// Opens file and writes data
		try {
			File dataFile = new File("KnapsackGreedyQuiz1.txt");
			Scanner dataReader = new Scanner(dataFile);

			WeightCapacity = dataReader.nextInt();
			n = dataReader.nextInt();


			for (int i = 0; i < n; i++) {
				Weights.add(dataReader.nextInt());
				Values.add(dataReader.nextInt());
			}


			dataReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error opening file.");
			e.printStackTrace();
		}

		long startTime = System.nanoTime();
		
//		 ACTUAL ALGORITHM IMPLEMENTATION
//		 EXHAUSTIVE
//		ArrayList<Integer> ArrayIndexes = arrayOfIndexes();
//		ArrayList<ArrayList<Integer>> PowerSet = recursivePowerSet(ArrayIndexes);
//		ArrayList<ArrayList<Integer>> setsUnderCap = SetsUnderCap(PowerSet);
//		HighestValue(setsUnderCap);

		// GREEDY HEURISTIC
		ArrayList<Double> unsortedRatios = Ratios(Weights, Values);
		ArrayList<Integer> sortedIndexVals = RatiosSortedIndex(unsortedRatios);
		ArrayList<Integer> objectsInBag = PackBag(sortedIndexVals);
		GreedyAnswers(objectsInBag);
		
		long endTime = System.nanoTime();
		System.out.println("Time: " + (endTime - startTime));
	}
}
