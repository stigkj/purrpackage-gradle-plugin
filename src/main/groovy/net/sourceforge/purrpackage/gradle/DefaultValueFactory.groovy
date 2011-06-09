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

import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

interface DefaultValueFactory {
	
	void setProject( Project project );
		
	File getCoverageDataFile();

	File getClassesDir();

	File getSaveClassesDir();
    
    File getTestClassesDir();
    
    File getSaveTestClassesDir();
    
    String getSelectorClass();

	String getClasspathWithCobertura();
	
    String getClasspathWithPurrpackage();
    
	File getPurrpackageReportDir();

	Collection getSourceDirs();
	
	String getCoveragePolicy();
	
	String getBuildTimeReportScript();
	
}