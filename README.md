# Credit-Union-Bank
Tech Used: Spring Framework and MySQL Database

-> It is regular kind of banking app. Where a Customer can request for opening a new account with his basic detials, any document type(name),
  and document number, contact number, password, maturity date and amount(in case of Certificate of Deposit type of account) and type of account.

-> These details will be saved in the database as the pending request.

-> If the admin approves then a new account will be created and the reqeust will be marked as approved and the admin whoever approves the account in his profile number of opened account will be increase by 1(total number of account created by the admin) else no account will get create and the request will be marked as rejected.

-> Once the account is created then the Customer can log in with the account number and password (which he fill while filling the form for account opening).
If he/she successfully authenticate himself/herself with the credentials then he can perform transactonal operations like(withdraw, deposit as well as transfer).

There can be Two types of User
  1. Admin (Bank Manger or Employee): who has the authority to get the lists of accounts, transactions, pending requests, approve or reject account opening requests and         some more.
  2. Customer: 
      A customer can have 3 types of account 
      2.1 Consumer: 
      2.2 Commercial
      2.3 Certificate of Deposit
