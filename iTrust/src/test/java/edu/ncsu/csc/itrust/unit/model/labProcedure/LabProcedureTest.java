package edu.ncsu.csc.itrust.unit.model.labProcedure;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;

public class LabProcedureTest {
	
	LabProcedure proc;

	@Before
	public void setUp() throws Exception {
		proc = new LabProcedure();
	}


	@Test
	public void testGetLabProcedureID() {
		proc.setLabProcedureID(4L);
		Assert.assertTrue(proc.getLabProcedureID() == 4L);
	}

	@Test
	public void testSetLabProcedureID() {
		proc.setLabProcedureID(4L);
		Assert.assertTrue(proc.getLabProcedureID() == 4L);
	}

	@Test
	public void testGetLabTechnicianID() {
		proc.setLabTechnicianID(4L);
		Assert.assertTrue(proc.getLabTechnicianID() == 4L);
	}

	@Test
	public void testSetLabTechnicianID() {
		proc.setLabTechnicianID(4L);
		Assert.assertTrue(proc.getLabTechnicianID() == 4L);
	}

	@Test
	public void testGetOfficeVisitID() {
		proc.setOfficeVisitID(3L);
		Assert.assertTrue(proc.getOfficeVisitID() == 3L);
	}

	@Test
	public void testSetOfficeVisitID() {
		proc.setOfficeVisitID(3L);
		Assert.assertTrue(proc.getOfficeVisitID() == 3L);
	}

	@Test
	public void testGetLabProcedureCode() {
		proc.setLabProcedureCode("23232-4");
		Assert.assertEquals("23232-4", proc.getLabProcedureCode());
	}

	@Test
	public void testSetLabProcedureCode() {
		proc.setLabProcedureCode("23232-4");
		Assert.assertEquals("23232-4", proc.getLabProcedureCode());
	}

	@Test
	public void testGetPriority() {
		proc.setPriority(3);
		Assert.assertTrue(proc.getPriority() == 3);
	}

	@Test
	public void testSetPriority() {
		proc.setPriority(3);
		Assert.assertTrue(proc.getPriority() == 3);
	}

	@Test
	public void testIsRestricted() {
		proc.setIsRestricted(true);
		Assert.assertTrue(proc.isRestricted());
	}

	@Test
	public void testSetIsRestricted() {
		proc.setIsRestricted(true);
		Assert.assertTrue(proc.isRestricted());
	}

	@Test
	public void testGetStatus() {
		proc.setStatus(3);
		Assert.assertTrue(proc.getStatus().getID() == 3);
	}

	@Test
	public void testSetStatusLong() {
		proc.setStatus(3);
		Assert.assertTrue(proc.getStatus().getID() == 3);
	}

	@Test
	public void testSetStatusString() {
		proc.setStatus("Completed");
		Assert.assertEquals("Completed", proc.getStatus().getName());
	}

	@Test
	public void testGetCommentary() {
		proc.setCommentary("foobar");
		Assert.assertEquals("foobar", proc.getCommentary());
	}

	@Test
	public void testSetCommentary() {
		proc.setCommentary("foobar");
		Assert.assertEquals("foobar", proc.getCommentary());
	}

	@Test
	public void testGetResults() {
		proc.setResults("foobar");
		Assert.assertEquals("foobar", proc.getResults());
	}

	@Test
	public void testSetResults() {
		proc.setResults("foobar");
		Assert.assertEquals("foobar", proc.getResults());
	}

	@Test
	public void testGetUpdatedDate() {
		Timestamp t = new Timestamp(new Date().getTime());
		proc.setUpdatedDate(t);
		Assert.assertEquals(t, proc.getUpdatedDate());
	}

	@Test
	public void testSetUpdatedDate() {
		Timestamp t = new Timestamp(new Date().getTime());
		proc.setUpdatedDate(t);
		Assert.assertEquals(t, proc.getUpdatedDate());
	}

	@Test
	public void testGetConfidenceIntervalLower() {
		proc.setConfidenceIntervalLower(30);
		Assert.assertTrue(proc.getConfidenceIntervalLower() == 30);
	}

	@Test
	public void testSetConfidenceIntervalLower() {
		proc.setConfidenceIntervalLower(30);
		Assert.assertTrue(proc.getConfidenceIntervalLower() == 30);
	}

	@Test
	public void testGetConfidenceIntervalUpper() {
		proc.setConfidenceIntervalUpper(30);
		Assert.assertTrue(proc.getConfidenceIntervalUpper() == 30);
	}

	@Test
	public void testSetConfidenceIntervalUpper() {
		proc.setConfidenceIntervalUpper(30);
		Assert.assertTrue(proc.getConfidenceIntervalUpper() == 30);
	}

}
