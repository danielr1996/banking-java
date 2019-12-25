package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Konto;
import org.kapott.hbci.callback.HBCICallback;
import org.springframework.stereotype.Service;

@Service
public interface HBCICallbackFactory {

  // FIXME: Remove rpcId
  public HBCICallback getCallBack(Konto konto, String rpcId);
}
