package com.rockhopper.blog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.rockhopper.blog.model.CityStates;

public class TopCities {

	// data is from: http://federalgovernmentzipcodes.us
	// converted to to tab delimited, to make the data easier to parse
	
	private static Map<String, CityStates> cityStates = 
			new ConcurrentHashMap<String, CityStates>();

	public static void main(String[] args) throws IOException {

		Class<?> currentClass = new Object() { }.getClass().getEnclosingClass();

		ClassLoader thisClassLoader = currentClass.getClassLoader();

		File file = new File(
				thisClassLoader.getResource("resources/free-zipcode-database.txt").getFile());
		String filePath = file.getAbsolutePath();
		
		useStreams(filePath);
		printMostUsedCityNames(10);
		
		useScanner(filePath);
		printMostUsedCityNames(10, true);
		
	}

	private static void useStreams(String filePath) throws IOException {
		
		long startTime = new Date().getTime();
		
		Files.lines(Paths.get(filePath))
			.map(line -> line.split("\\t"))
			.forEach(fields -> addStateToCity(fields[3], fields[4]));
		
		System.out.println("Streams took: " + (new Date().getTime() - startTime) + " ms.");
	}

	private static void useScanner(String filePath) {
		
		long startTime = new Date().getTime();
		
		try (Scanner scanner = new Scanner(Paths.get(filePath))) {
			while (scanner.hasNext()){
				String[] lineParsed = scanner.nextLine().split("\\t");
				addStateToCity(lineParsed[3], lineParsed[4]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Scanner took: " + (new Date().getTime() - startTime) + " ms.");

	}
	
	private static Function<?, ?> addStateToCity(String city, String state) {

		// make sure that the combination of city/state is unique
		// as there may be multiple zipcode records for a city
		
		CityStates cityInStates = cityStates.get(city);

		if (cityInStates == null) {
			cityStates.put(city,  new CityStates(city, state));
		} else {
			if (!cityInStates.statesContainsState(state)) {
				cityInStates.addState(state);
				cityStates.put(city, cityInStates);
			}
		}
		return null;
	}
	
	private static void printMostUsedCityNames(int limit) {
		printMostUsedCityNames(limit, false);
	}
	
	private static void printMostUsedCityNames(int limit, boolean printStates) {

		cityStates.entrySet()
        	.stream()
        	.sorted((e2, e1) -> e1.getValue().getStateCount().compareTo(e2.getValue().getStateCount()))
        	.limit(limit)
        	.forEach(e -> System.out.println(
        			printStates ? e.getValue().printCityCountWithStates() : e.getValue().printCityCount()));
	}
}

