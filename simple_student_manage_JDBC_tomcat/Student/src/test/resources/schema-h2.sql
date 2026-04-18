CREATE TABLE IF NOT EXISTS teacher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS major (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sex VARCHAR(10),
    indate DATE,
    claname VARCHAR(100),
    majorid INT,
    teaid INT
);

CREATE TABLE IF NOT EXISTS course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS stucourse (
    stuid INT,
    couid INT,
    score FLOAT,
    PRIMARY KEY (stuid, couid)
);

CREATE TABLE IF NOT EXISTS class (
    name VARCHAR(100) PRIMARY KEY,
    num INT,
    teaid INT
);

CREATE VIEW IF NOT EXISTS v_stu_avgscore AS
SELECT sc.stuid as stu_id, AVG(sc.score) as avg_score
FROM stucourse sc
GROUP BY sc.stuid;

INSERT INTO teacher (id, name) VALUES (1, '张老师');
INSERT INTO teacher (id, name) VALUES (2, '李老师');
INSERT INTO teacher (id, name) VALUES (3, '王老师');

INSERT INTO major (id, name) VALUES (1, '计算机科学与技术');
INSERT INTO major (id, name) VALUES (2, '软件工程');
INSERT INTO major (id, name) VALUES (3, '信息工程');

INSERT INTO course (id, name) VALUES (1, '高等数学');
INSERT INTO course (id, name) VALUES (2, '大学英语');
INSERT INTO course (id, name) VALUES (3, '程序设计');
INSERT INTO course (id, name) VALUES (4, '数据结构');

INSERT INTO class (name, num, teaid) VALUES ('计算机一班', 30, 1);
INSERT INTO class (name, num, teaid) VALUES ('计算机二班', 28, 2);

INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) 
VALUES (1001, '张三', '男', '2023-09-01', '计算机一班', 1, 1);

INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) 
VALUES (1002, '李四', '女', '2023-09-01', '计算机一班', 1, 1);

INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) 
VALUES (1003, '王五', '男', '2023-09-01', '计算机二班', 2, 2);

INSERT INTO stucourse (stuid, couid, score) VALUES (1001, 1, 85.5);
INSERT INTO stucourse (stuid, couid, score) VALUES (1001, 2, 90.0);
INSERT INTO stucourse (stuid, couid, score) VALUES (1001, 3, 88.0);
INSERT INTO stucourse (stuid, couid, score) VALUES (1002, 1, 78.0);
INSERT INTO stucourse (stuid, couid, score) VALUES (1002, 2, 82.5);
