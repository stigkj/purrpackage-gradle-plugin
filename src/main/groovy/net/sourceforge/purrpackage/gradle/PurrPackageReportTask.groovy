package net.sourceforge.purrpackage.gradle;

import java.io.File;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.DefaultTask;

/** If you need to custom configure this task, call initDefaults() first. */

class PurrPackageReportTask extends DefaultTask {

	boolean defaultsInited = false;
	File purrpackageReportDir;
	String coverageDataFile;
	String classpathWithPurrpackage;
	String coveragePolicy;
	String buildTimeReportScript; 
	Collection sourceDirs = [];
	DefaultValueFactory defaultValueFactory = new ProjectValueFactory();

	def initDefaults() {
		def dvf = defaultValueFactory;
		dvf.setProject( project );
		coverageDataFile = dvf.coverageDataFile;
		classpathWithPurrpackage = dvf.classpathWithPurrpackage;
		sourceDirs = dvf.sourceDirs;
		coveragePolicy = dvf.coveragePolicy;
		purrpackageReportDir = dvf.purrpackageReportDir;
		buildTimeReportScript = dvf.buildTimeReportScript;
		defaultsInited = true;
	}
		
	@TaskAction
	def runReport() {
		if ( ! defaultsInited ) {
			initDefaults();
		}
		logger.log( LogLevel.INFO, "Generating PurrPackage report to ${purrpackageReportDir} frome data in ${coverageDataFile}" );
		logger.log( LogLevel.INFO, "Coverage policy ${coveragePolicy} and buildtime reports ${buildTimeReportScript}" );
		logger.log( LogLevel.DEBUG, "Sourcedirs ${sourceDirs} and classpath ${classpathWithPurrpackage}" );
		def sourceDirString = sourceDirs.join(",");
		purrpackageReportDir.mkdirs();
		project.ant {
			taskdef(resource:'purrPackageAntTasks.properties',
					classpath: classpathWithPurrpackage );
			'package-coverage-report'(destdir: purrpackageReportDir.canonicalPath,
					format:'js', 
					srcdir:sourceDirString,
					datafile: coverageDataFile,
					coveragePolicy:coveragePolicy,
					buildTimeReportScript:buildTimeReportScript
			);
		}
	}
}


