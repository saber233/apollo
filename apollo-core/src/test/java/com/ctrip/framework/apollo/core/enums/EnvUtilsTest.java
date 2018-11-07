package com.ctrip.framework.apollo.core.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnvUtilsTest {

  @Test
  public void testTransformEnv() throws Exception {
    assertEquals(Env.DEV, EnvUtils.transformEnv(Env.DEV.name()));
    assertEquals(Env.UNKNOWN, EnvUtils.transformEnv("someInvalidEnv"));
  }

  @Test
  public void testFromString() throws Exception {
    assertEquals(Env.DEV, Env.fromString(Env.DEV.name()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromInvalidString() throws Exception {
    Env.fromString("someInvalidEnv");
  }
}
