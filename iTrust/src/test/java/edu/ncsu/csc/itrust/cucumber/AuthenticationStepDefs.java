package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Assert;

public class AuthenticationStepDefs {
	
	private UserDataShared data;
	
	public AuthenticationStepDefs(UserDataShared MIDs){
		this.data = MIDs;
		
	}
	
	@Given("^I login as (.*) using (.*)$")
	public void login_With_MID(String MID, String pwd){
		long mid;
		try{
			mid = Long.parseLong(MID);
			AuthDAO login = new AuthDAO(TestDAOFactory.getTestInstance());
			try {
				if(login.authenticatePassword(mid, pwd)){
					data.loginID= mid;
				}
			} catch (DBException e) {
				// TODO Auto-generated catch block
				Assert.fail("Unable to authenticate Password");
			}
		}
		catch(NumberFormatException ne){
			Assert.fail("Could not parse MID");
		}
	}


}
