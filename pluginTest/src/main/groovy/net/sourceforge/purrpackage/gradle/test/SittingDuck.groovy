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