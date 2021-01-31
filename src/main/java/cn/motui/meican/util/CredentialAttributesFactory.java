package cn.motui.meican.util;

import com.intellij.credentialStore.CredentialAttributes;
import org.jetbrains.annotations.NotNull;

/**
 * CredentialAttributesFactory
 */
public final class CredentialAttributesFactory {

  static CredentialAttributes create(@NotNull String serviceName, String userName) {
    return new CredentialAttributes(serviceName, userName);
  }
}
