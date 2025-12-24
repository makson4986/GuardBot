INSERT INTO departments (name)
VALUES ('Орган дознания'),
       ('Тяжелое подразделение'),
       ('Сан. отдел'),
       ('Управляющий отдел');

INSERT INTO ranks (name, max_points, max_special_reports, position)
VALUES ('Стажёр', 0, 0, 1),
       ('Сержант', 120, 3, 2),
       ('Лейтенант', 185, 4, 3),
       ('Капитан', 235, 6, 4),
       ('Майор', 300, 8, 5),
       ('Полковник', 300, 8, 6),
       ('Зам. главы', -1, -1, 7),
       ('Глава гвардии', -1, -1, 8);

INSERT INTO guardsmen(name, rank_id, points, special_report, last_report)
VALUES ('Faust_Bismarkvil', 8, 0, 0, null),
       ('Mayzito', 7, 0, 0, null),
       ('_A1d', 2, 0, 0, null),
       ('KoftachkaWTF', 1, 0, 0, null),
       ('Olenik636', 1, 0, 0, null),
       ('MaksonLegend', 2, 17, 0, null),
       ('Chertila13', 1, 0, 0, null),
       ('Ay_ro_', 3, 30, 0, null),
       ('Nick_Torch14', 6, 300, 8, null),
       ('Charlotan', 2, 7, 0, null),
       ('Aleksandlolkek', 1, 0, 0, null),
       ('MaksMaruS_', 2, 0, 0, null);

INSERT INTO department_members(guardsman_id, department_id, role)
VALUES (1, 4, 'HEADMAN'),
       (2, 4, 'HEADMAN');
