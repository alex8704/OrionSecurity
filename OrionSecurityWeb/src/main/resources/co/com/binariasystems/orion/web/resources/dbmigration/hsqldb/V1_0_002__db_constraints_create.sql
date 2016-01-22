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
ALTER TABLE SEGT_APLICACIONES ADD CONSTRAINT APLICACIONES_PK PRIMARY KEY (ID_APLICACION);
ALTER TABLE SEGT_APLICACIONES ADD CONSTRAINT APLICACIONES_COD_UK UNIQUE (COD_APLICACION);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_MODULOS
**************************************************************************************************************************************/
ALTER TABLE SEGT_MODULOS ADD CONSTRAINT MODULOS_PK PRIMARY KEY (ID_MODULO);
ALTER TABLE SEGT_MODULOS ADD CONSTRAINT MODULOS_APLICACION_FK FOREIGN KEY (ID_APLICACION) REFERENCES SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_MODULOS ADD CONSTRAINT MODULOS_PADRE_FK FOREIGN KEY (ID_MODULO_PADRE) REFERENCES SEGT_MODULOS (ID_MODULO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_MODULOS ADD CONSTRAINT MODULOS_PADRE_CK CHECK (ID_MODULO_PADRE <> ID_MODULO);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_RECURSOS
**************************************************************************************************************************************/
ALTER TABLE SEGT_RECURSOS ADD CONSTRAINT RECURSOS_PK PRIMARY KEY (ID_RECURSO);
ALTER TABLE SEGT_RECURSOS ADD CONSTRAINT RECURS_APLICACION_FK FOREIGN KEY (ID_APLICACION) REFERENCES SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_RECURSOS ADD CONSTRAINT RECURS_MODULO_FK FOREIGN KEY (ID_MODULO) REFERENCES SEGT_MODULOS (ID_MODULO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_RECURSOS ADD CONSTRAINT RECURS_RUTA_UK UNIQUE (RUTA_RECURSO, ID_APLICACION);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_ROLES
**************************************************************************************************************************************/
ALTER TABLE SEGT_ROLES ADD CONSTRAINT ROLES_PK PRIMARY KEY (ID_ROL);
ALTER TABLE SEGT_ROLES ADD CONSTRAINT ROLES_APLICACION_FK FOREIGN KEY (ID_APLICACION) REFERENCES SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_ROLES ADD CONSTRAINT ROLES_NOMBRE_UK UNIQUE (NOMBRE, ID_APLICACION);


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_RECURSOS_X_ROLES
**************************************************************************************************************************************/
ALTER TABLE SEGT_RECURSOS_X_ROLES ADD CONSTRAINT RECURSOS_X_ROLES_PK PRIMARY KEY (ID_ROL, ID_RECURSO);
ALTER TABLE SEGT_RECURSOS_X_ROLES ADD CONSTRAINT RECXROL_ROL_FK FOREIGN KEY (ID_ROL) REFERENCES SEGT_ROLES (ID_ROL) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_RECURSOS_X_ROLES ADD CONSTRAINT RECXROL_RECURSO_FK FOREIGN KEY (ID_RECURSO) REFERENCES SEGT_RECURSOS (ID_RECURSO) ON UPDATE NO ACTION ON DELETE NO ACTION;


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_USUARIOS
**************************************************************************************************************************************/
ALTER TABLE SEGT_USUARIOS ADD CONSTRAINT USUARIOS_PK PRIMARY KEY (ID_USUARIO);
ALTER TABLE SEGT_USUARIOS ADD CONSTRAINT USR_LOGIN_UK UNIQUE (LOGIN_ALIAS);
ALTER TABLE SEGT_USUARIOS ADD CONSTRAINT USR_ACTIVO_CK CHECK (ES_ACTIVO IN('N','S'));
ALTER TABLE SEGT_USUARIOS ADD CONSTRAINT USR_ESBLOQU_CK CHECK (ES_BLOQUEADO_REINTENTOS IN('N','S'));


/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_ROLES_X_USUARIO
**************************************************************************************************************************************/
ALTER TABLE SEGT_ROLES_X_USUARIO ADD CONSTRAINT ROLES_X_USUARIO_PK PRIMARY KEY (ID_USUARIO,ID_ROL);
ALTER TABLE SEGT_ROLES_X_USUARIO ADD CONSTRAINT ROLXUSR_USR_FK FOREIGN KEY (ID_USUARIO) REFERENCES SEGT_USUARIOS (ID_USUARIO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_ROLES_X_USUARIO ADD CONSTRAINT ROLXUSR_ROL_FK FOREIGN KEY (ID_ROL) REFERENCES SEGT_ROLES (ID_ROL) ON UPDATE NO ACTION ON DELETE NO ACTION;

/**************************************************************************************************************************************
* Creacion de Constraints para tabla SEGT_TOKENS_ACCESO
**************************************************************************************************************************************/
ALTER TABLE SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_ACCESO_PK UNIQUE(ID_USUARIO,ID_APLICACION);
ALTER TABLE SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_USR_FK FOREIGN KEY (ID_USUARIO) REFERENCES SEGT_USUARIOS (ID_USUARIO) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_APP_FK FOREIGN KEY (ID_APLICACION) REFERENCES SEGT_APLICACIONES (ID_APLICACION) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE SEGT_TOKENS_ACCESO ADD CONSTRAINT TOKENS_ESVIGENTE_CK CHECK (ES_VIGENTE IN('N','S'));
