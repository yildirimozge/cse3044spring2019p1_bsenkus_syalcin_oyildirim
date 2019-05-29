package com.buraksergenozge.coursediary;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegexCheckerTest {

    @Test
    public void checkDatePattern() {
        assertFalse(RegexChecker.check("00-11-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("01-00-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("11-11-0000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("11-13-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("32-11-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("10-11-20000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("10-110-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("-11-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("111-11-2000", RegexChecker.datePattern));
        assertFalse(RegexChecker.check("11-2000", RegexChecker.datePattern));
    }

    @Test
    public void checkClockPattern() {
        assertTrue(RegexChecker.check("00:00", RegexChecker.clockPattern));
        assertTrue(RegexChecker.check("00:59", RegexChecker.clockPattern));
        assertTrue(RegexChecker.check("01:00", RegexChecker.clockPattern));
        assertTrue(RegexChecker.check("10:00", RegexChecker.clockPattern));
        assertTrue(RegexChecker.check("23:09", RegexChecker.clockPattern));
        assertTrue(RegexChecker.check("23:59", RegexChecker.clockPattern));
        assertFalse(RegexChecker.check("0000", RegexChecker.clockPattern));
        assertFalse(RegexChecker.check("00.00", RegexChecker.clockPattern));
        assertFalse(RegexChecker.check("30:00", RegexChecker.clockPattern));
        assertFalse(RegexChecker.check("24:00", RegexChecker.clockPattern));
        assertFalse(RegexChecker.check("00:60", RegexChecker.clockPattern));
    }

    @Test
    public void checkNumberPattern() {
        assertFalse(RegexChecker.check("asdasd", RegexChecker.numberPattern));
        assertFalse(RegexChecker.check("0.", RegexChecker.numberPattern));
        assertFalse(RegexChecker.check(".222", RegexChecker.numberPattern));
        assertFalse(RegexChecker.check("54645a45465", RegexChecker.numberPattern));
        assertTrue(RegexChecker.check("4645465", RegexChecker.numberPattern));
        assertTrue(RegexChecker.check("0", RegexChecker.numberPattern));
        assertTrue(RegexChecker.check("100000", RegexChecker.numberPattern));
    }
}