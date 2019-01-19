BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `product` (
	`product_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`product_name`	TEXT NOT NULL UNIQUE,
	`product_qty`	INTEGER,
	`product_gst`	REAL,
	`product_hsn`	TEXT
);
CREATE TABLE IF NOT EXISTS `customer` (
	`cust_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`cust_name`	TEXT NOT NULL,
	`cust_phone`	TEXT NOT NULL,
	`cust_address`	TEXT,
	`cust_gstn`	TEXT
);
COMMIT;
