package edu.ncsu.csc.itrust.model.loinccode;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface LOINCCodeData extends DataBean<LOINCCode> {
	public LOINCCode getByCode(String code) throws DBException;
}
