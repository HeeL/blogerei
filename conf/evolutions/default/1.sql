# Posts schema

# --- !Ups

CREATE TABLE "posts" (
    "id" SERIAL NOT NULL PRIMARY KEY,
    "title" VARCHAR NOT NULL,
    "task" VARCHAR NOT NULL,
    "solution" VARCHAR NOT NULL,
    "solution2" VARCHAR,
    "tests" VARCHAR
    )

# --- !Downs

DROP TABLE "posts";