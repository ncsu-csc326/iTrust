package edu.ncsu.csc.itrust.unit.report;

import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.report.MedicalReportFilter;
import edu.ncsu.csc.itrust.report.MedicalReportFilter.MedicalReportFilterType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class MedicalReportFilterTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private PatientDAO pDAO = factory.getPatientDAO();
	private List<PatientBean> allPatients;
	private MedicalReportFilter filter;
	private TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		allPatients = pDAO.getAllPatients();
	}

	public void testFilterByAllergy() throws Exception {
		filter = new MedicalReportFilter(MedicalReportFilterType.ALLERGY, "00882219", factory);
		List<PatientBean> res = filter.filter(allPatients);
		assertEquals(1, res.size());
		assertTrue(res.get(0).getMID() == 100L);
	}

	public void testFilterByAllergyNoResult() {
		filter = new MedicalReportFilter(MedicalReportFilterType.ALLERGY, "Dalpe", factory);
		List<PatientBean> res = filter.filter(allPatients);
		assertTrue(res.isEmpty());
	}

	public void testToString() {
		String expected = "";
		filter = new MedicalReportFilter(MedicalReportFilterType.ALLERGY, "val", factory);
		expected = "Filter by ALLERGY with value val";
		assertEquals(expected, filter.toString());
	}

	public void testFilterTypeFromString() {
		MedicalReportFilterType expected = MedicalReportFilterType.CURRENT_PRESCRIPTIONS;
		MedicalReportFilterType actual = MedicalReportFilter.filterTypeFromString("CurrENt_PREscriptions");
		assertEquals(expected, actual);
	}

	public void testGetFilterType() {
		filter = new MedicalReportFilter(MedicalReportFilterType.PROCEDURE, "city!", factory);
		MedicalReportFilterType expected = MedicalReportFilterType.PROCEDURE;
		assertEquals(expected, filter.getFilterType());
	}

	public void testGetFilterValue() {
		filter = new MedicalReportFilter(MedicalReportFilterType.PROCEDURE, "city!", factory);
		String expected = "city!";
		assertEquals(expected, filter.getFilterValue());
	}

	public void testGetFilterTypeString() {
		filter = new MedicalReportFilter(MedicalReportFilterType.PROCEDURE, "city!", factory);
		assertEquals("PROCEDURE", filter.getFilterTypeString());

		filter = new MedicalReportFilter(MedicalReportFilterType.CURRENT_PRESCRIPTIONS, "city!", factory);
		assertEquals("CURRENT PRESCRIPTIONS", filter.getFilterTypeString());
	}
}
