package net.sourceforge.purrpackage.gradle

import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

interface DefaultValueFactory {
	
	void setProject( Project project );
		
	File getCoverageDataFile();

	File getClassesDir();

	File getSaveUninstrumentedDir();

	String getClasspathWithCobertura();
	
	String getClasspathWithPurrpackage();
	
	File getPurrpackageReportDir();

	Collection getSourceDirs();
	
	String getCoveragePolicy();
	
	String getBuildTimeReportScript();

	boolean getUseJunit();
	
	Collection<String> getJunitIncludes();

	Collection<String> getJunitExcludes();

	Collection<String> getJunitJvmArgs();

	File getJunitOutputDir();	
	
}