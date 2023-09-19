CREATE TABLE vets (
	id INTEGER,
	first_name VARCHAR(30),
	last_name VARCHAR(30),
	CONSTRAINT pk_vets PRIMARY KEY (id)
);

CREATE INDEX idx_vets_last_name ON vets (last_name);

CREATE SEQUENCE vets_id_seq START WITH 100;

CREATE TABLE specialties (
	id INTEGER,
	name VARCHAR(80),
	CONSTRAINT pk_specialties PRIMARY KEY (id)
);

CREATE INDEX idx_specialties_name ON specialties (name);

CREATE SEQUENCE specialties_id_seq START WITH 100;

CREATE TABLE vet_specialties (
	vet_id INTEGER NOT NULL,
	specialty_id INTEGER NOT NULL,
	FOREIGN KEY (vet_id) REFERENCES vets(id),
	FOREIGN KEY (specialty_id) REFERENCES specialties(id),
	CONSTRAINT unique_ids UNIQUE (vet_id, specialty_id)
);

CREATE TABLE types (
	id INTEGER,
	name VARCHAR(80),
	CONSTRAINT pk_type PRIMARY KEY (id)
);

CREATE INDEX idx_types_name ON types (name);

CREATE SEQUENCE types_id_seq START WITH 100;

CREATE TABLE owners (
	id INTEGER,
	first_name VARCHAR(30),
	last_name VARCHAR(30),
	address VARCHAR(255),
	city VARCHAR(80),
	telephone VARCHAR(20),
	CONSTRAINT pk_owners PRIMARY KEY (id)
);

CREATE INDEX idx_owners_last_name ON owners (last_name);

CREATE SEQUENCE owners_id_seq START WITH 100;

CREATE TABLE pets (
	id INTEGER,
	name VARCHAR(30),
	birth_date DATE,
	type_id INTEGER NOT NULL,
	owner_id INTEGER NOT NULL,
	FOREIGN KEY (owner_id) REFERENCES owners(id),
	FOREIGN KEY (type_id) REFERENCES types(id),
	CONSTRAINT pk_pet PRIMARY KEY (id)
);

CREATE INDEX idx_pets_name ON pets (name);

CREATE SEQUENCE pets_id_seq START WITH 100;

CREATE TABLE visits (
	id INTEGER,
	pet_id INTEGER NOT NULL,
	visit_date DATE,
	description VARCHAR(255),
	FOREIGN KEY (pet_id) REFERENCES pets(id),
	CONSTRAINT pk_visits PRIMARY KEY (id)
);

CREATE SEQUENCE visits_id_seq START WITH 100;
