package net.sourceforge.purrpackage.gradle.test

import org.testng.annotations.Test;

class SittingDuckTest {

    def sittingDuck = new SittingDuck();

    @Test
    void quackTest( ) {
       sittingDuck.quack( "quack" );    
    }

    @Test 
    void foo() {
//       throw new RuntimeException( "on purpose" );    
    }
}
