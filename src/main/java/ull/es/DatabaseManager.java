package ull.es;

import java.sql.*;
import java.util.List;

public class DatabaseManager {

    private final String databasePath = "database/supermarketProducts.db";

    public DatabaseManager() {}

    public Connection connect() throws SQLException {
        try {
            // Obtén la ruta absoluta al archivo de la base de datos
            String absolutePath = getClass().getClassLoader().getResource(databasePath).getFile();


            // Reemplaza %20 con espacios si es necesario (problema común en rutas de archivos en URLs)
            absolutePath = absolutePath.replaceAll("%20", " ");

            // Construye la URL de conexión a la base de datos SQLite
            String url = "jdbc:sqlite:" + absolutePath;

            // Conéctate a la base de datos
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            // Imprime información detallada sobre la excepción
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void storeProductsInDatabase(List<Product> products, int idSupermarket) {
        Connection connection = null;
        try {
            connection = connect();
            connection.setAutoCommit(false); // Iniciar transacción

            String insertProductQuery = "INSERT INTO Products (id_by_supermarket, id_supermarket, name, price, imageUrl, urlProduct) VALUES (?, ?, ?, ?, ?, ?)";
            String insertPriceHistoryQuery = "INSERT INTO PriceHistory (productId, price) VALUES (?, ?)";

            try (PreparedStatement insertProductStatement = connection.prepareStatement(insertProductQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertPriceHistoryStatement = connection.prepareStatement(insertPriceHistoryQuery)) {

                for (Product product : products) {
                    // Insertar en la tabla Products
                    insertProductStatement.setInt(1, product.getId());
                    insertProductStatement.setInt(2, idSupermarket);
                    insertProductStatement.setString(3, product.getName());
                    insertProductStatement.setDouble(4, product.getPrice());
                    insertProductStatement.setString(5, product.getImageUrl());
                    insertProductStatement.setString(6, product.getUrlProduct());

                    int affectedRows = insertProductStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("La inserción del producto falló, no se generaron claves.");
                    }

                    // Consulta para obtener el ID del producto recién insertado
                    String getIdQuery = "SELECT last_insert_rowid() AS id";
                    try (PreparedStatement getIdStatement = connection.prepareStatement(getIdQuery);
                         ResultSet idResult = getIdStatement.executeQuery()) {

                        if (idResult.next()) {
                            int productId = idResult.getInt("id");

                            // Insertar en la tabla PriceHistory
                            insertPriceHistoryStatement.setInt(1, productId);
                            insertPriceHistoryStatement.setDouble(2, product.getPrice());
                            insertPriceHistoryStatement.executeUpdate();
                        } else {
                            throw new SQLException("La consulta para obtener el ID del producto falló.");
                        }
                    }
                }
                connection.commit(); // Confirmar transacción
            }
        } catch (SQLException e) {
            // En caso de error, hacer rollback
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Cerrar la conexión en el bloque finally
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}
