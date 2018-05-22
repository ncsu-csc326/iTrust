package edu.ncsu.csc.itrust.model.loinccode;

public class LOINCCode {
	private String code;
	private String component;
	private String kindOfProperty;
	private String timeAspect;
	private String system;
	private String scaleType;
	private String methodType;
	public LOINCCode(String code, String component, String kindOfProperty) {
		this.code = code;
		this.component = component;
		this.kindOfProperty = kindOfProperty;
	}
	public LOINCCode(String code, String component, String kindOfProperty, String timeAspect, String system,
			String scaleType, String methodType) {
		this(code, component, kindOfProperty);
		this.timeAspect = timeAspect;
		this.system = system;
		this.scaleType = scaleType;
		this.methodType = methodType;
	}
	public String getCode() {
		return code;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getKindOfProperty() {
		return kindOfProperty;
	}
	public void setKindOfProperty(String kindOfProperty) {
		this.kindOfProperty = kindOfProperty;
	}
	public String getTimeAspect() {
		return timeAspect;
	}
	public void setTimeAspect(String timeAspect) {
		this.timeAspect = timeAspect;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getScaleType() {
		return scaleType;
	}
	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public String toString(){
	    return code + " - " + component + " - " + kindOfProperty + " - " + timeAspect + " - " + system + " - " + scaleType + " - " + methodType;
	}
}
