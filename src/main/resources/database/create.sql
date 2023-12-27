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
    urlProduct TEXT,
    UNIQUE (id_by_supermarket, id_supermarket)
);

CREATE TABLE PriceHistory (
    id INTEGER PRIMARY KEY,
    productId INTEGER,
    price REAL NOT NULL,
    read_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE CASCADE
);

---- Crear un trigger que se activa antes de la inserción en la tabla PriceHistory
--CREATE TRIGGER update_or_insert_price
--BEFORE INSERT ON PriceHistory
--FOR EACH ROW
--BEGIN
--    -- Verificar si ya existe un registro para el mismo productId en el mismo día
--    SELECT id FROM PriceHistory WHERE productId = NEW.productId AND DATE(read_at) = DATE('now') LIMIT 1;
--
--    -- Si existe, actualizar el valor price y evitar la inserción del nuevo registro
--    UPDATE PriceHistory SET price = NEW.price WHERE productId = NEW.productId AND DATE(read_at) = DATE('now') AND id IS NOT NULL;
--
--    -- Si no existe, la siguiente línea no hará nada, pero es necesaria para que el trigger sea válido
--    SELECT 1;
--
--    -- Si existe, evitar la inserción del nuevo registro
--    SELECT RAISE(IGNORE);
--END;

-- Crear un trigger que se activa antes de la inserción en la tabla PriceHistory
CREATE TRIGGER update_or_insert_price
BEFORE INSERT ON PriceHistory
FOR EACH ROW
WHEN EXISTS (SELECT 1 FROM PriceHistory WHERE productId = NEW.productId AND DATE(read_at) = DATE('now'))
BEGIN
    -- Si existe, actualizar el valor price y evitar la inserción del nuevo registro
    UPDATE PriceHistory SET price = NEW.price WHERE productId = NEW.productId AND DATE(read_at) = DATE('now');
    -- Actualizar el precio del producto en la tabla Products
        UPDATE Products SET price = NEW.price WHERE id = NEW.productId;
    -- Evitar la inserción del nuevo registro
    SELECT RAISE(IGNORE);
END;