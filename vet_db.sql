
-- 1. CREACIÓN DEL ESQUEMA
CREATE SCHEMA IF NOT EXISTS veterinaria_app;

-- Establecemos el esquema para que todo lo que sigue se cree dentro de él
SET search_path TO veterinaria_app;

-- 2. DEFINICIÓN DE TIPOS ENUM
CREATE TYPE tipo_genero AS ENUM ('MASCULINO', 'FEMENINO', 'NO_ESPECIFICADO');
CREATE TYPE tipo_rol AS ENUM ('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE');
CREATE TYPE tipo_especialidad AS ENUM (
    'MEDICINA_GENERAL', 'CIRUGIA', 'DERMATOLOGIA', 'CARDIOLOGIA', 
    'OFTALMOLOGIA', 'NUTRICION', 'ODONTOLOGIA', 'URGENCIAS'
);
CREATE TYPE tipo_especie AS ENUM ('CANINO', 'FELINO', 'AVE', 'REPTIL', 'ROEDOR', 'OTRO');
CREATE TYPE tipo_sexo_mascota AS ENUM ('MACHO', 'HEMBRA');
CREATE TYPE tipo_dia_semana AS ENUM ('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO');
CREATE TYPE tipo_estado_cita AS ENUM ('PENDIENTE', 'CONFIRMADA', 'PAGADA', 'COMPLETADA', 'CANCELADA');
CREATE TYPE tipo_estado_vacuna AS ENUM ('APLICADA', 'PROGRAMADA', 'VENCIDA');

-- 3. TABLAS DE USUARIO Y PERSONAL
CREATE TABLE usuario (
    id_usuario VARCHAR(128) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    celular VARCHAR(15),
    dni VARCHAR(8) NOT NULL UNIQUE,
    foto_url VARCHAR(255),
    genero tipo_genero,
    rol tipo_rol NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE veterinario (
    id_veterinario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario VARCHAR(128) NOT NULL UNIQUE,
    num_colegiatura VARCHAR(20) NOT NULL UNIQUE,
    especialidad tipo_especialidad NOT NULL,
    CONSTRAINT fk_usuario_veterinario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- 4. GESTIÓN DE PACIENTES Y AGENDA
CREATE TABLE mascota (
    id_mascota BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_cliente VARCHAR(128) NOT NULL,
    nombre_mascota VARCHAR(255) NOT NULL,
    especie tipo_especie NOT NULL,
    raza VARCHAR(255) NOT NULL,
    sexo tipo_sexo_mascota NOT NULL,
    fecha_nacimiento DATE,
    peso_actual DECIMAL(5, 2) NOT NULL,
    foto_url VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_usuario_mascota FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE horario_atencion (
    id_horario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_veterinario BIGINT NOT NULL,
    dia_semana tipo_dia_semana NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
	activo BOOLEAN DEFAULT TRUE,
    duracion_minutos INTEGER DEFAULT 30,
    CONSTRAINT fk_veterinario_horario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario) ON DELETE CASCADE,
    UNIQUE(id_veterinario, dia_semana)
);

CREATE TABLE cita (
    id_cita BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_mascota BIGINT NOT NULL,
    id_veterinario BIGINT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,        
    motivo VARCHAR(255),
    estado tipo_estado_cita DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,
    CONSTRAINT fk_cita_vet FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario)
);

-- 5. SALUD Y TRATAMIENTOS
CREATE TABLE vacuna (
    id_vacuna BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_vacuna VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE vacuna_aplicada (
    id_aplicacion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_mascota BIGINT NOT NULL,
    id_vacuna BIGINT NOT NULL,
    id_consulta BIGINT,
    fecha_aplicacion DATE,
    proxima_dosis DATE,
    nro_dosis INTEGER DEFAULT 1,
    estado tipo_estado_vacuna DEFAULT 'APLICADA',
    observaciones TEXT,
    CONSTRAINT fk_vacuna_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,
    CONSTRAINT fk_vacuna_catalogo FOREIGN KEY (id_vacuna) REFERENCES vacuna(id_vacuna),
    CONSTRAINT fk_vacuna_consulta FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta) ON DELETE SET NULL
);

CREATE TABLE consulta (
    id_consulta BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_cita BIGINT NOT NULL UNIQUE,
    peso_actual DECIMAL(5,2),
    temperatura DECIMAL(4,2),
    diagnostico TEXT NOT NULL,
    recomendaciones TEXT,
    CONSTRAINT fk_consulta_cita FOREIGN KEY (id_cita) REFERENCES cita(id_cita) ON DELETE CASCADE
);

CREATE TABLE tratamiento_medicamento (
    id_tratamiento BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_consulta BIGINT NOT NULL,
    nombre_medicamento VARCHAR(100) NOT NULL,
    dosis VARCHAR(100),
    frecuencia VARCHAR(100),
    duracion_dias INTEGER,
    CONSTRAINT fk_tratamiento_consulta FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta) ON DELETE CASCADE
);

CREATE TABLE log_sistema (
    id_log BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario VARCHAR(128),
    tabla_afectada VARCHAR(50) NOT NULL, 
    accion VARCHAR(20) NOT NULL, 
    descripcion TEXT, 
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_usuario FOREIGN KEY (id_usuario) 
        REFERENCES usuario(id_usuario) 
        ON DELETE SET NULL
);

-- 6. ÍNDICES ESTRATÉGICOS
-- Optimiza la búsqueda de slots disponibles por fecha y veterinario
CREATE INDEX idx_cita_fecha_vet ON cita(fecha, id_veterinario);

-- Optimiza la carga de las mascotas de un cliente (Login/Home en el iPhone)
CREATE INDEX idx_mascota_cliente ON mascota(id_cliente);

-- Optimiza la generación del carnet de vacunación por mascota
CREATE INDEX idx_vacuna_aplicada_mascota ON vacuna_aplicada(id_mascota);

-- Optimiza la búsqueda de citas por estado (para el panel del administrador)
CREATE INDEX idx_cita_estado ON cita(estado);

-- Índice para que el administrador pueda ver los logs rápido por fecha
CREATE INDEX idx_log_fecha ON log_sistema(fecha_registro);

select * from veterinaria_app.usuario;

select * from veterinaria_app.vacuna;

select * from veterinaria_app.veterinario;

select * from veterinaria_app.mascota;

select * from veterinaria_app.horario_atencion;

select * from veterinaria_app.cita;

select * from veterinaria_app.consulta;

select * from veterinaria_app.vacuna_aplicada;

select * from veterinaria_app.tratamiento_medicamento;