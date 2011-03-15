package net.sourceforge.purrpackage.gradle

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import groovy.lang.MissingPropertyException;
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction


class ProjectValueFactory implements DefaultValueFactory {
	
	Project project;
	def sourceTypes = [ "java", "groovy", "scala" ];
		
	File getCoverageDataFile() { return new File( project.buildDir, "purrpackage-coverage-data/cobertura.ser" ); }

	File getClassesDir() { return project.sourceSets.main.classesDir };

	File getSaveUninstrumentedDir() { return new File( project.buildDir, "cobertura-uninstrumented-tmp" ) };

	String getClasspathWithCobertura() { return project.configurations.testRuntime.asPath;}
	
	String getClasspathWithPurrpackage() { return project.configurations.purrpackage.asPath; }
	
	File getPurrpackageReportDir() { return new File( project.buildDir, "purrpackage-report" ); }

	Collection getSourceDirs() { 
		def result = []
		sourceTypes.each {
			try { 
				def ss = project.sourceSets.main["${it}"];
				if ( ss != null ) {
					result.addAll( ss.srcDirs );
				}
			}
			catch( MissingPropertyException mpe ) {
				//ignore
			}
		}
		return result;
	};

	String coveragePolicy = "";
	String buildTimeReportScript = "";

	boolean useJunit = false;

	Collection<String> junitIncludes = [ "**/Test*.class", "**/*Test.class" ];
	Collection<String> junitExcludes = [ "**/Abstract*" ];
	Collection<String> junitJvmArgs = [ ];

	File getJunitOutputDir() {
		return new File( project.buildDir, "junitOutput" );		
	}
		
}