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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.testng.TestNGOptions;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.logging.LogLevel;

class PurrPackagePlugin implements Plugin<Project> {

	public void apply(Project p) {
                String purrpackageVersion = p.properties.get( "purrpackageVersion" );
                if ( purrpackageVersion == null || purrpackageVersion.trim().length() == 0 ) {
                   throw new RuntimeException( "You must set the purrpackageVersion property before apply the Purrpackage Plugin" );
                }
		PurrPackagePluginConvention config = new PurrPackagePluginConvention(p);
		p.convention.plugins.purrpackage = config;

		p.configurations.add( "purrpackage" );
		p.dependencies.add( "purrpackage",
			"net.sourceforge.purrpackage:purrpackage:${purrpackageVersion}", { exclude ( group: "org.apache.ant" ) } );
                p.dependencies.add( "purrpackage", "org.apache.ant:ant-junit:1.8.2" );
		p.dependencies.add( "testRuntime",
				"net.sourceforge.purrpackage:purrpackage-runtime:${purrpackageVersion}", { exclude ( group: "org.apache.ant" ) } );

		CoberturaInstrumentTask instrumentTask = p.tasks.add( "purrpackageInstrument", CoberturaInstrumentTask.class );
		instrumentTask.defaultValueFactory = config;

                PurrPackageInstrumentJUnitTestTask testInstrumentTask = p.tasks.add( "purrpackageInstrumentTests", PurrPackageInstrumentJUnitTestTask.class );
                testInstrumentTask.defaultValueFactory = config;

		PurrPackageReportTask reportTask = p.tasks.add( "purrpackageReport", PurrPackageReportTask.class );
		reportTask.defaultValueFactory = config;

		CoberturaUninstrumentTask uninstrumentTask = p.tasks.add( "purrpackageUninstrument", CoberturaUninstrumentTask.class ) ;
		
		instrumentTask.dependsOn( p.testClasses );
                testInstrumentTask.dependsOn( p.testClasses );
//		reportTask.dependsOn( p.test );
		uninstrumentTask.dependsOn( reportTask );
		p.assemble.dependsOn( uninstrumentTask );

		p.tasks.each { testTask ->
			if ( testTask instanceof Test ) {
				configureTestTask(p, (Test) testTask, config)
				testTask.dependsOn( instrumentTask );
                                testTask.dependsOn( testInstrumentTask );
                                reportTask.dependsOn( testTask );
				uninstrumentTask.dependsOn( testTask );
			}
		}

//		PurrPackageJUnitTask junitTask = p.tasks.add( "purrpackageJunitRunner", PurrPackageJUnitTask.class )
//		junitTask.defaultValueFactory = config;
		
//		junitTask.dependsOn( p.test );
//		reportTask.dependsOn( junitTask );
		
		p.logger.log( LogLevel.INFO, "Applied PurrPackagePlugin version ${purrpackageVersion}" );
	}

	def configureTestTask( Project p, Test testTask, PurrPackagePluginConvention config ) {
		testTask.doFirst {
			systemProperties["net.sourceforge.cobertura.datafile"] = config.coverageDataFile.canonicalPath;
			def opts = testTask.getOptions();
			if ( opts instanceof TestNGOptions ) {
				TestNGOptions tngo = opts;
				tngo.listeners << "net.sourceforge.purrpackage.recording.PerPackageCoverageListener";
			}
		}
	}
}


class PurrPackagePluginConvention implements DefaultValueFactory {

	File coverageDataFile;
	File classesDir;
	File saveUninstrumentedDir;
	File purrpackageReportDir;
	Collection sourceDirs;
	boolean useJunit = false;
	Collection<String> junitIncludes;
	Collection<String> junitExcludes;
	Collection<String> junitJvmArgs;
	File junitOutputDir;
	
	Project project;
	
	String getClasspathWithPurrpackage() {
		return project.configurations.purrpackage.asPath;
	}
	String getClasspathWithCobertura() {
		return getClasspathWithPurrpackage();
	}

	String coveragePolicy = "";
	String buildTimeReportScript = "";
	
	def configure(Closure close){
		close.delegate = this;
		close.run();
	}
	
	PurrPackagePluginConvention(Project project){
		ProjectValueFactory dvf = new ProjectValueFactory( );
		dvf.setProject( project );
		coverageDataFile = dvf.coverageDataFile;
		saveUninstrumentedDir = dvf.saveUninstrumentedDir;
		purrpackageReportDir = dvf.purrpackageReportDir;
		classesDir = dvf.classesDir
		purrpackageReportDir = dvf.purrpackageReportDir;
		sourceDirs = dvf.sourceDirs;
                useJunit = dvf.useJunit;
		junitIncludes = dvf.junitIncludes;
		junitExcludes = dvf.junitExcludes;
		junitJvmArgs = dvf.junitJvmArgs;
		junitOutputDir = dvf.junitOutputDir;
	}
}