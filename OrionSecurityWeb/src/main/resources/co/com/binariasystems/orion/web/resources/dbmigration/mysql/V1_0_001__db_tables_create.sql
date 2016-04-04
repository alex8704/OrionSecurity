/********************************************************************************************
*						GestPymeSOC - DataBase Schemas and Objects Script
* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* Prefijo MAT_, para tablas maestras que apliquen para cualquier aplicacion ademas de GestPymeSOC,
* Prefijo GPT_, para tablas del modelo propio de GestPymeSOC
* Prefijo SEGT_, paratablas del modelo de Seguridad Centralizado
* ----------------------------------------------------------------------------------------------------------------------------
* Usar el prefijo ID_, concatenado con el sufijo de la tabla, para columnas de tipo Clave primaria numerica
* Ejemplo: Tabla MAT_PAISES, tiene el sufijo "PAISES" por tanto la columna de clave primaria se llama ID_PAIS
* ----------------------------------------------------------------------------------------------------------------------------
* Para columnas que representen codigos estandar establecidos por entidades gubernamentales o como codificacion
* propia definida internamente, se usa el prefijo COD_
* ----------------------------------------------------------------------------------------------------------------------------
* Para columnas que hacen referencia a valores cuantitativos, por ejemplo Valor de compra, se usa el prefijo
* VLR_, seguido del sufijo correspondiente
*********************************************************************************************/

CREATE TABLE ORION.SEGT_APLICACIONES(
	ID_APLICACION INTEGER NOT NULL AUTO_INCREMENT,
	COD_APLICACION VARCHAR(64) NOT NULL,
	NOMBRE VARCHAR(128) NOT NULL,
	DESCRIPCION VARCHAR(256),
	CONSTRAINT APLICACIONES_PK PRIMARY KEY (ID_APLICACION)
) ENGINE = InnoDB;

CREATE TABLE ORION.SEGT_MODULOS(
	ID_MODULO INTEGER NOT NULL AUTO_INCREMENT,
	ID_APLICACION INTEGER NOT NULL,
	ID_MODULO_PADRE INTEGER,
	NOMBRE VARCHAR(128) NOT NULL,
	DESCRIPCION VARCHAR(256),
	POSICION INTEGER,
	CONSTRAINT MODULOS_PK PRIMARY KEY (ID_MODULO)
) ENGINE = InnoDB;

CREATE TABLE ORION.SEGT_RECURSOS(
	ID_RECURSO INTEGER NOT NULL AUTO_INCREMENT,
	ID_APLICACION INTEGER NOT NULL,
	ID_MODULO INTEGER,
	RUTA_RECURSO VARCHAR(256) NOT NULL,
	NOMBRE  VARCHAR(128) NOT NULL,
	DESCRIPCION VARCHAR(256),
	POSICION INTEGER,
	CONSTRAINT RECURSOS_PK PRIMARY KEY (ID_RECURSO)
) ENGINE = InnoDB;

CREATE TABLE ORION.SEGT_ROLES(
	ID_ROL INTEGER NOT NULL AUTO_INCREMENT,
	ID_APLICACION INTEGER NOT NULL,
	NOMBRE VARCHAR(45) NOT NULL,
	DESCRIPCION VARCHAR(256),
	CONSTRAINT ROLES_PK PRIMARY KEY (ID_ROL)
) ENGINE = InnoDB;

CREATE TABLE ORION.SEGT_RECURSOS_X_ROLES(
	ID_ROL INTEGER,
	ID_RECURSO INTEGER,
	CONSTRAINT RECURSOS_X_ROLES_PK PRIMARY KEY (ID_ROL, ID_RECURSO)
) ENGINE = InnoDB;

CREATE TABLE ORION.SEGT_USUARIOS(
	ID_USUARIO INTEGER NOT NULL AUTO_INCREMENT,
	LOGIN_ALIAS VARCHAR(30) NOT NULL,
	PASSWORD VARCHAR(128) NOT NULL,
	PASSWORD_SALT VARCHAR(128),
	NUM_IDENTIFICACION VARCHAR(15),
	NOMBRE_COMPLETO VARCHAR(128) NOT NULL,
	FEC_ULTIMO_INGRESO TIMESTAMP,
	IP_ULTIMO_INGRESO VARCHAR(64),
	COD_ISO_IDIOMA VARCHAR(6),
	CORREO_ELECTRONICO VARCHAR(256),
	REINTENTOS_FALLIDOS INTEGER,
	ES_BLOQUEADO_REINTENTOS VARCHAR(1),
	FEC_BLOQUEO TIMESTAMP,
	ES_ACTIVO VARCHAR(1),
	CONSTRAINT USUARIOS_PK PRIMARY KEY (ID_USUARIO)
) ENGINE = InnoDB;

CREATE TABLE ORION.SEGT_ROLES_X_USUARIO(
	ID_USUARIO INTEGER,
	ID_ROL INTEGER,
	CONSTRAINT ROLES_X_USUARIO_PK PRIMARY KEY (ID_USUARIO,ID_ROL)
);

CREATE TABLE ORION.SEGT_TOKENS_ACCESO(
	ID_USUARIO INTEGER NOT NULL,
	ID_APLICACION INTEGER NOT NULL,
	TOKEN VARCHAR(64) NOT NULL,
	FEC_CREACION TIMESTAMP NOT NULL,
	FEC_EXPIRACION TIMESTAMP,
	ES_VIGENTE VARCHAR(1),
	CONSTRAINT TOKENS_ACCESO_PK UNIQUE(ID_USUARIO,ID_APLICACION)
) ENGINE = InnoDB;

