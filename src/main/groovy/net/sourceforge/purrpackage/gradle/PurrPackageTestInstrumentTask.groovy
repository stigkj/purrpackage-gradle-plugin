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

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

/** If you need to custom configure this task, call initDefaults() first. */

class PurrPackageTestInstrumentTask extends DefaultTask {

    boolean defaultsInited = false;
    File saveTestClassesDir;
    File testClassesDir;
    String classpathWithPurrpackage;
    String selectorClass;
    DefaultValueFactory defaultValueFactory = new ProjectValueFactory();

    def initDefaults() {
        def dvf = defaultValueFactory;
		dvf.setProject( project );
		testClassesDir = dvf.testClassesDir;
		saveTestClassesDir = dvf.saveTestClassesDir;
		classpathWithPurrpackage = dvf.classpathWithPurrpackage;
        selectorClass = dvf.selectorClass;
		defaultsInited = true;
    }

    @TaskAction
    def instrumentTestClasses() {
		if( !defaultsInited ) {
			initDefaults();
		}
		logger.log( LogLevel.INFO, "PurrPackage will instrument ${testClassesDir} for testing, "
            + "saving originals to ${saveTestClassesDir}" );
        String cp = classpathWithPurrpackage;
        cp += ":" + project.configurations.testRuntime.asPath;
        cp += ":" + project.sourceSets.main.classesDir;
        cp += ":" + project.sourceSets.test.classesDir;
        logger.info( "Using classpath " + cp );
        def bd = project.buildDir;
        def gp = project;
        File tmpdir = new File( bd, "test-instrument-tmp" );
        tmpdir.mkdirs();
        project.ant {		
			delete(dir: saveTestClassesDir, failonerror:false);
            copy( todir: saveTestClassesDir ) {
                fileset( dir: testClassesDir )
            }
            java( classname:"net.sourceforge.purrpackage.recording.instrument.PurrPackageTestInstrumenterMain",
                fork:"true",
                failonerror:"true",
                classpath: cp ) {
                    arg( value: "${testClassesDir}" )
                    arg( value: "${tmpdir}")
                    if ( selectorClass != null && selectorClass.trim().length() > 0 ) {
                        arg( value: selectorClass )
                    }
                    arg( value: "${bd}/test-instrument.log" )
                };
            delete( dir: testClassesDir );
            copy( todir: testClassesDir) {
                fileset( dir: tmpdir );
            }
            logger.log( LogLevel.DEBUG, "PurrPackage test instrument task finished." );
        }
    }
}