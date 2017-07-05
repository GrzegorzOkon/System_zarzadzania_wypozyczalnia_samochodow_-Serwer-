DROP TABLE rezerwacje;
DROP TABLE pracownicy;
DROP TABLE uzytkownicy;
DROP TABLE adresy;
DROP TABLE samochody;
DROP TABLE stanowiska;

CREATE TABLE adresy(
id NUMBER(5) PRIMARY KEY,
ulica VARCHAR(50) CONSTRAINT adresy_ulica NOT NULL UNIQUE,
miasto VARCHAR(50) CONSTRAINT adresy_miasto NOT NULL UNIQUE
);

CREATE TABLE uzytkownicy(
id NUMBER(5) PRIMARY KEY,
login VARCHAR2(25) CONSTRAINT uzytkownicy_login NOT NULL UNIQUE,
haslo VARCHAR2(25) CONSTRAINT uzytkownicy_haslo NOT NULL,
imie VARCHAR2(25) CONSTRAINT uzytkownicy_imie NOT NULL,
nazwisko VARCHAR(25) CONSTRAINT uzytkownicy_nazwisko NOT NULL,
id_adresu NUMBER(5) REFERENCES adresy(id)
);

CREATE TABLE stanowiska(
id NUMBER(5) PRIMARY KEY,
nazwa VARCHAR2(50) CONSTRAINT stanowiska_nazwa NOT NULL UNIQUE,
wynagrodzenie NUMBER(5) CONSTRAINT stanowiska_wynagrodzenie NOT NULL
);

CREATE TABLE pracownicy(
id NUMBER(5) PRIMARY KEY,
id_stanowiska NUMBER(5) CONSTRAINT pracownicy_id_stanowiska NOT NULL REFERENCES stanowiska(id),
id_uzytkownika NUMBER(5) CONSTRAINT pracownicy_id_uzytkownika NOT NULL UNIQUE REFERENCES uzytkownicy(id)
);

CREATE TABLE samochody(
id NUMBER(5) PRIMARY KEY,
marka VARCHAR(50) CONSTRAINT samochody_marka NOT NULL,
model VARCHAR(50) CONSTRAINT samochody_model NOT NULL,
kolor VARCHAR(25) CONSTRAINT samochody_kolor NOT NULL,
paliwo VARCHAR(25) CONSTRAINT samochody_paliwo NOT NULL,
rok NUMBER(4) CONSTRAINT samochody_rok NOT NULL
);

CREATE TABLE rezerwacje(
id NUMBER(5) PRIMARY KEY,
id_uzytkownika NUMBER(5) REFERENCES uzytkownicy(id),
id_samochodu NUMBER(5) REFERENCES samochody(id),
data_od date CONSTRAINT rezerwacje_dataod NOT NULL,
data_do date CONSTRAINT rezerwacje_datado NOT NULL
);

--INSERT INTO adresy VALUES(1, 'Moniuszki', 'Busko-Zdroj');
--INSERT INTO uzytkownicy VALUES(1, 'admin', 'admin', 'Sebastian', 'Palys', 1);

SELECT * FROM uzytkownicy;
SELECT * FROM adresy;
SELECT * FROM samochody;
SELECT * FROM stanowiska;
select * from rezerwacje;