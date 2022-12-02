package com.CUBank.creditunionbank.constants;

public abstract class ErrorCode {
    public static final String INTERNAL_SERVER_ERROR = "ERR-G-000";
    // Errors related to Account Opening
    public static final String DUPLICATE_ACCOUNT = "ERR-ACCOUNT-OPENING-001";
    public static final String INVALID_ACCOUNT_OPENING_COMMAND = "ERR-ACCOUNT-OPENING-002";
    // Error related to Authentication
    public static final String INVALID_CREDENTIALS = "ERR-USER-001";

    // Error with Account Numbers
    public static final String ACCOUNT_NUMBER_NOT_FOUND = "ERR-ACCOUNT-001";
//    public static final String CONSUMER_ACCOUNT_NUMBER_NOT_FOUND = "ERR-ACCOUNT-CONSUMER-004";

    // Error related to Money Market Account
    public static final String MONEY_MARKET_UNKNOWN_ERROR = "ERR-ACCOUNT-MONEY-MARKET-001";
    public static final String MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND = "ERR-ACCOUNT-MONEY-MARKET-002";

    // Error related to Amount

    // Error related to Certificate of Deposit Account
    public static final String COD_ACCOUNT_NUMBER_NOT_FOUND = "ERR-ACCOUNT-COD-001";

    // Error related to Account Type
    public static final String INVALID_ACCOUNT_TYPE = "ERR-ACCOUNT-TYPE-001";


    // Error related to transaction
    public static final String NOT_SUFFICIENT_BALANCE = "ERR-TRANSACTION-001";
    public static final String MIN_BALANCE_REQUIREMENT = "ERR-TRANSACTION-002";
    public static final String INVALID_AMOUNT = "ERR-TRANSACTION-003";
    public static final String SAME_SENDER_AND_RECEIVER = "ERR-TRANSACTION-004";

}
