package net.sourceforge.purrpackage.gradle.test.other

import junit.framework.*;
import net.sourceforge.purrpackage.gradle.test.SittingDuck;

class OtherDuckJUnitTest extends TestCase {

    def sittingDuck = new SittingDuck();

    public void testWaddle( ) {
       sittingDuck.waddle( ); 
    }

}