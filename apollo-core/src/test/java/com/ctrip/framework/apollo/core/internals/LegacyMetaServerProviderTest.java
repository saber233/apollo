package com.ctrip.framework.apollo.core.internals;

import com.ctrip.framework.apollo.core.enums.Env;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LegacyMetaServerProviderTest {

  @After
  public void tearDown() throws Exception {
    System.clearProperty("dev_meta");
  }

  @Test
  public void testFromPropertyFile() {
    LegacyMetaServerProvider legacyMetaServerProvider = new LegacyMetaServerProvider();
    assertEquals("http://localhost:8080", legacyMetaServerProvider.getMetaServerAddress(Env.LOCAL));
    assertEquals("http://dev:8080", legacyMetaServerProvider.getMetaServerAddress(Env.DEV));
    assertEquals(null, legacyMetaServerProvider.getMetaServerAddress(Env.PROD));
  }

  @Test
  public void testWithSystemProperty() throws Exception {
    String someDevMetaAddress = "someMetaAddress";
    String someFatMetaAddress = "someFatMetaAddress";
    System.setProperty("dev_meta", someDevMetaAddress);
    System.setProperty("sit_meta", someFatMetaAddress);

    LegacyMetaServerProvider legacyMetaServerProvider = new LegacyMetaServerProvider();

    assertEquals(someDevMetaAddress, legacyMetaServerProvider.getMetaServerAddress(Env.DEV));
    assertEquals(someFatMetaAddress, legacyMetaServerProvider.getMetaServerAddress(Env.SIT));
  }
}
