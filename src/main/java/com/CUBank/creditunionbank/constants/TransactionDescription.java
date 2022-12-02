package com.CUBank.creditunionbank.constants;

import lombok.Data;

@Data
public abstract class TransactionDescription {
    public static final String BALANCE_NOT_AVAILABLE = "Balance not available";
    public static final String MINIMUM_REQ_BAL = "Minimum require balance";
    public static final String RECEIVER_ACC_NOT_AVAILABLE = "Invalid Receiver account";
    public static final String DEACTIVATE_ACCOUNT = "Account is deactivate please contact branch manager";
    public static final String TRANSFER_TO_SAME_ACC = "Sender and Receiver Cannot be same";

    public static final String COD_WITHDRAW_SUCCESS = "Certificate of Deposit Withdrawal Success";
    public static final String COD_WITHDRAW_BEFORE_MATURITY = "Certificate of Deposit Withdrawal before Maturity Success";
    public static final String COD_WITHDRAW_FAIL = "Certificate of Deposit Withdrawal Fail";
}
