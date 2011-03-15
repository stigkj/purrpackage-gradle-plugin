package net.sourceforge.purrpackage.gradle.test.other

import junit.framework.*;
import net.sourceforge.purrpackage.gradle.test.SittingDuck;

class OtherDuckTest extends TestCase {

    def sittingDuck = new SittingDuck();

    public void testWaddle( ) {
       throw new Exception( "This is supposed to be ignored by both frameworks." );
    }

}
