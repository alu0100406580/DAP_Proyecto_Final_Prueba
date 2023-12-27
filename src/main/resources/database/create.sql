CREATE TABLE Supermarkets (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE Products (
    id INTEGER PRIMARY KEY,
    id_by_supermarket INTEGER NOT NULL,
    id_supermarket INTEGER NOT NULL,
    name TEXT NOT NULL,
    price REAL NOT NULL,
    imageUrl TEXT,
    urlProduct TEXT
);

CREATE TABLE PriceHistory (
    id INTEGER PRIMARY KEY,
    productId INTEGER,
    price REAL NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE CASCADE
);