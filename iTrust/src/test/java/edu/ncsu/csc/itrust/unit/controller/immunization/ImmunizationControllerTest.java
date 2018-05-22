package edu.ncsu.csc.itrust.unit.controller.immunization;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import edu.ncsu.csc.itrust.controller.immunization.ImmunizationController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.immunization.ImmunizationMySQL;
import junit.framework.TestCase;

public class ImmunizationControllerTest extends TestCase {

    private DataSource ds;
    
    @Override
    public void setUp(){
        ds = ConverterDAO.getDataSource();
    }
    
    @Test
    public void testDiabolicals() throws DBException, SQLException{
        ImmunizationController controller = new ImmunizationController(ds);
        ImmunizationMySQL sql = spy(new ImmunizationMySQL(ds));
        controller.setSQL(sql);
        Immunization i = new Immunization();
        i.setCptCode(new CPTCode("90044", "blah"));
        when(sql.add(i)).thenThrow(new NullPointerException());
        controller.add(i);
        when(sql.update(i)).thenThrow(new NullPointerException());
        controller.edit(i);
        when(sql.remove(0)).thenThrow(new SQLException());
        controller.remove(0);
        when(sql.remove(1)).thenThrow(new NullPointerException());
        controller.remove(1);
        when(sql.getImmunizationsForOfficeVisit(1)).thenThrow(new NullPointerException());
        controller.getImmunizationsByOfficeVisit("1");
        when(sql.getCodeName("9")).thenThrow(new SQLException());
        controller.getCodeName("9");
    }
}
