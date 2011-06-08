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
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.testng.TestNGOptions;
import org.gradle.api.logging.LogLevel;

class CoberturaUninstrumentTask extends DefaultTask {

	CoberturaInstrumentTask instrumentTask;

	@TaskAction
	def restoreClasses() {
		def c = instrumentTask;

		def found = [];
		if ( c == null ) {
			project.tasks.each {
				if ( it instanceof CoberturaInstrumentTask ) {
					found.add( it );
				}
			}
			if ( found.size() == 1 ) {
				c = found[0];
			}
		}
		if ( c == null ) {
			throw new NullPointerException( "instrumentTask not defined, and no (unique) CoberturaInstrumentTask found in ${found}" );
		}
		logger.log( LogLevel.DEBUG, "Restoring ${c.classesDir} from uninstrumented ${c.saveClassesDir}" );
		project.ant {
			delete(dir: c.classesDir );
			mkdir(dir: c.classesDir );
			copy(todir: c.classesDir) {
				fileset(dir: c.saveClassesDir);
			}
		}
	}
}