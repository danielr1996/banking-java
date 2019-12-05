package de.danielr1996.banking.infrastructure.fints;

import org.kapott.hbci.callback.HBCICallback;
import org.springframework.stereotype.Service;

@Service
public interface HBCICallbackFactory {

  // FIXME: Remove rpcId
  public HBCICallback getCallBack(String blz, String kontonummer, String password, String rpcId);
}
