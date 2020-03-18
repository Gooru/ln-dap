package org.gooru.dap.processors.events;

import org.gooru.dap.processors.Processor;
import org.gooru.dap.processors.ProcessorContext;

public abstract class AbstractEventProcessor implements Processor {
  protected final ProcessorContext context;

  protected AbstractEventProcessor(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public void process() {
    processEvent();
  }

  protected abstract void processEvent();
}
