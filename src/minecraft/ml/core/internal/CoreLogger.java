package ml.core.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import ml.core.MLCore;
import cpw.mods.fml.common.FMLLog;

public class CoreLogger {

	public static Logger cr_logger;

	static {
		cr_logger = Logger.getLogger(MLCore.mlcore_name);
		cr_logger.setParent(FMLLog.getLogger());
	}

	public static void log(Level lvl, String msg) {
		cr_logger.log(lvl, msg);
	}
	
	public static void log(Level lvl, String msg, Throwable e) {
		cr_logger.log(lvl, msg, e);
	}

	public static void severe(String msg) {
		log(Level.SEVERE, msg);
	}

	public static void warn(String msg) {
		log(Level.WARNING, msg);
	}

	public static void info(String msg) {
		log(Level.INFO, msg);
	}

	public static void config(String msg) {
		log(Level.CONFIG, msg);
	}

	public static void fine(String msg) {
		log(Level.FINE, msg);
	}

	public static void finer(String msg) {
		log(Level.FINER, msg);
	}

	public static void finest(String msg) {
		log(Level.FINEST, msg);
	}
}
