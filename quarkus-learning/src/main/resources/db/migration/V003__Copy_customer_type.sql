UPDATE customer
    SET customer_type = type;

ALTER TABLE customer
    ALTER COLUMN customer_type SET NOT NULL;