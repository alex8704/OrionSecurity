/**************************************************************************************************************************************
 * Insercion de datos semilla en la tabla SEGT_APLICACIONES
 **************************************************************************************************************************************/
INSERT INTO SEGT_APLICACIONES(COD_APLICACION,NOMBRE,DESCRIPCION)
VALUES('ORION','OrionSecurity','Plataforma  de Gesti\u00f3n Centralizada de Modelo de Autenticaion y Autorizacion de Usuarios');
INSERT INTO SEGT_APLICACIONES(COD_APLICACION,NOMBRE,DESCRIPCION)
VALUES('GESTPYMESOC','GestPymeSOC','Plataforma  de Gesti\u00f3n para Pequenas y Medianas empresas dedicadas a Servicios de Obra Civil y Similares');

/**************************************************************************************************************************************
 * Insercion de datos semilla en la tabla SEGT_ROLES
 **************************************************************************************************************************************/
INSERT INTO SEGT_ROLES(ID_APLICACION,NOMBRE,DESCRIPCION) VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION' ),'SUPERADMIN','ROL ESPECIAL RESERVADO POR LA APLICACION PARA ASIGNAR AL USUARIO QUE TIENE ACCESO SIN RESTRICCIONES A TODAS LAS OPCIONES');
INSERT INTO SEGT_ROLES(ID_APLICACION,NOMBRE,DESCRIPCION) VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'GESTPYMESOC' ),'SUPERADMIN','ROL ESPECIAL RESERVADO POR LA APLICACION PARA ASIGNAR AL USUARIO QUE TIENE ACCESO SIN RESTRICCIONES A TODAS LAS OPCIONES');

/**************************************************************************************************************************************
 * Insercion de datos semilla en la tabla SEGT_USUARIOS
 **************************************************************************************************************************************/
INSERT INTO SEGT_USUARIOS(LOGIN_ALIAS,PASSWORD,PASSWORD_SALT,COD_TIPO_IDENTIFICACION,NUM_IDENTIFICACION,
NOMBRE_COMPLETO,FEC_ULTIMO_INGRESO,IP_ULTIMO_INGRESO,COD_ISO_IDIOMA,
REINTENTOS_FALLIDOS,ES_BLOQUEDADO_REINTENTOS,FEC_BLOQUEO,ES_ACTIVO)
VALUES('superadmin','ae454a608ba2bbcea43da001c640633a0ce14fc672de35b1c409f9a2164688f6','53d3eb556755fc2642d3659195c3b1c9','CC','1047386637',
'ALEXANDER CASTRO OLIVO',NULL,NULL,'es',
NULL,'N',NULL,'S');

/**************************************************************************************************************************************
 * Insercion de datos semilla en la tabla SEGT_ROLES_X_USUARIO
 **************************************************************************************************************************************/
INSERT INTO SEGT_ROLES_X_USUARIO(ID_USUARIO,ID_ROL) VALUES((SELECT ID_USUARIO FROM SEGT_USUARIOS WHERE LOGIN_ALIAS='superadmin'),(SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='SUPERADMIN' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
INSERT INTO SEGT_ROLES_X_USUARIO(ID_USUARIO,ID_ROL) VALUES((SELECT ID_USUARIO FROM SEGT_USUARIOS WHERE LOGIN_ALIAS='superadmin'),(SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='SUPERADMIN' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'GESTPYMESOC')));
 
 
INSERT INTO SEGT_DUAL VALUES('X');
