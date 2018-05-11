package edu.ncsu.csc.itrust.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.action.ReviewsAction;
import edu.ncsu.csc.itrust.action.ZipCodeAction;
import edu.ncsu.csc.itrust.beans.DistanceComparator;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.RatingComparator;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class DistanceComparatorTest extends TestCase{

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	private ZipCodeAction action;
	
	@Override
	protected void setUp() throws FileNotFoundException, IOException, SQLException
	{
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		action = new ZipCodeAction(factory, 2L);
	}
	
	protected void tearDown()
	{
		
	}
	
	public void testComparator()
	{
		List<PersonnelBean> beans = new ArrayList<PersonnelBean>();
		PersonnelDAO dao = new PersonnelDAO(factory);
		List<PersonnelBean> list = new ArrayList<PersonnelBean>();
		try {
			list = dao.getAllPersonnel();
		} catch (DBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			if(list.size() != 0)
				Collections.sort(list, new DistanceComparator(action, "27587"));
			
		}
		catch(Exception e)
		{
			fail();
		}
		
	}
	
}
