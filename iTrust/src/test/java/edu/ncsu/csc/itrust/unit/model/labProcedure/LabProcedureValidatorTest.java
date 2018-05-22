package edu.ncsu.csc.itrust.unit.model.labProcedure;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureValidator;

/**
 * Tests the LabProcedureValidator class.
 * 
 * @author mwreesjo
 *
 */
public class LabProcedureValidatorTest {

	/**
	 * Used to validate fields with 500-char limit. 'o' is the 501th character.
	 */
	private static final String stringWith501Chars = "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff" + "o";

	LabProcedureValidator validator;
	LabProcedure proc;

	@Before
	public void setUp() throws Exception {
		validator = new LabProcedureValidator();

		proc = new LabProcedure();
		proc.setCommentary("commentary");
		proc.setIsRestricted(true);
		proc.setLabProcedureID(8L);
		proc.setLabTechnicianID(9L);
		proc.setOfficeVisitID(10L);
		proc.setPriority(3);
		proc.setResults("results");
		proc.setLabProcedureCode("00000-0");
		proc.setConfidenceIntervalLower(50);
		proc.setConfidenceIntervalUpper(60);
		proc.setStatus(1L);
		proc.setUpdatedDate(new Timestamp(100L));
		proc.setHcpMID(9000000001L);
	}

	@Test
	public void testValidLabProcedure() {
		try {
			validator.validate(proc);
		} catch (FormValidationException f) {
			fail("Valid lab procedure wasn't validated. Error: " + f.getMessage());
		}
	}

	@Test
	public void testInvalidCommentary() {
		proc.setCommentary(stringWith501Chars);
		tryValidateWithInvalidField(proc, "commentary");
	}

	@Test
	public void testInvalidConfidenceIntervalLower() {
		proc.setConfidenceIntervalLower(-1);
		tryValidateWithInvalidField(proc, "confidence interval lower");
		proc.setConfidenceIntervalLower(101);
		tryValidateWithInvalidField(proc, "confidence interval lower");
	}

	@Test
	public void testInvalidConfidenceIntervalUpper() {
		proc.setConfidenceIntervalUpper(-1);
		tryValidateWithInvalidField(proc, "confidence interval upper");
		proc.setConfidenceIntervalUpper(101);
		tryValidateWithInvalidField(proc, "confidence interval upper");
	}

	@Test
	public void testInvalidLabProcedureCode() {
		proc.setLabProcedureCode("0");
		tryValidateWithInvalidField(proc, "lab procedure code");
		proc.setLabProcedureCode("00000");
		tryValidateWithInvalidField(proc, "lab procedure code");
		proc.setLabProcedureCode("-0");
		tryValidateWithInvalidField(proc, "lab procedure code");
	}

	@Test
	public void testInvalidLabProcedureID() {
		proc.setLabProcedureID(-1L);
		tryValidateWithInvalidField(proc, "lab procedure ID");
		// Lab procedure can be null but cannot be negative
		proc.setLabProcedureID(null);
		try {
			validator.validate(proc);
		} catch (FormValidationException f) {
			fail("Valid lab procedure wasn't validated. Error: " + f.getMessage());
		}
	}

	@Test
	public void testInvalidOfficeVisitID() {
		proc.setOfficeVisitID(-1L);
		tryValidateWithInvalidField(proc, "office visit ID");
		proc.setOfficeVisitID(null);
		tryValidateWithInvalidField(proc, "office visit ID");
	}

	@Test
	public void testInvalidLabTechnicianID() {
		proc.setLabTechnicianID(-1L);
		tryValidateWithInvalidField(proc, "lab technician ID");
		proc.setLabTechnicianID(null);
		tryValidateWithInvalidField(proc, "lab technician ID");
	}

	@Test
	public void testInvalidPriority() {
		proc.setPriority(0);
		tryValidateWithInvalidField(proc, "priority");
		proc.setPriority(4);
		tryValidateWithInvalidField(proc, "priority");
	}

	@Test
	public void testInvalidResults() {
		proc.setResults(stringWith501Chars);
		tryValidateWithInvalidField(proc, "results");
	}

	@Test
	public void testInvalidStatus() {
		// Create new LP since proc.setStatus(null) isn't possible
		LabProcedure statusNotInitialized = new LabProcedure();

		statusNotInitialized.setCommentary("f");
		statusNotInitialized.setConfidenceIntervalLower(30);
		statusNotInitialized.setConfidenceIntervalUpper(40);
		statusNotInitialized.setIsRestricted(true);
		statusNotInitialized.setLabProcedureCode("12345-6");
		statusNotInitialized.setLabProcedureID(4L);
		statusNotInitialized.setLabTechnicianID(5L);
		statusNotInitialized.setOfficeVisitID(1L);
		statusNotInitialized.setPriority(2);
		statusNotInitialized.setResults("results yeah");
		statusNotInitialized.setUpdatedDate(new Timestamp(new Date().getTime()));

		try {
			validator.validate(statusNotInitialized);
			fail("Validator should catch invalid status");
		} catch (FormValidationException e) {
			// Exception should be thrown
		}
	}

	@Test
	public void testInvalidUpdatedDate() {
		proc.setUpdatedDate(null);
		tryValidateWithInvalidField(proc, "updated date");
	}
	
	@Test
	public void testInvalidHcpMID() {
		proc.setHcpMID(null);
		tryValidateWithInvalidField(proc, "HCP MID");
		proc.setHcpMID(1L);
		tryValidateWithInvalidField(proc, "HCP MID");
		proc.setHcpMID(-1L);
		tryValidateWithInvalidField(proc, "HCP MID");
		proc.setHcpMID(12345678901L);
		tryValidateWithInvalidField(proc, "HCP MID");
		proc.setHcpMID(92345678901L);
		tryValidateWithInvalidField(proc, "HCP MID");
	}

	/**
	 * Invokes the validator.validate() method on the given lab procedure,
	 * Assert.fail()ing if the validator does not throw an exception.
	 * 
	 * @param proc
	 *            The procedure with one invalid field
	 * @param nameOfInvalidField
	 *            Name of the field which is invalid
	 */
	private void tryValidateWithInvalidField(LabProcedure proc, String nameOfInvalidField) {
		try {
			validator.validate(proc);
			fail("Validator should catch invalid field " + nameOfInvalidField);
		} catch (FormValidationException e) {
			// Exception should be thrown
			assertThat(e.getMessage().toLowerCase(), containsString(nameOfInvalidField.toLowerCase()));
		}
	}
}
