/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class Logger.
 */
public class Logger implements Log
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "Logger";
	/**
	 * The log.
	 */
	private Log log = null;
	/**
	 * The logger.
	 */
	private static Logger logger = null;
	
	static
	{
		logger = new Logger();
	}
	
	/**
	 * The Constructor.
	 */
	private Logger()
	{
	}
	
	/**
	 * This method gets the logger instance.
	 * 
	 * @return the logger instance
	 */
	public static Logger getLoggerInstance()
	{
		return logger;
	}
	
	/**
	 * This method sets the log.
	 * 
	 * @param parameter the parameter
	 */
	public void setLog(Class parameter)
	{
		log = LogFactory.getLog(parameter);
	}

	/**
	 * This method checks if is debug enabled.
	 * 
	 * @return true, if is debug enabled
	 */
	public boolean isDebugEnabled()
	{
		return true;
	}

	/**
	 * This method checks if is error enabled.
	 * 
	 * @return true, if is error enabled
	 */
	public boolean isErrorEnabled()
	{
		return true;
	}

	/**
	 * This method checks if is fatal enabled.
	 * 
	 * @return true, if is fatal enabled
	 */
	public boolean isFatalEnabled()
	{
		return true;
	}

	/**
	 * This method checks if is info enabled.
	 * 
	 * @return true, if is info enabled
	 */
	public boolean isInfoEnabled()
	{
		return true;
	}

	/**
	 * This method checks if is trace enabled.
	 * 
	 * @return true, if is trace enabled
	 */
	public boolean isTraceEnabled()
	{
		return true;
	}

	/**
	 * This method checks if is warn enabled.
	 * 
	 * @return true, if is warn enabled
	 */
	public boolean isWarnEnabled()
	{
		return true;
	}

	/**
	 * Trace.
	 * 
	 * @param arg the arg
	 */
	public void trace(Object arg)
	{
		if(isTraceEnabled())
		{
			log.trace(arg);
		}
	}
	
	/**
	 * Trace.
	 * 
	 * @param arg the arg
	 * @param className the class name
	 */
	public void trace(String className, Object arg)
	{
		if(isTraceEnabled())
		{
			log.trace(className+"-"+arg);
		}
	}
	

	/**
	 * Trace.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 */
	public void trace(Object arg, Throwable throwable)
	{
		if(isTraceEnabled())
		{
			log.trace(arg,throwable);
		}
	}
	
	/**
	 * Trace.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 * @param className the class name
	 */
	public void trace(String className, Object arg, Throwable throwable)
	{
		if(isTraceEnabled())
		{
			log.trace(className+"-"+arg,throwable);
		}
	}

	/**
	 * Debug.
	 * 
	 * @param arg the arg
	 */
	public void debug(Object arg)
	{
		if(isDebugEnabled())
		{
			log.debug(arg);
		}
	}
	
	/**
	 * Debug.
	 * 
	 * @param arg the arg
	 * @param className the class name
	 */
	public void debug(String className,Object arg)
	{
		if(isDebugEnabled())
		{
			log.debug(className+"-"+arg);
			
		}
	}

	/**
	 * Debug.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 */
	public void debug(Object arg, Throwable throwable)
	{
		if(isDebugEnabled())
		{
			log.debug(arg,throwable);
		}
	}
	
	/**
	 * Debug.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 * @param className the class name
	 */
	public void debug(String className,Object arg,Throwable throwable)
	{
		if(isDebugEnabled())
		{
			log.debug(className+"-"+arg,throwable);
			
		}
	}

	/**
	 * Info.
	 * 
	 * @param arg the arg
	 */
	public void info(Object arg)
	{
		if(isInfoEnabled())
		{
			log.info(arg);
		}
	}
	
	/**
	 * Info.
	 * 
	 * @param arg the arg
	 * @param className the class name
	 */
	public void info(String className, Object arg)
	{
		if(isInfoEnabled())
		{
			log.info(className+"-"+arg);
		}
	}

	/**
	 * Info.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 */
	public void info(Object arg, Throwable throwable)
	{
		if(isInfoEnabled())
		{
			log.info(arg,throwable);
		}
	}
	
	/**
	 * Info.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 * @param className the class name
	 */
	public void info(String className, Object arg, Throwable throwable)
	{
		if(isInfoEnabled())
		{
			log.info(className+"-"+arg,throwable);
		}
	}

	/**
	 * Warn.
	 * 
	 * @param arg the arg
	 */
	public void warn(Object arg)
	{
		if(isWarnEnabled())
		{
			log.warn(arg);
		}
	}
	
	/**
	 * Warn.
	 * 
	 * @param arg the arg
	 * @param className the class name
	 */
	public void warn(String className, Object arg)
	{
		if(isWarnEnabled())
		{
			log.warn(className+"-"+arg);
		}
	}

	/**
	 * Warn.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 */
	public void warn(Object arg, Throwable throwable)
	{
		if(isWarnEnabled())
		{
			log.warn(arg,throwable);
		}
	}
	
	/**
	 * Warn.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 * @param className the class name
	 */
	public void warn(String className, Object arg, Throwable throwable)
	{
		if(isWarnEnabled())
		{
			log.warn(className+"-"+arg,throwable);
		}
	}

	/**
	 * Error.
	 * 
	 * @param arg the arg
	 */
	public void error(Object arg)
	{
		if(isErrorEnabled())
		{
			log.error(arg);
		}
	}
	
	/**
	 * Error.
	 * 
	 * @param arg the arg
	 * @param className the class name
	 */
	public void error(String className, Object arg)
	{
		if(isErrorEnabled())
		{
			log.error(className+"-"+arg);
		}
	}

	/**
	 * Error.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 */
	public void error(Object arg, Throwable throwable)
	{
		if(isErrorEnabled())
		{
			log.error(arg,throwable);
		}
	}
	
	/**
	 * Error.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 * @param className the class name
	 */
	public void error(String className, Object arg, Throwable throwable)
	{
		if(isErrorEnabled())
		{
			log.error(className+"-"+arg,throwable);
		}
	}

	/**
	 * Fatal.
	 * 
	 * @param arg the arg
	 */
	public void fatal(Object arg)
	{
		if(isFatalEnabled())
		{
			log.fatal(arg);
		}
	}
	
	/**
	 * Fatal.
	 * 
	 * @param arg the arg
	 * @param className the class name
	 */
	public void fatal(String className, Object arg)
	{
		if(isFatalEnabled())
		{
			log.fatal(className+"-"+arg);
		}
	}
	
	/**
	 * Fatal.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 */
	public void fatal(Object arg, Throwable throwable)
	{
		if(isFatalEnabled())
		{
			log.fatal(arg,throwable);
		}
	}
	
	/**
	 * Fatal.
	 * 
	 * @param throwable the throwable
	 * @param arg the arg
	 * @param className the class name
	 */
	public void fatal(String className, Object arg, Throwable throwable)
	{
		if(isFatalEnabled())
		{
			log.fatal(className+"-"+arg,throwable);
		}
	}
}
