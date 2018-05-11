package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.FamilyMemberBean;
import edu.ncsu.csc.itrust.model.old.beans.GroupReportBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.FamilyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.report.DemographicReportFilter.DemographicReportFilterType;
import edu.ncsu.csc.itrust.report.MedicalReportFilter.MedicalReportFilterType;
import edu.ncsu.csc.itrust.report.PersonnelReportFilter.PersonnelReportFilterType;
import edu.ncsu.csc.itrust.report.ReportFilter;

/**
 * 
 *
 */
public class GroupReportAction {

	private PatientDAO pDAO;
	private AllergyDAO aDAO;
	private FamilyDAO fDAO;

	/**
	 * 
	 * @param factory
	 * @throws DBException 
	 */
	public GroupReportAction(DAOFactory factory, long mid) throws DBException {
		pDAO = factory.getPatientDAO();
		aDAO = factory.getAllergyDAO();
		fDAO = factory.getFamilyDAO();
		TransactionLogger.getInstance().logTransaction(TransactionType.GROUP_REPORT_VIEW, mid, 0L , "");
	}

	/**
	 * 
	 * @param filters
	 * @return
	 */
	public GroupReportBean generateReport(List<ReportFilter> filters) {
		List<PatientBean> patients;

		try {
			patients = getAllPatients();
		} catch (DBException e) {
			return null;
		}

		for (ReportFilter filter : filters) {
			patients = filter.filter(patients);
		}

		return new GroupReportBean(patients, filters);
	}

	/**
	 * 
	 * @param patient
	 * @param filterType
	 * @return
	 */
	public String getComprehensiveDemographicInfo(PatientBean patient, DemographicReportFilterType filterType) {
		switch (filterType) {
		case GENDER:
			return patient.getGender().toString();
		case LAST_NAME:
			return patient.getLastName();
		case FIRST_NAME:
			return patient.getFirstName();
		case CONTACT_EMAIL:
			return patient.getEmail();
		case STREET_ADDR:
			return patient.getStreetAddress1() + " " + patient.getStreetAddress2();
		case CITY:
			return patient.getCity();
		case STATE:
			return patient.getState();
		case ZIP:
			return patient.getZip();
		case PHONE:
			return patient.getPhone();
		case EMER_CONTACT_NAME:
			return patient.getEmergencyName();
		case EMER_CONTACT_PHONE:
			return patient.getEmergencyPhone();
		case INSURE_NAME:
			return patient.getIcName();
		case INSURE_ADDR:
			return patient.getIcAddress1() + " " + patient.getIcAddress2();
		case INSURE_CITY:
			return patient.getIcCity();
		case INSURE_STATE:
			return patient.getIcState();
		case INSURE_ZIP:
			return patient.getIcZip();
		case INSURE_PHONE:
			return patient.getIcPhone();
		case MID:
			return Long.toString(patient.getMID());
		case INSURE_ID:
			return patient.getIcID();
		case PARENT_FIRST_NAME:
			try {
				List<FamilyMemberBean> parents = fDAO.getParents(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (FamilyMemberBean parent : parents) {
					buff.append(parent.getFirstName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		case PARENT_LAST_NAME:
			try {
				List<FamilyMemberBean> parents = fDAO.getParents(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (FamilyMemberBean parent : parents) {
					buff.append(parent.getLastName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		case CHILD_FIRST_NAME:
			try {
				List<FamilyMemberBean> children = fDAO.getChildren(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (FamilyMemberBean child : children) {
					buff.append(child.getFirstName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		case CHILD_LAST_NAME:
			try {
				List<FamilyMemberBean> children = fDAO.getChildren(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (FamilyMemberBean child : children) {
					buff.append(child.getLastName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		case SIBLING_FIRST_NAME:
			try {
				List<FamilyMemberBean> siblings = fDAO.getSiblings(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (FamilyMemberBean sibling : siblings) {
					buff.append(sibling.getFirstName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		case SIBLING_LAST_NAME:
			try {
				List<FamilyMemberBean> siblings = fDAO.getSiblings(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (FamilyMemberBean sibling : siblings) {
					buff.append(sibling.getLastName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		case DEACTIVATED:
			return patient.getDateOfDeactivationStr();
		default:
			break;
		}
		return null;
	}

	/**
	 * 
	 * @param patient
	 * @param filterType
	 * @return
	 */
	public String getComprehensiveMedicalInfo(PatientBean patient, MedicalReportFilterType filterType) {
		try {
			String out;
			StringBuffer buff = new StringBuffer();
			switch (filterType) {
			case ALLERGY:
				List<AllergyBean> allergies = aDAO.getAllergies(patient.getMID());
				for (AllergyBean allergy : allergies) {
					buff.append(allergy.getNDCode());
					buff.append("\n");
				}
				out = buff.toString();
				return out;
			
			default:
				break;
			}
		} catch(Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param patient
	 * @param filterType
	 * @return
	 */
	public String getComprehensivePersonnelInfo(PatientBean patient, PersonnelReportFilterType filterType) {
		switch (filterType) {
		case DLHCP:
			try {
				List<PersonnelBean> dlhcps = pDAO.getDeclaredHCPs(patient.getMID());
				StringBuffer buff = new StringBuffer();
				for (PersonnelBean dlhcp : dlhcps) {
					buff.append(dlhcp.getFullName());
					buff.append("\n");
				}
				String out = buff.toString();
				return out;
			} catch (Exception e) {
				break;
			}
		default:
			break;
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws DBException
	 */
	private List<PatientBean> getAllPatients() throws DBException {
		return pDAO.getAllPatients();
	}
}
