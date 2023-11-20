curl -k -X POST http://do378-expense-deploy-openshift.apps-crc.testing/expense \
     -H "Content-Type: application/json" \
     -d '{"name": "Sample Expense", "paymentMethod": "CREDIT_CARD", "amount": "100.00"}'
