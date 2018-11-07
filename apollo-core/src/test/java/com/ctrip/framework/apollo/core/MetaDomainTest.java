package com.ctrip.framework.apollo.core;

import com.google.common.collect.Maps;

import com.ctrip.framework.apollo.BaseIntegrationTest;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.internals.LegacyMetaServerProvider;
import com.ctrip.framework.apollo.core.spi.MetaServerProvider;

import org.junit.After;
import org.junit.Test;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MetaDomainTest extends BaseIntegrationTest {

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();
    MockMetaServerProvider.clear();
  }

  @Test
  public void testGetMetaDomain() {
    assertEquals("http://localhost:8080", MetaDomainConsts.getDomain(Env.LOCAL));
    assertEquals("http://dev:8080", MetaDomainConsts.getDomain(Env.DEV));
    assertEquals(MetaDomainConsts.DEFAULT_META_URL, MetaDomainConsts.getDomain(Env.PROD));
  }

  @Test
  public void testGetValidAddress() throws Exception {
    String someResponse = "some response";
    startServerWithHandlers(mockServerHandler(HttpServletResponse.SC_OK, someResponse));

    String validServer = " http://localhost:" + PORT + " ";
    String invalidServer = "http://localhost:" + findFreePort();

    MockMetaServerProvider.mock(Env.PRE, validServer + "," + invalidServer);
    MockMetaServerProvider.mock(Env.SIT, invalidServer + "," + validServer);

    assertEquals(validServer.trim(), MetaDomainConsts.getDomain(Env.PRE));
    assertEquals(validServer.trim(), MetaDomainConsts.getDomain(Env.SIT));
  }

  @Test
  public void testInvalidAddress() throws Exception {
    String invalidServer = "http://localhost:" + findFreePort() + " ";
    String anotherInvalidServer = "http://localhost:" + findFreePort() + " ";

    MockMetaServerProvider.mock(Env.SIT, invalidServer + "," + anotherInvalidServer);

    String metaServer = MetaDomainConsts.getDomain(Env.SIT);

    assertTrue(metaServer.equals(invalidServer.trim()) || metaServer.equals(anotherInvalidServer.trim()));
  }

  public static class MockMetaServerProvider implements MetaServerProvider {

    private static Map<Env, String> mockMetaServerAddress = Maps.newHashMap();

    private static void mock(Env env, String metaServerAddress) {
      mockMetaServerAddress.put(env, metaServerAddress);
    }

    private static void clear() {
      mockMetaServerAddress.clear();
    }

    @Override
    public String getMetaServerAddress(Env targetEnv) {
      return mockMetaServerAddress.get(targetEnv);
    }

    @Override
    public int getOrder() {
      return LegacyMetaServerProvider.ORDER - 1;// just in front of LegacyMetaServerProvider
    }
  }
}
