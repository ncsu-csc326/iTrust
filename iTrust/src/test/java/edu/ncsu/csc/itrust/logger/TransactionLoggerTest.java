package edu.ncsu.csc.itrust.logger;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO;

public class TransactionLoggerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetInstance() {
		TransactionLogger first = TransactionLogger.getInstance();
		TransactionLogger second = TransactionLogger.getInstance();
		// Should be a singleton
		Assert.assertEquals(first, second);
	}

	@Test
	public void testLogTransaction() throws DBException {
		DAOFactory prod = spy(DAOFactory.getProductionInstance());
		TransactionDAO mockDAO = mock(TransactionDAO.class);
		when(prod.getTransactionDAO()).thenReturn(mockDAO);
		doNothing().when(mockDAO).logTransaction(any(), any(), any(), any());
	}
}
