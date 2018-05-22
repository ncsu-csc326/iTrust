package edu.ncsu.csc.itrust.controller.labtechnician;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureData;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;

@ManagedBean(name = "lab_technician_controller")
@SessionScoped
public class LabTechnicianController {
	private LabProcedureData ldata;
	private PersonnelDAO pdao;

	/**
	 * Default constructor.
	 * 
	 * @throws DBException
	 *             when error in database
	 */
	public LabTechnicianController() throws DBException {
		ldata = new LabProcedureMySQL();
		pdao = DAOFactory.getProductionInstance().getPersonnelDAO();
	}

	/**
	 * Constructor for testing purposes.
	 * 
	 * @param ds
	 *            data source
	 * @param personnelDAO
	 *            personnel DAO created from testing instance DAO factory
	 */
	public LabTechnicianController(DataSource ds, PersonnelDAO personnelDAO) {
		ldata = new LabProcedureMySQL(ds);
		pdao = personnelDAO;
	}

	public List<PersonnelBean> getLabTechnicianList() throws DBException {
		return pdao.getLabTechs();
	}

	public List<Pair<String, Long>> getLabTechnicianStatusMID() throws DBException {
		return getLabTechnicianList().stream().map((lt) -> {
			try {
				List<LabProcedure> labProcedures = ldata.getLabProceduresForLabTechnician(lt.getMID());
				Map<Integer, Long> priorityQueueCounter = labProcedures.stream()
						.filter((proc)->{
							return proc.getStatus() == LabProcedure.LabProcedureStatus.PENDING
									|| proc.getStatus() == LabProcedure.LabProcedureStatus.IN_TRANSIT
									|| proc.getStatus() == LabProcedure.LabProcedureStatus.TESTING;
						})
						.collect(Collectors.groupingBy(LabProcedure::getPriority, Collectors.counting()));
				String display = String.format("%s, %s (Specialty: %s | Queue Status - High: %d, Medium: %d, Low: %d)",
						lt.getLastName(), lt.getFirstName(), lt.getSpecialty(),
						priorityQueueCounter.getOrDefault(LabProcedure.PRIORITY_HIGH, 0L),
						priorityQueueCounter.getOrDefault(LabProcedure.PRIORITY_MEDIUM, 0L),
						priorityQueueCounter.getOrDefault(LabProcedure.PRIORITY_LOW, 0L));
				return new ImmutablePair<>(display, lt.getMID());
			} catch (DBException e) {
				return null;
			}
		}).filter((pair) -> {
			return pair != null;
		}).collect(Collectors.toList());
	}
}
