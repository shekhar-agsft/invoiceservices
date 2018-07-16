package com.agile.aggrement.invoice.util;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * The Class VelocityUtility.
 */
/**
 * @author shekhar
 *
 */
@SuppressWarnings("deprecation")
@Component
public class VelocityUtility {

	/** The velocity engine. */
	@Autowired
	private VelocityEngine velocityEngine;

	

	/**
	 * Gets the templateto text.
	 *
	 * @param templatepdf
	 *            the templatepdf
	 * @return the templateto text
	 */
	@SuppressWarnings("unchecked")
	public String getTemplatetoText(String templateName, Object object) {
		ObjectMapper m = new ObjectMapper();
		Map<String, Object> props = m.convertValue(object, Map.class);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", props);
		return text;
	}

	/**
	 * @param templateName
	 * @param props
	 * @return
	 */
	public String getTemplatetoText(String templateName, Map<String, Object> props) {
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", props);
		return text;
	}

	/**
	 * @param templateName
	 * @param props
	 * @return
	 */
	public String getTemplatetoTextUsingProps(String templateName, Map<String, Object> props) {

		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", props);
		return text;
	}

	/**
	 * @param templateName
	 * @param object
	 * @return text
	 */
	@SuppressWarnings("unchecked")
	public String getTemplatetoText(String templateName, Object... object) {
		ObjectMapper m = new ObjectMapper();
		Map<String, Object> props = m.convertValue(object, Map.class);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", props);
		return text;
	}

}