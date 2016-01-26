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
INSERT INTO SEGT_ROLES(ID_APLICACION,NOMBRE,DESCRIPCION) VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION' ),'CLIENT_APPLICATION','ROL ESPECIAL RESERVADO PARA CONCEDER ACCESO A LAS DIFERENTES APLICACIONES CLIENTES DE ORION');
INSERT INTO SEGT_ROLES(ID_APLICACION,NOMBRE,DESCRIPCION) VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'GESTPYMESOC' ),'SUPERADMIN','ROL ESPECIAL RESERVADO POR LA APLICACION PARA ASIGNAR AL USUARIO QUE TIENE ACCESO SIN RESTRICCIONES A TODAS LAS OPCIONES');

/**************************************************************************************************************************************
 * Insercion de datos semilla en la tabla SEGT_USUARIOS
 **************************************************************************************************************************************/
INSERT INTO SEGT_USUARIOS(LOGIN_ALIAS,PASSWORD,PASSWORD_SALT,COD_TIPO_IDENTIFICACION,NUM_IDENTIFICACION,
NOMBRE_COMPLETO,FEC_ULTIMO_INGRESO,IP_ULTIMO_INGRESO,COD_ISO_IDIOMA,
REINTENTOS_FALLIDOS,ES_BLOQUEADO_REINTENTOS,FEC_BLOQUEO,ES_ACTIVO)
VALUES('superadmin','ae454a608ba2bbcea43da001c640633a0ce14fc672de35b1c409f9a2164688f6','53d3eb556755fc2642d3659195c3b1c9','CC','1047386637',
'ALEXANDER CASTRO OLIVO',NULL,NULL,'es',
NULL,'N',NULL,'S');

INSERT INTO SEGT_USUARIOS(LOGIN_ALIAS,PASSWORD,PASSWORD_SALT,COD_TIPO_IDENTIFICACION,NUM_IDENTIFICACION,
NOMBRE_COMPLETO,FEC_ULTIMO_INGRESO,IP_ULTIMO_INGRESO,COD_ISO_IDIOMA,
REINTENTOS_FALLIDOS,ES_BLOQUEADO_REINTENTOS,FEC_BLOQUEO,ES_ACTIVO)
VALUES('gestpymesoc','ae454a608ba2bbcea43da001c640633a0ce14fc672de35b1c409f9a2164688f6','53d3eb556755fc2642d3659195c3b1c9',NULL,NULL,
'GESTPYMESOC (APLICACION CLIENTE)',NULL,NULL,'es',
NULL,'N',NULL,'S');

/**************************************************************************************************************************************
 * Insercion de datos semilla en la tabla SEGT_ROLES_X_USUARIO
 **************************************************************************************************************************************/
INSERT INTO SEGT_ROLES_X_USUARIO(ID_USUARIO,ID_ROL) VALUES((SELECT ID_USUARIO FROM SEGT_USUARIOS WHERE LOGIN_ALIAS='superadmin'),(SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='SUPERADMIN' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
INSERT INTO SEGT_ROLES_X_USUARIO(ID_USUARIO,ID_ROL) VALUES((SELECT ID_USUARIO FROM SEGT_USUARIOS WHERE LOGIN_ALIAS='gestpymesoc'),(SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
INSERT INTO SEGT_ROLES_X_USUARIO(ID_USUARIO,ID_ROL) VALUES((SELECT ID_USUARIO FROM SEGT_USUARIOS WHERE LOGIN_ALIAS='superadmin'),(SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='SUPERADMIN' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'GESTPYMESOC')));
 
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/services/security/usercredentials','FIND USER CREDENTIALS BY LOGIN ALIAS', 'FIND USER CREDENTIALS BY LOGIN ALIAS');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/services/security/usercredentials' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/services/security/saveauthentication','FIND USER CREDENTIALS BY LOGIN ALIAS', 'saveauthentication');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/services/security/saveauthentication' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/services/security/invalidatesession','FIND USER CREDENTIALS BY LOGIN ALIAS', 'invalidatesession');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/services/security/invalidatesession' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/services/security/accesstokenvalidity','FIND USER CREDENTIALS BY LOGIN ALIAS', 'accesstokenvalidity');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/services/security/accesstokenvalidity' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/services/security/userroles','FIND USER CREDENTIALS BY LOGIN ALIAS', 'userroles');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/services/security/userroles' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/services/security/roleresources','FIND USER CREDENTIALS BY LOGIN ALIAS', 'roleresources');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='CLIENT_APPLICATION' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/services/security/roleresources' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
		
INSERT INTO SEGT_RECURSOS(ID_APLICACION,ID_MODULO,RUTA_RECURSO,NOMBRE,DESCRIPCION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'/admin/modules','Administraci\u00f3n de Modulos', 'Administraci\u00f3n de Modulos');

INSERT INTO SEGT_RECURSOS_X_ROLES(ID_ROL,ID_RECURSO)
VALUES((SELECT ID_ROL FROM SEGT_ROLES WHERE NOMBRE='SUPERADMIN' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
		(SELECT ID_RECURSO FROM SEGT_RECURSOS WHERE RUTA_RECURSO='/admin/modules' AND ID_APLICACION =(SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')));
		
INSERT INTO SEGT_MODULOS(ID_APLICACION,ID_MODULO_PADRE,NOMBRE,DESCRIPCION,POSICION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'Administracion', 'Modulo con opciones de administracion de maestras de seguridad',NULL);

INSERT INTO SEGT_MODULOS(ID_APLICACION,ID_MODULO_PADRE,NOMBRE,DESCRIPCION,POSICION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),NULL,'Informes', 'Informes varios de seguridad',NULL);

INSERT INTO SEGT_MODULOS(ID_APLICACION,ID_MODULO_PADRE,NOMBRE,DESCRIPCION,POSICION)
VALUES((SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION'),
(SELECT ID_MODULO FROM SEGT_MODULOS WHERE NOMBRE = 'Administracion' AND ID_MODULO_PADRE IS NULL AND ID_APLICACION = (SELECT ID_APLICACION FROM SEGT_APLICACIONES WHERE COD_APLICACION = 'ORION')),
'Otras', 'Tras opciones de administracion',NULL);



INSERT INTO SEGT_DUAL VALUES('X');
