package ml.core.internal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ml.core.MLCore;

public class CoreLogger {

	public static Logger cr_logger;

	static {
		cr_logger = LogManager.getLogger(MLCore.mlcore_name);
//		cr_logger.setParent(FMLLog.getLogger());
	}

	public static void log(Level lvl, String msg) {
		cr_logger.log(lvl, msg);
	}
	
	public static void log(Level lvl, String msg, Throwable e) {
		cr_logger.log(lvl, msg, e);
	}

	public static void severe(String msg) {
		log(Level.ERROR, msg);
	}

	public static void warn(String msg) {
		log(Level.WARN, msg);
	}

	public static void info(String msg) {
		log(Level.INFO, msg);
	}

	public static void config(String msg) {
		log(Level.DEBUG, msg);
	}

	public static void fine(String msg) {
		log(Level.DEBUG, msg);
	}

	public static void finer(String msg) {
		log(Level.TRACE, msg);
	}

	public static void finest(String msg) {
		log(Level.ALL, msg);
	}
}
