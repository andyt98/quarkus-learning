CREATE TABLE customer (
                          uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          type VARCHAR(255) NOT NULL
);
