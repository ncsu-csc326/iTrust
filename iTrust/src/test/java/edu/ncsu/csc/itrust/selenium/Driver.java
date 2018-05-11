package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;


/**
 * Custom implementation of an HtmlUnitDriver that does not report js and css errors.
 */
public class Driver extends HtmlUnitDriver {
	/**
	 * Construct a new driver and disable script error reporting.
	 */
	public Driver() {
		super();
		this.getWebClient().getOptions().setThrowExceptionOnScriptError(false);
		this.getWebClient().setCssErrorHandler(new SilentCssErrorHandler());

	}
	
}
