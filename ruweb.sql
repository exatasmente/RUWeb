--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.7
-- Dumped by pg_dump version 10.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: atualiza_cartao(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION atualiza_cartao() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE cartao_id INTEGER;
DECLARE usuario_id INTEGER;
DECLARE valor FLOAT;
    BEGIN
	SELECT id_cartao,saldo,id_usuario INTO cartao_id,valor,usuario_id
	FROM HISTORICO_CARTAO 
	WHERE id = NEW.id_cartao;
	
        IF (NEW.id_operacao = 1) THEN
            UPDATE CARTAO
            SET saldo = CARTAO.saldo+valor
            WHERE id = cartao_id and id_usuario = usuario_id;
        END IF;
        IF (NEW.id_operacao = 2) THEN
            UPDATE CARTAO
            SET saldo = CARTAO.saldo-valor
            WHERE id = cartao_id and id_usuario = usuario_id;
        END IF;
        IF (NEW.id_operacao = 3) THEN
            UPDATE CARTAO
            SET ativo = false
            WHERE id = cartao_id and id_usuario = usuario_id;
        END IF;
        IF (NEW.id_operacao = 4) THEN
            UPDATE CARTAO
            SET ativo = false
            WHERE id = cartao_id and id_usuario = usuario_id;
        END IF;
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.atualiza_cartao() OWNER TO postgres;

--
-- Name: check_tipo(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION check_tipo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (SELECT * FROM verifica_funcionario(NEW.id_funcionario)) = FALSE THEN
            RAISE EXCEPTION 'O USUARIO NÃO TEM PERMISÃO PARA FAZER ESTA OPERACAO';
        END IF;
        
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.check_tipo() OWNER TO postgres;

--
-- Name: novo_aluno(character varying, character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION novo_aluno(nome character varying, email character varying, login character varying, senha character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
   c_id integer;
BEGIN
   
   INSERT INTO cartao (saldo)
   VALUES (0)
   RETURNING id INTO c_id;

   INSERT INTO aluno (nome,email,login,senha,id_cartao)
   VALUES (nome, email,login,senha,c_id);

   
    RETURN c_id;
END;
$$;


ALTER FUNCTION public.novo_aluno(nome character varying, email character varying, login character varying, senha character varying) OWNER TO postgres;

--
-- Name: verifica_funcionario(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION verifica_funcionario(integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE func BOOLEAN;
BEGIN
        SELECT  (tipo = 4) INTO func
        FROM    USUARIO
        WHERE   id = $1;

        RETURN func;
END;
$_$;


ALTER FUNCTION public.verifica_funcionario(integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuario (
    id integer NOT NULL,
    nome character varying(100) NOT NULL,
    sobrenome character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    login character varying(30) NOT NULL,
    senha character varying(30) NOT NULL,
    tipo integer NOT NULL,
    ativo integer NOT NULL
);


ALTER TABLE usuario OWNER TO postgres;

--
-- Name: aluno; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW aluno AS
 SELECT usuario.id,
    usuario.nome,
    usuario.sobrenome,
    usuario.email,
    usuario.login,
    usuario.senha,
    usuario.tipo,
    usuario.ativo
   FROM usuario
  WHERE (usuario.tipo = 1);


ALTER TABLE aluno OWNER TO postgres;

--
-- Name: cardapio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cardapio (
    id integer NOT NULL,
    dia_semana date NOT NULL,
    horario integer NOT NULL
);


ALTER TABLE cardapio OWNER TO postgres;

--
-- Name: refeicao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE refeicao (
    id integer NOT NULL,
    id_cardapio integer NOT NULL,
    nome_refeicao character varying(100) NOT NULL,
    informacao_nutricional character varying(100) NOT NULL,
    tipo integer NOT NULL
);


ALTER TABLE refeicao OWNER TO postgres;

--
-- Name: tipo_refeicao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tipo_refeicao (
    id integer NOT NULL,
    valor character varying(50)
);


ALTER TABLE tipo_refeicao OWNER TO postgres;

--
-- Name: cardapio_api; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW cardapio_api AS
 SELECT cardapio.id AS id_cardapio,
    cardapio.dia_semana AS dia,
    cardapio.horario,
    refeicao.id AS id_refeicao,
    refeicao.nome_refeicao,
    refeicao.informacao_nutricional AS info_nutricional,
    refeicao.tipo AS id_tipo,
    tipo_refeicao.valor AS nome_tipo
   FROM cardapio,
    refeicao,
    tipo_refeicao
  WHERE ((refeicao.id_cardapio = cardapio.id) AND (refeicao.tipo = tipo_refeicao.id));


ALTER TABLE cardapio_api OWNER TO postgres;

--
-- Name: cardapio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cardapio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cardapio_id_seq OWNER TO postgres;

--
-- Name: cardapio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cardapio_id_seq OWNED BY cardapio.id;


--
-- Name: cartao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cartao (
    id integer NOT NULL,
    id_usuario integer NOT NULL,
    saldo double precision,
    ativo boolean DEFAULT true
);


ALTER TABLE cartao OWNER TO postgres;

--
-- Name: cartao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cartao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cartao_id_seq OWNER TO postgres;

--
-- Name: cartao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cartao_id_seq OWNED BY cartao.id;


--
-- Name: endereco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE endereco (
    id integer NOT NULL,
    logadouro character varying(100),
    cep integer NOT NULL,
    cidade character varying(60) NOT NULL,
    estado character varying(60) NOT NULL,
    bairro character varying(60) NOT NULL,
    pais character varying(60) NOT NULL,
    id_usuario integer NOT NULL
);


ALTER TABLE endereco OWNER TO postgres;

--
-- Name: endereco_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE endereco_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE endereco_id_seq OWNER TO postgres;

--
-- Name: endereco_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE endereco_id_seq OWNED BY endereco.id;


--
-- Name: historico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE historico (
    id integer NOT NULL,
    id_funcionario integer NOT NULL,
    id_cartao integer NOT NULL,
    id_operacao integer
);


ALTER TABLE historico OWNER TO postgres;

--
-- Name: historico_cartao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE historico_cartao (
    id integer NOT NULL,
    id_usuario integer NOT NULL,
    id_cartao integer NOT NULL,
    saldo double precision NOT NULL
);


ALTER TABLE historico_cartao OWNER TO postgres;

--
-- Name: historico_cartao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE historico_cartao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE historico_cartao_id_seq OWNER TO postgres;

--
-- Name: historico_cartao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE historico_cartao_id_seq OWNED BY historico_cartao.id;


--
-- Name: historico_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE historico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE historico_id_seq OWNER TO postgres;

--
-- Name: historico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE historico_id_seq OWNED BY historico.id;


--
-- Name: obs_refeicao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE obs_refeicao (
    id integer NOT NULL,
    id_refeicao integer NOT NULL,
    valor character varying(100)
);


ALTER TABLE obs_refeicao OWNER TO postgres;

--
-- Name: obs_refeicao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE obs_refeicao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE obs_refeicao_id_seq OWNER TO postgres;

--
-- Name: obs_refeicao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE obs_refeicao_id_seq OWNED BY obs_refeicao.id;


--
-- Name: operacao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE operacao (
    id integer NOT NULL,
    valor text NOT NULL
);


ALTER TABLE operacao OWNER TO postgres;

--
-- Name: operacao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE operacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE operacao_id_seq OWNER TO postgres;

--
-- Name: operacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE operacao_id_seq OWNED BY operacao.id;


--
-- Name: refeicao_api; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW refeicao_api AS
 SELECT refeicao.id,
    refeicao.nome_refeicao,
    refeicao.informacao_nutricional AS info_nutricional,
    refeicao.tipo AS id_tipo,
    tipo_refeicao.valor AS nome_tipo
   FROM refeicao,
    tipo_refeicao
  WHERE (refeicao.tipo = tipo_refeicao.id);


ALTER TABLE refeicao_api OWNER TO postgres;

--
-- Name: refeicao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE refeicao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE refeicao_id_seq OWNER TO postgres;

--
-- Name: refeicao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE refeicao_id_seq OWNED BY refeicao.id;


--
-- Name: tipo_refeicao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tipo_refeicao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tipo_refeicao_id_seq OWNER TO postgres;

--
-- Name: tipo_refeicao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tipo_refeicao_id_seq OWNED BY tipo_refeicao.id;


--
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE usuario_id_seq OWNER TO postgres;

--
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- Name: cardapio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cardapio ALTER COLUMN id SET DEFAULT nextval('cardapio_id_seq'::regclass);


--
-- Name: cartao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cartao ALTER COLUMN id SET DEFAULT nextval('cartao_id_seq'::regclass);


--
-- Name: endereco id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco ALTER COLUMN id SET DEFAULT nextval('endereco_id_seq'::regclass);


--
-- Name: historico id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico ALTER COLUMN id SET DEFAULT nextval('historico_id_seq'::regclass);


--
-- Name: historico_cartao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico_cartao ALTER COLUMN id SET DEFAULT nextval('historico_cartao_id_seq'::regclass);


--
-- Name: obs_refeicao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY obs_refeicao ALTER COLUMN id SET DEFAULT nextval('obs_refeicao_id_seq'::regclass);


--
-- Name: operacao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operacao ALTER COLUMN id SET DEFAULT nextval('operacao_id_seq'::regclass);


--
-- Name: refeicao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY refeicao ALTER COLUMN id SET DEFAULT nextval('refeicao_id_seq'::regclass);


--
-- Name: tipo_refeicao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tipo_refeicao ALTER COLUMN id SET DEFAULT nextval('tipo_refeicao_id_seq'::regclass);


--
-- Name: usuario id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- Data for Name: cardapio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cardapio (id, dia_semana, horario) FROM stdin;
1	2018-06-19	1
2	2018-06-19	2
3	2018-06-19	3
4	2018-06-19	1
5	2018-06-20	1
6	2018-06-19	1
7	2018-06-19	1
8	2018-06-19	2
9	2018-06-19	2
\.


--
-- Data for Name: cartao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cartao (id, id_usuario, saldo, ativo) FROM stdin;
2	2	0	t
1	1	10	t
\.


--
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY endereco (id, logadouro, cep, cidade, estado, bairro, pais, id_usuario) FROM stdin;
\.


--
-- Data for Name: historico; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY historico (id, id_funcionario, id_cartao, id_operacao) FROM stdin;
10	3	6	1
12	3	9	1
13	3	10	2
\.


--
-- Data for Name: historico_cartao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY historico_cartao (id, id_usuario, id_cartao, saldo) FROM stdin;
6	1	1	10
9	1	1	1.10000000000000009
10	1	1	1.10000000000000009
\.


--
-- Data for Name: obs_refeicao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY obs_refeicao (id, id_refeicao, valor) FROM stdin;
1	6	Contém Glútem
2	2	Contém Lactose
3	5	Contém Glútem
4	9	Contém Lactose
5	9	Contém Glutém
6	13	ContÃ©m GlÃºtem
7	14	ContÃ©m GÄºutem
8	15	ContÃ©m Lactose
9	15	ContÃ©m GlÃºtem
10	17	ContÃ©m Lactose
11	22	ContÃ©m GlÃºtem
12	23	ContÃ©m GÄºutem
13	24	ContÃ©m Lactose
14	24	ContÃ©m GlÃºtem
15	26	ContÃ©m Lactose
\.


--
-- Data for Name: operacao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY operacao (id, valor) FROM stdin;
1	compra
2	debito
3	segunda via
4	bloqueio
\.


--
-- Data for Name: refeicao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY refeicao (id, id_cardapio, nome_refeicao, informacao_nutricional, tipo) FROM stdin;
1	1	Café	------------	1
2	1	Leite Quente/Frio	------------	1
3	1	Leite de Soja	------------	1
4	1	Suco de Manga	------------	1
5	1	Pão Carioca	------------	2
6	1	Pão Sovado	------------	2
7	1	Tangerina	------------	3
8	1	Melão Japonês	------------	3
9	1	Mingau	------------	4
10	4	Tangerina	------------	1
11	5	Tangerina	------------	1
12	5	MelÃ£o JaponÃªs	------------	1
13	5	PÃ£o Carioca	------------	1
14	5	PÃ£o Sovado	------------	1
15	5	Mingau	------------	1
16	5	CafÃ©	------------	1
17	5	Leite Quente/Frio	------------	1
18	5	Leite de Soja	------------	1
19	5	Suco de Manga	------------	1
20	9	Tangerina	------------	1
21	9	MelÃ£o JaponÃªs	------------	1
22	9	PÃ£o Carioca	------------	1
23	9	PÃ£o Sovado	------------	1
24	9	Mingau	------------	1
25	9	CafÃ©	------------	1
26	9	Leite Quente/Frio	------------	1
27	9	Leite de Soja	------------	1
28	9	Suco de Manga	------------	1
\.


--
-- Data for Name: tipo_refeicao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY tipo_refeicao (id, valor) FROM stdin;
1	bebida
2	pão
3	fruta
4	especial
5	principal
6	vegetariano
7	salada
8	guarnição
9	acompanhamento
10	sobremesa
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY usuario (id, nome, sobrenome, email, login, senha, tipo, ativo) FROM stdin;
1	leticia 	freitas ventura	leticia@email.com	qualquer coisa	123456789	1	1
2	Luiz 	vieira Gonzaga Neto	luiz@email.com	sr fernando	123456789	1	1
3	sistema	ruWeb	sistema@ruweb.com	sistema	ruweb	4	1
\.


--
-- Name: cardapio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cardapio_id_seq', 9, true);


--
-- Name: cartao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cartao_id_seq', 2, true);


--
-- Name: endereco_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('endereco_id_seq', 1, false);


--
-- Name: historico_cartao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('historico_cartao_id_seq', 10, true);


--
-- Name: historico_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('historico_id_seq', 13, true);


--
-- Name: obs_refeicao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('obs_refeicao_id_seq', 15, true);


--
-- Name: operacao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('operacao_id_seq', 4, true);


--
-- Name: refeicao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('refeicao_id_seq', 28, true);


--
-- Name: tipo_refeicao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tipo_refeicao_id_seq', 10, true);


--
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('usuario_id_seq', 3, true);


--
-- Name: cardapio cardapio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cardapio
    ADD CONSTRAINT cardapio_pkey PRIMARY KEY (id);


--
-- Name: cartao cartao_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cartao
    ADD CONSTRAINT cartao_id_usuario_key UNIQUE (id_usuario);


--
-- Name: cartao cartao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cartao
    ADD CONSTRAINT cartao_pkey PRIMARY KEY (id, id_usuario);


--
-- Name: endereco endereco_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT endereco_id_usuario_key UNIQUE (id_usuario);


--
-- Name: endereco endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id, id_usuario);


--
-- Name: historico_cartao historico_cartao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico_cartao
    ADD CONSTRAINT historico_cartao_pkey PRIMARY KEY (id);


--
-- Name: historico historico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico
    ADD CONSTRAINT historico_pkey PRIMARY KEY (id, id_funcionario, id_cartao);


--
-- Name: obs_refeicao obs_refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY obs_refeicao
    ADD CONSTRAINT obs_refeicao_pkey PRIMARY KEY (id_refeicao, id);


--
-- Name: operacao operacao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operacao
    ADD CONSTRAINT operacao_pkey PRIMARY KEY (id);


--
-- Name: refeicao refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY refeicao
    ADD CONSTRAINT refeicao_pkey PRIMARY KEY (id, id_cardapio);


--
-- Name: tipo_refeicao tipo_refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tipo_refeicao
    ADD CONSTRAINT tipo_refeicao_pkey PRIMARY KEY (id);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- Name: historico atualiza_cartao; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER atualiza_cartao BEFORE INSERT OR UPDATE ON historico FOR EACH ROW EXECUTE PROCEDURE atualiza_cartao();


--
-- Name: historico check_tipo; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER check_tipo BEFORE INSERT OR UPDATE ON historico FOR EACH ROW EXECUTE PROCEDURE check_tipo();


--
-- Name: cartao cartao_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cartao
    ADD CONSTRAINT cartao_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: endereco endereco_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT endereco_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: historico_cartao historico_cartao_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico_cartao
    ADD CONSTRAINT historico_cartao_id_usuario_fkey FOREIGN KEY (id_usuario, id_cartao) REFERENCES cartao(id_usuario, id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: historico historico_id_cartao_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico
    ADD CONSTRAINT historico_id_cartao_fkey FOREIGN KEY (id_cartao) REFERENCES historico_cartao(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: historico historico_id_funcionario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico
    ADD CONSTRAINT historico_id_funcionario_fkey FOREIGN KEY (id_funcionario) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: historico historico_id_operacao_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico
    ADD CONSTRAINT historico_id_operacao_fkey FOREIGN KEY (id_operacao) REFERENCES operacao(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: refeicao refeicao_id_cardapio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY refeicao
    ADD CONSTRAINT refeicao_id_cardapio_fkey FOREIGN KEY (id_cardapio) REFERENCES cardapio(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: refeicao refeicao_tipo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY refeicao
    ADD CONSTRAINT refeicao_tipo_fkey FOREIGN KEY (tipo) REFERENCES tipo_refeicao(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

