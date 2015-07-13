# Users schema

# --- !Ups

CREATE TABLE "users" ("id" SERIAL NOT NULL PRIMARY KEY,
  "email" VARCHAR NOT NULL,
  "password" VARCHAR NOT NULL
)

# --- !Downs

DROP TABLE "users";
