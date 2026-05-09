ALTER TABLE order_items ADD COLUMN source       VARCHAR(50);
ALTER TABLE order_items ADD COLUMN product_name VARCHAR(255);
ALTER TABLE order_items MODIFY COLUMN catalogue_item_id BIGINT NULL;
