/********************************************************************************************
*					GestPymeSOC - DataBase Constraints Creation
* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* Convencion de Nombramiento de constraints
* [ABREVIACION_TABLA]_[ABREVIACION_CAMPOS_IMPLICADOS]_[SUFIJO]
*
* Sufijos Validos:
* FOREIGN KEY:		_FK
* UNIQUE:			_UK
* CHECK:			_CK
* PRIMARY KEY:		_PK
*
* Ejemplo: Tabla SGT_USUARIOS tiene un campo llamado LOGIN_ALIAS que debe ser UNICO
*
* El constraint se llamaria asi:
* ABREVIACION TABLA: USRS
* ABREVIACION_CAMPOS_IMPLICADOS: LOGIN
* SUFIJO: _UK
*
* Resultado: USRS_LOGIN_UK
*
*********************************************************************************************/

/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_APLICACIONES
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_APLICACIONES ADD CONSTRAINT APLICACIONES_COD_UK UNIQUE (COD_APLICACION);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_MODULOS
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_MODULOS ADD CONSTRAINT MODULOS_APLICACION_FK FOREIGN KEY (ID_APLICACION) REFERENCES ORION.SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_MODULOS ADD CONSTRAINT MODULOS_PADRE_FK FOREIGN KEY (ID_MODULO_PADRE) REFERENCES ORION.SEGT_MODULOS (ID_MODULO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_MODULOS ADD CONSTRAINT MODULOS_PADRE_CK CHECK (ID_MODULO_PADRE <> ID_MODULO);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_RECURSOS
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_RECURSOS ADD CONSTRAINT RECURS_APLICACION_FK FOREIGN KEY (ID_APLICACION) REFERENCES ORION.SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_RECURSOS ADD CONSTRAINT RECURS_MODULO_FK FOREIGN KEY (ID_MODULO) REFERENCES ORION.SEGT_MODULOS (ID_MODULO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_RECURSOS ADD CONSTRAINT RECURS_RUTA_UK UNIQUE (RUTA_RECURSO, ID_APLICACION);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_ROLES
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_ROLES ADD CONSTRAINT ROLES_APLICACION_FK FOREIGN KEY (ID_APLICACION) REFERENCES ORION.SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_ROLES ADD CONSTRAINT ROLES_NOMBRE_UK UNIQUE (NOMBRE, ID_APLICACION);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_RECURSOS_X_ROLES
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_RECURSOS_X_ROLES ADD CONSTRAINT RECXROL_ROL_FK FOREIGN KEY (ID_ROL) REFERENCES ORION.SEGT_ROLES (ID_ROL) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_RECURSOS_X_ROLES ADD CONSTRAINT RECXROL_RECURSO_FK FOREIGN KEY (ID_RECURSO) REFERENCES ORION.SEGT_RECURSOS (ID_RECURSO) ON UPDATE NO ACTION ON DELETE NO ACTION;


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_USUARIOS
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_USUARIOS ADD CONSTRAINT USR_LOGIN_UK UNIQUE (LOGIN_ALIAS);
ALTER TABLE ORION.SEGT_USUARIOS ADD CONSTRAINT USR_ACTIVO_CK CHECK (ES_ACTIVO IN('N','S'));
ALTER TABLE ORION.SEGT_USUARIOS ADD CONSTRAINT USR_ESBLOQU_CK CHECK (ES_BLOQUEADO_REINTENTOS IN('N','S'));


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_ROLES_X_USUARIO
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_ROLES_X_USUARIO ADD CONSTRAINT ROLXUSR_USR_FK FOREIGN KEY (ID_USUARIO) REFERENCES ORION.SEGT_USUARIOS (ID_USUARIO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_ROLES_X_USUARIO ADD CONSTRAINT ROLXUSR_ROL_FK FOREIGN KEY (ID_ROL) REFERENCES ORION.SEGT_ROLES (ID_ROL) ON UPDATE NO ACTION ON DELETE NO ACTION;

/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_TOKENS_ACCESO
**************************************************************************************************************************************/
ALTER TABLE ORION.SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_USR_FK FOREIGN KEY (ID_USUARIO) REFERENCES ORION.SEGT_USUARIOS (ID_USUARIO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_APP_FK FOREIGN KEY (ID_APLICACION) REFERENCES ORION.SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ORION.SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_ESVIGENTE_CK CHECK (ES_VIGENTE IN('N','S'));
