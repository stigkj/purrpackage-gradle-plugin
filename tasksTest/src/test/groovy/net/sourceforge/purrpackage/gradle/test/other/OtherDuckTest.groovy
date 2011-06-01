package net.sourceforge.purrpackage.gradle.test.other

import org.junit.Test;
import net.sourceforge.purrpackage.gradle.test.SittingDuck;

// JUnit test, and in the "wrong" package for SittingDuck

class OtherDuckTest {

    def sittingDuck = new SittingDuck();

    @Test public void testWaddle( ) {
       sittingDuck.waddle( ); 
    }

}
