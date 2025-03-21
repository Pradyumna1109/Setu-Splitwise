CREATE TABLE IF NOT EXISTS Expense (
    id UUID PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    type VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    group_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

ALTER TABLE Expense
ADD CONSTRAINT fk_expense_user_group FOREIGN KEY (user_id, group_id) REFERENCES USER_GROUP (user_id, group_id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX IF NOT EXISTS idx_expense_user_group ON USER_GROUP (user_id, group_id);


