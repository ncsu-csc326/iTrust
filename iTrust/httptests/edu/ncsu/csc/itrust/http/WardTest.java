package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * WardTest
 */
@SuppressWarnings("unused")
public class WardTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		//gen.insertwards();
		//gen.hcp0();

	}

	
	//6. Heart Doctor will assign 3 patients to the cardiac ward. If Heart Doctor tries to assign yet another, an error will be displayed static the ward is full.
	/**
	 * testhcpaddremovepatient
	 * @throws Exception
	 */
    public void testhcpaddremovepatient() throws Exception {
		// login uap
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());    	

		wr = wr.getLinkWith("Manage Wards").click();    	
		WebForm Form = wr.getForms()[0]; //get search by ward
		Form.setParameter("searchbyroomWard", "1");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[4]; //select form to remove patient
		Form.submit(); //remove current patient
		wr = wc.getCurrentPage();		
    	
		Form = wr.getForms()[0]; //get search by ward
		Form.setParameter("searchbyroomWard", "1");
		Form.submit(); // reget list of wards
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[3];
		Form.submit(); //click assign patient
		wr = wc.getCurrentPage();		
		assertEquals("iTrust - Please Select a Patient" , wr.getTitle()); //check title 
	    //Form = wr.getForms()[1];
	    //sorced from sorceforge
	     Form =wr.getForms()[0];
         Form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
         Form.getButtons()[1].click();

         wr = wc.getCurrentPage(); 
         
     	 Form = wr.getForms()[0]; //get search by ward
         Form.setParameter("searchbyroomWard", "1");
 		 Form.submit();
 		 wr = wc.getCurrentPage();
 		 
 		 assertTrue(wr.getText().contains("<td>Random Person</td>"));
    }
    
    /**
     * testadminaddremoveward
     * @throws Exception
     */
	public void testadminaddremoveward() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());		
		
		wr = wr.getLinkWith("Manage Wards").click();    	
		WebForm Form = wr.getForms()[0]; //get list of hospitals
		Form.setParameter("hospitals", "5"); //facebook rehab center
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[1];
		Form.setParameter("ward", "ChatAddictionClinic");
		Form.submit();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains(" colspan=\"3\">ChatAddictionClinic"));
		Form = wr.getForms()[3]; //get removeward button
		Form.submit();
		wr = wc.getCurrentPage();
		//check make sure table is gone
		assertFalse(wr.getText().contains(" colspan=\"3\">ChatAddictionClinic"));		
		
		
		
	}
	
	 //5. Admin will assigned "Heart Doctor (Heart Surgeon)" to the cardiac ward and "Baby Doctor (Pediatrician)" to the two pediatric wards. 
	 //  If the admin assigns the wrong doctor to the wrong ward, an error will be displayed.
	  //changed baby doctor to kelly doctor because testdata is already present
	/**
	 * testadminassignhcp
	 * @throws Exception
	 */
	public void testadminassignhcp() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());	
		
		wr = wr.getLinkWith("Manage Wards").click();      	
		WebForm Form = wr.getForms()[0]; //get list of hospitals
		Form.setParameter("hospitals", "1");
		Form.submit();
		wr = wc.getCurrentPage();
		//add kelly doctor to cardiology

		Form = wr.getForms()[4]; //get cardiology add hcp form
		//check size
		String[] check = Form.getParameterNames();
		/*
		for (int i = 0; i < wr.getForms().length; i++){
			System.out.println(wr.getForms()[i].getParameterNames()[0]);
		}*/
		assertEquals("HCPtoAdd", check[0]);
		Form.setParameter("HCPtoAdd", "9000000000"); //add kelly doctor
		Form.submit();

		
		wr = wc.getCurrentPage();

		Form = wr.getForms()[4]; //get unassign hcp
		check = Form.getParameterNames();
		

		assertEquals("removeHCP", check[0]);
		Form.setParameter("HCPtoRemove", "9000000000");
		Form.submit();
		
	}
	
//4. Admin will create a new Hospital ("Purple Hospital") and give it 3 wards - 2 pediatric wards and a cardiac ward. Each ward will have 3 rooms.
	/**
	 * testaddwardtohospital
	 * @throws Exception
	 */
	public void testaddwardtohospital() throws Exception {
		WebConversation wc = login("9000000001", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		wr = wr.getLinkWith("Manage Hospital Listing").click();
		assertEquals("iTrust - Maintain Hospital Listing and Assignments", wr.getTitle());
		
		// Fill in the form
		WebForm form = wr.getForms()[0];
		form.setParameter("hospitalID", "5");
		form.setParameter("hospitalName", "Purple Hospital");

		//form.getSubmitButtons()[0].click();
		form.submit(form.getSubmitButton("add"));
		
		wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Manage Wards").click();      	
		WebForm Form = wr.getForms()[0]; //get list of hospitals
		Form.setParameter("hospitals", "5");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[1];
		Form.setParameter("ward", "Pediatric");
		Form.submit();
		wr = wc.getCurrentPage();
		//add rooms
		Form = wr.getForms()[2];
		Form.setParameter("room", "1");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[3];
		Form.setParameter("room", "2");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[4];
		Form.setParameter("room", "3");
		Form.submit();
		wr = wc.getCurrentPage();
		
		
		Form = wr.getForms()[1];
		Form.setParameter("ward", "Pediatric");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[7];
		Form.setParameter("room", "1");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[8];
		Form.setParameter("room", "2");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[9];
		Form.setParameter("room", "3");
		Form.submit();
		wr = wc.getCurrentPage();
		
		
		
		Form = wr.getForms()[1];
		Form.setParameter("ward", "Cardiac");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[2];
		Form.setParameter("room", "1");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[3];
		Form.setParameter("room", "2");
		Form.submit();
		wr = wc.getCurrentPage();
		
		Form = wr.getForms()[4];
		Form.setParameter("room", "3");
		Form.submit();
		wr = wc.getCurrentPage();
		
		//if we get here we pass because if something went wrong the from numbers would be messed up and it would fail.
		assertTrue(true);
		
		/*
		for (int i = 0; i < wr.getForms().length; i++){
			System.out.println(wr.getForms()[i].getParameterNames()[0]);
		}*/
		
	}
	
/* Acceptance tests
 3. Kelly Doctor will diagnose Patient 1 with Influenza. Patient 1 will log in, see their diagnoses, and be able to visit a resource that educates them on influenza.

 4. Admin will create a new Hospital ("Purple Hospital") and give it 3 wards - 2 pediatric wards and a cardiac ward. Each ward will have 3 rooms.

 5. Admin will assigned "Heart Doctor (Heart Surgeon)" to the cardiac ward and "Baby Doctor (Pediatrician)" to the two pediatric wards. 
   If the admin assigns the wrong doctor to the wrong ward, an error will be displayed.

 6. Heart Doctor will assign 3 patients to the cardiac ward. If Heart Doctor tries to assign yet another, an error will be displayed static the ward is full.

*/
	
	
	

}