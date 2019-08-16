package com.abner.c1n.beans.dto.logs;

import org.apache.commons.lang3.StringUtils;

import com.abner.c1n.beans.enums.AttributeType;

/**
 * 根据属性查询条件
 * @author LW
 * @time 2019年6月8日 下午3:16:15
 */
public class AttributeLogCondition extends LogCondition{
	
	private AttributeType attribute;
	
	private String name;
	
	private String condition;
	
	

	public AttributeType getAttribute() {
		return attribute;
	}

	public void setAttribute(AttributeType attribute) {
		this.attribute = attribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public AttributeLogCondition init(){
		this.name = attribute.getName();
		if(StringUtils.isNotEmpty(attribute.getCondition())){
			this.condition = String.format(attribute.getCondition(), this.condition);
		}
		return this;
	}
	
	

}
