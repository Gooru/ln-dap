package org.gooru.dap.processors;

public enum ExecutionStatus {

    SUCCESSFUL(true), FAILED(false);

    private boolean status;

    ExecutionStatus(boolean status) {
        this.status = status;
    }

    public boolean isSuccessFul() {
        return this.status;
    }
}
