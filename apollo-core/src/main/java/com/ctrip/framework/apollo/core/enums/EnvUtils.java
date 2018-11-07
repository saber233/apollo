package com.ctrip.framework.apollo.core.enums;

import com.ctrip.framework.apollo.core.utils.StringUtils;

public final class EnvUtils {
  
  public static Env transformEnv(String envName) {
    if (StringUtils.isBlank(envName)) {
      return Env.UNKNOWN;
    }
    switch (envName.trim().toUpperCase()) {
      case "PRE":
        return Env.PRE;
      case "SIT":
        return Env.SIT;
      case "PRO":
      case "PROD": //just in case
        return Env.PROD;
      case "DEV":
        return Env.DEV;
      case "LOCAL":
        return Env.LOCAL;
      default:
        return Env.UNKNOWN;
    }
  }
}
