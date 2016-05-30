package com.rockhopper.blog.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityStates {
	
	private String city;
	private List<String> states = new ArrayList<String>();

	public CityStates(String city, String state) {
		super();
		this.city = city;
		this.states.add(state);
	}
	
	public void addState(String state) {
		this.states.add(state);
		return;
	}
	
	public boolean statesContainsState(String state) {
		return this.states.contains(state);
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.city).append(": ");
		for (String state : states) {
			sb.append(state).append(", ");			
		}
		return sb.toString().substring(0, sb.toString().length()-2);
	}
	
	public String getStatesCommaSeparated() {

		Collections.sort(states);
		
		StringBuffer sb = new StringBuffer();
		for (String state : states) {
			sb.append(state).append(", ");			
		}
		return sb.toString().substring(0, sb.toString().length()-2);
	}
	
	public Integer getStateCount() {
		return this.states.size();
	}
	
	public String printCityCount() {
		return city + " is in " + states.size() + " states/territories";
	}
	
	public String printCityCountWithStates() {
		return city + " is in " + states.size() + " states/territories : " + getStatesCommaSeparated();
	}
}
