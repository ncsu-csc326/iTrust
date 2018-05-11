package edu.ncsu.csc.itrust.testutils;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.validate.BeanValidator;
import edu.ncsu.csc.itrust.validate.ValidationFormat;

public class ValidatorProxy extends BeanValidator<TestBean> {
	
	@Override
	public String checkFormat(String name, Long value, ValidationFormat format, boolean isNullable) {
		return super.checkFormat(name, value, format, isNullable);
	}
	
	@Override
	public String checkFormat(String name, String value, ValidationFormat format, boolean isNullable) {
		return super.checkFormat(name, value, format, isNullable);
	}

	@Override
	public void validate(TestBean bean) throws FormValidationException {
		throw new IllegalStateException("Mock object acts as a proxy to protected BeanValidator classes. Do not call this method");
	}
}
