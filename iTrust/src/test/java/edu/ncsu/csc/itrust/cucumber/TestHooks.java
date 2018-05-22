package edu.ncsu.csc.itrust.cucumber;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import cucumber.api.java.Before;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class TestHooks {
	@Before
	public static void testPrep(){
		TestDataGenerator gen = new TestDataGenerator();
		try {
			gen.clearAllTables();
			gen.standardData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
