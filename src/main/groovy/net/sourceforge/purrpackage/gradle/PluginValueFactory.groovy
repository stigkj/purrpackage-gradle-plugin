package net.sourceforge.purrpackage.gradle

import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

class PluDefaultValueFactory {
	
	Project project;
	def sourceTypes = [ "java", "groovy", "scala" ];
		
	File coverageDataFile() { return new File( project.buildDir, "purrpackage-coverage-data/cobertura.ser" ); }

	File classesDir() { return project.sourceSets.main.classesDir };

	File saveUninstrumentedDir() { return new File( project.buildDir, "cobertura-uninstrumented-tmp" ) };

	String classpathWithCobertura() { return project.configurations.testRuntime.asPath;}
	
	String classpathWithPurrpackage() { return project.configuration.purrpackage.asPath; }
	
	File purrpackageReportDir() { return new File( project.buildDir, "purrpackage-report" ); }

	Collection sourceDirs() { 
		def result = []
		sourceTypes.each {
			def ss = project.sourceSets.main["${it}"];
			if ( ss != null ) {
				result.addAll( ss.srcDirs );
			}
		}
		return result;
	};
}