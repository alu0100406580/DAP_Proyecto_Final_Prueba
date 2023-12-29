import sqlite3
import io

def extract_data_from_file(file_path):
    with io.open(file_path, 'r', encoding='utf-8') as file:
        # Configuración de la conexión a la base de datos
        db_path = "database/supermarketProducts.db"
        connection = sqlite3.connect(db_path)
        connection.execute("PRAGMA foreign_keys = 1")  # Habilitar claves foráneas

        update_query = "UPDATE Products SET urlProduct=?, imageUrl=? WHERE id_by_supermarket=? AND id_supermarket=2"

        for line in file:
            # Extraer información de la línea
            id_start_index = line.find("product with id_by_supermarket") + 31
            id_end_index = line.find(":", id_start_index)
            id = line[id_start_index:id_end_index].strip()

            url_start_index = line.find("https://tienda.mercadona.es")
            url_end_index = line.find("https://prod-mercadona.imgix.net", url_start_index)
            url = line[url_start_index:url_end_index].strip()

            image_url_start_index = line.find("https://prod-mercadona.imgix.net", url_end_index)
            image_url = line[image_url_start_index:].strip()

            # Ejecutar la actualización en la base de datos
            try:
                connection.execute(update_query, (url, image_url, int(id)))
                connection.commit()
                print(f"Filas afectadas por la actualización: {connection.total_changes}")
            except Exception as e:
                print(f"Error al actualizar: {e}")

        connection.close()

if __name__ == "__main__":
    file_path = "database/mercadonaImgUrlFixed.txt"
    extract_data_from_file(file_path)
