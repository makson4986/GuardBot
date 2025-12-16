INSERT INTO departments (name)
VALUES ('Отряд "Санитаров"'),
       ('Отряд "Эпсилон"'),
       ('Отряд "Дельта"'),
       ('Управляющий отдел');

INSERT INTO ranks (name, max_points, max_special_reports, position, discord_role_id)
VALUES ('Стажер', 0, 0, 1, 1450034741548941312),
       ('Сержант', 120, 3, 2, 1449728045014319196),
       ('Лейтенант', 185, 4, 3, 1450034812860497930),
       ('Капитан', 235, 6, 4, 1450034877985722439),
       ('Майор', 300, 8, 5, 1450034914060665056),
       ('Полковник', 300, 8, 6, 1450034949842272349),
       ('Зам. главы', -1, -1, 7, 1448703095482744924),
       ('Глава гвардии', -1, -1, 8, 1448703095482744924);

INSERT INTO guardsmen(name, rank_id, points, special_report, last_report)
VALUES ('Faust_Bismarkvil', 8, -1, -1, null),
       ('Mayzito', 7, -1, -1, null),
       ('MaksonLegend', 2, 17, 0, null);

INSERT INTO department_members(guardsman_id, department_id, role)
VALUES (1, 4, 'HEADMAN'),
       (2, 4, 'HEADMAN')
