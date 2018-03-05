SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Indexes */

DROP INDEX REG_ID ON TB_RC_HISTORY;
DROP INDEX REQ_ID ON tb_rc_report;
DROP INDEX REPORT_ID ON tb_rc_report_item;
DROP INDEX REQ_ID ON tb_rc_target;
DROP INDEX TGT_ID ON tb_rc_target_network;



/* Drop Tables */

DROP TABLE IF EXISTS FAVOR_PLAN_LOCATION;
DROP TABLE IF EXISTS TB_RC_ACTION_INFO;
DROP TABLE IF EXISTS TB_RC_FLOOR_PLAN;
DROP TABLE IF EXISTS TB_RC_HISTORY;
DROP TABLE IF EXISTS TB_RC_PHASE_INFO;
DROP TABLE IF EXISTS tb_rc_report_item;
DROP TABLE IF EXISTS tb_rc_report;
DROP TABLE IF EXISTS tb_rc_target_network;
DROP TABLE IF EXISTS tb_rc_target;
DROP TABLE IF EXISTS TB_RC_REQUEST;
DROP TABLE IF EXISTS TB_RC_PROCESS_INFO;




/* Create Tables */

CREATE TABLE FAVOR_PLAN_LOCATION
(
	FAVOR_ID bigint NOT NULL AUTO_INCREMENT,
	REQ_ID bigint unsigned NOT NULL,
	DATACENTER_ID bigint,
	FLOOR_PLAN_ID bigint,
	ROOM_INFO_ID bigint,
	RACK_ID bigint,
	PRIMARY KEY (FAVOR_ID)
);


CREATE TABLE TB_RC_ACTION_INFO
(
	ACTION_ID bigint unsigned NOT NULL,
	PHASE_ID varchar(30) NOT NULL,
	ACTION_NAME varchar(50),
	NEXT_ID varchar(30),
	DESCRIPTION text,
	DEL_YN varchar(1) DEFAULT '''N''' NOT NULL,
	REG_DT datetime,
	UPD_DT datetime,
	DEL_DT datetime,
	PRIMARY KEY (ACTION_ID)
);


CREATE TABLE TB_RC_FLOOR_PLAN
(
	FAVOR_ID bigint NOT NULL AUTO_INCREMENT,
	REPORT_ID bigint unsigned NOT NULL,
	DATACENTER_ID bigint,
	FLOOR_PLAN_ID bigint,
	ROOM_INFO_ID bigint,
	RACK_ID bigint,
	PRIMARY KEY (FAVOR_ID)
);


CREATE TABLE TB_RC_HISTORY
(
	HIS_ID bigint unsigned NOT NULL AUTO_INCREMENT,
	REG_ID bigint unsigned NOT NULL,
	PHASE_ID varchar(30) NOT NULL,
	-- STARTED
	-- RUNNING
	-- COMPLETED
	STATE varchar(10) NOT NULL COMMENT 'STARTED
RUNNING
COMPLETED',
	-- 단계 완료 액션이름을 저장
	RESULT varchar(50) COMMENT '단계 완료 액션이름을 저장',
	-- 단계완료시 사유가 필요할 경우에 입력
	-- 예) 반려사유
	REASON text COMMENT '단계완료시 사유가 필요할 경우에 입력
예) 반려사유',
	APPROVER bigint,
	-- 현 프로세서에서 단계시작일시가 등록일시와 동일
	STT_DT datetime COMMENT '현 프로세서에서 단계시작일시가 등록일시와 동일',
	-- 완료/반려 시점
	END_DT datetime COMMENT '완료/반려 시점',
	PRIMARY KEY (HIS_ID)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE TB_RC_PHASE_INFO
(
	PHASE_ID varchar(30) NOT NULL,
	P_ID varchar(10) NOT NULL,
	PHASE_NAME varchar(50) NOT NULL,
	-- START
	-- MIDDLE
	-- END
	TYPE varchar(10) DEFAULT '''MIDDLE''' COMMENT 'START
MIDDLE
END',
	-- 삭제 플래그
	DEL_YN varchar(1) DEFAULT '''N''' NOT NULL COMMENT '삭제 플래그',
	-- 프로세서 내 단계순서
	SEQ tinyint unsigned COMMENT '프로세서 내 단계순서',
	DESCRIPTION text,
	REG_DT datetime,
	UPD_DT datetime,
	DEL_DT datetime,
	PRIMARY KEY (PHASE_ID)
);


-- 상면검토 프로세서 기본정보
CREATE TABLE TB_RC_PROCESS_INFO
(
	P_ID varchar(10) NOT NULL,
	P_NAME varchar(50) NOT NULL,
	DESCRIPTION text,
	-- 삭제 플래그
	DEL_YN varchar(1) DEFAULT '''N''' NOT NULL COMMENT '삭제 플래그',
	REG_DT datetime,
	UPD_DT datetime,
	DEL_DT datetime,
	PRIMARY KEY (P_ID)
) COMMENT = '상면검토 프로세서 기본정보';


CREATE TABLE tb_rc_report
(
	REPORT_ID bigint unsigned NOT NULL AUTO_INCREMENT,
	REQ_ID bigint unsigned NOT NULL,
	REPORT_NAME varchar(100),
	DATACENTER_ID bigint,
	MANAGER varchar(50),
	FLOOR_PLAN_ID varchar(50),
	DESCRIPTION text,
	-- Nework 회선
	CHK_ITEM_1 varchar(1000) COMMENT 'Nework 회선',
	-- 전원
	CHK_ITEM_2 varchar(1000) COMMENT '전원',
	-- 상면 공간
	CHK_ITEM_3 varchar(1000) COMMENT '상면 공간',
	CHK_ITEM_4 varchar(1000),
	-- 종합의견
	CHK_ITEM_5 varchar(1000) COMMENT '종합의견',
	REG_DT datetime,
	UPD_DT datetime,
	PRIMARY KEY (REPORT_ID)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE tb_rc_report_item
(
	ITEM_ID bigint unsigned NOT NULL AUTO_INCREMENT,
	REPORT_ID bigint unsigned NOT NULL,
	TYPE varchar(100),
	CHECK_ITEM varchar(200),
	CNC_PART varchar(60),
	CNC_USER bigint,
	CNC_COMMENT varchar(1000),
	PRIMARY KEY (ITEM_ID)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE TB_RC_REQUEST
(
	REQ_ID bigint unsigned NOT NULL AUTO_INCREMENT,
	P_ID varchar(10) NOT NULL,
	REQ_TITLE varchar(200) NOT NULL,
	REQESTER bigint NOT NULL,
	APPROVER bigint NOT NULL,
	-- 실자 장비 반일/이설/반출 일자
	WORK_DT datetime COMMENT '실자 장비 반일/이설/반출 일자',
	-- 관련 프로젝트 명
	SERVICE_NAME varchar(512) COMMENT '관련 프로젝트 명',
	DESCRIPTION text,
	-- 현재 사용하지 않음
	PHASE varchar(128) COMMENT '현재 사용하지 않음',
	-- 프로세서 시작일자
	-- 요청이 이루어진 시간
	STT_DT datetime COMMENT '프로세서 시작일자
요청이 이루어진 시간',
	-- 삭제 플래그
	DEL_YN varchar(1) DEFAULT 'N' NOT NULL COMMENT '삭제 플래그',
	-- 최종적으로 프로세서가 종료된 시간
	END_DT datetime COMMENT '최종적으로 프로세서가 종료된 시간',
	-- 요청서가 저장된 시간
	REG_DT datetime COMMENT '요청서가 저장된 시간',
	-- 요청서 내용이 최근 수정시간
	UPD_DT datetime COMMENT '요청서 내용이 최근 수정시간',
	DEL_DT datetime,
	PRIMARY KEY (REQ_ID)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE tb_rc_target
(
	TGT_ID bigint NOT NULL AUTO_INCREMENT,
	REQ_ID bigint unsigned NOT NULL,
	TYPE varchar(100),
	MODEL varchar(150),
	-- 20170621 현재 사용하지 않음
	RACK_LOC varchar(300) COMMENT '20170621 현재 사용하지 않음',
	HEIGHT int unsigned,
	CALORIFIC varchar(150),
	-- Y/N
	STORAGE_USE_YN varchar(1) DEFAULT 'N' NOT NULL COMMENT 'Y/N',
	POWER float,
	POWER_GTY int unsigned,
	TOT_WEIGHT float,
	INNOR_SERVER_TOT_CNT int,
	INNOR_SERVER_CNT int,
	INNOR_SERVER_INFO varchar(1000),
	PRIMARY KEY (TGT_ID)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE tb_rc_target_network
(
	NET_ID bigint unsigned NOT NULL AUTO_INCREMENT,
	REQ_ID bigint unsigned NOT NULL,
	TGT_ID bigint NOT NULL,
	NET_INFO varchar(20) NOT NULL,
	PRIMARY KEY (NET_ID)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;



/* Create Foreign Keys */

ALTER TABLE TB_RC_ACTION_INFO
	ADD FOREIGN KEY (PHASE_ID)
	REFERENCES TB_RC_PHASE_INFO (PHASE_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_RC_HISTORY
	ADD FOREIGN KEY (PHASE_ID)
	REFERENCES TB_RC_PHASE_INFO (PHASE_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_RC_PHASE_INFO
	ADD FOREIGN KEY (P_ID)
	REFERENCES TB_RC_PROCESS_INFO (P_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_RC_REQUEST
	ADD FOREIGN KEY (P_ID)
	REFERENCES TB_RC_PROCESS_INFO (P_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_RC_FLOOR_PLAN
	ADD FOREIGN KEY (REPORT_ID)
	REFERENCES tb_rc_report (REPORT_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE tb_rc_report_item
	ADD CONSTRAINT tb_rc_report_item_ibfk_1 FOREIGN KEY (REPORT_ID)
	REFERENCES tb_rc_report (REPORT_ID)
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE FAVOR_PLAN_LOCATION
	ADD FOREIGN KEY (REQ_ID)
	REFERENCES TB_RC_REQUEST (REQ_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_RC_HISTORY
	ADD CONSTRAINT tb_rc_phase_ibfk_1 FOREIGN KEY (REG_ID)
	REFERENCES TB_RC_REQUEST (REQ_ID)
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE tb_rc_report
	ADD CONSTRAINT tb_rc_report_ibfk_1 FOREIGN KEY (REQ_ID)
	REFERENCES TB_RC_REQUEST (REQ_ID)
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE tb_rc_target
	ADD CONSTRAINT tb_rc_target_ibfk_1 FOREIGN KEY (REQ_ID)
	REFERENCES TB_RC_REQUEST (REQ_ID)
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE tb_rc_target_network
	ADD FOREIGN KEY (REQ_ID)
	REFERENCES TB_RC_REQUEST (REQ_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE tb_rc_target_network
	ADD CONSTRAINT tb_rc_target_network_ibfk_1 FOREIGN KEY (TGT_ID)
	REFERENCES tb_rc_target (TGT_ID)
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;



/* Create Indexes */

CREATE INDEX REG_ID USING BTREE ON TB_RC_HISTORY (REG_ID ASC);
CREATE INDEX REQ_ID USING BTREE ON tb_rc_report (REQ_ID ASC);
CREATE INDEX REPORT_ID USING BTREE ON tb_rc_report_item (REPORT_ID ASC);
CREATE INDEX REQ_ID USING BTREE ON tb_rc_target (REQ_ID ASC);
CREATE INDEX TGT_ID USING BTREE ON tb_rc_target_network (TGT_ID ASC);



