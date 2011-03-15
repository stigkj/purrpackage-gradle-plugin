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
    File saveUninstrumentedDir;
    File classesDir;
    String classpathWithCobertura;
    DefaultValueFactory defaultValueFactory = new ProjectValueFactory();

    def initDefaults() {
                def dvf = defaultValueFactory;
		dvf.setProject( project );
		coverageDataFile = dvf.coverageDataFile;
		classesDir = dvf.classesDir;
		saveUninstrumentedDir = dvf.saveUninstrumentedDir;
		classpathWithCobertura = dvf.classpathWithCobertura;
		defaultsInited = true;
    }

    @TaskAction
    def instrumentClasses() {
		if( !defaultsInited ) {
			initDefaults();
		}
		logger.log( LogLevel.INFO, "Cobertura will instrument ${classesDir} saving originals to ${saveUninstrumentedDir}" );
		logger.log( LogLevel.INFO, "Cobertura coverage data will be stored in ${coverageDataFile}");
		logger.log( LogLevel.DEBUG, "We will look for Cobertura in classpath: ${classpathWithCobertura}")

        File cobDir = coverageDataFile.parentFile;
        project.ant {		
			delete(dir: cobDir.canonicalPath, failonerror:false);
            mkdir(dir: cobDir.canonicalPath );
            taskdef(resource:'tasks.properties', classpath: classpathWithCobertura );
            delete(dir: saveUninstrumentedDir, failonerror:false);
            mkdir(dir: saveUninstrumentedDir)
            copy(todir: saveUninstrumentedDir) {
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