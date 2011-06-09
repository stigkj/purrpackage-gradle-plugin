/*
 * Purrpackage gradle plugin - http://purrpkg-gradle.sourceforge.net
 * Copyright (C) 2011 John Charles Roth
 *
 * This is free software, licensed under the terms of the GNU GPL 
 * Version 2 or, at your option, any later version. You should have 
 * received a copy of the license with this file. See the above web address
 * for more information, or contact the Free Software Foundation, Boston, MA. 
 * It is distributed WITHOUT WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

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

	File getSaveClassesDir() { return new File( project.buildDir, "cobertura-uninstrumented-tmp" ) };

    File getTestClassesDir() {return project.sourceSets.test.classesDir };
    
    File getSaveTestClassesDir() { return new File( project.buildDir, "test-uninstrumented-tmp" ) };
    
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
    String selectorClass;
		
}