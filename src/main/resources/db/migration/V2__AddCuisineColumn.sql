-- Step 1: Add the new column without NOT NULL constraint
ALTER TABLE restaurant
    ADD COLUMN cuisine VARCHAR(255);


-- Step 2: Update existing rows with a default value for cuisine
UPDATE restaurant
SET cuisine = 'Unknown'
WHERE cuisine IS NULL;

-- Step 3: Add the NOT NULL constraint
ALTER TABLE restaurant
    ALTER COLUMN cuisine SET NOT NULL;