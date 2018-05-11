package edu.ncsu.csc.itrust.bean;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.RequiredProceduresBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.RequiredProceduresDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

@SuppressWarnings("unused")
public class RequiredProceduresBeanTest extends TestCase {
	
	@Override
	public void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
	}
	
	public void testGetSetCptCode() throws Exception {
		RequiredProceduresBean bean = new RequiredProceduresBean();
		
		String testString = "Test";
		//Test that the cptCode is properly set and gotten
		bean.setCptCode(testString);
		assertEquals(testString, bean.getCptCode());
	}
	
	public void testGetDescription() throws Exception {
		RequiredProceduresBean bean = new RequiredProceduresBean();
		
		String testString = "Test";
		//Test that the description is properly set and gotten
		bean.setDescription(testString);
		assertEquals(testString, bean.getDescription());
	}
	
	public void testGetSetAgeGroup() throws Exception {
		RequiredProceduresBean bean = new RequiredProceduresBean();
		
		int testInt = 1;
		//Test that the age group is properly set and gotten
		bean.setAgeGroup(testInt);
		assertEquals(testInt, bean.getAgeGroup());
	}
	
	public void testGetSetAttribute() throws Exception {
		RequiredProceduresBean bean = new RequiredProceduresBean();
		
		String testString = "Test";
		//Test that the attribute is properly set and gotten
		bean.setAttribute(testString);
		assertEquals(testString, bean.getAttribute());
	}
	
	public void testGetSetAgeMax() throws Exception {
		RequiredProceduresBean bean = new RequiredProceduresBean();
		
		int testInt = 1;
		//Test that the age group is properly set and gotten
		bean.setAgeMax(testInt);
		assertEquals(testInt, bean.getAgeMax());	
	}
}
