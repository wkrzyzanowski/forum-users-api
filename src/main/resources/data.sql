INSERT INTO USERS (ID, EMAIL, HELP_COUNT, LAST_LOGIN, NICK, PASSWORD, UUID)
VALUES (1, 'test1@test.com', 0, LOCALTIME(), 'test1', '$2a$10$3IfG8KewsWl.a8TFbnfb4OGB1uU94QYv20NREi55KPxRs2RFoWm9u',
        '795756fd-e6e3-4266-b8b1-95fa0d73418a'),
       (2, 'test2@test.com', 0, LOCALTIME(), 'test2', '$2a$10$GqD1SFu5p7gz6WeDOml6Nu0Ifm80pIHkVek.HeqeDSJKPcVD2CtG6',
        '3b56a26b-b8b3-4ed8-9bf2-60c2b981213a');


INSERT INTO ROLES (ID, NAME)
VALUES (101, 'USER'),
       (102, 'MOD'),
       (103, 'ADMIN');


INSERT INTO USERS_ROLES (USER_ID, ROLE_ID)
VALUES (1, 101),
       (1, 102),
       (1, 103),
       (2, 101);