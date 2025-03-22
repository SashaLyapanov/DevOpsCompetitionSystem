DROP TABLE IF EXISTS sportsman_competition;

DROP TABLE IF EXISTS competitions;
CREATE TABLE competitions (
    id varchar(36) NOT NULL PRIMARY KEY UNIQUE,
    name varchar(50) NOT NULL,
    description varchar(500) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    participant_quantity int
);

DROP TABLE IF EXISTS sportsmen;
CREATE TABLE sportsmen (
    id varchar(36) NOT NULL PRIMARY KEY UNIQUE,
    surname varchar(30) NOT NULL,
    first_name varchar(30) NOT NULL,
    patronymic varchar(30),
    age int NOT NULL,
    email varchar(100) NOT NULL,
    phone varchar(12)
);

CREATE TABLE sportsman_competition (
    sportsman_id varchar(36),
    competition_id varchar(36),
    FOREIGN KEY (sportsman_id) REFERENCES sportsmen (id),
    FOREIGN KEY (competition_id) REFERENCES competitions (id)
);