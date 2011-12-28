package com.elevenquest.sol.upnp.model;

public class UPnPAllowedValueRange extends UPnPBase {
	
	UPnPDataType type;
	String minimum;
	String maximum;
	String step;
	
	public UPnPDataType getType() {
		return type;
	}
	public void setType(UPnPDataType type) {
		this.type = type;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public String getMaximum() {
		return maximum;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}

}
