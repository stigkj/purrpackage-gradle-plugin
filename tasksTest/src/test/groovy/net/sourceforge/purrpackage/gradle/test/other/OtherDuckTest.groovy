package net.sourceforge.purrpackage.gradle.test.other

import junit.framework.*;
import net.sourceforge.purrpackage.gradle.test.SittingDuck;

// JUnit test, and in the "wrong" package for SittingDuck

class OtherDuckTest extends TestCase {

    def sittingDuck = new SittingDuck();

    public void testWaddle( ) {
       sittingDuck.waddle( ); 
    }

}
