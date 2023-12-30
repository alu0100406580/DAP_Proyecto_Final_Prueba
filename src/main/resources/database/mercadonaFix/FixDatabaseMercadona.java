package database.mercadonaFix;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FixDatabaseMercadona {

    private final String databasePath = "database/supermarketProducts.db";

    public static void main(String[] args) {
        try {
            String filePath = "database/mercadonaFix/mercadonaImgUrlFixed.txt";
            extractDataFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractDataFromFile(String filePath) throws IOException {

        ClassLoader classLoader = FixDatabaseMercadona.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                // El recurso no se encontró
                throw new IOException("No se pudo encontrar el archivo en el recurso: " + filePath);
            }

            // Configuración de la conexión a la base de datos
            String jdbcUrl = "jdbc:sqlite::resource:database/supermarketProducts.db";
            try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
                connection.setAutoCommit(true);
                String updateQuery = "UPDATE Products SET urlProduct=?, imageUrl=? WHERE id_by_supermarket=? AND id_supermarket=2";

                String line;
                while ((line = reader.readLine()) != null) {
                    // Extraer información de la línea
                    int idStartIndex = line.indexOf("product with id_by_supermarket") + 31;
                    int idEndIndex = line.indexOf(":", idStartIndex);
                    String id = line.substring(idStartIndex, idEndIndex).trim();

                    int urlStartIndex = line.indexOf("https://tienda.mercadona.es");
                    int urlEndIndex = line.indexOf("https://prod-mercadona.imgix.net", urlStartIndex);
                    String url = line.substring(urlStartIndex, urlEndIndex).trim();

                    int imageUrlStartIndex = line.indexOf("https://prod-mercadona.imgix.net", urlEndIndex);
                    String imageUrl = line.substring(imageUrlStartIndex).trim();

                    // Ejecutar la actualización en la base de datos
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        // Convertir las cadenas a bytes utilizando la codificación UTF-8
                        byte[] urlBytes = url.getBytes(StandardCharsets.UTF_8);
                        byte[] imageUrlBytes = imageUrl.getBytes(StandardCharsets.UTF_8);

                        // Establecer los parámetros como bytes
                        preparedStatement.setBytes(1, urlBytes);
                        preparedStatement.setBytes(2, imageUrlBytes);
                        preparedStatement.setInt(3, Integer.parseInt(id));

                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Filas afectadas por la actualización: " + rowsAffected);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
