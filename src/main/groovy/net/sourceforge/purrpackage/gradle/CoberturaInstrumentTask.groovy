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

class CoberturaInstrumentTask extends DefaultTask {

    boolean defaultsInited = false;
    File coverageDataFile;
    File saveClassesDir;
    File classesDir;
    String classpathWithCobertura;
    DefaultValueFactory defaultValueFactory = new ProjectValueFactory();

    def initDefaults() {
                def dvf = defaultValueFactory;
		dvf.setProject( project );
		coverageDataFile = dvf.coverageDataFile;
		classesDir = dvf.classesDir;
		saveClassesDir = dvf.saveClassesDir;
		classpathWithCobertura = dvf.classpathWithCobertura;
		defaultsInited = true;
    }

    @TaskAction
    def instrumentClasses() {
		if( !defaultsInited ) {
			initDefaults();
		}
		logger.log( LogLevel.INFO, "Cobertura will instrument ${classesDir} saving originals to ${saveClassesDir}" );
		logger.log( LogLevel.INFO, "Cobertura coverage data will be stored in ${coverageDataFile}");
		logger.log( LogLevel.DEBUG, "We will look for Cobertura in classpath: ${classpathWithCobertura}")

        File cobDir = coverageDataFile.parentFile;
        project.ant {		
			delete(dir: cobDir.canonicalPath, failonerror:false);
            mkdir(dir: cobDir.canonicalPath );
            taskdef(resource:'tasks.properties', classpath: classpathWithCobertura );
            delete(dir: saveClassesDir, failonerror:false);
            mkdir(dir: saveClassesDir)
            copy(todir: saveClassesDir) {
                fileset(dir: classesDir);
            }
            'cobertura-instrument'(datafile: "${coverageDataFile.canonicalPath}") {
                fileset(dir: classesDir,
                        includes:"**" + "/*.class" );
            }
            logger.log( LogLevel.DEBUG, "CoberturaInstrumentTask finished." );
        }
    }
}