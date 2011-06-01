package net.sourceforge.purrpackage.gradle.test.other

import org.junit.Test;
import net.sourceforge.purrpackage.gradle.test.SittingDuck;

class OtherDuckJUnitTest {

    def sittingDuck = new SittingDuck();

    @Test public void testWaddle( ) {
       sittingDuck.waddle( ); 
    }

}
