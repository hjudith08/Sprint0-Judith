-- --------------------------------------------------------------
-- Fichero: crear_tabla.sql
-- Descripción: Crea la tabla Mediciones utilizada en el Sprint 0.
-- Autora: Judith Civico Aporta
-- --------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Mediciones (
    id INT NOT NULL AUTO_INCREMENT,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(50) NOT NULL,
    valor DOUBLE NOT NULL,
    PRIMARY KEY (id)
);
