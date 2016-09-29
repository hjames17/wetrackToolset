/*
 * Copyright (C), 2014-2015, 
 * FileName: AjaxException.java
 * Author:   xia zhou
 * Date:     2015年6月12日 下午8:44:49
 * Description: 
 */
package studio.wetrack.web.exception;

import studio.wetrack.web.result.ErrorMessage;

/**
 * 一句话是什么
 * 详细描述做什么
 *
 * @author xia zhou
 */
public class BusinessException extends RuntimeException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;

	public BusinessException(String code, String message){
		this.code = code;
		this.message = message;
	}
	public BusinessException(ErrorMessage errorMessage){
		this.code = errorMessage.getCode();
		this.message = errorMessage.getMessage();
	}
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
