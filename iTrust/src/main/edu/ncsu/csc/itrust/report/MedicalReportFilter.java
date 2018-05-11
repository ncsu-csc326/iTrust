package edu.ncsu.csc.itrust.report;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;

/**
 * 
 *
 */
public class MedicalReportFilter extends ReportFilter {

	/**
	 * 
	 * 
	 */
	public enum MedicalReportFilterType {
		PROCEDURE("PROCEDURE"),
		ALLERGY("ALLERGY"),
		CURRENT_PRESCRIPTIONS("CURRENT PRESCRIPTIONS"),
		PASTCURRENT_PRESCRIPTIONS("PAST AND CURRENT PRESCRIPTIONS"),
		DIAGNOSIS_ICD_CODE("DIAGNOSIS"),
		MISSING_DIAGNOSIS_ICD_CODE("MISSING DIAGNOSIS"),
		LOWER_OFFICE_VISIT_DATE("LOWER OFFICE VISIT DATE LIMIT"),
		UPPER_OFFICE_VISIT_DATE("UPPER OFFICE VISIT DATE LIMIT");

		private final String name;

		/**
		 * 
		 * @param name
		 */
		private MedicalReportFilterType(String name) {
			this.name = name;
		}

		/**
		 * 
		 */
		@Override
		public String toString() {
			return this.name;
		}
	}

	private MedicalReportFilterType filterType;
	private String filterValue;
	private AllergyDAO aDAO;

	/**
	 * 
	 * @param filterType
	 * @param filterValue
	 */
	public MedicalReportFilter(MedicalReportFilterType filterType, String filterValue, DAOFactory factory) {
		this.filterType = filterType;
		this.filterValue = filterValue;
		aDAO = factory.getAllergyDAO();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static MedicalReportFilterType filterTypeFromString(String name) {
		for(MedicalReportFilterType type : MedicalReportFilterType.values()) {
			if(type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public List<PatientBean> filter(List<PatientBean> patients) {
		List<PatientBean> prunedList = new ArrayList<PatientBean>();
		boolean add = filterValue != null && !filterValue.isEmpty();
		if (add) {
			for (PatientBean patient : patients) {
				add = false;
				switch (filterType) {
				case ALLERGY:
					try {
						List<AllergyBean> allergies = aDAO.getAllergies(patient.getMID());
						for (AllergyBean allergy : allergies) {
							if (filterValue.equalsIgnoreCase(allergy.getNDCode())) {
								add = true;
								break;
							}
						}
					} catch (Exception e) {
						break;
					}
					break;
				default:
					break;
				}
				if(add)
					prunedList.add(patient);
			}
		}
		return prunedList;
	}

	/**
	 * 
	 * @return
	 */
	public MedicalReportFilterType getFilterType() {
		return filterType;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public String getFilterTypeString() {
		return filterType.toString();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getFilterValue() {
		return filterValue;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		String out = "Filter by " + filterType.toString() + " with value " + filterValue;
		return out;
	}

}
