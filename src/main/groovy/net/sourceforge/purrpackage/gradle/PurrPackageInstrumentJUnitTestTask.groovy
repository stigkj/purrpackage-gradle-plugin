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

class PurrPackageInstrumentJUnitTestTask extends DefaultTask {

	DefaultValueFactory defaultValueFactory = new ProjectValueFactory();

	boolean defaultsInited = false;

	def initDefaults() {
		defaultValueFactory.setProject( project );
		defaultsInited = true;
	}

	@TaskAction def runJUnitTests() {
		if ( ! defaultsInited ) {
			initDefaults();
		}
		String testClasses = project.sourceSets.test.classesDir.canonicalPath;
                String testInstrumentDir = "${project.buildDir}/pp-tests-instrumented"
                String testSaveDir = "${project.buildDir}/pp-tests-uninstrumented"

                project.ant.java( 
                        classname: "net.sourceforge.purrpackage.recording.instrument.SimpleInstrumenterMain", 
                        classpath: "${project.configurations.purrpackage.asPath}" ) {
                        arg( value: "net.sourceforge.purrpackage.recording.instrument.PurrPackageTestMethodTransformer" )
                        arg( value: testClasses )
                        arg( value: testInstrumentDir )
                }
                ant.rename( src: testClasses, dest:testSaveDir );
                ant.rename( src: testInstrumentDir, dest:testClasses);
	}
}