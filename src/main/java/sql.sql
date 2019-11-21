CREATE TABLE public.usuariopessoa
(
  id bigint NOT NULL,
  email character varying(255),
  idade integer NOT NULL,
  login character varying(255),
  nome character varying(255),
  senha character varying(255),
  sobrenome character varying(255),
  CONSTRAINT usuariopessoa_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usuariopessoa
  OWNER TO postgres;