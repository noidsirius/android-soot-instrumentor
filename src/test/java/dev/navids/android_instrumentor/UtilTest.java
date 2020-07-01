package dev.navids.android_instrumentor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void isMethodSignature() {
        assertTrue(Util.isMethodSignature("<Test: int test()>"));
        assertTrue(Util.isMethodSignature("<Text.Test: int test(int)>"));
        assertTrue(Util.isMethodSignature("<dev.navids.android_instrumentor.Test: a.b test(dev.navids.android_instrumentor.Test)>"));
        assertFalse(Util.isMethodSignature("< a: dev.navids.android_instrumentor.Test test(dev.navids.android_instrumentor.Test)>"));
        assertFalse(Util.isMethodSignature("< : void test(int)>"));
        assertFalse(Util.isMethodSignature("<Test int test()>"));
    }

    @Test
    void isMethodSubsignature() {
        assertTrue(Util.isMethodSubsignature("int test()"));
        assertTrue(Util.isMethodSubsignature("int test(int)"));
        assertTrue(Util.isMethodSubsignature("void test(int)"));
        assertTrue(Util.isMethodSubsignature("dev.navids.android_instrumentor.Test test(dev.navids.android_instrumentor.Test)"));
        assertFalse(Util.isMethodSubsignature("dev.2navids.android_instrumentor.Test test(dev.navids.android_instrumentor.Test)"));
        assertFalse(Util.isMethodSubsignature("void test(int"));
        assertFalse(Util.isMethodSubsignature("void test"));

    }

    @Test
    void isMethodName() {
        assertTrue(Util.isMethodName("test"));
        assertTrue(Util.isMethodName("test_23s"));
        assertFalse(Util.isMethodName("test_23s.2"));
        assertFalse(Util.isMethodName("2test"));
        assertFalse(Util.isMethodName("test()"));
    }
}