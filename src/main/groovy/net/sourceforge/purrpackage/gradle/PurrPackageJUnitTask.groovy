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
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.testng.TestNGOptions;
import org.gradle.api.logging.LogLevel;

class PurrPackageJUnitTask extends DefaultTask {

	File toDir;
	Collection<String> includes;
	Collection<String> excludes;
	Collection<String> jvmArgs;
	File coverageDataFile;
	File classesDir;
	DefaultValueFactory defaultValueFactory = new ProjectValueFactory();

	boolean defaultsInited = false;

	def initDefaults() {
		defaultValueFactory.setProject( project );
		DefaultValueFactory dvf = defaultValueFactory;
		toDir = dvf.junitOutputDir;
		includes = dvf.junitIncludes;
		excludes = dvf.junitExcludes;
		jvmArgs = dvf.junitJvmArgs;
		coverageDataFile = dvf.coverageDataFile;
		classesDir = dvf.classesDir;
		defaultsInited = true;
	}

	@TaskAction def runJUnitTests() {
		if ( ! defaultsInited ) {
			initDefaults();
		}
		if ( toDir == null ) {
			toDir = "${project.buildDir}/PurrPackageJUnitTaskTmp";
		}
		String testRuntimePath = project.configurations.testRuntime.filter( { !it.name.endsWith(".pom" ) } ).asPath; // TODO filter elswhere?
		logger.log( LogLevel.DEBUG, "PurrPackage is using Ant to run JUnit tests" );
		logger.log( LogLevel.DEBUG, "PurrPackage junit to ${toDir} with args ${jvmArgs} tetRuntime path is ${testRuntimePath}" );
		String mainClasses = classesDir.canonicalPath;
		String testClasses = project.sourceSets.test.classesDir.canonicalPath;

		project.mkdir( toDir );
		File xmlDir = new File( toDir, "xml" );
		project.mkdir( xmlDir );
		project.ant {
			taskdef(name: 'junit',
					classname: 'org.apache.tools.ant.taskdefs.optional.junit.JUnitTask',
					classpath: testRuntimePath);
			'junit'( printsummary:"yes", fork:"yes", forkMode:"once", haltonfailure:"yes" ) {
				jvmarg( value:"-Dnet.sourceforge.cobertura.datafile=${coverageDataFile.canonicalPath}" );
				jvmArgs.each {
					jvmarg( value:"${it}" );
				}
				formatter( type:"xml" );
				formatter( classname:"net.sourceforge.purrpackage.recording.PerPackageCoverageJUnitFormatter", useFile:"false" );
				classpath() {
					pathelement( path: testRuntimePath );
					pathelement( path: mainClasses );
					pathelement( path: testClasses );
				}
				batchtest( todir: xmlDir ) {
					fileset( dir: testClasses ) {
						includes.each {
							include( name:"${it}" );
						}
						excludes.each {
							exclude( name:"${it}" );
						}
					}
				}
			}
			'junitreport'(  todir: toDir ) { 
 			    fileset( dir : xmlDir ) {
					 include( name:"TEST-*.xml" );
 			    }
				report( todir: toDir );
			}
		}			
	}
}