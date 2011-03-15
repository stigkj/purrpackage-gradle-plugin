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

package net.sourceforge.purrpackage.gradle.test

class SittingDuck {

    def thing

    def quack( String x ) {
       println x
    }

    def waddle() {
       println "waddling."
    }

    def untestedAction() {
       println( "Missed me." )
    }
}