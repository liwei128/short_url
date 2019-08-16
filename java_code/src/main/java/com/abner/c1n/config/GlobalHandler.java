package com.abner.c1n.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.exception.RetCodeException;


/**
 * 全局Controller处理
 * @author liwei
 * @date: 2018年9月10日 上午10:54:48
 *
 */
@ControllerAdvice
public class GlobalHandler{
	
	private static  Logger logger = LoggerFactory.getLogger(GlobalHandler.class);
	
	/**
	 * 全局异常处理
	 * @param req
	 * @param e
	 * @return
	 */

	@ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData<Void> defaultErrorHandler(HttpServletRequest req,HttpServletResponse res, Exception e){
		logger.error("defaultErrorHandler,uri:{},exception",req.getRequestURI(),e);
		/**
		 * 参数缺失的异常
		 */
		if(e instanceof BindException || e instanceof MissingServletRequestParameterException){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		
		/**
		 * 需要返回响应码的异常
		 */
		if(e instanceof RetCodeException){
			return ((RetCodeException)e).getRetCode();
		}
		return ResultData.getInstance(BaseRetCode.EXCEPTION_FORMAT,e.getMessage());
    }
	
	/**
	 * 应用到所有@RequestMapping注解方法
	 * 请求参数实例化为bean时的转换处理
	 * @param binder
	 */
	@InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }
	
	/**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
	@ModelAttribute
    public void addAttributes(Model model) {
        //model.addAttribute("author", "liwei");
    }
}
